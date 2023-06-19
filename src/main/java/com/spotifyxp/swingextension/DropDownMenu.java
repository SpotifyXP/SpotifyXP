package com.spotifyxp.swingextension;

import com.spotifyxp.panels.ContentPanel;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.*;

public class DropDownMenu {
    final JPopupMenu popupMenu = new JPopupMenu();

    int xcache = 0;
    int ycache = 0;

    public DropDownMenu(JImagePanel panel, boolean animate) {
        popupMenu.setLightWeightPopupEnabled(true);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                panel.setRotation(0);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                if(animate) {
                    if (!popupMenu.isVisible()) {
                        ycache = e.getY();
                        xcache = e.getX();
                        panel.setRotation(10);
                        popupMenu.show(ContentPanel.frame, panel.getX(), panel.getY()+panel.getHeight()*3-5);
                    }
                }else{
                    if (!popupMenu.isVisible()) {
                        ycache = e.getY();
                        xcache = e.getX();
                        popupMenu.show(ContentPanel.frame, panel.getX(), panel.getY()+panel.getHeight()*3-5);
                    }
                }
            }
        });
    }
    public DropDownMenu(JSVGPanel panel, boolean animate) {
        popupMenu.setLightWeightPopupEnabled(true);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                panel.setRotation(0);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });
        panel.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                if(animate) {
                    if (!popupMenu.isVisible()) {
                        ycache = e.getY();
                        xcache = e.getX();
                        panel.setRotation(10);
                        popupMenu.show(ContentPanel.frame, panel.getJComponent().getX(), panel.getJComponent().getY()+panel.getJComponent().getHeight()*3-5);
                    }
                }else{
                    if (!popupMenu.isVisible()) {
                        ycache = e.getY();
                        xcache = e.getX();
                        popupMenu.show(ContentPanel.frame, panel.getJComponent().getX(), panel.getJComponent().getY()+panel.getJComponent().getHeight()*3-5);
                    }
                }
            }
        });
    }
    public void addItem(String text, Runnable onClick) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(e -> onClick.run());
        popupMenu.add(item);
    }
}
