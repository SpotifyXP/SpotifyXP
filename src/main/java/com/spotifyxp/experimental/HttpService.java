package com.spotifyxp.experimental;

import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
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
            server.createContext("/play", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    PublicValues.spotifyplayer.play();
                    HttpUtils.sendResource("OK.html", exchange);
                }
            });
            server.createContext("/stop", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    cors(exchange);
                    PublicValues.spotifyplayer.pause();
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
