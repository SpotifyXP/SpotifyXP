package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Paging;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.utils.TrackUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Playlists extends JPanel {
    public static JPanel playlistsplaylistslist;
    public static JScrollPane playlistsplaylistsscroll;
    public static JPanel playlistssonglist;
    public static JScrollPane playlistssongsscroll;
    public static DefTable playlistsplayliststable;
    public static DefTable playlistssongtable;
    public static final ArrayList<String> playlistsuricache = new ArrayList<>();
    public static final ArrayList<String> playlistssonguricache = new ArrayList<>();


    public Playlists() {
        setBounds(0, 0, 784, 421);
        ContentPanel.tabpanel.add(this);
        setLayout(null);
        playlistsplaylistslist = new JPanel();
        playlistsplaylistslist.setBounds(0, 0, 259, 421);
        add(playlistsplaylistslist);
        playlistsplaylistslist.setLayout(null);
        playlistsplaylistsscroll = new JScrollPane();
        playlistsplaylistsscroll.setBounds(0, 0, 259, 421);
        playlistsplaylistslist.add(playlistsplaylistsscroll);
        playlistsplayliststable = new DefTable();
        playlistsplayliststable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.playlists.playlists.playlistname")}));
        playlistsplayliststable.setForeground(PublicValues.globalFontColor);
        playlistsplayliststable.getColumnModel().getColumn(0).setPreferredWidth(623);
        playlistsplayliststable.setFillsViewportHeight(true);
        playlistsplayliststable.setColumnSelectionAllowed(true);
        playlistsplayliststable.getTableHeader().setForeground(PublicValues.globalFontColor);
        playlistsplaylistsscroll.setViewportView(playlistsplayliststable);
        playlistssonglist = new JPanel();
        playlistssonglist.setBounds(260, 0, 524, 421);
        add(playlistssonglist);
        playlistssonglist.setLayout(null);
        playlistssongsscroll = new JScrollPane();
        playlistssongsscroll.setBounds(0, 0, 524, 421);
        playlistssonglist.add(playlistssongsscroll);
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
        playlistsplayliststable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefThread thread = new DefThread(() -> {
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
                                    ((DefaultTableModel) playlistssongtable.getModel()).addRow(new Object[]{track.getTrack().getName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
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
                    });
                    thread.start();
                }
            }
        });
        playlistssongtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(playlistssonguricache.get(playlistssongtable.getSelectedRow()), true, PublicValues.shuffle, false);
                    TrackUtils.addAllToQueue(playlistssonguricache, playlistssongtable);
                }
            }
        });
    }
}
