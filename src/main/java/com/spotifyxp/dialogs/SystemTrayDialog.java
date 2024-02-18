package com.spotifyxp.dialogs;

import com.spotifyxp.logging.ConsoleLogging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

@SuppressWarnings("CanBeFinal")
public class SystemTrayDialog {
    String toolt = null;
    SystemTray systemTray = null;
    Image image = null;
    TrayIcon trayIcon = null;
    boolean calledadd = false;
    boolean calledopen = false;
    int eventCnt = 0;
    java.util.Timer timer = new java.util.Timer("doubleClickTimer", false);
    @SuppressWarnings("Convert2Lambda")
    PopupMenu menu;

    public void add(String trayicon, String tooltip) {
        calledadd = true;
        systemTray = SystemTray.getSystemTray();
        image = Toolkit.getDefaultToolkit().getImage(trayicon);
        menu = new PopupMenu("TrayDialog menu");
        toolt = tooltip;
    }
    public void add(ImageIcon trayicon, String tooltip) {
        calledadd = true;
        systemTray = SystemTray.getSystemTray();
        image = trayicon.getImage();
        menu = new PopupMenu("TrayDialog menu");
        toolt = tooltip;
    }
    public void addEntry(String name, ActionListener onclick) {
        if(calledadd) {
            MenuItem action = new MenuItem(name);
            action.addActionListener(onclick);
            menu.add(action);
        }
    }
    public void open(ActionListener ondoubleclick) {
        if(!calledopen) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(ondoubleclick);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                ConsoleLogging.Throwable(awtException);
            }
        }
    }
    public void open(MouseAdapter adapter) {
        if(!calledopen) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt);
            trayIcon.setPopupMenu(menu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(adapter);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                ConsoleLogging.Throwable(awtException);
            }
        }
    }
}
