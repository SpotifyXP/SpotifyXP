package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Artist;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.utils.AsyncMouseListener;
import com.spotifyxp.utils.SpotifyUtils;
import com.spotifyxp.utils.StringUtils;
import com.spotifyxp.utils.TrackUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class HomePanel implements View {
    JScrollPane scrollholder;
    JPanel content;

    final UnofficialSpotifyAPI.HomeTab tab;

    public HomePanel() {
        try {
            tab = InstanceManager.getUnofficialSpotifyApi().getHomeTab();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initializeLayout();
    }

    public void initializeLayout() {
        content = new JPanel();
        content.setPreferredSize(new Dimension(content.getWidth(), 337 * (tab.getSections().size() - 1)));
        scrollholder = new JScrollPane(content);
        scrollholder.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollholder.setVisible(false);
        Thread t = new Thread(this::initializeContent, "Get home");
        t.start();
    }

    int addCache = 302;

    int cache = 0;

    public enum ContentTypes {
        show,
        track,
        album,
        artist,
        episode,
        user,
        playlist
    }

    public void addModule(UnofficialSpotifyAPI.HomeTabSection section) {
        ArrayList<String> uricache = new ArrayList<>();
        JLabel homepanelmoduletext = new JLabel(section.getName().orElse("N/A"));
        homepanelmoduletext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        homepanelmoduletext.setBounds(0, addCache + 11, 375, 24);
        content.add(homepanelmoduletext);

        homepanelmoduletext.setForeground(PublicValues.globalFontColor);

        JScrollPane homepanelmodulescrollpanel = new JScrollPane();
        homepanelmodulescrollpanel.setBounds(0, addCache + 38, 777, 281);
        content.add(homepanelmodulescrollpanel);

        DefTable homepanelmodulecontenttable = new DefTable() {
        };
        homepanelmodulescrollpanel.setViewportView(homepanelmodulecontenttable);
        homepanelmodulecontenttable.setForeground(PublicValues.globalFontColor);
        homepanelmodulecontenttable.getTableHeader().setForeground(PublicValues.globalFontColor);

        homepanelmodulecontenttable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Name", "Artist"
                }
        ));

        for(UnofficialSpotifyAPI.HomeTabSectionItem item : section.getItems()) {
            switch (item.getType()) {
                case AlbumResponseWrapper:
                    if(!item.getAlbum().isPresent()) break;
                    UnofficialSpotifyAPI.HomeTabAlbum album = item.getAlbum().get();
                    uricache.add(album.getUri());
                    homepanelmodulecontenttable.addModifyAction(() -> ((DefaultTableModel) homepanelmodulecontenttable.getModel()).addRow(new Object[]{album.getName(), artistParser(album.getArtists())}));
                    break;
                case ArtistResponseWrapper:
                    if(!item.getArtist().isPresent()) break;
                    UnofficialSpotifyAPI.HomeTabArtist artist = item.getArtist().get();
                    uricache.add(artist.getUri());
                    homepanelmodulecontenttable.addModifyAction(() -> ((DefaultTableModel) homepanelmodulecontenttable.getModel()).addRow(new Object[]{artist.getName(), ""}));
                    break;
                case EpisodeOrChapterResponseWrapper:
                    if(!item.getEpisodeOrChapter().isPresent()) break;
                    UnofficialSpotifyAPI.HomeTabEpisodeOrChapter episodeOrChapter = item.getEpisodeOrChapter().get();
                    uricache.add(episodeOrChapter.getUri());
                    homepanelmodulecontenttable.addModifyAction(() -> ((DefaultTableModel) homepanelmodulecontenttable.getModel()).addRow(new Object[]{episodeOrChapter.getEpisodeOrChapterName(), episodeOrChapter.getName() + " - " + episodeOrChapter.getPublisherName()}));
                    break;
                case PlaylistResponseWrapper:
                    if(!item.getPlaylist().isPresent()) break;
                    UnofficialSpotifyAPI.HomeTabPlaylist playlist = item.getPlaylist().get();
                    uricache.add(playlist.getUri());
                    homepanelmodulecontenttable.addModifyAction(() -> ((DefaultTableModel) homepanelmodulecontenttable.getModel()).addRow(new Object[]{playlist.getName(), playlist.getOwnerName()}));
                    break;
            }
        }

        homepanelmodulecontenttable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    ContentTypes ct = ContentTypes.valueOf(uricache.get(homepanelmodulecontenttable.getSelectedRow()).split(":")[1]);
                    String uri = uricache.get(homepanelmodulecontenttable.getSelectedRow());
                    String id = uri.split(":")[2];
                    try {
                        switch (ct) {
                            case track:
                                PublicValues.spotifyplayer.load(uri, true, PublicValues.shuffle);
                                Events.triggerEvent(SpotifyXPEvents.queueUpdate.getName());
                                break;
                            case artist:
                                scrollholder.setVisible(false);
                                ContentPanel.artistPanel.reset();
                                ContentPanel.switchView(Views.ARTIST);
                                try {
                                    Artist a = InstanceManager.getSpotifyApi().getArtist(id).build().execute();
                                    try {
                                        ArtistPanel.artistimage.setImage(new URL(SpotifyUtils.getImageForSystem(a.getImages()).getUrl()).openStream());
                                    } catch (ArrayIndexOutOfBoundsException exception) {
                                        //No artist image (when this is raised it's a bug)
                                    }
                                    ArtistPanel.artisttitle.setText(a.getName());
                                    Thread trackthread = new Thread(() -> {
                                        try {
                                            for (Track t : InstanceManager.getSpotifyApi().getArtistsTopTracks(id, PublicValues.countryCode).build().execute()) {
                                                ArtistPanel.popularuricache.add(t.getUri());
                                                InstanceManager.getSpotifyAPI().addSongToList(TrackUtils.getArtists(t.getArtists()), t, ArtistPanel.artistpopularsonglist);
                                            }
                                        } catch (IOException ex) {
                                            ConsoleLogging.Throwable(ex);
                                        }
                                    }, "Get tracks (HomePanel)");
                                    InstanceManager.getSpotifyAPI().addAllAlbumsToList(ArtistPanel.albumuricache, uri, ArtistPanel.artistalbumalbumtable);
                                    trackthread.start();
                                } catch (IOException ex) {
                                    ConsoleLogging.Throwable(ex);
                                }
                                break;
                            case episode:
                                PublicValues.spotifyplayer.load(uri, true, PublicValues.shuffle);
                                Events.triggerEvent(SpotifyXPEvents.queueUpdate.getName());
                                break;
                            default:
                                ContentPanel.trackPanel.open(uri, ct);
                                break;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }));

        addCache += 319;
        cache++;
        content.revalidate();
        content.repaint();
    }

    String artistParser(ArrayList<UnofficialSpotifyAPI.HomeTabArtist> cache) {
        StringBuilder builder = new StringBuilder();
        int read = 0;
        for (UnofficialSpotifyAPI.HomeTabArtist s : cache) {
            if (read == cache.size()) {
                builder.append(s.getName());
            } else {
                builder.append(s.getName()).append(",");
            }
            read++;
        }
        return StringUtils.replaceLast(builder.toString(), ",", "");
    }


    public void initializeContent() {
        content.setLayout(null);
        ArrayList<String> usersuricache = new ArrayList<>();

        JScrollPane homepaneluserscrollpanel = new JScrollPane();
        homepaneluserscrollpanel.setBounds(0, 39, 777, 261);
        content.add(homepaneluserscrollpanel);

        DefTable homepanelusertable = new DefTable() {
        };
        homepaneluserscrollpanel.setViewportView(homepanelusertable);
        homepanelusertable.setForeground(PublicValues.globalFontColor);
        homepanelusertable.getTableHeader().setForeground(PublicValues.globalFontColor);

        homepanelusertable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Name", "Artist"
                }
        ));

        homepanelusertable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    ContentTypes ct = ContentTypes.valueOf(usersuricache.get(homepanelusertable.getSelectedRow()).split(":")[1]);
                    String uri = usersuricache.get(homepanelusertable.getSelectedRow());
                    String id = uri.split(":")[2];
                    try {
                        switch (ct) {
                            case track:
                                PublicValues.spotifyplayer.load(uri, true, PublicValues.shuffle);
                                Events.triggerEvent(SpotifyXPEvents.queueUpdate.getName());
                                break;
                            case artist:
                                scrollholder.setVisible(false);
                                ContentPanel.artistPanel.reset();
                                ContentPanel.switchView(Views.ARTIST);
                                try {
                                    Artist a = InstanceManager.getSpotifyApi().getArtist(id).build().execute();
                                    try {
                                        ArtistPanel.artistimage.setImage(new URL(SpotifyUtils.getImageForSystem(a.getImages()).getUrl()).openStream());
                                    } catch (ArrayIndexOutOfBoundsException exception) {
                                        //No artist image (when this is raised it's a bug)
                                    }
                                    ArtistPanel.artisttitle.setText(a.getName());
                                    Thread trackthread = new Thread(() -> {
                                        try {
                                            for (Track t : InstanceManager.getSpotifyApi().getArtistsTopTracks(id, PublicValues.countryCode).build().execute()) {
                                                ArtistPanel.popularuricache.add(t.getUri());
                                                InstanceManager.getSpotifyAPI().addSongToList(TrackUtils.getArtists(t.getArtists()), t, ArtistPanel.artistpopularsonglist);
                                            }
                                        } catch (IOException ex) {
                                            ConsoleLogging.Throwable(ex);
                                        }
                                    }, "Get tracks (Artist)");
                                    InstanceManager.getSpotifyAPI().addAllAlbumsToList(ArtistPanel.albumuricache, uri, ArtistPanel.artistalbumalbumtable);
                                    trackthread.start();
                                } catch (IOException ex) {
                                    ConsoleLogging.Throwable(ex);
                                }
                                break;
                            default:
                                ContentPanel.trackPanel.open(uri, ct);
                                break;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }));

        JLabel homepanelgreetingstext = new JLabel(tab.getGreeting());
        homepanelgreetingstext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        homepanelgreetingstext.setBounds(0, 11, 375, 24);
        content.add(homepanelgreetingstext);
        homepanelgreetingstext.setForeground(PublicValues.globalFontColor);

        for(UnofficialSpotifyAPI.HomeTabSectionItem item : tab.getSections().get(0).getItems()) {
            switch (item.getType()) {
                case AlbumResponseWrapper:
                    if(!item.getAlbum().isPresent()) break;
                    UnofficialSpotifyAPI.HomeTabAlbum album = item.getAlbum().get();
                    usersuricache.add(album.getUri());
                    homepanelusertable.addModifyAction(() -> ((DefaultTableModel) homepanelusertable.getModel()).addRow(new Object[]{album.getName(), artistParser(album.getArtists())}));
                    break;
                case ArtistResponseWrapper:
                    if(!item.getArtist().isPresent()) break;
                    UnofficialSpotifyAPI.HomeTabArtist artist = item.getArtist().get();
                    usersuricache.add(artist.getUri());
                    homepanelusertable.addModifyAction(() -> ((DefaultTableModel) homepanelusertable.getModel()).addRow(new Object[]{artist.getName(), ""}));
                    break;
                case EpisodeOrChapterResponseWrapper:
                    if(!item.getEpisodeOrChapter().isPresent()) break;
                    UnofficialSpotifyAPI.HomeTabEpisodeOrChapter episodeOrChapter = item.getEpisodeOrChapter().get();
                    usersuricache.add(episodeOrChapter.getUri());
                    homepanelusertable.addModifyAction(() -> ((DefaultTableModel) homepanelusertable.getModel()).addRow(new Object[]{episodeOrChapter.getEpisodeOrChapterName(), episodeOrChapter.getName() + " - " + episodeOrChapter.getPublisherName()}));
                    break;
                case PlaylistResponseWrapper:
                    if(!item.getPlaylist().isPresent()) break;
                    UnofficialSpotifyAPI.HomeTabPlaylist playlist = item.getPlaylist().get();
                    usersuricache.add(playlist.getUri());
                    homepanelusertable.addModifyAction(() -> ((DefaultTableModel) homepanelusertable.getModel()).addRow(new Object[]{playlist.getName(), playlist.getOwnerName()}));
                    break;
            }
        }

        for (UnofficialSpotifyAPI.HomeTabSection section : tab.getSections().subList(1, tab.getSections().size())) {
            addModule(section);
        }
    }


    public JScrollPane getComponent() {
        return scrollholder;
    }

    public JPanel getPanel() {
        return content;
    }

    @Override
    public void makeVisible() {
        scrollholder.setVisible(true);
    }

    @Override
    public void makeInvisible() {
        scrollholder.setVisible(false);
    }
}
