package com.spotifyxp.lib;


import com.spotifyxp.PublicValues;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;

public class libBrowser {
    String url;
    CefApp app;
    CefBrowser browser;
    CefClient client;
    JFrame frame;
    boolean windowVisible = false;
    public libBrowser(String url, JFrame frame) {
        this.url = url;
        this.frame = frame;
        if(!System.getProperty("os.name").toLowerCase().startsWith("win")) {
            throw new UnknownError("Unsupported Operating System");
        }
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = false;
        app = CefApp.getInstance(settings);
        client = app.createClient();
        browser = client.createBrowser(url, false, false);
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {

            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) {

            }

            @Override
            public void componentShown(ComponentEvent componentEvent) {
                windowVisible = true;
            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {
                windowVisible = false;
            }
        });
        startHandleKeyEvents();
    }

    void startHandleKeyEvents() {
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
        try {
            keyboardHook.addKeyListener(new GlobalKeyListener() {
                @Override
                public void keyPressed(GlobalKeyEvent event) {
                }
                @Override
                public void keyReleased(GlobalKeyEvent event) {
                    if(!windowVisible && !frame.isFocused()) {
                        return;
                    }
                    if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_F5) {
                        browser.reloadIgnoreCache();
                    }
                    if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_F11) {
                        toggleDevTools();
                    }
                }
            });
        }catch (IllegalStateException e) {
            keyboardHook.shutdownHook();
        }
    }

    public CefBrowser getBrowser() {
        return browser;
    }

    boolean devtoolsOpen = false;
    JFrame DevToolsWindow;

    public void openDevTools() {
        if(devtoolsOpen) {
            return;
        }
        JFrame frame = new JFrame("SpotifyXP libBrowser - Devtools (" + browser.getURL() + ")");
        frame.getContentPane().add(browser.getDevTools().getUIComponent());
        frame.setPreferredSize(new Dimension(200, 100));
        frame.setVisible(true);
        frame.pack();
        DevToolsWindow = frame;
        devtoolsOpen = true;
    }

    public void toggleDevTools() {
        if(devtoolsOpen) {
            closeDevTools();
        }else{
            openDevTools();
        }
    }

    public void closeDevTools() {
        DevToolsWindow = null;
        devtoolsOpen = false;
    }

    public Component getComponent() {
        return browser.getUIComponent();
    }
}
