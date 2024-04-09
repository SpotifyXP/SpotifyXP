package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.SavedTrack;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.utils.ClipboardUtil;
import com.spotifyxp.utils.TrackUtils;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Library extends JPanel {
    public static DefTable librarysonglist;
    public static JScrollPane libraryscrollpane;
    public static DefaultTableModel librarydefaulttablemodel;
    public static final ArrayList<String> libraryuricache = new ArrayList<>();
    private static boolean libraryLoadingInProgress = false;
    public static final DefThread librarythread = new DefThread(new Runnable() {
        public void run() {
            try {
                libraryLoadingInProgress = true;
                int visibleCount = 28;
                int total = InstanceManager.getSpotifyApi().getUsersSavedTracks().limit(visibleCount).build().execute().getTotal();
                int counter = 0;
                int last = 0;
                int parsed = 0;
                while (parsed != visibleCount) {
                    SavedTrack[] track = InstanceManager.getSpotifyApi().getUsersSavedTracks().limit(visibleCount).build().execute().getItems();
                    for (SavedTrack t : track) {
                        libraryuricache.add(t.getTrack().getUri());
                        String a = TrackUtils.getArtists(t.getTrack().getArtists());
                        librarysonglist.addModifyAction(() -> ((DefaultTableModel) librarysonglist.getModel()).addRow(new Object[]{t.getTrack().getName() + " - " + a, TrackUtils.calculateFileSizeKb(t.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(t.getTrack().getDurationMs())}));
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
                }
                libraryLoadingInProgress = false;
            } catch (Exception e) {
                ConsoleLogging.error("Error loading users library! Library now locked");
                libraryLoadingInProgress = true;
                throw new RuntimeException(e);
            }
        }
    });

    public Library() {
        setBounds(0, 0, 784, 421);
        setLayout(null);
        libraryscrollpane = new JScrollPane();
        libraryscrollpane.setBounds(0, 0, 784, 421);
        add(libraryscrollpane);
        final boolean[] inProg = {false};
        libraryscrollpane.addMouseWheelListener(e -> {
            if (!inProg[0]) {
                inProg[0] = true;
                BoundedRangeModel m = libraryscrollpane.getVerticalScrollBar().getModel();
                int extent = m.getExtent();
                int maximum = m.getMaximum();
                int value = m.getValue();
                if (value + extent >= maximum / 2) {
                    if (ContentPanel.libraryVisble) {
                        if (!libraryLoadingInProgress) {
                            DefThread thread = new DefThread(Library::loadNext);
                            thread.start();
                        }
                    }
                }
                inProg[0] = false;
            }
        });
        librarysonglist = new DefTable();
        librarysonglist.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.library.songlist.songname"), PublicValues.language.translate("ui.library.songlist.filesize"), PublicValues.language.translate("ui.library.songlist.bitrate"), PublicValues.language.translate("ui.library.songlist.length")}));
        librarysonglist.getTableHeader().setForeground(PublicValues.globalFontColor);
        librarysonglist.setForeground(PublicValues.globalFontColor);
        librarydefaulttablemodel = (DefaultTableModel) librarysonglist.getModel();
        librarysonglist.getColumnModel().getColumn(0).setPreferredWidth(347);
        librarysonglist.getColumnModel().getColumn(3).setPreferredWidth(51);
        librarysonglist.setFillsViewportHeight(true);
        libraryscrollpane.setViewportView(librarysonglist);
        librarysonglist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(libraryuricache.get(librarysonglist.getSelectedRow()), true, PublicValues.shuffle, true);
                    DefThread thread1 = new DefThread(() -> TrackUtils.addAllToQueue(libraryuricache, librarysonglist));
                    thread1.start();
                }
            }
        });
        ContextMenu librarymenu = new ContextMenu(librarysonglist);
        librarymenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(libraryuricache.get(librarysonglist.getSelectedRow())));
        librarymenu.addItem(PublicValues.language.translate("ui.general.refresh"), () -> {
            libraryuricache.clear();
            librarysonglist.removeAll();
            librarythread.start();
        });
        librarymenu.addItem(PublicValues.language.translate("ui.general.remove"), () -> {
            InstanceManager.getSpotifyApi().removeUsersSavedTracks(libraryuricache.get(librarysonglist.getSelectedRow()).split(":")[2]);
            libraryuricache.remove(librarysonglist.getSelectedRow());
            ((DefaultTableModel) librarysonglist.getModel()).removeRow(librarysonglist.getSelectedRow());
        });
    }

    public static void loadNext() {
        if (libraryLoadingInProgress) {
            return;
        }
        libraryLoadingInProgress = true;
        try {
            int visibleCount = 19;
            int total = InstanceManager.getSpotifyApi().getUsersSavedTracks().build().execute().getTotal();
            int parsed = 0;
            int counter = 0;
            int last = 0;
            if (total != libraryuricache.size()) {
                while (parsed != 19) {
                    SavedTrack[] track = InstanceManager.getSpotifyApi().getUsersSavedTracks().limit(visibleCount).offset(libraryuricache.size()).build().execute().getItems();
                    for (SavedTrack t : track) {
                        libraryuricache.add(t.getTrack().getUri());
                        String a = TrackUtils.getArtists(t.getTrack().getArtists());
                        librarysonglist.addModifyAction(() -> ((DefaultTableModel) librarysonglist.getModel()).addRow(new Object[]{t.getTrack().getName() + " - " + a, TrackUtils.calculateFileSizeKb(t.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(t.getTrack().getDurationMs())}));
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
                }
            }
        } catch (Exception e) {
            libraryLoadingInProgress = true;
            ConsoleLogging.error("Error loading users library! Library now locked");
            throw new RuntimeException(e);
        }
        libraryLoadingInProgress = false;
    }

    public static void fetchOnlyFirstSongsFromUserLibrary() {
        DefaultTableModel model = (DefaultTableModel) librarysonglist.getModel();
        try {
            int count = 0;
            for (SavedTrack track : InstanceManager.getSpotifyApi().getUsersSavedTracks().limit(10).build().execute().getItems()) {
                if (!libraryuricache.contains(track.getTrack().getUri())) {
                    String a = TrackUtils.getArtists(track.getTrack().getArtists());
                    model.insertRow(count, new Object[]{track.getTrack().getName() + " - " + a, TrackUtils.calculateFileSizeKb(track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                    libraryuricache.add(count, track.getTrack().getUri());
                    count++;
                }
            }
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
