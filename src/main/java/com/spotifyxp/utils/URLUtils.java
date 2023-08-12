package com.spotifyxp.utils;


import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class URLUtils {
    public static String getURLResponseAsString(String url) {
        ConsoleLogging.debug("Try to connect to: " + url);
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }
    public static boolean isURLReachable(String url) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(url.split(":")[0], Integer.parseInt(url.split(":")[1])), 6);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public static boolean isURLReachable(String url, int port) {
        try {
            HttpClient client = new HttpClient();
            GetMethod get = new GetMethod(url + ":" + port);
            client.executeMethod(get);
            return true;
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return false;
        }
    }
}
