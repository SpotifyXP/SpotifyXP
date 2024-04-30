package com.spotifyxp.swingextension;

import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.AsyncActionListener;
import com.spotifyxp.utils.AsyncMouseListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ContextMenu {
    public static class GlobalContextMenuItem {
        public Runnable torun;
        public String name;
    }
    final JPopupMenu holder = new JPopupMenu();
    public ContextMenu(JComponent container, JComponent click) {
        click.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(SwingUtilities.isRightMouseButton(e)) {
                    for(MouseListener listener : container.getMouseListeners()) {
                        listener.mouseClicked(e);
                    }
                }
            }
        }));
        container.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(SwingUtilities.isRightMouseButton(e)) {
                    holder.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }));
        for(GlobalContextMenuItem item : PublicValues.globalContextMenuItems) {
            JMenuItem i = new JMenuItem(item.name);
            i.addActionListener(new AsyncActionListener(e -> item.torun.run()));
            holder.add(i);
        }
        PublicValues.contextMenus.add(this);
    }
    public ContextMenu(JPanel panel) {
        panel.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(SwingUtilities.isRightMouseButton(e)) {
                    holder.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }));
        for(GlobalContextMenuItem item : PublicValues.globalContextMenuItems) {
            JMenuItem i = new JMenuItem(item.name);
            i.addActionListener(new AsyncActionListener(e -> item.torun.run()));
            holder.add(i);
        }
        PublicValues.contextMenus.add(this);
    }
    public ContextMenu(JComponent component) {
        component.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(SwingUtilities.isRightMouseButton(e)) {
                    holder.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }));
        for(GlobalContextMenuItem item : PublicValues.globalContextMenuItems) {
            JMenuItem i = new JMenuItem(item.name);
            i.addActionListener(new AsyncActionListener(e -> item.torun.run()));
            holder.add(i);
        }
        PublicValues.contextMenus.add(this);
    }
    public void addItem(String text, Runnable onClick) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(new AsyncActionListener(e -> onClick.run()));
        holder.add(item);
    }
    public void addSeperator() {
        holder.add(new JSeparator());
    }
}
