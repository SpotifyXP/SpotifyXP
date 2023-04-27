package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
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

    UnofficialSpotifyAPI.HomeTab tab;

    public HomePanel(int width, int height) {
        content = new JPanel();
        scrollholder = new ExtendedJScrollBar(content);
        scrollholder.setPreferredSize(new Dimension(width, height));
        content.setPreferredSize(new Dimension(width, height));
        tab = new UnofficialSpotifyAPI(ContentPanel.api.getSpotifyApi().getAccessToken()).getHomeTab();
        initializeContent();
    }

    public void initializeContent() {
        JPanel homepaneluser = new JPanel();
        homepaneluser.setBounds(0, 39, 777, 261);
        content.add(homepaneluser);
        homepaneluser.setLayout(null);

        JScrollPane homepaneluserscrollpanel = new JScrollPane();
        homepaneluserscrollpanel.setBounds(0, 0, 777, 261);
        homepaneluser.add(homepaneluserscrollpanel);

        JTable homepanelusertable = new JTable();
        homepaneluserscrollpanel.setViewportView(homepanelusertable);

        JLabel homepanelgreetingstext = new JLabel("Greetings SpotifyXP user");
        homepanelgreetingstext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        homepanelgreetingstext.setBounds(0, 11, 375, 24);
        content.add(homepanelgreetingstext);

        JPanel homepanelmodule = new JPanel();
        homepanelmodule.setBounds(0, 299, 777, 319);
        content.add(homepanelmodule);
        homepanelmodule.setLayout(null);

        JLabel homepanelmoduletext = new JLabel("Greetings SpotifyXP user");
        homepanelmoduletext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        homepanelmoduletext.setBounds(0, 11, 375, 24);
        homepanelmodule.add(homepanelmoduletext);

        JScrollPane homepanelmodulescrollpanel = new JScrollPane();
        homepanelmodulescrollpanel.setBounds(0, 38, 777, 281);
        homepanelmodule.add(homepanelmodulescrollpanel);

        JTable homepanelmodulecontenttable = new JTable();
        homepanelmodulescrollpanel.setViewportView(homepanelmodulecontenttable);
    }


    public JScrollPane getComponent() {
        return scrollholder;
    }

    public JPanel getPanel() {
        return content;
    }
}
