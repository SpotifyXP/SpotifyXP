package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class Library extends JScrollPane implements View {
    public static LibraryTracks libraryTracks;
    public static JTabbedPane tabbedPane;
    public static JPanel contentPanel;
    public static LibraryPlaylists libraryPlaylists;
    public static LibraryArtists libraryArtists;
    public static LibraryEpisodes libraryEpisodes;
    public static LibraryShows libraryShows;

    public Library() {
        setVisible(false);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        setViewportView(contentPanel);

        tabbedPane = new JTabbedPane();
        tabbedPane.setUI(new BasicTabbedPaneUI());
        tabbedPane.setForeground(PublicValues.globalFontColor);
        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (tabbedPane.getSelectedIndex()) {
                    case 1:
                        if (LibraryPlaylists.playlistsPlaylistsTable.getModel().getRowCount() == 0) {
                            libraryPlaylists.fill();
                        }
                        break;
                    case 2:
                        if (LibraryArtists.artistsTable.getModel().getRowCount() == 0) {
                            libraryArtists.fill();
                        }
                        break;
                    case 3:
                        if (LibraryEpisodes.episodesTable.getModel().getRowCount() == 0) {
                            libraryEpisodes.fill();
                        }
                    case 4:
                        if (LibraryShows.showsTable.getModel().getRowCount() == 0) {
                            libraryShows.fill();
                        }
                }
            }
        });

        libraryTracks = new LibraryTracks();
        tabbedPane.addTab(PublicValues.language.translate("ui.library.tabs.tracks"), libraryTracks);

        libraryPlaylists = new LibraryPlaylists();
        tabbedPane.addTab(PublicValues.language.translate("ui.library.tabs.playlists"), libraryPlaylists);

        libraryArtists = new LibraryArtists();
        tabbedPane.addTab(PublicValues.language.translate("ui.library.tabs.artists"), libraryArtists);

        libraryEpisodes = new LibraryEpisodes();
        tabbedPane.addTab(PublicValues.language.translate("ui.library.tabs.episodes"), libraryEpisodes);

        libraryShows = new LibraryShows();
        tabbedPane.addTab(PublicValues.language.translate("ui.library.tabs.shows"), libraryShows);
    }

    @Override
    public void makeVisible() {
        setVisible(true);
    }

    @Override
    public void makeInvisible() {
        setVisible(false);
    }
}
