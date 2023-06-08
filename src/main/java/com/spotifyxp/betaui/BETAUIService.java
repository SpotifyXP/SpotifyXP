package com.spotifyxp.betaui;

import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.WebUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class BETAUIService implements HttpHandler {
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(6969), 0);
                server.createContext("/", BETAUIService.this);
                server.start();
            }catch (Exception e) {
                ConsoleLogging.Throwable(e);
                ExceptionDialog.open(e);
            }
        }
    };
    DefThread thread;
    public BETAUIService() {
        thread = new DefThread(runnable);
        thread.start();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Handling => " + httpExchange.getRequestURI().toString());
        if(new File("debugnewui").exists()) {
            if(new File("debugnewui", httpExchange.getRequestURI().toString()).exists()) {
                WebUtils.sendFile(httpExchange, 200, "debugnewui/" + httpExchange.getRequestURI().toString().replaceFirst("/", ""));
            }else{
                WebUtils.sendFile(httpExchange, 404, "debugnewui/notfound.html");
            }
        }else {
            if (new Resources(true).readToInputStream("newui/" + httpExchange.getRequestURI().toString().replaceFirst("/", "")) != null) {
                WebUtils.sendFileStream(httpExchange, 200, new Resources(true).readToInputStream("newui/" + httpExchange.getRequestURI().toString().replaceFirst("/", "")));
            } else {
                WebUtils.sendFile(httpExchange, 404, "newui/notfound.html");
            }
        }
    }
}
