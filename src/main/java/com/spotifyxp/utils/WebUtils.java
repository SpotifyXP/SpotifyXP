package com.spotifyxp.utils;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class WebUtils {
    public static void sendFile(HttpExchange exchange, int code, File file) {
        try {
            exchange.sendResponseHeaders(code, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
        }catch (IOException ignored) {
        }
    }
    public static void sendFile(HttpExchange exchange, int code, String path) {
        try {
            File file = new File(path);
            exchange.sendResponseHeaders(code, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
        }catch (IOException ignored) {
        }
    }
    public static void sendCode(HttpExchange exchange, int code, String htmlcode) {
        try {
            exchange.sendResponseHeaders(code, htmlcode.length());
            OutputStream os = exchange.getResponseBody();
            os.write(htmlcode.getBytes());
            os.close();
        }catch (IOException ignored) {
        }
    }
    public static void sendFileStream(HttpExchange exchange, int code, InputStream stream) {
        try {
            exchange.sendResponseHeaders(code, stream.available());
            OutputStream os = exchange.getResponseBody();
            os.write(IOUtils.toByteArray(stream));
            os.close();
        }catch (IOException ignored) {
        }
    }
}

