package com.spotifyxp.lib;


import com.spotifyxp.custom.DoubleArrayListForEach;
import com.spotifyxp.custom.StoppableThreadRunnable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.threading.StoppableThread;
import com.spotifyxp.utils.DoubleArrayList;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class libWebServer {
    int p = 0;
    HttpServer server = null;
    DoubleArrayList contexts = new DoubleArrayList();
    StoppableThread serverthread = null;
    public libWebServer(int port) {
        p = port;
    }
    StoppableThreadRunnable st = new StoppableThreadRunnable() {
        @Override
        public void run(int counter) {
            try {
                server = HttpServer.create(new InetSocketAddress(p), 0);
                server.setExecutor(null);
                contexts.forEach(new DoubleArrayListForEach() {
                    @Override
                    public void run(Object first, Object second) {
                        server.createContext((String) first, (HttpHandler) second);
                    }
                });
                server.start();
            }catch (IOException ex) {
                ConsoleLogging.Throwable(ex);
            }
        }
    };
    public void start() {
        if(!(serverthread==null)) {
            //The Server is already started
            return;
        }
        serverthread = new StoppableThread(st, false);
        serverthread.start();
    }
    public void addHttpContext(String path, HttpHandler handler) {
        contexts.add(path, handler);
    }
    public void stop() {
        server.stop(0);
    }
}
