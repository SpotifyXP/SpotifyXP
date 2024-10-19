package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.*;
import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.utils.*;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Search extends JPanel {
    public static JPanel searchplaylistpanel;
    public static JButton searchbackbutton;
    public static DefTable searchplaylisttable;
    public static JRadioButton searchfilterplaylist;
    public static JRadioButton searchfilteralbum;
    public static JRadioButton searchfiltershow;
    public static JRadioButton searchfiltertrack;
    public static JScrollPane searchplaylistscrollpanel;
    public static JRadioButton searchfilterartist;
    public static DefTable searchsonglist;
    public static JTextField searchartistfield;
    public static JTextField searchsongtitlefield;
    public static JPanel searchfieldspanel;
    public static JLabel searchartistlabel;
    public static JLabel searchsongtitlelabel;
    public static JButton searchclearfieldsbutton;
    public static JButton searchfinditbutton;
    public static JPanel searchfilterpanel;
    public static JRadioButton searchfilterexcludeexplicit;
    public static JScrollPane searchscrollpanel;
    public static final ArrayList<String> searchsonglistcache = new ArrayList<>();
    public static final ArrayList<String> searchplaylistsongscache = new ArrayList<>();
    public static ContextMenu searchplaylistsongscontextmenu;
    public static ContextMenu searchcontextmenu;
    private String searchCacheTitle = "";
    private String searchCacheArtist = "";
    private boolean excludeExplicit = false;

    public Search() {
        setBounds(0, 0, 784, 421);
        ContentPanel.tabpanel.add(this);
        setLayout(null);
        searchfieldspanel = new JPanel();
        searchfieldspanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.search.searchfield.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchfieldspanel.setBounds(0, 0, 784, 128);
        add(searchfieldspanel);
        searchfieldspanel.setLayout(null);
        searchfieldspanel.setForeground(PublicValues.globalFontColor);
        searchartistlabel = new JLabel(PublicValues.language.translate("ui.search.searchfield.artist"));
        searchartistlabel.setHorizontalAlignment(SwingConstants.RIGHT);
        searchartistlabel.setBounds(30, 25, 46, 14);
        searchfieldspanel.add(searchartistlabel);
        searchartistlabel.setForeground(PublicValues.globalFontColor);
        searchsongtitlelabel = new JLabel(PublicValues.language.translate("ui.search.searchfield.title"));
        searchsongtitlelabel.setHorizontalAlignment(SwingConstants.RIGHT);
        searchsongtitlelabel.setBounds(10, 62, 66, 14);
        searchfieldspanel.add(searchsongtitlelabel);
        searchsongtitlelabel.setForeground(PublicValues.globalFontColor);
        searchclearfieldsbutton = new JButton(PublicValues.language.translate("ui.search.searchfield.button.clear"));
        searchclearfieldsbutton.setBounds(30, 94, 194, 23);
        searchfieldspanel.add(searchclearfieldsbutton);
        searchclearfieldsbutton.setForeground(PublicValues.globalFontColor);
        searchclearfieldsbutton.addActionListener(e -> {
            searchartistfield.setText("");
            searchsongtitlefield.setText("");
        });
        searchfinditbutton = new JButton(PublicValues.language.translate("ui.search.searchfield.button.findit"));
        searchfinditbutton.setBounds(234, 94, 194, 23);
        searchfieldspanel.add(searchfinditbutton);
        searchfinditbutton.setForeground(PublicValues.globalFontColor);
        searchartistfield = new JTextField();
        searchartistfield.setBounds(86, 22, 356, 20);
        searchfieldspanel.add(searchartistfield);
        searchartistfield.setColumns(10);
        searchartistfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchfinditbutton.doClick();
                }
            }
        });
        searchsongtitlefield = new JTextField();
        searchsongtitlefield.setColumns(10);
        searchsongtitlefield.setBounds(86, 59, 356, 20);
        searchfieldspanel.add(searchsongtitlefield);
        searchsongtitlefield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchfinditbutton.doClick();
                }
            }
        });
        searchfilterpanel = new JPanel();
        searchfilterpanel.setLayout(null);
        searchfilterpanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.search.searchfield.filters.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchfilterpanel.setBounds(452, 11, 322, 106);
        searchfieldspanel.add(searchfilterpanel);
        searchfilterexcludeexplicit = new JRadioButton(PublicValues.language.translate("ui.search.searchfield.filters.excludeexplicit"));
        searchfilterexcludeexplicit.setBounds(6, 24, 130, 23);
        searchfilterpanel.add(searchfilterexcludeexplicit);
        searchfilterexcludeexplicit.setForeground(PublicValues.globalFontColor);
        searchfilterartist = new JRadioButton(PublicValues.language.translate("ui.search.filter.artist"));
        searchfilterartist.setBounds(160, 23, 130, 23);
        searchfilterpanel.add(searchfilterartist);
        searchfilterartist.setForeground(PublicValues.globalFontColor);
        searchfiltertrack = new JRadioButton(PublicValues.language.translate("ui.search.filter.track"));
        searchfiltertrack.setBounds(6, 50, 130, 23);
        searchfilterpanel.add(searchfiltertrack);
        searchfiltertrack.setForeground(PublicValues.globalFontColor);
        searchfiltertrack.setSelected(true);
        searchfilteralbum = new JRadioButton(PublicValues.language.translate("ui.search.filter.album"));
        searchfilteralbum.setBounds(160, 50, 130, 23);
        searchfilterpanel.add(searchfilteralbum);
        searchfilteralbum.setForeground(PublicValues.globalFontColor);
        searchfilterplaylist = new JRadioButton(PublicValues.language.translate("ui.search.filter.playlist"));
        searchfilterplaylist.setBounds(6, 75, 130, 23);
        searchfilterpanel.add(searchfilterplaylist);
        searchfilterplaylist.setForeground(PublicValues.globalFontColor);
        searchfiltershow = new JRadioButton(PublicValues.language.translate("ui.search.filter.show"));
        searchfiltershow.setBounds(160, 75, 130, 23);
        searchfilterpanel.add(searchfiltershow);
        searchfiltershow.setForeground(PublicValues.globalFontColor);
        searchfilterartist.addActionListener(e -> {
            searchfiltertrack.setSelected(false);
            searchfilteralbum.setSelected(false);
            searchfiltershow.setSelected(false);
            searchfilterplaylist.setSelected(false);
        });
        searchfilteralbum.addActionListener(e -> {
            searchfiltertrack.setSelected(false);
            searchfiltershow.setSelected(false);
            searchfilterplaylist.setSelected(false);
            searchfilterartist.setSelected(false);
        });
        searchfilterplaylist.addActionListener(e -> {
            searchfiltertrack.setSelected(false);
            searchfilteralbum.setSelected(false);
            searchfiltershow.setSelected(false);
            searchfilterartist.setSelected(false);
        });
        searchfiltershow.addActionListener(e -> {
            searchfiltertrack.setSelected(false);
            searchfilteralbum.setSelected(false);
            searchfilterplaylist.setSelected(false);
            searchfilterartist.setSelected(false);
        });
        searchfiltertrack.addActionListener(e -> {
            searchfilteralbum.setSelected(false);
            searchfiltershow.setSelected(false);
            searchfilterplaylist.setSelected(false);
            searchfilterartist.setSelected(false);
        });
        searchfinditbutton.addActionListener(new AsyncActionListener(e -> {
            Thread thread1 = new Thread(() -> {
                String searchartist = searchartistfield.getText();
                String searchtitle = searchsongtitlefield.getText();
                boolean track = searchfiltertrack.isSelected();
                boolean artist = searchfilterartist.isSelected();
                boolean album = searchfilteralbum.isSelected();
                boolean show = searchfiltershow.isSelected();
                boolean playlist = searchfilterplaylist.isSelected();
                if(!track & !artist & !album & !show & !playlist) {
                    //No search filter was selected! Selecting tracks
                    searchfiltertrack.setSelected(true);
                    track = true;
                }
                searchCacheTitle = searchtitle;
                searchCacheArtist = searchartist;
                excludeExplicit = searchfilterexcludeexplicit.isSelected();
                searchsonglistcache.clear();
                ((DefaultTableModel) searchsonglist.getModel()).setRowCount(0);
                if (searchtitle.isEmpty() && searchartist.isEmpty()) {
                    return; // User didn't type anything in so we just return
                }
                try {
                    if (track) {
                        for (Track t : InstanceManager.getSpotifyApi().searchTracks(searchtitle + " " + searchartist).limit(50).build().execute().getItems()) {
                            String artists = TrackUtils.getArtists(t.getArtists());
                            if (!searchartist.equalsIgnoreCase("")) {
                                if (!TrackUtils.trackHasArtist(t.getArtists(), searchartist, true)) {
                                    continue;
                                }
                            }
                            if (excludeExplicit) {
                                if (!t.getIsExplicit()) {
                                    searchsonglistcache.add(t.getUri());
                                    InstanceManager.getSpotifyAPI().addSongToList(artists, t, searchsonglist);
                                }
                            } else {
                                searchsonglistcache.add(t.getUri());
                                InstanceManager.getSpotifyAPI().addSongToList(artists, t, searchsonglist);
                            }
                        }
                    }
                    if (artist) {
                        ArtistPanel.albumuricache.clear();
                        ArtistPanel.popularuricache.clear();
                        if (searchtitle.isEmpty()) {
                            searchtitle = searchartist;
                        }
                        for (Artist a : InstanceManager.getSpotifyApi().searchArtists(searchtitle).build().execute().getItems()) {
                            searchsonglistcache.add(a.getUri());
                            InstanceManager.getSpotifyAPI().addArtistToList(a, searchsonglist);
                        }

                    }
                    if (album) {
                        for (AlbumSimplified a : InstanceManager.getSpotifyApi().searchAlbums(searchtitle).build().execute().getItems()) {
                            if (!searchartist.isEmpty()) {
                                if (!TrackUtils.trackHasArtist(a.getArtists(), searchartist, true)) {
                                    continue;
                                }
                            }
                            searchsonglistcache.add(a.getUri());
                            ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{a.getName()});
                        }
                    }
                    if (show) {
                        for (ShowSimplified s : InstanceManager.getSpotifyApi().searchShows(searchtitle).build().execute().getItems()) {
                            if (!searchartist.isEmpty()) {
                                if (!s.getPublisher().equalsIgnoreCase(searchartist)) {
                                    continue;
                                }
                            }
                            searchsonglistcache.add(s.getUri());
                            ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{s.getName()});
                        }
                    }
                    if (playlist) {
                        for (PlaylistSimplified t : InstanceManager.getSpotifyApi().searchPlaylists(searchtitle).build().execute().getItems()) {
                            if (!searchartist.isEmpty()) {
                                if (!t.getOwner().getDisplayName().equalsIgnoreCase(searchartist)) {
                                    continue;
                                }
                            }
                            searchsonglistcache.add(t.getUri());
                            InstanceManager.getSpotifyAPI().addPlaylistToList(t, searchsonglist);
                        }
                    }
                } catch (IOException | SpotifyWebApiException | ParseException ex) {
                    ConsoleLogging.Throwable(ex);
                }
                searchsonglist.addModifyAction(() -> ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore")}));
            }, "Search thread");
            thread1.start();
        }));
        searchscrollpanel = new JScrollPane();
        searchscrollpanel.setBounds(0, 139, 784, 282);
        add(searchscrollpanel);
        searchsonglist = new DefTable();
        searchsonglist.getTableHeader().setReorderingAllowed(false);
        searchsonglist.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        if (searchsonglist.getModel().getValueAt(searchsonglist.getSelectedRow(), 2).toString().equals(PublicValues.language.translate("ui.general.loadmore"))) {
                            ((DefaultTableModel) searchsonglist.getModel()).setRowCount(searchsonglist.getRowCount() - 1);
                            Thread thread1 = new Thread(() -> {
                                String searchartist = searchCacheArtist;
                                String searchtitle = searchCacheTitle;
                                boolean track = searchsonglistcache.get(0).split(":")[1].equals("track");
                                boolean artist = searchsonglistcache.get(0).split(":")[1].equals("artist");
                                boolean album = searchsonglistcache.get(0).split(":")[1].equals("album");
                                boolean show = searchsonglistcache.get(0).split(":")[1].equals("show");
                                boolean playlist = searchsonglistcache.get(0).split(":")[1].equals("playlist");
                                try {
                                    if (track) {
                                        for (Track t : InstanceManager.getSpotifyApi().searchTracks(searchtitle + " " + searchartist).limit(50).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            String artists = TrackUtils.getArtists(t.getArtists());
                                            if (!searchartist.equalsIgnoreCase("")) {
                                                if (!TrackUtils.trackHasArtist(t.getArtists(), searchartist, true)) {
                                                    continue;
                                                }
                                            }
                                            if (excludeExplicit) {
                                                if (!t.getIsExplicit()) {
                                                    searchsonglistcache.add(t.getUri());
                                                    InstanceManager.getSpotifyAPI().addSongToList(artists, t, searchsonglist);
                                                }
                                            } else {
                                                searchsonglistcache.add(t.getUri());
                                                InstanceManager.getSpotifyAPI().addSongToList(artists, t, searchsonglist);
                                            }
                                        }
                                    }
                                    if (artist) {
                                        ArtistPanel.albumuricache.clear();
                                        ArtistPanel.popularuricache.clear();
                                        if (searchtitle.isEmpty()) {
                                            searchtitle = searchartist;
                                        }
                                        for (Artist a : InstanceManager.getSpotifyApi().searchArtists(searchtitle).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            searchsonglistcache.add(a.getUri());
                                            InstanceManager.getSpotifyAPI().addArtistToList(a, searchsonglist);
                                        }
                                    }
                                    if (album) {
                                        for (AlbumSimplified a : InstanceManager.getSpotifyApi().searchAlbums(searchtitle).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            if (!searchartist.isEmpty()) {
                                                if (!TrackUtils.trackHasArtist(a.getArtists(), searchartist, true)) {
                                                    continue;
                                                }
                                            }
                                            searchsonglistcache.add(a.getUri());
                                            ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{a.getName()});
                                        }
                                    }
                                    if (show) {
                                        for (ShowSimplified s : InstanceManager.getSpotifyApi().searchShows(searchtitle).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            if (!searchartist.isEmpty()) {
                                                if (!s.getPublisher().equalsIgnoreCase(searchartist)) {
                                                    continue;
                                                }
                                            }
                                            searchsonglistcache.add(s.getUri());
                                            ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{s.getName()});
                                        }
                                    }
                                    if (playlist) {
                                        for (PlaylistSimplified t : InstanceManager.getSpotifyApi().searchPlaylists(searchtitle).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            if (!searchartist.isEmpty()) {
                                                if (!t.getOwner().getDisplayName().equalsIgnoreCase(searchartist)) {
                                                    continue;
                                                }
                                            }
                                            searchsonglistcache.add(t.getUri());
                                            InstanceManager.getSpotifyAPI().addPlaylistToList(t, searchsonglist);
                                        }
                                    }
                                } catch (IOException | SpotifyWebApiException | ParseException ex) {
                                    ConsoleLogging.Throwable(ex);
                                }
                                searchsonglist.addModifyAction(() -> ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore")}));
                            });
                            thread1.start();
                            return;
                        }
                    }catch (NullPointerException ignored) {
                    }
                    switch (searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[1]) {
                        case "playlist":
                        case "album":
                        case "show":
                            setVisible(false);
                            ContentPanel.blockTabSwitch();
                            searchplaylistpanel.setVisible(true);
                            searchplaylistsongscache.clear();
                            ((DefaultTableModel) searchplaylisttable.getModel()).setRowCount(0);
                            break;
                        case "artist":
                            ArtistPanel.popularuricache.clear();
                            ArtistPanel.albumuricache.clear();
                            ((DefaultTableModel) ArtistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                            ((DefaultTableModel) ArtistPanel.artistpopularsonglist.getModel()).setRowCount(0);
                            ArtistPanel.artisttitle.setText("");
                            ContentPanel.artistPanel.openPanel();
                            ArtistPanel.isFirst = true;
                            ArtistPanel.contentPanel.setVisible(true);
                            ContentPanel.artistPanelBackButton.setVisible(true);
                            ContentPanel.artistPanelVisible = true;
                            setVisible(false);
                            ContentPanel.blockTabSwitch();
                            break;
                    }
                    Thread thread = new Thread(() -> {
                        switch (searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[1].toLowerCase()) {
                            case "playlist":
                                try {
                                    Playlist pl = InstanceManager.getSpotifyApi().getPlaylist(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute();
                                    int parsed = 0;
                                    int offset = 0;
                                    int counter = 0;
                                    int last = 0;
                                    int total = pl.getTracks().getTotal();
                                    while (parsed != total) {
                                        for (PlaylistTrack track : InstanceManager.getSpotifyApi().getPlaylistsItems(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).limit(100).build().execute().getItems()) {
                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{track.getTrack().getName() + " - " + pl.getName() + " - " + pl.getOwner().getDisplayName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                            searchplaylistsongscache.add(track.getTrack().getUri());
                                            parsed++;
                                        }
                                        if (last == parsed) {
                                            if (counter > 1) {
                                                break;
                                            }
                                            counter++;
                                        } else {
                                            counter = 0;
                                        }
                                        last = parsed;
                                        offset += 100;
                                    }
                                } catch (Exception e1) {
                                    ConsoleLogging.Throwable(e1);
                                }
                                break;
                            case "artist":
                                try {
                                    Artist a = InstanceManager.getSpotifyApi().getArtist(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute();
                                    try {
                                        ArtistPanel.artistimage.setImage(new URL(SpotifyUtils.getImageForSystem(a.getImages()).getUrl()).openStream());
                                    } catch (ArrayIndexOutOfBoundsException exception) {
                                        // No artist image (when this is raised it's a bug)
                                    }
                                    ArtistPanel.artisttitle.setText(a.getName());
                                    Thread trackthread = new Thread(() -> {
                                        try {
                                            for (Track t : InstanceManager.getSpotifyApi().getArtistsTopTracks(a.getUri().split(":")[2], PublicValues.countryCode).build().execute()) {
                                                if (!ContentPanel.artistPanel.isVisible()) {
                                                    break;
                                                }
                                                ArtistPanel.popularuricache.add(t.getUri());
                                                InstanceManager.getSpotifyAPI().addSongToList(TrackUtils.getArtists(t.getArtists()), t, ArtistPanel.artistpopularsonglist);
                                            }
                                        } catch (IOException | ParseException | SpotifyWebApiException ex) {
                                            ConsoleLogging.Throwable(ex);
                                        }
                                    });
                                    Thread albumthread = new Thread(() -> InstanceManager.getSpotifyAPI().addAllAlbumsToList(ArtistPanel.albumuricache, a.getUri(), ArtistPanel.artistalbumalbumtable));
                                    albumthread.start();
                                    trackthread.start();
                                } catch (Exception e1) {
                                    throw new RuntimeException(e1);
                                }
                                break;
                            case "show":
                                try {
                                    int parsed = 0;
                                    int offset = 0;
                                    int last = 0;
                                    int counter = 0;
                                    int total = InstanceManager.getSpotifyApi().getShow(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute().getEpisodes().getTotal();
                                    while (parsed != total) {
                                        for (EpisodeSimplified episode : InstanceManager.getSpotifyApi().getShowEpisodes(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).limit(50).build().execute().getItems()) {
                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{episode.getName(), TrackUtils.calculateFileSizeKb(episode.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(episode.getDurationMs())});
                                            searchplaylistsongscache.add(episode.getUri());
                                            parsed++;
                                        }
                                        if (last == parsed) {
                                            if (counter > 1) {
                                                break;
                                            }
                                            counter++;
                                        } else {
                                            counter = 0;
                                        }
                                        last = parsed;
                                        offset += 50;
                                    }
                                } catch (Exception e1) {
                                    throw new RuntimeException(e1);
                                }
                                break;
                            case "album":
                                try {
                                    int parsed = 0;
                                    int offset = 0;
                                    int last = 0;
                                    int counter = 0;
                                    int total = InstanceManager.getSpotifyApi().getAlbumsTracks(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute().getTotal();
                                    while (parsed != total) {
                                        for (TrackSimplified simplified : InstanceManager.getSpotifyApi().getAlbumsTracks(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).limit(50).build().execute().getItems()) {
                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                                            searchplaylistsongscache.add(simplified.getUri());
                                            parsed++;
                                        }
                                        if (last == parsed) {
                                            if (counter > 1) {
                                                break;
                                            }
                                            counter++;
                                        } else {
                                            counter = 0;
                                        }
                                        last = parsed;
                                        offset += 50;
                                    }
                                } catch (Exception e1) {
                                    throw new RuntimeException(e1);
                                }
                                break;
                            case "track":
                                InstanceManager.getPlayer().getPlayer().load(searchsonglistcache.get(searchsonglist.getSelectedRow()), true, PublicValues.shuffle);
                                break;
                            default:
                                throw new RuntimeException("Invalid uri '" + searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[1].toLowerCase() + "'");
                        }
                    });
                    thread.start();
                    searchsonglist.setColumnSelectionInterval(0, searchsonglist.getColumnCount() - 1);
                } else {
                    searchsonglist.setColumnSelectionInterval(0, searchsonglist.getColumnCount() - 1);
                }
            }
        }));
        searchsonglist.getTableHeader().setForeground(PublicValues.globalFontColor);
        searchsonglist.setForeground(PublicValues.globalFontColor);
        searchsonglist.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")}));
        searchsonglist.getColumnModel().getColumn(0).setPreferredWidth(342);
        searchsonglist.getColumnModel().getColumn(1).setPreferredWidth(130);
        searchsonglist.setFillsViewportHeight(true);
        searchsonglist.setColumnSelectionAllowed(true);
        searchscrollpanel.setViewportView(searchsonglist);
        searchplaylistpanel = new JPanel();
        searchplaylistpanel.setBounds(0, 0, 784, 421);
        ContentPanel.tabpanel.add(searchplaylistpanel);
        searchplaylistpanel.setLayout(null);
        searchbackbutton = new JButton(PublicValues.language.translate("ui.back"));
        searchbackbutton.setBounds(0, 0, 89, 23);
        searchplaylistpanel.add(searchbackbutton);
        searchbackbutton.setForeground(PublicValues.globalFontColor);
        searchplaylistscrollpanel = new JScrollPane();
        searchplaylistscrollpanel.setBounds(0, 22, 784, 399);
        searchplaylistpanel.add(searchplaylistscrollpanel);
        searchplaylisttable = new DefTable();
        searchplaylistscrollpanel.setViewportView(searchplaylisttable);
        searchplaylisttable.setForeground(PublicValues.globalFontColor);
        searchplaylisttable.getTableHeader().setForeground(PublicValues.globalFontColor);
        searchplaylisttable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(searchplaylistsongscache.get(searchplaylisttable.getSelectedRow()), true, PublicValues.shuffle);
                    searchplaylisttable.setColumnSelectionInterval(0, searchplaylisttable.getColumnCount() - 1);
                    TrackUtils.addAllToQueue(searchplaylistsongscache, searchplaylisttable);
                }
            }
        }));
        searchplaylisttable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")}));
        searchplaylistpanel.setVisible(false);
        searchplaylistsongscontextmenu = new ContextMenu(searchplaylisttable);
        searchplaylistsongscontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(searchplaylistsongscache.get(searchplaylisttable.getSelectedRow())));
        searchbackbutton.addActionListener(new AsyncActionListener(e -> {
            searchplaylistpanel.setVisible(false);
            if(ContentPanel.artistPanelVisible) {
                ArtistPanel.contentPanel.setVisible(true);
            }else{
                ContentPanel.searchpanel.setVisible(true);
            }
            PublicValues.contentPanel.setVisible(true);
            if (!ContentPanel.artistPanelVisible) {
                ContentPanel.enableTabSwitch();
            }
        }));
        searchcontextmenu = new ContextMenu(searchsonglist);
        searchcontextmenu.addItem(PublicValues.language.translate("ui.general.addtolibrary"), () -> {
            PlayerArea.heart.isFilled = true;
            PlayerArea.heart.setImage(Graphics.HEARTFILLED.getPath());
            InstanceManager.getSpotifyApi().saveTracksForUser("https://api.spotify.com/v1/me/tracks?ids=" + searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]);
            if (!Library.libraryuricache.isEmpty()) {
                Library.fetchOnlyFirstSongsFromUserLibrary();
            }
        });
        searchcontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(searchsonglistcache.get(searchsonglist.getSelectedRow())));
    }
}
