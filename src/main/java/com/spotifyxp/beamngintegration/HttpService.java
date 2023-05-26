package com.spotifyxp.beamngintegration;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Paging;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.HttpUtils;
import com.spotifyxp.utils.WebUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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
        while(server == null) {
            try {
                Thread.sleep(99);
            } catch (InterruptedException e) {
            }
        }
    }
    void createHttpServer() {
        try {
            server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/playpause", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    PublicValues.spotifyplayer.playPause();
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                }
            });
            server.createContext("/search", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                    String out = PublicValues.elevated.makeGet("https://api.spotify.com/v1/search?type=track&q=" + exchange.getRequestURI().toString().split("\\?key=")[1] + "market=" + ContentPanel.countryCode+ "&limit=6");
                    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                        exchange.sendResponseHeaders(204, -1);
                    }
                    System.out.println("OUt: " + out);
                    OutputStream os = exchange.getResponseBody();
                    os.write(out.getBytes());
                    os.close();
                }
            });
            server.createContext("/load", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    try {
                        PublicValues.spotifyplayer.load(ContentPanel.api.getSpotifyApi().searchTracks(exchange.getRequestURI().toString().split("\\?key=")[1]).build().execute().getItems()[0].getUri(), true, ContentPanel.shuffle);
                    }catch (Exception ignored) {
                    }
                }
            });
            server.createContext("/volumeup", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    if(!(ContentPanel.playerareavolumeslider.getValue() == 10)) {
                        ContentPanel.playerareavolumeslider.setValue(ContentPanel.playerareavolumeslider.getValue()+1);
                    }
                }
            });
            server.createContext("/volumedown", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    if(!(ContentPanel.playerareavolumeslider.getValue() == 0)) {
                        ContentPanel.playerareavolumeslider.setValue(ContentPanel.playerareavolumeslider.getValue() - 1);
                    }
                }
            });
            server.createContext("/next", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    PublicValues.spotifyplayer.next();
                }
            });
            server.createContext("/prev", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    HttpUtils.sendResource("OK.html", exchange);
                    PublicValues.spotifyplayer.previous();
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
                    if(!exchange.getRequestURI().toString().equals("/")) {
                        HttpUtils.sendResource(exchange.getRequestURI().toString().replaceFirst("/", ""), exchange);
                    }else {
                        cors(exchange);
                        HttpUtils.sendResource("index.html", exchange);
                    }
                }
            });
            server.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    void cors(HttpExchange exchange) {
        try {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                exchange.sendResponseHeaders(204, -1);
                return;
            }
        }catch (Exception e) {
        }
    }
    public void stop() {
        server.stop(0);
    }
}
