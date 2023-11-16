package com.spotifyxp.swingextension;

import com.spotifyxp.PublicValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class JFrame2 extends JFrame {
    public JFrame2(String title) {
        super.setTitle(title);
    }
    public JFrame2() {
    }

    public void close() {
        this.setVisible(false);
    }
    boolean aa = false;
    public void setAntiAliasingActive(boolean value) {
        aa = value;
    }

    private JMenuBar copyJMenuBar(JMenuBar originalMenuBar) {
        JMenuBar newMenuBar = new JMenuBar();
        for (int i = 0; i < originalMenuBar.getMenuCount(); i++) {
            JMenu originalMenu = originalMenuBar.getMenu(i);
            JMenu newMenu = new JMenu(originalMenu.getText());
            for (int j = 0; j < originalMenu.getItemCount(); j++) {
                JMenuItem originalMenuItem = originalMenu.getItem(j);
                JMenuItem newMenuItem = new JMenuItem(originalMenuItem.getText());
                for(ActionListener listener : originalMenuItem.getActionListeners()) {
                    newMenuItem.addActionListener(listener);
                }
                newMenu.add(newMenuItem);
            }
            newMenuBar.add(newMenu);
        }
        return newMenuBar;
    }

    @Override
    public void paintComponents(Graphics g) {
        if(aa) {
            Graphics2D g2d = (Graphics2D) g.create();
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2d.setRenderingHints(hints);
        }
        super.paintComponents(g);
    }

    public void open() {
        if(!(PublicValues.menuBar == null)) {
            setJMenuBar(copyJMenuBar(PublicValues.menuBar));
        }
        this.setVisible(true);
        this.pack();
    }

    public void openMain() {
        this.setVisible(true);
        this.pack();
    }
}
