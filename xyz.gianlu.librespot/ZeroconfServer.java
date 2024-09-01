//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.spotifyxp.deps.xyz.gianlu.librespot;

import com.google.gson.JsonObject;
import com.spotifyxp.deps.com.spotify.connectstate.Connect;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.spotifyxp.logging.ConsoleLoggingModules;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.spotifyxp.deps.xyz.gianlu.librespot.common.NameThreadFactory;
import com.spotifyxp.deps.xyz.gianlu.librespot.common.Utils;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.crypto.DiffieHellman;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import xyz.gianlu.zeroconf.Service;
import xyz.gianlu.zeroconf.Zeroconf;

public class ZeroconfServer implements Closeable {
    public static final String SERVICE = "spotify-connect";
    private static final int MAX_PORT = 65536;
    private static final int MIN_PORT = 1024;
    private static final byte[] EOL = new byte[]{13, 10};
    private static final JsonObject DEFAULT_GET_INFO_FIELDS = new JsonObject();
    private static final JsonObject DEFAULT_SUCCESSFUL_ADD_USER = new JsonObject();
    private static final byte[][] VIRTUAL_INTERFACES = new byte[][]{{0, 15, 75}, {0, 19, 7}, {0, 19, -66}, {0, 33, -10}, {0, 36, 11}, {0, -96, -79}, {0, -32, -56}, {84, 82, 0}, {0, 33, -10}, {24, -110, 44}, {60, -13, -110}, {0, 5, 105}, {0, 12, 41}, {0, 80, 86}, {0, 28, 66}, {0, 3, -1}, {0, 22, 62}, {8, 0, 39}, {0, 21, 93}};
    private final HttpRunner runner;
    private final DiffieHellman keys;
    private final List<SessionListener> sessionListeners;
    private final Zeroconf zeroconf;
    private final Object connectionLock;
    private final Inner inner;
    private volatile Session session;
    private String connectingUsername;

    private ZeroconfServer(@NotNull Inner inner, int listenPort, boolean listenAllInterfaces, String[] interfacesList) throws IOException {
        this.connectionLock = new Object();
        this.connectingUsername = null;
        this.inner = inner;
        this.keys = new DiffieHellman(inner.random);
        this.sessionListeners = new ArrayList();
        if (listenPort == -1) {
            listenPort = inner.random.nextInt(64513) + 1024;
        }

        (new Thread(this.runner = new HttpRunner(listenPort), "zeroconf-http-server")).start();
        Object nics;
        if (listenAllInterfaces) {
            nics = getAllInterfaces();
        } else if (interfacesList != null && interfacesList.length != 0) {
            nics = new ArrayList();
            String[] var6 = interfacesList;
            int var7 = interfacesList.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String str = var6[var8];
                NetworkInterface nif = NetworkInterface.getByName(str);
                if (nif == null) {
                    ConsoleLoggingModules.warning("Interface {} doesn't exists.", str);
                } else {
                    checkInterface((List)nics, nif);
                }
            }
        } else {
            nics = getAllInterfaces();
        }

        this.zeroconf = new Zeroconf();
        this.zeroconf.setLocalHostName(getUsefulHostname());
        this.zeroconf.setUseIpv4(true).setUseIpv6(false);
        this.zeroconf.addNetworkInterfaces((Collection)nics);
        Map<String, String> txt = new HashMap();
        txt.put("CPath", "/");
        txt.put("VERSION", "1.0");
        txt.put("Stack", "SP");
        Service service = new Service(inner.deviceName, "spotify-connect", listenPort);
        service.setText(txt);
        this.zeroconf.announce(service);
    }

    public static @NotNull String getUsefulHostname() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostName();
        if (Objects.equals(host, "localhost")) {
            host = Utils.toBase64(BigInteger.valueOf(ThreadLocalRandom.current().nextLong()).toByteArray()) + ".local";
            ConsoleLoggingModules.warning("Hostname cannot be `localhost`, temporary hostname: " + host);
            return host;
        } else {
            return host;
        }
    }

    private static boolean isVirtual(@NotNull NetworkInterface nif) throws SocketException {
        byte[] mac = nif.getHardwareAddress();
        if (mac == null) {
            return true;
        } else {
            byte[][] var2 = VIRTUAL_INTERFACES;
            int var3 = var2.length;

            label28:
            for(int var4 = 0; var4 < var3; ++var4) {
                byte[] virtual = var2[var4];

                for(int i = 0; i < Math.min(virtual.length, mac.length); ++i) {
                    if (virtual[i] != mac[i]) {
                        continue label28;
                    }
                }

                return true;
            }

            return false;
        }
    }

    private static void checkInterface(List<NetworkInterface> list, @NotNull NetworkInterface nif) throws SocketException {
        if (!nif.isLoopback() && !isVirtual(nif)) {
            list.add(nif);
        }
    }

    private static @NotNull List<NetworkInterface> getAllInterfaces() throws SocketException {
        List<NetworkInterface> list = new ArrayList();
        Enumeration<NetworkInterface> is = NetworkInterface.getNetworkInterfaces();

        while(is.hasMoreElements()) {
            checkInterface(list, (NetworkInterface)is.nextElement());
        }

        return list;
    }

    private static @NotNull Map<String, String> parsePath(@NotNull String path) {
        HttpUrl url = HttpUrl.get("http://host" + path);
        Map<String, String> map = new HashMap();
        Iterator var3 = url.queryParameterNames().iterator();

        while(var3.hasNext()) {
            String name = (String)var3.next();
            map.put(name, url.queryParameter(name));
        }

        return map;
    }

    public void close() throws IOException {
        this.zeroconf.close();
        this.runner.close();
    }

    public void closeSession() throws IOException {
        if (this.session != null) {
            this.sessionListeners.forEach((l) -> {
                l.sessionClosing(this.session);
            });
            this.session.close();
            this.session = null;
        }
    }

    private boolean hasValidSession() {
        try {
            boolean valid = this.session != null && this.session.isValid();
            if (!valid) {
                this.session = null;
            }

            return valid;
        } catch (IllegalStateException var2) {
            this.session = null;
            return false;
        }
    }

    private void handleGetInfo(OutputStream out, String httpVersion) throws IOException {
        JsonObject info = DEFAULT_GET_INFO_FIELDS.deepCopy();
        info.addProperty("deviceID", this.inner.deviceId);
        info.addProperty("remoteName", this.inner.deviceName);
        info.addProperty("publicKey", Utils.toBase64(this.keys.publicKeyArray()));
        info.addProperty("deviceType", this.inner.deviceType.name().toUpperCase());
        synchronized(this.connectionLock) {
            info.addProperty("activeUser", this.connectingUsername != null ? this.connectingUsername : (this.hasValidSession() ? this.session.username() : ""));
        }

        out.write(httpVersion.getBytes());
        out.write(" 200 OK".getBytes());
        out.write(EOL);
        out.flush();
        out.write("Content-Type: application/json".getBytes());
        out.write(EOL);
        out.flush();
        out.write(EOL);
        out.write(info.toString().getBytes());
        out.flush();
    }

    private void handleAddUser(OutputStream out, Map<String, String> params, String httpVersion) throws GeneralSecurityException, IOException {
        String username = (String)params.get("userName");
        if (username != null && !username.isEmpty()) {
            String blobStr = (String)params.get("blob");
            if (blobStr != null && !blobStr.isEmpty()) {
                String clientKeyStr = (String)params.get("clientKey");
                if (clientKeyStr != null && !clientKeyStr.isEmpty()) {
                    synchronized(this.connectionLock) {
                        if (username.equals(this.connectingUsername)) {
                            ConsoleLoggingModules.info("{} is already trying to connect.", username);
                            out.write(httpVersion.getBytes());
                            out.write(" 403 Forbidden".getBytes());
                            out.write(EOL);
                            out.write(EOL);
                            out.flush();
                            return;
                        }
                    }

                    byte[] sharedKey = Utils.toByteArray(this.keys.computeSharedKey(Utils.fromBase64(clientKeyStr)));
                    byte[] blobBytes = Utils.fromBase64(blobStr);
                    byte[] iv = Arrays.copyOfRange(blobBytes, 0, 16);
                    byte[] encrypted = Arrays.copyOfRange(blobBytes, 16, blobBytes.length - 20);
                    byte[] checksum = Arrays.copyOfRange(blobBytes, blobBytes.length - 20, blobBytes.length);
                    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                    sha1.update(sharedKey);
                    byte[] baseKey = Arrays.copyOfRange(sha1.digest(), 0, 16);
                    Mac hmac = Mac.getInstance("HmacSHA1");
                    hmac.init(new SecretKeySpec(baseKey, "HmacSHA1"));
                    hmac.update("checksum".getBytes());
                    byte[] checksumKey = hmac.doFinal();
                    hmac.init(new SecretKeySpec(baseKey, "HmacSHA1"));
                    hmac.update("encryption".getBytes());
                    byte[] encryptionKey = hmac.doFinal();
                    hmac.init(new SecretKeySpec(checksumKey, "HmacSHA1"));
                    hmac.update(encrypted);
                    byte[] mac = hmac.doFinal();
                    if (!Arrays.equals(mac, checksum)) {
                        ConsoleLoggingModules.error("Mac and checksum don't match!");
                        out.write(httpVersion.getBytes());
                        out.write(" 400 Bad Request".getBytes());
                        out.write(EOL);
                        out.write(EOL);
                        out.flush();
                    } else {
                        Cipher aes = Cipher.getInstance("AES/CTR/NoPadding");
                        aes.init(2, new SecretKeySpec(Arrays.copyOfRange(encryptionKey, 0, 16), "AES"), new IvParameterSpec(iv));
                        byte[] decrypted = aes.doFinal(encrypted);

                        try {
                            this.closeSession();
                        } catch (IOException var28) {
                            IOException ex = var28;
                            ConsoleLoggingModules.warning("Failed closing previous session.", ex);
                        }

                        try {
                            synchronized(this.connectionLock) {
                                this.connectingUsername = username;
                            }

                            ConsoleLoggingModules.info("Accepted new user from {}. {deviceId: {}}", params.get("deviceName"), this.inner.deviceId);
                            String resp = DEFAULT_SUCCESSFUL_ADD_USER.toString();
                            out.write(httpVersion.getBytes());
                            out.write(" 200 OK".getBytes());
                            out.write(EOL);
                            out.write("Content-Length: ".getBytes());
                            out.write(String.valueOf(resp.length()).getBytes());
                            out.write(EOL);
                            out.flush();
                            out.write(EOL);
                            out.write(resp.getBytes());
                            out.flush();
                            this.session = ((Session.Builder)((Session.Builder)((Session.Builder)((Session.Builder)(new Session.Builder(this.inner.conf)).setDeviceId(this.inner.deviceId)).setDeviceName(this.inner.deviceName)).setDeviceType(this.inner.deviceType)).setPreferredLocale(this.inner.preferredLocale)).blob(username, decrypted).create();
                            synchronized(this.connectionLock) {
                                this.connectingUsername = null;
                            }

                            this.sessionListeners.forEach((l) -> {
                                l.sessionChanged(this.session);
                            });
                        } catch (MercuryClient.MercuryException | IOException | GeneralSecurityException | Session.SpotifyAuthenticationException var27) {
                            Exception ex = var27;
                            ConsoleLoggingModules.error("Couldn't establish a new session.", ex);
                            synchronized(this.connectionLock) {
                                this.connectingUsername = null;
                            }

                            out.write(httpVersion.getBytes());
                            out.write(" 500 Internal Server Error".getBytes());
                            out.write(EOL);
                            out.write(EOL);
                            out.flush();
                        }

                    }
                } else {
                    ConsoleLoggingModules.error("Missing clientKey!");
                }
            } else {
                ConsoleLoggingModules.error("Missing blob!");
            }
        } else {
            ConsoleLoggingModules.error("Missing userName!");
        }
    }

    public void addSessionListener(@NotNull SessionListener listener) {
        this.sessionListeners.add(listener);
    }

    public void removeSessionListener(@NotNull SessionListener listener) {
        this.sessionListeners.remove(listener);
    }

    static {
        DEFAULT_GET_INFO_FIELDS.addProperty("status", 101);
        DEFAULT_GET_INFO_FIELDS.addProperty("statusString", "OK");
        DEFAULT_GET_INFO_FIELDS.addProperty("spotifyError", 0);
        DEFAULT_GET_INFO_FIELDS.addProperty("version", "2.7.1");
        DEFAULT_GET_INFO_FIELDS.addProperty("libraryVersion", Version.versionNumber());
        DEFAULT_GET_INFO_FIELDS.addProperty("accountReq", "PREMIUM");
        DEFAULT_GET_INFO_FIELDS.addProperty("brandDisplayName", "librespot-org");
        DEFAULT_GET_INFO_FIELDS.addProperty("modelDisplayName", "librespot-java");
        DEFAULT_GET_INFO_FIELDS.addProperty("voiceSupport", "NO");
        DEFAULT_GET_INFO_FIELDS.addProperty("availability", "");
        DEFAULT_GET_INFO_FIELDS.addProperty("productID", 0);
        DEFAULT_GET_INFO_FIELDS.addProperty("tokenType", "default");
        DEFAULT_GET_INFO_FIELDS.addProperty("groupStatus", "NONE");
        DEFAULT_GET_INFO_FIELDS.addProperty("resolverVersion", "0");
        DEFAULT_GET_INFO_FIELDS.addProperty("scope", "streaming,client-authorization-universal");
        DEFAULT_SUCCESSFUL_ADD_USER.addProperty("status", 101);
        DEFAULT_SUCCESSFUL_ADD_USER.addProperty("spotifyError", 0);
        DEFAULT_SUCCESSFUL_ADD_USER.addProperty("statusString", "OK");
        Utils.removeCryptographyRestrictions();
    }

    private class HttpRunner implements Runnable, Closeable {
        private final ServerSocket serverSocket;
        private final ExecutorService executorService = Executors.newCachedThreadPool(new NameThreadFactory((r) -> {
            return "zeroconf-client-" + r.hashCode();
        }));
        private volatile boolean shouldStop = false;

        HttpRunner(int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
            ConsoleLoggingModules.info("Zeroconf HTTP server started successfully on port {}!", port);
        }

        public void run() {
            while(!this.shouldStop) {
                try {
                    Socket socket = this.serverSocket.accept();
                    this.executorService.execute(() -> {
                        try {
                            this.handle(socket);
                            socket.close();
                        } catch (IOException var3) {
                            IOException ex = var3;
                            ConsoleLoggingModules.error("Failed handling request!", ex);
                        }

                    });
                } catch (IOException var2) {
                    IOException ex = var2;
                    if (!this.shouldStop) {
                        ConsoleLoggingModules.error("Failed handling connection!", ex);
                    }
                }
            }

        }

        private void handleRequest(@NotNull OutputStream out, @NotNull String httpVersion, @NotNull String action, @Nullable Map<String, String> params) {
            if (Objects.equals(action, "addUser")) {
                if (params == null) {
                    throw new IllegalArgumentException();
                }

                try {
                    ZeroconfServer.this.handleAddUser(out, params, httpVersion);
                } catch (IOException | GeneralSecurityException var7) {
                    Exception exx = var7;
                    ConsoleLoggingModules.error("Failed handling addUser!", exx);
                }
            } else if (Objects.equals(action, "getInfo")) {
                try {
                    ZeroconfServer.this.handleGetInfo(out, httpVersion);
                } catch (IOException var6) {
                    IOException ex = var6;
                    ConsoleLoggingModules.error("Failed handling getInfo!", ex);
                }
            } else {
                ConsoleLoggingModules.warning("Unknown action: " + action);
            }

        }

        private void handle(@NotNull Socket socket) throws IOException {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            OutputStream out = socket.getOutputStream();
            String[] requestLine = Utils.split(Utils.readLine(in), ' ');
            if (requestLine.length != 3) {
                ConsoleLoggingModules.warning("Unexpected request line: " + Arrays.toString(requestLine));
            } else {
                String method = requestLine[0];
                String path = requestLine[1];
                String httpVersion = requestLine[2];
                Map<String, String> headers = new HashMap();

                String header;
                while(!(header = Utils.readLine(in)).isEmpty()) {
                    String[] split = Utils.split(header, ':');
                    headers.put(split[0], split[1].trim());
                }

                if (!ZeroconfServer.this.hasValidSession()) {
                    ConsoleLoggingModules.info("Handling request: {} {} {}, headers: {}", new Object[]{method, path, httpVersion, headers});
                }

                String action;
                Object params;
                if (Objects.equals(method, "POST")) {
                    action = (String)headers.get("Content-Type");
                    if (!Objects.equals(action, "application/x-www-form-urlencoded")) {
                        ConsoleLoggingModules.error("Bad Content-Type: " + action);
                        return;
                    }

                    String contentLengthStr = (String)headers.get("Content-Length");
                    if (contentLengthStr == null) {
                        ConsoleLoggingModules.error("Missing Content-Length header!");
                        return;
                    }

                    int contentLength = Integer.parseInt(contentLengthStr);
                    byte[] body = new byte[contentLength];
                    in.readFully(body);
                    String bodyStr = new String(body);
                    String[] pairs = Utils.split(bodyStr, '&');
                    params = new HashMap(pairs.length);
                    String[] var17 = pairs;
                    int var18 = pairs.length;

                    for(int var19 = 0; var19 < var18; ++var19) {
                        String pair = var17[var19];
                        String[] splitx = Utils.split(pair, '=');
                        ((Map)params).put(URLDecoder.decode(splitx[0], "UTF-8"), URLDecoder.decode(splitx[1], "UTF-8"));
                    }
                } else {
                    params = ZeroconfServer.parsePath(path);
                }

                action = (String)((Map)params).get("action");
                if (action == null) {
                    ConsoleLoggingModules.debug("Request is missing action.");
                } else {
                    this.handleRequest(out, httpVersion, action, (Map)params);
                }
            }
        }

        public void close() throws IOException {
            this.shouldStop = true;
            this.serverSocket.close();
            this.executorService.shutdown();
        }
    }

    private static class Inner {
        final Random random = new SecureRandom();
        final Connect.DeviceType deviceType;
        final String deviceName;
        final String deviceId;
        final String preferredLocale;
        final Session.Configuration conf;

        Inner(@NotNull Connect.@NotNull DeviceType deviceType, @NotNull String deviceName, @Nullable String deviceId, @NotNull String preferredLocale, @NotNull Session.@NotNull Configuration conf) {
            this.deviceType = deviceType;
            this.deviceName = deviceName;
            this.preferredLocale = preferredLocale;
            this.conf = conf;
            this.deviceId = deviceId != null && !deviceId.isEmpty() ? deviceId : Utils.randomHexString(this.random, 40).toLowerCase();
        }
    }

    public static class Builder extends Session.AbsBuilder<Builder> {
        private boolean listenAll = true;
        private int listenPort = -1;
        private String[] listenInterfaces = null;

        public Builder(Session.@NotNull Configuration conf) {
            super(conf);
        }

        public Builder() {
        }

        public Builder setListenAll(boolean listenAll) {
            this.listenAll = listenAll;
            this.listenInterfaces = null;
            return this;
        }

        public Builder setListenPort(int listenPort) {
            this.listenPort = listenPort;
            return this;
        }

        public Builder setListenInterfaces(@NotNull String[] listenInterfaces) {
            this.listenAll = false;
            this.listenInterfaces = listenInterfaces;
            return this;
        }

        public @NonNls ZeroconfServer create() throws IOException {
            return new ZeroconfServer(new Inner(this.deviceType, this.deviceName, this.deviceId, this.preferredLocale, this.conf), this.listenPort, this.listenAll, this.listenInterfaces);
        }
    }

    public interface SessionListener {
        void sessionClosing(@NotNull Session var1);

        void sessionChanged(@NotNull Session var1);
    }
}
