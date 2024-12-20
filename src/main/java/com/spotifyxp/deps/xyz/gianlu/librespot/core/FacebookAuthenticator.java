/*
 * Copyright 2021 devgianlu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spotifyxp.deps.xyz.gianlu.librespot.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.ByteString;
import com.spotifyxp.deps.com.spotify.Authentication;
import com.spotifyxp.deps.xyz.gianlu.librespot.Version;
import com.spotifyxp.deps.xyz.gianlu.librespot.common.NetUtils;
import com.spotifyxp.deps.xyz.gianlu.librespot.common.Utils;
import com.spotifyxp.logging.ConsoleLoggingModules;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * @author Gianlu
 */
public final class FacebookAuthenticator implements Closeable {
    private static final URL LOGIN_SPOTIFY;
    private static final byte[] EOL = new byte[]{'\r', '\n'};

    static {
        try {
            LOGIN_SPOTIFY = new URL("https://login2.spotify.com/v1/config");
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private final String credentialsUrl;
    private final Object credentialsLock = new Object();
    private HttpPolling polling;
    private Authentication.LoginCredentials credentials = null;

    FacebookAuthenticator() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) LOGIN_SPOTIFY.openConnection();
        try (Reader reader = new InputStreamReader(conn.getInputStream())) {
            conn.connect();
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            credentialsUrl = obj.get("credentials_url").getAsString();
            String loginUrl = obj.get("login_url").getAsString();
            ConsoleLoggingModules.info("Visit {} in your browser.", loginUrl);
            startPolling();
        } finally {
            conn.disconnect();
        }
    }

    private void startPolling() throws IOException {
        polling = new HttpPolling();
        new Thread(polling, "facebook-auth-polling").start();
    }

    @NotNull
    Authentication.LoginCredentials lockUntilCredentials() throws InterruptedException {
        synchronized (credentialsLock) {
            credentialsLock.wait();
            return credentials;
        }
    }

    @Override
    public void close() throws IOException {
        if (polling != null) polling.stop();
    }

    private void authData(@NotNull String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        if (!obj.get("error").isJsonNull()) {
            ConsoleLoggingModules.error("Error during authentication: " + obj.get("error"));
            return;
        }

        JsonObject data = obj.getAsJsonObject("credentials");
        credentials = Authentication.LoginCredentials.newBuilder()
                .setUsername(data.get("username").getAsString())
                .setTyp(Authentication.AuthenticationType.forNumber(data.get("auth_type").getAsInt()))
                .setAuthData(ByteString.copyFrom(Utils.fromBase64(data.get("encoded_auth_blob").getAsString())))
                .build();

        synchronized (credentialsLock) {
            credentialsLock.notifyAll();
        }
    }

    private class HttpPolling implements Runnable {
        private final String host;
        private final String path;
        private final Socket socket;
        private volatile boolean shouldStop = false;

        HttpPolling() throws IOException {
            URL url = new URL(credentialsUrl);
            path = url.getPath() + "?" + url.getQuery();
            host = url.getHost();

            socket = SSLSocketFactory.getDefault().createSocket(host, url.getDefaultPort());
        }

        private void stop() throws IOException {
            shouldStop = true;
            socket.close();
        }

        @Override
        public void run() {
            try {
                OutputStream out = socket.getOutputStream();
                DataInputStream in = new DataInputStream(socket.getInputStream());

                while (!shouldStop) {
                    out.write("GET ".getBytes());
                    out.write(path.getBytes());
                    out.write(" HTTP/1.1".getBytes());
                    out.write(EOL);
                    out.write("Host: ".getBytes());
                    out.write(host.getBytes());
                    out.write(EOL);
                    out.write("User-Agent: ".getBytes());
                    out.write(Version.versionString().getBytes());
                    out.write(EOL);
                    out.write("Accept: */*".getBytes());
                    out.write(EOL);
                    out.write(EOL);
                    out.flush();

                    NetUtils.StatusLine sl = NetUtils.parseStatusLine(Utils.readLine(in));
                    int length = 0;
                    String header;
                    while (!(header = Utils.readLine(in)).isEmpty()) {
                        if (header.startsWith("Content-Length") && sl.statusCode == 200)
                            length = Integer.parseInt(header.substring(16));
                    }

                    if (sl.statusCode == 200) {
                        String json;
                        if (length != 0) {
                            byte[] buffer = new byte[length];
                            in.readFully(buffer);
                            json = new String(buffer);
                        } else {
                            json = Utils.readLine(in);
                        }

                        ConsoleLoggingModules.debug("Received authentication data: " + json);
                        authData(json);
                        break;
                    }
                }
            } catch (IOException ex) {
                ConsoleLoggingModules.error("Failed polling Spotify credentials URL!", ex);
            }
        }
    }
}
