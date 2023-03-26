package com.spotifyxp.swingextension;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContextMenu {
    final JPopupMenu holder = new JPopupMenu();
    public ContextMenu(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(SwingUtilities.isRightMouseButton(e)) {
                    holder.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    public ContextMenu(JComponent component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(SwingUtilities.isRightMouseButton(e)) {
                    holder.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    public void addItem(String text, Runnable onClick) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(e -> onClick.run());
        holder.add(item);
    }
    public void addSeperator() {
        holder.add(new JSeparator());
    }
}
