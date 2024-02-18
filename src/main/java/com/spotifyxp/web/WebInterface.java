package com.spotifyxp.web;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.panels.PlayerArea;
import com.spotifyxp.utils.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class WebInterface {
    Thread httpThread;
    final int port = 790;
    HttpServer server;

    public WebInterface() {
        createThread();
    }

    void createThread() {
        httpThread = new Thread(this::createHttpServer);
        httpThread.start();
    }

    final ArrayList<String> ips = new ArrayList<>();
    boolean enablePassAuth = true;

    void createHttpServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/playpause", exchange -> {
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
            });
            server.createContext("/search", exchange -> {
                if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                    String out = PublicValues.elevated.makeGet("https://api.spotify.com/v1/search?type=track&q=" + exchange.getRequestURI().toString().split("\\?key=")[1] + "market=" + PublicValues.countryCode + "&limit=6");
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
                        String out = PublicValues.elevated.makeGet("https://api.spotify.com/v1/search?type=track&q=" + exchange.getRequestURI().toString().split("\\?key=")[1] + "market=" + PublicValues.countryCode + "&limit=6");
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
            });
            server.createContext("/load", exchange -> {
                if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    try {
                        PublicValues.spotifyplayer.load(Factory.getSpotifyApi().searchTracks(exchange.getRequestURI().toString().split("\\?key=")[1]).build().execute().getItems()[0].getUri(), true, PublicValues.shuffle, false);
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
                            PublicValues.spotifyplayer.load(Factory.getSpotifyApi().searchTracks(exchange.getRequestURI().toString().split("\\?key=")[1]).build().execute().getItems()[0].getUri(), true, PublicValues.shuffle, false);
                        } catch (Exception ignored) {
                        }
                    } else {
                        HttpUtils.sendResource("auth.html", exchange);
                    }
                }
            });
            server.createContext("/doLogin", exchange -> {
                cors(exchange);
                try {
                    if (exchange.getRequestURI().toString().split("\\?key=")[1].equals(get_SHA_512_SecurePassword(PublicValues.config.getString(ConfigValues.password.name)))) {
                        HttpUtils.sendResource("OK.html", exchange);
                        ips.add(String.valueOf(exchange.getRemoteAddress().getAddress().toString()));
                    } else {
                        HttpUtils.sendResource("OK.html", exchange);
                    }
                } catch (Exception e) {
                    HttpUtils.sendResource("OK.html", exchange);
                }
            });
            server.createContext("/doLeave", exchange -> {
                cors(exchange);
                HttpUtils.sendResource("OK.html", exchange);
                if(ips.isEmpty()) {
                    return;
                }
                ConsoleLoggingModules.debug("Deauth: " + exchange.getRemoteAddress().getAddress().toString());
                ips.remove(exchange.getRemoteAddress().getAddress().toString());
            });
            server.createContext("/volumeup", exchange -> {
                if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    if (!(PlayerArea.playerareavolumeslider.getValue() == 10)) {
                        PlayerArea.playerareavolumeslider.setValue(PlayerArea.playerareavolumeslider.getValue() + 1);
                    }
                }else{
                    if (ips.isEmpty()) {
                        HttpUtils.sendResource("auth.html", exchange);
                        return;
                    }
                    if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        if (!(PlayerArea.playerareavolumeslider.getValue() == 10)) {
                            PlayerArea.playerareavolumeslider.setValue(PlayerArea.playerareavolumeslider.getValue() + 1);
                        }
                    } else {
                        HttpUtils.sendResource("auth.html", exchange);
                    }
                }
            });
            server.createContext("/volumedown", exchange -> {
                if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    if (!(PlayerArea.playerareavolumeslider.getValue() == 0)) {
                        PlayerArea.playerareavolumeslider.setValue(PlayerArea.playerareavolumeslider.getValue() - 1);
                    }
                }else{
                    if (ips.isEmpty()) {
                        HttpUtils.sendResource("auth.html", exchange);
                        return;
                    }
                    if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        if (!(PlayerArea.playerareavolumeslider.getValue() == 0)) {
                            PlayerArea.playerareavolumeslider.setValue(PlayerArea.playerareavolumeslider.getValue() - 1);
                        }
                    } else {
                        HttpUtils.sendResource("auth.html", exchange);
                    }
                }
            });
            server.createContext("/next", exchange -> {
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
            });
            server.createContext("/prev", exchange -> {
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
            });
            server.createContext("/apikey", exchange -> {
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
            });
            server.createContext("/loadId", exchange -> {
                if(exchange.getRemoteAddress().getAddress().toString().startsWith("/127")) {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    PublicValues.spotifyplayer.load(exchange.getRequestURI().toString().split("\\?key=")[1], true, PublicValues.shuffle, false);
                }else{
                    if (ips.isEmpty()) {
                        HttpUtils.sendResource("auth.html", exchange);
                        return;
                    }
                    if (ips.contains(exchange.getRemoteAddress().getAddress().toString())) {
                        cors(exchange);
                        HttpUtils.sendResource("OK.html", exchange);
                        PublicValues.spotifyplayer.load(exchange.getRequestURI().toString().split("\\?key=")[1], true, PublicValues.shuffle, false);
                    } else {
                        HttpUtils.sendResource("auth.html", exchange);
                    }
                }
            });
            server.createContext("/testConnection", exchange -> {
                cors(exchange);
                HttpUtils.sendResource("OK.html", exchange);
            });
            server.createContext("/", exchange -> {
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
            });
            server.start();
        } catch (Exception ignored) {
        }
    }

    public String get_SHA_512_SecurePassword(String passwordToHash){
        String generatedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
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
