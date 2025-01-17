package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Paging;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import com.spotifyxp.dialogs.AddPlaylistDialog;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.guielements.DefTable;
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
    public static JScrollPane playlistsplaylistsscroll;
    public static JScrollPane playlistssongsscroll;
    public static DefTable playlistsplayliststable;
    public static DefTable playlistssongtable;
    public static final ArrayList<String> playlistsuricache = new ArrayList<>();
    public static final ArrayList<String> playlistssonguricache = new ArrayList<>();
    private boolean[] inProg = {false};
    private boolean loadnew = false;
    private Runnable lazyLoadingDeInit;


    public Playlists() {
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setVisible(false);
        playlistsplaylistsscroll = new JScrollPane();
        playlistsplaylistsscroll.setPreferredSize(new Dimension(259, getHeight()));
        setLeftComponent(playlistsplaylistsscroll);
        playlistsplayliststable = new DefTable();
        playlistsplayliststable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.playlists.playlists.playlistname")}));
        playlistsplayliststable.setForeground(PublicValues.globalFontColor);
        playlistsplayliststable.getColumnModel().getColumn(0).setPreferredWidth(623);
        playlistsplayliststable.setFillsViewportHeight(true);
        playlistsplayliststable.setColumnSelectionAllowed(true);
        playlistsplayliststable.getTableHeader().setForeground(PublicValues.globalFontColor);
        playlistsplaylistsscroll.setViewportView(playlistsplayliststable);
        playlistssongsscroll = new JScrollPane();
        setRightComponent(playlistssongsscroll);
        playlistssongtable = new DefTable();
        playlistssongtable.getTableHeader().setForeground(PublicValues.globalFontColor);
        playlistssongtable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.playlists.songslist.songtitle"), PublicValues.language.translate("ui.playlists.songslist.filesize"), PublicValues.language.translate("ui.playlists.songslist.bitrate"), PublicValues.language.translate("ui.playlists.songslist.length")}));
        playlistssongtable.setForeground(PublicValues.globalFontColor);
        playlistssongtable.getColumnModel().getColumn(0).setPreferredWidth(363);
        playlistssongtable.getColumnModel().getColumn(1).setPreferredWidth(89);
        playlistssongtable.getColumnModel().getColumn(3).setPreferredWidth(96);
        playlistssongtable.setFillsViewportHeight(true);
        playlistssongtable.setColumnSelectionAllowed(true);
        playlistssongsscroll.setViewportView(playlistssongtable);

        ContextMenu songsCTXMenu = new ContextMenu(playlistssongtable);
        songsCTXMenu.addItem("All to queue", () -> {
            for(String s : playlistssonguricache) {
                Events.triggerEvent(SpotifyXPEvents.addtoqueue.getName(), s);
            }
        });
        songsCTXMenu.addItem("Add to queue", () -> {
            if(playlistssongtable.getSelectedRow() == -1) return;
            Events.triggerEvent(SpotifyXPEvents.addtoqueue.getName(), playlistssonguricache.get(playlistssongtable.getSelectedRow()));
        });

        ContextMenu menu = new ContextMenu(playlistsplayliststable);
        menu.addItem(PublicValues.language.translate("ui.general.remove.playlist"), () -> {
            InstanceManager.getSpotifyApi().unfollowPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]);
            playlistsuricache.remove(playlistsuricache.get(playlistsplayliststable.getSelectedRow()));
            ((DefaultTableModel) playlistsplayliststable.getModel()).removeRow(playlistsplayliststable.getSelectedRow());
        });
        menu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            StringSelection strSel = new StringSelection(playlistsuricache.get(playlistsplayliststable.getSelectedRow()));
            clipboard.setContents(strSel, null);
        });
        menu.addItem(PublicValues.language.translate("playlists.create.title"), () -> {
            AddPlaylistDialog dialog = new AddPlaylistDialog();
            dialog.show((playlistname, playlistvisibility) -> {
                try {
                    String uri = InstanceManager.getSpotifyApi().createPlaylist(PublicValues.session.username(), playlistname).public_(playlistvisibility).build().execute().getUri();
                    playlistsplayliststable.addModifyAction(new Runnable() {
                        @Override
                        public void run() {
                            playlistsuricache.add(uri);
                            ((DefaultTableModel) playlistsplayliststable.getModel()).addRow(new Object[]{playlistname});
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, () -> {
            }, dialog::dispose);
        });
        playlistsplayliststable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if(lazyLoadingDeInit != null) {
                        lazyLoadingDeInit.run();
                        lazyLoadingDeInit = null;
                    }
                    if(PublicValues.config.getBoolean(ConfigValues.load_all_tracks.name)) {
                        Thread thread = new Thread(() -> {
                            playlistssonguricache.clear();
                            ((DefaultTableModel) playlistssongtable.getModel()).setRowCount(0);
                            try {
                                int offset = 0;
                                int parsed = 0;
                                int counter = 0;
                                int last = 0;
                                int total = InstanceManager.getSpotifyApi().getPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).build().execute().getTracks().getTotal();
                                while (parsed != total) {
                                    Paging<PlaylistTrack> ptracks = InstanceManager.getSpotifyApi().getPlaylistsItems(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).offset(offset).limit(100).build().execute();
                                    for (PlaylistTrack track : ptracks.getItems()) {
                                        ((DefaultTableModel) playlistssongtable.getModel()).addRow(new Object[]{track.getTrack().getName(), TrackUtils.calculateFileSizeKb(track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                        playlistssonguricache.add(track.getTrack().getUri());
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
                        loadnew = true;
                        lazyLoadingDeInit = TrackUtils.initializeLazyLoadingForPlaylists(
                                playlistssongsscroll,
                                playlistssonguricache,
                                playlistssongtable,
                                new int[] {28},
                                playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2],
                                inProg,
                                loadnew
                        );
                        loadnew = false;
                    }
                }
            }
        }));
        playlistssongtable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(playlistssonguricache.get(playlistssongtable.getSelectedRow()), true, PublicValues.shuffle);
                    TrackUtils.addAllToQueue(playlistssonguricache, playlistssongtable);
                }
            }
        }));
    }

    @Override
    public void makeVisible() {
        Thread thread = new Thread(() -> {
            try {
                int total = InstanceManager.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute().getTotal();
                int parsed = 0;
                int counter = 0;
                int last = 0;
                int offset = 0;
                while (parsed != total) {
                    PlaylistSimplified[] playlists = InstanceManager.getSpotifyApi().getListOfCurrentUsersPlaylists().offset(offset).limit(50).build().execute().getItems();
                    for (PlaylistSimplified simplified : playlists) {
                        Playlists.playlistsuricache.add(simplified.getUri());
                        ((DefaultTableModel) Playlists.playlistsplayliststable.getModel()).addRow(new Object[]{simplified.getName()});
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
        }, "Playlists fetcher");
        if (Playlists.playlistsplayliststable.getModel().getRowCount() == 0) {
            thread.start();
        }
        setVisible(true);
    }

    @Override
    public void makeInvisible() {
        setVisible(false);
    }
}
