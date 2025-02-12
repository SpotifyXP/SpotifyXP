package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Paging;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Playlist;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import com.spotifyxp.dialogs.AddPlaylistDialog;
import com.spotifyxp.dialogs.ChangePlaylistDialog;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.utils.AsyncMouseListener;
import com.spotifyxp.utils.TrackUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Playlists extends JSplitPane implements View {
    public static JScrollPane playlistsPlaylistsScrollPane;
    public static JScrollPane playlistsSongsScrollPane;
    public static DefTable playlistsPlaylistsTable;
    public static DefTable playlistsSongTable;
    public static final ArrayList<String> playlistsUriCache = new ArrayList<>();
    public static final ArrayList<String> playlistsSongUriCache = new ArrayList<>();
    public static ContextMenu playlistsSongTableContextMenu;
    public static ContextMenu playlistsPlaylistsTableContextMenu;
    private final boolean[] inProg = {false};
    private boolean loadNew = false;
    private Runnable lazyLoadingDeInit;


    private void fetchPlaylists() {
        ((DefaultTableModel) playlistsPlaylistsTable.getModel()).setRowCount(0);
        playlistsUriCache.clear();
        try {
            int total = InstanceManager.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute().getTotal();
            int parsed = 0;
            int counter = 0;
            int last = 0;
            int offset = 0;
            while (parsed != total) {
                PlaylistSimplified[] playlists = InstanceManager.getSpotifyApi().getListOfCurrentUsersPlaylists().offset(offset).limit(50).build().execute().getItems();
                for (PlaylistSimplified simplified : playlists) {
                    Playlists.playlistsUriCache.add(simplified.getUri());
                    ((DefaultTableModel) Playlists.playlistsPlaylistsTable.getModel()).addRow(new Object[]{simplified.getName()});
                    parsed++;
                }
                if (parsed == last) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Playlists() {
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setVisible(false);

        playlistsPlaylistsTable = new DefTable();
        playlistsPlaylistsTable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.playlists.playlists.playlistname")}));
        playlistsPlaylistsTable.setForeground(PublicValues.globalFontColor);
        playlistsPlaylistsTable.getColumnModel().getColumn(0).setPreferredWidth(623);
        playlistsPlaylistsTable.setFillsViewportHeight(true);
        playlistsPlaylistsTable.setColumnSelectionAllowed(true);
        playlistsPlaylistsTable.getTableHeader().setForeground(PublicValues.globalFontColor);
        playlistsPlaylistsTable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if(lazyLoadingDeInit != null) {
                        lazyLoadingDeInit.run();
                        lazyLoadingDeInit = null;
                    }
                    if(PublicValues.config.getBoolean(ConfigValues.load_all_tracks.name)) {
                        Thread thread = new Thread(() -> {
                            playlistsSongUriCache.clear();
                            ((DefaultTableModel) playlistsSongTable.getModel()).setRowCount(0);
                            try {
                                int offset = 0;
                                int parsed = 0;
                                int counter = 0;
                                int last = 0;
                                int total = InstanceManager.getSpotifyApi().getPlaylist(playlistsUriCache.get(playlistsPlaylistsTable.getSelectedRow()).split(":")[2]).build().execute().getTracks().getTotal();
                                while (parsed != total) {
                                    Paging<PlaylistTrack> ptracks = InstanceManager.getSpotifyApi().getPlaylistsItems(playlistsUriCache.get(playlistsPlaylistsTable.getSelectedRow()).split(":")[2]).offset(offset).limit(100).build().execute();
                                    for (PlaylistTrack track : ptracks.getItems()) {
                                        ((DefaultTableModel) playlistsSongTable.getModel()).addRow(new Object[]{track.getTrack().getName(), TrackUtils.calculateFileSizeKb(track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                        playlistsSongUriCache.add(track.getTrack().getUri());
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
                                throw new RuntimeException(e1);
                            }
                        }, "Get playlist tracks");
                        thread.start();
                    }else {
                        loadNew = true;
                        lazyLoadingDeInit = TrackUtils.initializeLazyLoadingForPlaylists(
                                playlistsSongsScrollPane,
                                playlistsSongUriCache,
                                playlistsSongTable,
                                new int[] {28},
                                playlistsUriCache.get(playlistsPlaylistsTable.getSelectedRow()).split(":")[2],
                                inProg,
                                loadNew
                        );
                        loadNew = false;
                    }
                }
            }
        }));

        playlistsPlaylistsScrollPane = new JScrollPane();
        playlistsPlaylistsScrollPane.setPreferredSize(new Dimension(259, getHeight()));
        setLeftComponent(playlistsPlaylistsScrollPane);
        playlistsPlaylistsScrollPane.setViewportView(playlistsPlaylistsTable);

        playlistsSongTable = new DefTable();
        playlistsSongTable.getTableHeader().setForeground(PublicValues.globalFontColor);
        playlistsSongTable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.playlists.songslist.songtitle"), PublicValues.language.translate("ui.playlists.songslist.filesize"), PublicValues.language.translate("ui.playlists.songslist.bitrate"), PublicValues.language.translate("ui.playlists.songslist.length")}));
        playlistsSongTable.setForeground(PublicValues.globalFontColor);
        playlistsSongTable.getColumnModel().getColumn(0).setPreferredWidth(363);
        playlistsSongTable.getColumnModel().getColumn(1).setPreferredWidth(89);
        playlistsSongTable.getColumnModel().getColumn(3).setPreferredWidth(96);
        playlistsSongTable.setFillsViewportHeight(true);
        playlistsSongTable.setColumnSelectionAllowed(true);
        playlistsSongTable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(playlistsSongUriCache.get(playlistsSongTable.getSelectedRow()), true, PublicValues.shuffle);
                    TrackUtils.addAllToQueue(playlistsSongUriCache, playlistsSongTable);
                }
            }
        }));

        playlistsSongsScrollPane = new JScrollPane();
        setRightComponent(playlistsSongsScrollPane);
        playlistsSongsScrollPane.setViewportView(playlistsSongTable);

        playlistsSongTableContextMenu = new ContextMenu(playlistsSongTable);
        playlistsSongTableContextMenu.addItem("All to queue", () -> {
            for(String s : playlistsSongUriCache) {
                Events.triggerEvent(SpotifyXPEvents.addtoqueue.getName(), s);
            }
        });
        playlistsSongTableContextMenu.addItem("Add to queue", () -> {
            if(playlistsSongTable.getSelectedRow() == -1) return;
            Events.triggerEvent(SpotifyXPEvents.addtoqueue.getName(), playlistsSongUriCache.get(playlistsSongTable.getSelectedRow()));
        });

        playlistsPlaylistsTableContextMenu = new ContextMenu(playlistsPlaylistsTable);
        playlistsPlaylistsTableContextMenu.addItem(PublicValues.language.translate("ui.general.remove.playlist"), () -> {
            InstanceManager.getSpotifyApi().unfollowPlaylist(playlistsUriCache.get(playlistsPlaylistsTable.getSelectedRow()).split(":")[2]);
            playlistsUriCache.remove(playlistsUriCache.get(playlistsPlaylistsTable.getSelectedRow()));
            ((DefaultTableModel) playlistsPlaylistsTable.getModel()).removeRow(playlistsPlaylistsTable.getSelectedRow());
        });
        playlistsPlaylistsTableContextMenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            StringSelection strSel = new StringSelection(playlistsUriCache.get(playlistsPlaylistsTable.getSelectedRow()));
            clipboard.setContents(strSel, null);
        });
        playlistsPlaylistsTableContextMenu.addItem(PublicValues.language.translate("ui.general.refresh"), () -> {
            new Thread(this::fetchPlaylists, "Fetch playlists").start();
        });
        playlistsPlaylistsTableContextMenu.addItem("Change playlist", () -> {
            if(playlistsPlaylistsTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(ContentPanel.frame, "No playlist selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Playlist playlistRec = InstanceManager.getSpotifyApi().getPlaylist(playlistsUriCache.get(playlistsPlaylistsTable.getSelectedRow()).split(":")[2]).build().execute();
                ChangePlaylistDialog dialog = new ChangePlaylistDialog();
                dialog.show(
                        playlistRec
                        , new ChangePlaylistDialog.ChangedPlaylistRunnable() {
                            @Override
                            public void receive(ChangePlaylistDialog.ChangedPlaylist playlist) {
                                new Thread(() -> {
                                    try {
                                        InstanceManager.getSpotifyApi().changePlaylistsDetails(
                                                playlistRec.getId()
                                        )
                                                .collaborative(playlist.isCollaborative)
                                                .public_(playlist.isPublic)
                                                .name(playlist.playlistName)
                                                .description(playlist.playlistDescription)
                                                .build().execute();
                                        new Thread(Playlists.this::fetchPlaylists, "Fetch playlists").start();
                                    } catch (IOException e) {
                                        ConsoleLogging.Throwable(e);
                                    }
                                }, "Change playlist").start();
                            }
                        });
            } catch (IOException e) {
                ConsoleLogging.Throwable(e);
            }
        });
        playlistsPlaylistsTableContextMenu.addItem(PublicValues.language.translate("playlists.create.title"), () -> {
            try {
                AddPlaylistDialog dialog = new AddPlaylistDialog();
                dialog.show((data) -> {
                    new Thread(() -> {
                        try {
                            String uri = InstanceManager.getSpotifyApi().createPlaylist(
                                    PublicValues.session.username(),
                                    data.name
                            ).public_(data.isPublic).description(data.description).collaborative(data.isCollaborative).build().execute().getUri();
                            if(!data.imageBase64.isEmpty()) {
                                try {
                                    InstanceManager.getSpotifyApi().uploadCustomPlaylistCoverImage(uri.split(":")[2])
                                            .image_data(data.imageBase64)
                                            .build().execute();
                                }catch(IOException e) {
                                    ConsoleLogging.Throwable(e);
                                }
                            }
                            new Thread(this::fetchPlaylists, "Fetch playlists").start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }, "Create playlist thread").start();
                }, () -> {
                }, dialog::dispose);
            }catch (IOException e) {
                ConsoleLogging.Throwable(e);
            }
        });
    }

    @Override
    public void makeVisible() {
        if (Playlists.playlistsPlaylistsTable.getModel().getRowCount() == 0) {
            new Thread(this::fetchPlaylists, "Fetch playlists").start();
        }
        setVisible(true);
    }

    @Override
    public void makeInvisible() {
        setVisible(false);
    }
}
