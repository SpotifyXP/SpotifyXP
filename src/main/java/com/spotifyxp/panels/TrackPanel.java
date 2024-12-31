package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.*;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.utils.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
This class holds a panel that shows a table that contains a list of tracks
 **/
public class TrackPanel extends Panel implements View {
    public static DefTable advancedsongtable;
    public static JScrollPane advancedscrollpanel;
    public static JButton advancedbackbutton;
    public static final ArrayList<String> advanceduricache = new ArrayList<>();
    private static Runnable lazyLoadingDeInit;
    public static String advancedSongPanelUri;
    private boolean blockDefaultBackAction = false;

    public TrackPanel() {
        setBounds(0, 0, 784, 421);
        setLayout(null);
        advancedbackbutton = new JButton(PublicValues.language.translate("ui.back"));
        advancedbackbutton.setBounds(0, 0, 89, 23);
        add(advancedbackbutton);
        advancedbackbutton.setForeground(PublicValues.globalFontColor);
        setVisible(false);
        advancedbackbutton.addActionListener(new AsyncActionListener(e -> {
            if(lazyLoadingDeInit != null) {
                lazyLoadingDeInit.run();
                lazyLoadingDeInit = null;
            }
            if(blockDefaultBackAction) {
                blockDefaultBackAction = false;
                return;
            }
            ContentPanel.switchView(ContentPanel.lastView);
            ContentPanel.enableTabSwitch();
        }));
        advancedsongtable = new DefTable();
        advancedsongtable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")}));
        advancedsongtable.setForeground(PublicValues.globalFontColor);
        advancedsongtable.getTableHeader().setForeground(PublicValues.globalFontColor);
        advancedscrollpanel = new JScrollPane();
        advancedscrollpanel.setBounds(0, 22, 784, 399);
        add(advancedscrollpanel);
        advancedscrollpanel.setViewportView(advancedsongtable);
        advancedsongtable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(advanceduricache.get(advancedsongtable.getSelectedRow()), true, PublicValues.shuffle);
                    advancedsongtable.setColumnSelectionInterval(0, advancedsongtable.getColumnCount() - 1);
                    TrackUtils.addAllToQueue(advanceduricache, advancedsongtable);
                }
            }
        }));
    }

    ActionListener customListener;
    public void open(String foruri, HomePanel.ContentTypes contentType, Runnable onBack) {
        blockDefaultBackAction = true;
        customListener = e -> {
            onBack.run();
            advancedbackbutton.removeActionListener(customListener);
        };
        open(foruri, contentType);
        advancedbackbutton.addActionListener(customListener);
    }

    private static boolean[] inProg = {false};
    private static boolean loadnew = false;
    public void open(String foruri, HomePanel.ContentTypes contentType) {
        ContentPanel.switchView(Views.TRACKPANEL);
        advancedSongPanelUri = foruri;
        ((DefaultTableModel) advancedsongtable.getModel()).setRowCount(0);
        advanceduricache.clear();
        try {
            switch (contentType) {
                case playlist:
                    if(PublicValues.config.getBoolean(ConfigValues.load_all_tracks.name)) {
                        Thread thread = new Thread(() -> {
                            advanceduricache.clear();
                            ((DefaultTableModel)  advancedsongtable.getModel()).setRowCount(0);
                            try {
                                int offset = 0;
                                int parsed = 0;
                                int counter = 0;
                                int last = 0;
                                int total = InstanceManager.getSpotifyApi().getPlaylist(foruri.split(":")[2]).build().execute().getTracks().getTotal();
                                while (parsed != total) {
                                    Paging<PlaylistTrack> ptracks = InstanceManager.getSpotifyApi().getPlaylistsItems(foruri.split(":")[2]).offset(offset).limit(100).build().execute();
                                    for (PlaylistTrack track : ptracks.getItems()) {
                                        ((DefaultTableModel)  advancedsongtable.getModel()).addRow(new Object[]{track.getTrack().getName(), TrackUtils.calculateFileSizeKb(track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                        advanceduricache.add(track.getTrack().getUri());
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
                            } catch (NullPointerException e) {
                                ConsoleLogging.warning("Weird nullpointer in TrackPanel");
                            } catch (Exception e1) {
                                throw new RuntimeException(e1);
                            }
                        }, "Get playlist tracks");
                        thread.start();
                    }else {
                        loadnew = true;
                        lazyLoadingDeInit = TrackUtils.initializeLazyLoadingForPlaylists(
                                advancedscrollpanel,
                                advanceduricache,
                                advancedsongtable,
                                new int[] {28},
                                foruri.split(":")[2],
                                inProg,
                                loadnew
                        );
                    }
                    break;
                case show:
                    for (EpisodeSimplified simplified : SpotifyUtils.getAllEpisodesShow(foruri)) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                        advanceduricache.add(simplified.getUri());
                    }
                    break;
                case album:
                    for (TrackSimplified simplified : SpotifyUtils.getAllTracksAlbum(foruri)) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                        advanceduricache.add(simplified.getUri());
                    }
                    break;
                default:
                    GraphicalMessage.bug("tried to invoke showAdvancedSongPanel with incompatible type -> " + contentType);
                    break;
            }
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
        ContentPanel.blockTabSwitch();
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
