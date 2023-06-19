package com.spotifyxp.utils;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.io.Resource;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpUtils {
    public static boolean sendFile(String path, HttpExchange exchange) {
        try {
            File file = new File(path);
            exchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    public static boolean sendResource(String path, HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(200, IOUtils.toByteArray(new Resources().readToInputStream("html/" + path)).length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(IOUtils.toByteArray(new Resources().readToInputStream("html/" + path)));
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
