package com.spotifyxp.api;

import ch.randelshofer.quaqua.ext.base64.Base64;
import com.google.common.hash.Hashing;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.enums.HttpStatusCodes;
import com.spotifyxp.utils.PlayerUtils;
import com.spotifyxp.utils.Token;
import com.spotifyxp.utils.WebUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;


public class RestAPI implements Runnable {
    HttpServer server;
    final Thread t = new Thread(this);
    final int port = 4090;
    static final ArrayList<String> authIps = new ArrayList<>();
    static String username;
    static String password;
    private static final String ALGORITHM = "AES";


    public RestAPI() {
        username = PublicValues.config.get(ConfigValues.username.name);
        password = PublicValues.config.get(ConfigValues.password.name);
        if(PublicValues.spotifyplayer != null) {
            return;
        }
        if(!PublicValues.config.get(ConfigValues.username.name).equalsIgnoreCase("")) {
            try {
                PublicValues.spotifyplayer = PlayerUtils.buildPlayer(true);
            } catch (Session.SpotifyAuthenticationException e) {
                username = "";
                password = "";
            }
        }
    }

    @Override
    public void run() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", RestAPI::handleRoot);
            server.start();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    RestAPI structure

    1. ?method=api.authenticate&username=[HASHED]&password=[HASHED]
    2. ?method=[methodname]

    Methods:

    api.authenticate
    api.authenticateinsecure
    api.getloginstatus
    api.getkey
    api.initlogin
    api.iplogout
    api.logout
    player.getmetadata
    player.playpause
    player.next
    player.previous
    player.load
    player.loadplay
    player.getvolume
    player.setvolume
    player.volumeup
    player.volumedown
    player.currenttime

    api.initlogin
    Pass the parameter 'secure=true' and hash the username and passwort AES with the password 'xm9AR48qKvtCKQUCNfcfhCUJ"'

    JSON Response

    {'Response':'[RESPONSE]', [MORE PARAMETERS]}

     */

    public static void handleRoot(HttpExchange exchange) {
        sendCors(exchange);
        sendAdditionalHeaders(exchange);
        try {
            ArrayList<BasicNameValuePair> params = parseParameters(exchange.getRequestURI().getQuery());
            String method = getParams(params, "method");
            switch (method) {
                case "api.authenticate":
                    handleAuthorization(exchange, params);
                    break;
                case "api.authenticateinsecure":
                    handleAuthorizationInsecure(exchange, params);
                    break;
                case "api.initlogin":
                    handleInitialLogin(exchange, params);
                    break;
                case "api.getloginstatus":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    if(username.equalsIgnoreCase("")) {
                        WebUtils.sendCode(exchange, 200, makeJSONResponse("notLoggedIn"));
                    }else{
                        WebUtils.sendCode(exchange, 200, makeJSONResponse("loggedIn"));
                    }
                    break;
                case "api.getkey":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    WebUtils.sendCode(exchange, 200, makeJSONResponse(Token.getDefaultToken()));
                    break;
                case "player.getmetadata":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    getMetaData(exchange);
                    break;
                case "player.playpause":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    playPause(exchange);
                    break;
                case "player.next":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    next(exchange);
                    break;
                case "player.previous":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    previous(exchange);
                    break;
                case "player.load":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    load(exchange, params);
                    break;
                case "player.loadplay":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    loadPlay(exchange, params);
                    break;
                case "player.getvolume":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    WebUtils.sendCode(exchange, 200, makeJSONResponse(String.valueOf(PublicValues.spotifyplayer.getVolume())));
                    break;
                case "player.setvolume":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    PublicValues.spotifyplayer.setVolume(Integer.parseInt(getParams(params, "volume")));
                    WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok", new BasicNameValuePair("Now", String.valueOf(PublicValues.spotifyplayer.getVolume()))));
                    break;
                case "player.volumeup":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    PublicValues.spotifyplayer.volumeUp();
                    WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok", new BasicNameValuePair("Now", String.valueOf(PublicValues.spotifyplayer.getVolume()))));
                    break;
                case "player.volumedown":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    PublicValues.spotifyplayer.volumeDown();
                    WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok", new BasicNameValuePair("Now", String.valueOf(PublicValues.spotifyplayer.getVolume()))));
                    break;
                case "player.currenttime":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                        break;
                    }
                    WebUtils.sendCode(exchange, 200, makeJSONResponse(String.valueOf(PublicValues.spotifyplayer.time())));
                    break;
                case "api.iplogout":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                    }
                    iplogout(exchange);
                    break;
                case "api.logout":
                    if(!hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                        sendUnauthorized(exchange);
                    }
                    logout(exchange);
                    break;
                default:
                    respondWithError(exchange);
                    break;
            }
        }catch (Exception e) {
            respondWithError(exchange);
        }
    }

    static void playPause(HttpExchange exchange) {
        PublicValues.spotifyplayer.playPause();
        WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok"));
    }

    static void next(HttpExchange exchange) {
        PublicValues.spotifyplayer.next();
        WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok"));
    }

    static void previous(HttpExchange exchange) {
        PublicValues.spotifyplayer.previous();
        WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok"));
    }

    static void load(HttpExchange exchange, ArrayList<BasicNameValuePair> pairs) throws Exception {
        PublicValues.spotifyplayer.load(getParams(pairs, "uri"), false, false, false);
        WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok"));
    }

    static void loadPlay(HttpExchange exchange, ArrayList<BasicNameValuePair> pairs) throws Exception {
        PublicValues.spotifyplayer.load(getParams(pairs, "uri"), true, false, false);
        WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok"));
    }

    static void getMetaData(HttpExchange exchange) {
        ArrayList<BasicNameValuePair> pairs = new ArrayList<>();
        try {
            pairs.add(new BasicNameValuePair("Duration", String.valueOf(Objects.requireNonNull(PublicValues.spotifyplayer.currentMetadata()).duration())));
        }catch (NullPointerException ignored) {
        }
        try {
            pairs.add(new BasicNameValuePair("Uri", Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri()));
        }catch (NullPointerException ignored) {
        }
        WebUtils.sendCode(exchange, 200, makeJSONResponse("Metadata", pairs));
    }

    static void handleAuthorization(HttpExchange exchange, ArrayList<BasicNameValuePair> pairs) throws Exception {
        String gotusername = getParams(pairs, "username");
        String gotpassword = getParams(pairs, "password");
        if(Hashing.sha512().hashString(username, Charset.defaultCharset()).toString().equals(gotusername)) {
            if(Hashing.sha512().hashString(password, Charset.defaultCharset()).toString().equals(gotpassword)) {
                if(hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                    WebUtils.sendCode(exchange, HttpStatusCodes.OK.getValue(), makeJSONResponse("LoggedIn", new BasicNameValuePair("User", PublicValues.session.username())));
                    return;
                }
                authIps.add(exchange.getRemoteAddress().getAddress().toString());
                WebUtils.sendCode(exchange, 200, makeJSONResponse("OK", new BasicNameValuePair("User", getUserID())));
                return;
            }
        }
        WebUtils.sendCode(exchange, HttpStatusCodes.BAD_REQUEST.getValue(), makeJSONResponse("Bad Credentials"));
    }

    static void handleInitialLogin(HttpExchange exchange, ArrayList<BasicNameValuePair> pairs) throws Exception {
        if(!PublicValues.config.get(ConfigValues.username.name).equalsIgnoreCase("")) {
            WebUtils.sendCode(exchange, HttpStatusCodes.BAD_REQUEST.getValue(), makeJSONResponse("LoggedIn", new BasicNameValuePair("User", PublicValues.session.username())));
            return;
        }
        PublicValues.config.write(ConfigValues.username.name, getParams(pairs, "username"));
        PublicValues.config.write(ConfigValues.password.name, getParams(pairs, "password"));
        try {
            PublicValues.spotifyplayer = PlayerUtils.buildPlayer(true);
            WebUtils.sendCode(exchange, HttpStatusCodes.OK.getValue(), makeJSONResponse("LoggedIn", new BasicNameValuePair("User", PublicValues.session.username())));
        } catch (Session.SpotifyAuthenticationException e) {
            WebUtils.sendCode(exchange, HttpStatusCodes.FORBIDDEN.getValue(), "BadCredentials");
        }
    }

    static void handleAuthorizationInsecure(HttpExchange exchange, ArrayList<BasicNameValuePair> pairs) throws Exception {
        String gotusername = getParams(pairs, "username");
        String gotpassword = getParams(pairs, "password");
        if(username.equals(gotusername)) {
            if(password.equals(gotpassword)) {
                if(hasIp(exchange.getRemoteAddress().getAddress().toString())) {
                    WebUtils.sendCode(exchange, HttpStatusCodes.LOCKED.getValue(), makeJSONResponse("LoggedIn"));
                    return;
                }
                authIps.add(exchange.getRemoteAddress().getAddress().toString());
                WebUtils.sendCode(exchange, 200, makeJSONResponse("OK"));
                return;
            }
        }
        WebUtils.sendCode(exchange, HttpStatusCodes.BAD_REQUEST.getValue(), makeJSONResponse("BadCredentials"));
    }

    static void sendUnauthorized(HttpExchange exchange) {
        WebUtils.sendCode(exchange, HttpStatusCodes.FORBIDDEN.getValue(), makeJSONResponse("Unauthorized"));
    }

    static String getUserID() {
        return PublicValues.session.username();
    }

    static boolean hasIp(String ip) {
        for(String s : authIps) {
            if(s.equalsIgnoreCase(ip)) return true;
        }
        return false;
    }

    static String makeJSONResponse(String response, BasicNameValuePair... pairs) {
        JSONObject object = new JSONObject();
        object.put("Response", response);
        for(BasicNameValuePair pair : pairs) {
            object.put(pair.getName(), pair.getValue());
        }
        return object.toString();
    }

    static String makeJSONResponse(String response, ArrayList<BasicNameValuePair> pairs) {
        JSONObject object = new JSONObject();
        object.put("Response", response);
        for(BasicNameValuePair pair : pairs) {
            object.put(pair.getName(), pair.getValue());
        }
        return object.toString();
    }

    static void iplogout(HttpExchange exchange) {
        authIps.remove(exchange.getRemoteAddress().getAddress().toString());
        WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok"));
    }

    static void logout(HttpExchange exchange) {
        PublicValues.config.write(ConfigValues.username.name, "");
        PublicValues.config.write(ConfigValues.password.name, "");
        WebUtils.sendCode(exchange, 200, makeJSONResponse("Ok"));
        System.exit(0);
    }

    static boolean contains(ArrayList<BasicNameValuePair> pairs, String toget) {
        for(BasicNameValuePair pair : pairs) {
            if(pair.getName().equalsIgnoreCase(toget)) return true;
        }
        return false;
    }

    static String getParams(ArrayList<BasicNameValuePair> pairs, String toget) throws Exception {
        for(BasicNameValuePair pair : pairs) {
            if(pair.getName().equalsIgnoreCase(toget)) return pair.getValue();
        }
        throw new Exception();
    }

    static ArrayList<BasicNameValuePair> parseParameters(String uri) throws Exception {
        ArrayList<BasicNameValuePair> pairs = new ArrayList<>();
        boolean secure = false;
        try {
            for (String s : uri.split("&")) {
                if(s.split("=")[0].equalsIgnoreCase("secure")) {
                    secure = true;
                    continue;
                }
                if(secure) {
                    pairs.add(new BasicNameValuePair(s.split("=")[0], decode(s.split(s.split("=")[0] + "=")[1])));
                }else {
                    pairs.add(new BasicNameValuePair(s.split("=")[0], s.split("=")[1]));
                }
            }
        }catch (Exception e) {
            throw new Exception();
        }
        return pairs;
    }

    static String decode(String todecode) throws Exception {
        return decrypt(todecode, "xm9AR48qKvtCKQUCNfcfhCUJ");
    }

    public static String decrypt(String encrypted, String password) throws Exception {
        int keySize = 8;
        int ivSize = 4;
        // Start by decoding the encrypted string (Base64)
        // Here I used the Android implementation (other Java implementations might exist)
        byte[] cipherText = Base64.decode(encrypted);
        // Extract salt (next 8 bytes)
        byte[] salt = new byte[8];
        System.arraycopy(cipherText, 8, salt, 0, 8);
        // Extract the actual cipher text (the rest of the bytes)
        byte[] trueCipherText = new byte[cipherText.length - 16];
        System.arraycopy(cipherText, 16, trueCipherText, 0, cipherText.length - 16);
        byte[] javaKey = new byte[keySize * 4];
        byte[] javaIv = new byte[ivSize * 4];
        evpKDF(password.getBytes(StandardCharsets.UTF_8), keySize, ivSize, salt, javaKey, javaIv);
        Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(javaIv);
        aesCipherForEncryption.init(Cipher.DECRYPT_MODE, new SecretKeySpec(javaKey, "AES"), ivSpec);

        byte[] byteMsg = aesCipherForEncryption.doFinal(trueCipherText);
        return new String(byteMsg, StandardCharsets.UTF_8);
    }

    public static byte[] evpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        return evpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }

    public static void sendCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
    }

    public static void sendAdditionalHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
    }

    public static byte[] evpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(password);
            block = hasher.digest(salt);
            hasher.reset();

            // Iterations
            for (int i = 1; i < iterations; i++) {
                block = hasher.digest(block);
                hasher.reset();
            }

            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                    Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));

            numberOfDerivedWords += block.length/4;
        }

        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);

        return derivedBytes; // key + iv
    }

    static void respondWithError(HttpExchange exchange) {
        WebUtils.sendCode(exchange, HttpStatusCodes.BAD_REQUEST.getValue(), makeJSONResponse("Bad Request"));
    }

    public void start() {
        t.start();
    }
}
