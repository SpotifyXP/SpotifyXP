package com.spotifyxp.panels;

import com.spotifyxp.deps.se.michaelthelin.spotify.Base64;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class HomePanel {
    //ToDo: Implement this

    //The homepanel will display:

    // - Made for 'USER'
    // - Recently played
    // - Discover picks for you
    // - Episodes for you
    // - Your top mixes
    // - Stay tuned
    // - It's time for some classics
    // - Uniquely yours
    // - More of what you like
    // - Recommended for today
    // - Best of artists
    // - Suggested artists
    // - Throwback

    //Performance:

    //Don't load not visible objects (write custom JScrollPane that loads and unloads parts based on their visibility)

    public static class ExtendedJScrollBar extends JScrollPane {
        //This JScrollPane has the ability to load and unload parts based on their visibility
        public ExtendedJScrollBar(JComponent component) {
            this.setViewportView(component);
        }

        void registerEventListeners() {
            this.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    System.out.println(getViewport().getComponentAt(e.getX(), e.getY()).getName());
                }
            });
        }
    }

    ExtendedJScrollBar scrollholder;
    JPanel content;

    public HomePanel(int width, int height) {
        content = new JPanel();
        scrollholder = new ExtendedJScrollBar(content);
        scrollholder.setPreferredSize(new Dimension(width, height));
        content.setPreferredSize(new Dimension(width, height));
        int cache = 10;
        for(int i = 0; i < 40; i++) {
            JButton label = new JButton("Hello World " + i);
            label.setBounds(390, cache, 10, 5);
            System.out.println("Y: " + cache);
            content.add(label);
            cache = cache + 20;
        }
    }


    public JScrollPane getComponent() {
        return scrollholder;
    }

    public JPanel getPanel() {
        return content;
    }
}
