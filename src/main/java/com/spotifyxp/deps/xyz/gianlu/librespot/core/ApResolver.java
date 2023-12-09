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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.utils.GraphicalMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Gianlu
 */
@SuppressWarnings("resource")
public final class ApResolver {
    private static final String BASE_URL = "http://apresolve.spotify.com/";

    private final OkHttpClient client;
    private final Map<String, List<String>> pool = new HashMap<>(3);
    private volatile boolean poolReady = false;

    public ApResolver(OkHttpClient client) throws IOException {
        this.client = client;
        fillPool();
    }

    private void fillPool() throws IOException {
        request("accesspoint", "dealer", "spclient");
    }

    public void refreshPool() throws IOException {
        poolReady = false;
        pool.clear();
        fillPool();
    }

    @NotNull
    private static List<String> getUrls(@NotNull JsonObject body, @NotNull String type) {
        JsonArray aps = body.getAsJsonArray(type);
        List<String> list = new ArrayList<>(aps.size());
        for (JsonElement ap : aps) list.add(ap.getAsString());
        return list;
    }

    int times = 0;

    private void request(@NotNull String... types) throws IOException {
        if (types.length == 0) throw new IllegalArgumentException();

        StringBuilder url = new StringBuilder(BASE_URL + "?");
        for (int i = 0; i < types.length; i++) {
            if (i != 0) url.append("&");
            url.append("type=").append(types[i]);
        }

        Request request = new Request.Builder()
                .url(url.toString())
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (body == null) throw new IOException("No body");
            byte[] resp = IOUtils.toByteArray(body.byteStream());
            try {
                if (new String(resp).split("<title>")[1].replace("</title>", "").contains("502")) {
                    GraphicalMessage.sorryErrorExit("Spotify APResolve responded with 502");
                }
            }catch (ArrayIndexOutOfBoundsException e) {
                //200 OK
            }
            JsonObject obj = JsonParser.parseString(IOUtils.toString(resp)).getAsJsonObject();
            HashMap<String, List<String>> map = new HashMap<>();
            for (String type : types)
                map.put(type, getUrls(obj, type));

            synchronized (pool) {
                pool.putAll(map);
                poolReady = true;
                pool.notifyAll();
            }

            ConsoleLoggingModules.info("Loaded aps into pool: " + pool);
            times = 0;
        }catch (SocketTimeoutException e) {
            if(times > 10) {
                GraphicalMessage.sorryErrorExit("Socket timeout");
            }
            request(types);
            times++;
        }
    }

    private void waitForPool() {
        if (!poolReady) {
            synchronized (pool) {
                try {
                    pool.wait();
                } catch (InterruptedException ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }
    }

    boolean retry = false;
    public static boolean eof = false;
    boolean ret80 = true;
    int retryCounter = 0;

    private String returnPort80(String url, String type) {
        if(retryCounter > 15) {
            throw new RuntimeException("Can't find a suitable " + type);
        }
        retryCounter++;
        if(!isBlocked(url)) {
            return url;
        }else{
            ConsoleLoggingModules.warning("Port '" + url.split(":")[1] + "' is blocked");
        }
        if(ret80) {
            if (url.contains(":80") || url.contains(":443")) {
                if (isBlocked(url)) {
                    ConsoleLoggingModules.warning("Port 80 is blocked by your firewall! Trying other ports");
                    ret80 = false;
                    waitForPool();
                    List<String> urls = pool.get(type);
                    if (urls == null || urls.isEmpty()) throw new IllegalStateException();
                    return returnPort80(urls.get(ThreadLocalRandom.current().nextInt(urls.size())), type);
                }
                return url;
            } else {
                waitForPool();
                List<String> urls = pool.get(type);
                if (urls == null || urls.isEmpty()) throw new IllegalStateException();
                return returnPort80(urls.get(ThreadLocalRandom.current().nextInt(urls.size())), type);
            }
        }
        waitForPool();
        List<String> urls = pool.get(type);
        if (urls == null || urls.isEmpty()) throw new IllegalStateException();
        return returnPort80(urls.get(ThreadLocalRandom.current().nextInt(urls.size())), type);
    }

    public boolean isBlocked(String url) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(url.split(":")[0], Integer.parseInt(url.split(":")[1])));
        } catch (IOException e) {
            return true;
        }
        return false;
    }

    int counter = 0;

    @NotNull
    private String getRandomOf(@NotNull String type) {
        if(counter>200) {
            GraphicalMessage.sorryError();
        }
        waitForPool();

        List<String> urls = pool.get(type);
        if (urls == null || urls.isEmpty()) throw new IllegalStateException();
        try {
            return returnPort80(urls.get(ThreadLocalRandom.current().nextInt(urls.size())), type);
        }catch (StackOverflowError error) {
            //Just retry eventually it will work
            counter++;
            return returnPort80(urls.get(ThreadLocalRandom.current().nextInt(urls.size())), type);
        }
    }

    @NotNull
    public String getRandomDealer() {
        return getRandomOf("dealer");
    }

    @NotNull
    public String getRandomSpclient() {
        return getRandomOf("spclient");
    }

    @NotNull
    public String getRandomAccesspoint() {
        return getRandomOf("accesspoint");
    }
}
