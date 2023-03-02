package com.spotifyxp.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class WebUtils {
    public static void sendFile(HttpExchange exchange, int code, File file) {
        try {
            exchange.sendResponseHeaders(code, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
        }catch (IOException ex) {
        }
    }
    public static void sendFile(HttpExchange exchange, int code, String path) {
        try {
            File file = new File(path);
            exchange.sendResponseHeaders(code, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
        }catch (IOException ex) {
        }
    }
    public static void sendCode(HttpExchange exchange, int code, String htmlcode) {
        try {
            exchange.sendResponseHeaders(code, htmlcode.length());
            OutputStream os = exchange.getResponseBody();
            os.write(htmlcode.getBytes());
            os.close();
        }catch (IOException ex) {
        }
    }
}

