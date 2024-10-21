package com.spotifyxp.utils;


import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

public class ConnectionUtils {
    private static OkHttpClient client;

    public ConnectionUtils() {
        client = new OkHttpClient();
    }

    public static String makeGet(String url, @Nullable Map<String, String> headers) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", ApplicationUtils.getUserAgent())
                .get();
        if (headers != null) requestBuilder.headers(Headers.of(headers));
        return Objects.requireNonNull(client.newCall(requestBuilder.build()).execute().body()).string();
    }

    public static boolean isWebsiteReachable(String url) {
        try {
            makeGet(url, null);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String makePost(String url, NameValuePair[] topost, @Nullable Map<String, String> headers) throws Exception {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (NameValuePair pair : topost) {
            formBodyBuilder.add(pair.getName(), pair.getValue());
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", ApplicationUtils.getUserAgent())
                .post(formBodyBuilder.build());
        if (headers != null) requestBuilder.headers(Headers.of(headers));
        return Objects.requireNonNull(client.newCall(requestBuilder.build()).execute().body()).string();
    }

    public static String makeDelete(String url, @Nullable Map<String, String> headers) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .addHeader("User-Agent", ApplicationUtils.getUserAgent());
        if (headers != null) requestBuilder.headers(Headers.of(headers));
        return Objects.requireNonNull(client.newCall(requestBuilder.build()).execute().body()).string();
    }

    public static void openBrowser(String url) throws URISyntaxException, IOException {
        String browserpath = PublicValues.config.getString(ConfigValues.mypalpath.name);
        if (browserpath.isEmpty()) {
            JOptionPane.showConfirmDialog(ContentPanel.frame, "Please set the mypal path in settings", "Info", JOptionPane.OK_CANCEL_OPTION);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
            return;
        }
        ProcessBuilder builder = new ProcessBuilder("\"" + browserpath + "\"", url);
        try {
            builder.start();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
