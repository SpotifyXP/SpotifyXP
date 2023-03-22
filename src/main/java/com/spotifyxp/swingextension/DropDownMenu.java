package com.spotifyxp.swingextension;

import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import javafx.scene.input.MouseButton;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;

public class DropDownMenu {
    JPopupMenu popupMenu = new JPopupMenu();

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
    public void addItem(String text, Runnable onClick) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClick.run();
            }
        });
        popupMenu.add(item);
    }
}
