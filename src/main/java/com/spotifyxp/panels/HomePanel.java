package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Artist;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.utils.AsyncMouseListener;
import com.spotifyxp.utils.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HomePanel extends JScrollPane implements View {
    public static JPanel content;
    public static UnofficialSpotifyAPI.HomeTab tab;
    public static ContextMenu menu;
    public static CompletableFuture<?> future;
    public static ArrayList<ArrayList<String>> uriCacheForAll;

    public HomePanel() {
        uriCacheForAll = new ArrayList<>();
        content = new JPanel();
        content.setLayout(null);

        menu = new ContextMenu(content, null, getClass());
        menu.addItem("Refresh", this::refill);

        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVisible(false);
        setViewportView(content);

        future = new CompletableFuture<>();
        Thread requestTabThread = new Thread(() -> {
            try {
                tab = InstanceManager.getUnofficialSpotifyApi().getHomeTab();
                future.complete(null);
            } catch (IOException e) {
                future.cancel(false);
                throw new RuntimeException(e);
            }
        }, "Request home tab");
        requestTabThread.start();
        Events.subscribe(SpotifyXPEvents.onFrameVisible.getName(), (args) -> {
            Thread thread = new Thread(() -> {
                try {
                    future.get();
                }catch (CancellationException e) {
                    ConsoleLogging.error("Failed to get home tab");
                    return;
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                SwingUtilities.invokeLater(this::fill);
            }, "Wait for home tab");
            thread.start();
        });
    }

    public enum ContentTypes {
        show,
        track,
        album,
        artist,
        episode,
        user,
        playlist
    }

    public void addModule(UnofficialSpotifyAPI.HomeTabSection section, int titleHeight, int x, int y, int titleY, int width, int height) {
        ArrayList<String> uricache = new ArrayList<>();
        JLabel homepanelmoduletext = new JLabel(section.getName().orElse(""));
        homepanelmoduletext.setFont(new Font("Tahoma", Font.PLAIN, 16));
        homepanelmoduletext.setBounds(x, titleY, width, titleHeight);
        content.add(homepanelmoduletext);

        homepanelmoduletext.setForeground(PublicValues.globalFontColor);

        JScrollPane homepanelmodulescrollpanel = new JScrollPane();
        homepanelmodulescrollpanel.setBounds(x, y, width, height);
        content.add(homepanelmodulescrollpanel);

        DefTable homepanelmodulecontenttable = new DefTable() {
        };
        homepanelmodulecontenttable.setName(String.valueOf(uriCacheForAll.size()));
        homepanelmodulescrollpanel.setViewportView(homepanelmodulecontenttable);
        homepanelmodulecontenttable.setForeground(PublicValues.globalFontColor);
        homepanelmodulecontenttable.getTableHeader().setForeground(PublicValues.globalFontColor);

        new ContextMenu(homepanelmodulecontenttable, uricache, getClass());

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
                                InstanceManager.getSpotifyPlayer().load(uri, true, PublicValues.shuffle);
                                Events.triggerEvent(SpotifyXPEvents.queueUpdate.getName());
                                break;
                            case artist:
                                setVisible(false);
                                ContentPanel.switchView(Views.ARTIST);
                                try {
                                    Artist a = InstanceManager.getSpotifyApi().getArtist(id).build().execute();
                                    ContentPanel.artistPanel.fillWith(a);
                                } catch (IOException ex) {
                                    ConsoleLogging.Throwable(ex);
                                }
                                break;
                            case episode:
                                InstanceManager.getSpotifyPlayer().load(uri, true, PublicValues.shuffle);
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
        uriCacheForAll.add(uricache);
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
        int width = getWidth() - 32;
        int height = 261;
        int spacing = 70;
        int xCache = 10;
        int titleHeight = getFontMetrics(getFont()).getHeight();
        int yCache = titleHeight + 55;
        int titleSpacing = 5;

        JPanel homepanelgreetings = new JPanel();
        homepanelgreetings.setBounds(0, 11, getWidth(), getFontMetrics(getFont()).getHeight());
        homepanelgreetings.setLayout(new BorderLayout());
        JLabel homepanelgreetingstext = new JLabel(tab.getGreeting());
        homepanelgreetingstext.setFont(new Font("Tahoma", Font.PLAIN, 20));
        homepanelgreetingstext.setHorizontalAlignment(SwingConstants.CENTER);
        homepanelgreetingstext.setForeground(PublicValues.globalFontColor);
        homepanelgreetings.add(homepanelgreetingstext);
        content.add(homepanelgreetings);

        for (UnofficialSpotifyAPI.HomeTabSection section : tab.getSections()) {
            addModule(section, titleHeight, xCache, yCache, yCache - titleHeight - titleSpacing, width, height);
            yCache += height + spacing;
        }
    }

    void requestHome() {
        Thread thread = new Thread(() -> {
            try {
                tab = InstanceManager.getUnofficialSpotifyApi().getHomeTab();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fill();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "Request home");
        thread.start();
    }

    void refill() {
        content.removeAll();
        requestHome();
    }

    void fill() {
        Thread t = new Thread(this::initializeContent, "Get home");
        t.start();
        content.setPreferredSize(new Dimension(content.getWidth(), (261 + getFontMetrics(getFont()).getHeight() + 55) * tab.getSections().size()));
        content.revalidate();
        content.repaint();
    }

    public JPanel getPanel() {
        return content;
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
