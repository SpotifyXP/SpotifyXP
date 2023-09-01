package com.spotifyxp.webController;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class HttpService {
    Thread httpThread;
    int port = 790;
    com.sun.net.httpserver.HttpServer server;

    public HttpService() {
        createThread();
    }

    void createThread() {
        httpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                createHttpServer();
            }
        });
        httpThread.start();
        while (server == null) {
            try {
                Thread.sleep(99);
            } catch (InterruptedException ignored) {
            }
        }
    }

    ArrayList<String> ips = new ArrayList<>();
    boolean enablePassAuth = true;

    void createHttpServer() {
        get_SHA_512_SecurePassword("test");
        try {
            server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/playpause", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        PublicValues.spotifyplayer.playPause();
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            PublicValues.spotifyplayer.playPause();
                            cors(exchange);
                            HttpUtils.sendResource("OK.html", exchange);
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/search", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                        String out = PublicValues.elevated.makeGet("https://api.spotify.com/v1/search?type=track&q=" + exchange.getRequestURI().toString().split("\\?key=")[1] + "market=" + ContentPanel.countryCode + "&limit=6");
                        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                            exchange.sendResponseHeaders(204, -1);
                        }
                        OutputStream os = exchange.getResponseBody();
                        os.write(out.getBytes());
                        os.close();
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                            String out = PublicValues.elevated.makeGet("https://api.spotify.com/v1/search?type=track&q=" + exchange.getRequestURI().toString().split("\\?key=")[1] + "market=" + ContentPanel.countryCode + "&limit=6");
                            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                                exchange.sendResponseHeaders(204, -1);
                            }
                            OutputStream os = exchange.getResponseBody();
                            os.write(out.getBytes());
                            os.close();
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/load", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        try {
                            PublicValues.spotifyplayer.load(Factory.getSpotifyApi().searchTracks(exchange.getRequestURI().toString().split("\\?key=")[1]).build().execute().getItems()[0].getUri(), true, ContentPanel.shuffle, false);
                        } catch (Exception ignored) {
                        }
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            cors(exchange);
                            HttpUtils.sendResource("OK.html", exchange);
                            try {
                                PublicValues.spotifyplayer.load(Factory.getSpotifyApi().searchTracks(exchange.getRequestURI().toString().split("\\?key=")[1]).build().execute().getItems()[0].getUri(), true, ContentPanel.shuffle, false);
                            } catch (Exception ignored) {
                            }
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/doLogin", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    try {
                        if (exchange.getRequestURI().toString().split("\\?key=")[1].equals(get_SHA_512_SecurePassword(PublicValues.config.get(ConfigValues.password.name)))) {
                            HttpUtils.sendResource("OK.html", exchange);
                            ips.add(String.valueOf(exchange.getRemoteAddress().getAddress().toString()));
                        } else {
                            HttpUtils.sendResource("OK.html", exchange);
                        }
                    } catch (Exception e) {
                        HttpUtils.sendResource("OK.html", exchange);
                    }
                }
            });
            server.createContext("/doLeave", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    if(ips.isEmpty()) {
                        return;
                    }
                    System.out.println("Deauth: " + exchange.getRemoteAddress().getAddress().toString());
                    ips.remove(exchange.getRemoteAddress().getAddress().toString());
                }
            });
            server.createContext("/volumeup", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        if (!(ContentPanel.playerareavolumeslider.getValue() == 10)) {
                            ContentPanel.playerareavolumeslider.setValue(ContentPanel.playerareavolumeslider.getValue() + 1);
                        }
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            cors(exchange);
                            HttpUtils.sendResource("OK.html", exchange);
                            if (!(ContentPanel.playerareavolumeslider.getValue() == 10)) {
                                ContentPanel.playerareavolumeslider.setValue(ContentPanel.playerareavolumeslider.getValue() + 1);
                            }
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/volumedown", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        if (!(ContentPanel.playerareavolumeslider.getValue() == 0)) {
                            ContentPanel.playerareavolumeslider.setValue(ContentPanel.playerareavolumeslider.getValue() - 1);
                        }
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            cors(exchange);
                            HttpUtils.sendResource("OK.html", exchange);
                            if (!(ContentPanel.playerareavolumeslider.getValue() == 0)) {
                                ContentPanel.playerareavolumeslider.setValue(ContentPanel.playerareavolumeslider.getValue() - 1);
                            }
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/next", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        PublicValues.spotifyplayer.next();
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            cors(exchange);
                            HttpUtils.sendResource("OK.html", exchange);
                            PublicValues.spotifyplayer.next();
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/prev", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        PublicValues.spotifyplayer.previous();
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            cors(exchange);
                            HttpUtils.sendResource("OK.html", exchange);
                            PublicValues.spotifyplayer.previous();
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/apikey", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        cors(exchange);
                        HttpUtils.sendHTML(Factory.getSpotifyApi().getAccessToken(), exchange);
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            cors(exchange);
                            HttpUtils.sendHTML(Factory.getSpotifyApi().getAccessToken(), exchange);
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/loadId", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        PublicValues.spotifyplayer.load(exchange.getRequestURI().toString().split("\\?key=")[1], true, ContentPanel.shuffle, false);
                    }else{
                        if (ips.isEmpty()) {
                            HttpUtils.sendResource("auth.html", exchange);
                            return;
                        }
                        if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                            cors(exchange);
                            HttpUtils.sendResource("OK.html", exchange);
                            PublicValues.spotifyplayer.load(exchange.getRequestURI().toString().split("\\?key=")[1], true, ContentPanel.shuffle, false);
                        } else {
                            HttpUtils.sendResource("auth.html", exchange);
                        }
                    }
                }
            });
            server.createContext("/testConnection", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                }
            });
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    if(enablePassAuth) {
                        if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                            //Skipping password authentication for localhost
                            if (!exchange.getRequestURI().toString().equals("/") && !exchange.getRequestURI().toString().equals("/favicon.ico")) {
                                HttpUtils.sendResource(exchange.getRequestURI().toString().replaceFirst("/", ""), exchange);
                            } else {
                                cors(exchange);
                                HttpUtils.sendResource("index.html", exchange);
                            }
                        }else {
                            if (ips.isEmpty()) {
                                HttpUtils.sendResource("auth.html", exchange);
                                return;
                            }
                            if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                                if (!exchange.getRequestURI().toString().equals("/") && !exchange.getRequestURI().toString().equals("/favicon.ico")) {
                                    HttpUtils.sendResource(exchange.getRequestURI().toString().replaceFirst("/", ""), exchange);
                                } else {
                                    cors(exchange);
                                    HttpUtils.sendResource("index.html", exchange);
                                }
                            } else {
                                HttpUtils.sendResource("auth.html", exchange);
                            }
                        }
                    }else{
                        if (!exchange.getRequestURI().toString().equals("/") && !exchange.getRequestURI().toString().equals("/favicon.ico")) {
                            HttpUtils.sendResource(exchange.getRequestURI().toString().replaceFirst("/", ""), exchange);
                        } else {
                            cors(exchange);
                            HttpUtils.sendResource("index.html", exchange);
                        }
                    }
                }
            });
            server.start();
        } catch (Exception ignored) {
        }
    }

    public String get_SHA_512_SecurePassword(String passwordToHash){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            enablePassAuth = false;
            ConsoleLogging.warning("It seems like java doesn't like SHA512 disabling secure connection for SpotifyXP Web Control");
            return "";
        }
        return generatedPassword;
    }

    void cors(HttpExchange exchange) {
        try {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                exchange.sendResponseHeaders(204, -1);
            }
        } catch (Exception ignored) {
        }
    }

    public void stop() {
        server.stop(0);
    }
}
