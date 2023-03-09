package com.spotifyxp.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TimerTask;

@SuppressWarnings("CanBeFinal")
public class SystemTrayDialog {
    PopupMenu trayPopupMenu = null;
    String toolt = null;
    SystemTray systemTray = null;
    Image image = null;
    TrayIcon trayIcon = null;
    boolean calledadd = false;
    boolean calledopen = false;
    int eventCnt = 0;
    java.util.Timer timer = new java.util.Timer("doubleClickTimer", false);
    public void add(String trayicon, String tooltip) {
        calledadd = true;
        systemTray = SystemTray.getSystemTray();
        image = Toolkit.getDefaultToolkit().getImage(trayicon);
        trayPopupMenu = new PopupMenu();
        toolt = tooltip;
    }
    public void add(ImageIcon trayicon, String tooltip) {
        calledadd = true;
        systemTray = SystemTray.getSystemTray();
        image = trayicon.getImage();
        trayPopupMenu = new PopupMenu();
        toolt = tooltip;
    }
    public void addEntry(String name, ActionListener onclick) {
        if(calledadd) {
            MenuItem action = new MenuItem(name);
            action.addActionListener(onclick);
            trayPopupMenu.add(action);
        }
    }
    public void open(ActionListener ondoubleclick) {
        if(!calledopen) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt, trayPopupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(ondoubleclick);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
        }
    }
    public void open(MouseAdapter onclick) {
        if(!calledopen) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt, trayPopupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(onclick);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
        }
    }
    public void open(ActionListener ondoubleclick, MouseAdapter onclick) {
        if(!calledopen) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt, trayPopupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(ondoubleclick);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    eventCnt = e.getClickCount();
                    if (e.getClickCount() == 1) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (eventCnt == 1) {
                                    onclick.mouseClicked(e);
                                }
                                eventCnt = 0;
                            }
                        }, 400);
                    }
                }
            });
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
        }
    }
    public void open() {
        if(!calledopen) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt, trayPopupMenu);
            trayIcon.setImageAutoSize(true);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
        }
    }

    public PopupMenu getTrayPopupMenu() {
        if(calledopen) {
            return trayPopupMenu;
        }else{
            return null;
        }
    }

    public SystemTray getSystemTray() {
        if(calledopen) {
            return systemTray;
        }else{
            return null;
        }
    }

    public TrayIcon getTrayIcon() {
        if(calledopen) {
            return trayIcon;
        }else{
            return null;
        }
    }
}
