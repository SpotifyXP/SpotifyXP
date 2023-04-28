package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.deps.se.michaelthelin.spotify.Base64;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.TrackUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

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

    JScrollPane scrollholder;
    JPanel content;

    UnofficialSpotifyAPI.HomeTab tab;

    public HomePanel() {
        tab = new UnofficialSpotifyAPI(ContentPanel.api.getSpotifyApi().getAccessToken()).getHomeTab();
        initializeLayout();
    }

    public void initializeLayout() {
        content = new JPanel();
        content.setPreferredSize(new Dimension(784, 302 * tab.sections.size()));
        scrollholder = new JScrollPane(content);
        scrollholder.setSize(784, 421);
        scrollholder.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                System.out.println("Component Shown: " + scrollholder.getPreferredSize());
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                System.out.println("Component Hidden: " + scrollholder.getPreferredSize());
            }
        });
        initializeContent();
    }

    int addCache = 302;

    public void addModule(UnofficialSpotifyAPI.HomeTabSection section) {
        JPanel homepanelmodule = new JPanel();
        homepanelmodule.setBounds(0, addCache, 777, 319);
        content.add(homepanelmodule);
        homepanelmodule.setLayout(null);

        JLabel homepanelmoduletext = new JLabel("Greetings SpotifyXP user");
        homepanelmoduletext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        homepanelmoduletext.setBounds(0, homepanelmodule.getY() + 11, 375, 24);
        homepanelmodule.add(homepanelmoduletext);

        JScrollPane homepanelmodulescrollpanel = new JScrollPane();
        homepanelmodulescrollpanel.setBounds(0, homepanelmodule.getY() + 38, 777, 281);
        homepanelmodule.add(homepanelmodulescrollpanel);

        JTable homepanelmodulecontenttable = new JTable();
        homepanelmodulescrollpanel.setViewportView(homepanelmodulecontenttable);

        homepanelmodulecontenttable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "Name", "Artist"
                }
        ));

        addCache+=319;
    }

    String artistParser(ArrayList<UnofficialSpotifyAPI.HomeTabArtistNoImage> cache) {
        StringBuilder builder = new StringBuilder();
        int read = 0;
        for(UnofficialSpotifyAPI.HomeTabArtistNoImage s : cache) {
            if(read==cache.size()) {
                builder.append(s.name);
            }else{
                builder.append(s.name).append(",");
            }
            read++;
        }
        return builder.toString();
    }

    public void initializeContent() {
        content.setLayout(null);
        JPanel homepaneluser = new JPanel();
        homepaneluser.setBounds(0, 39, 777, 261);
        content.add(homepaneluser);
        homepaneluser.setLayout(null);
        ArrayList<String> usersuricache = new ArrayList<>();

        JScrollPane homepaneluserscrollpanel = new JScrollPane();
        homepaneluserscrollpanel.setBounds(0, 0, 777, 261);
        homepaneluser.add(homepaneluserscrollpanel);

        JTable homepanelusertable = new JTable();
        homepaneluserscrollpanel.setViewportView(homepanelusertable);

        homepanelusertable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "Name", "Artist"
                }
        ));

        JLabel homepanelgreetingstext = new JLabel(tab.greeting);
        homepanelgreetingstext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        homepanelgreetingstext.setBounds(0, 11, 375, 24);
        content.add(homepanelgreetingstext);

        for(UnofficialSpotifyAPI.HomeTabAlbum album : tab.firstSection.albums) {
            usersuricache.add(album.uri);
            ((DefaultTableModel) homepanelusertable.getModel()).addRow(new Object[]{album.name, artistParser(album.artists)});
        }
        for(UnofficialSpotifyAPI.HomeTabEpisodeOrChapter episodeOrChapter : tab.firstSection.episodeOrChapters) {
            usersuricache.add(episodeOrChapter.uri);
            ((DefaultTableModel) homepanelusertable.getModel()).addRow(new Object[]{episodeOrChapter.EpisodeOrChapterName, episodeOrChapter.name + " - " + episodeOrChapter.publisherName});
        }
        for(UnofficialSpotifyAPI.HomeTabPlaylist playlist : tab.firstSection.playlists) {
            usersuricache.add(playlist.uri);
            ((DefaultTableModel) homepanelusertable.getModel()).addRow(new Object[]{playlist.name, playlist.ownerName});
        }
        for(UnofficialSpotifyAPI.HomeTabArtist artist : tab.firstSection.artists) {
            usersuricache.add(artist.uri);
            ((DefaultTableModel) homepanelusertable.getModel()).addRow(new Object[]{artist.name, ""});
        }

        for(UnofficialSpotifyAPI.HomeTabSection section : tab.sections) {
            addModule(section);
        }

        libLanguage l = PublicValues.language;
    }


    public JScrollPane getComponent() {
        return scrollholder;
    }

    public JPanel getPanel() {
        return content;
    }
}
