package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.SavedTrack;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.utils.AsyncMouseListener;
import com.spotifyxp.utils.ClipboardUtil;
import com.spotifyxp.utils.TrackUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Library extends JScrollPane implements View {
    public static DefTable librarySongList;
    public static final ArrayList<String> libraryUriCache = new ArrayList<>();
    private static boolean libraryLoadingInProgress = false;
    public static ContextMenu contextMenu;
    public static final Thread libraryThread = new Thread(new Runnable() {
        public void run() {
            try {
                libraryLoadingInProgress = true;
                int visibleCount = 28;
                int counter = 0;
                int last = 0;
                int parsed = 0;
                while (parsed != visibleCount) {
                    SavedTrack[] track = InstanceManager.getSpotifyApi().getUsersSavedTracks().limit(visibleCount).build().execute().getItems();
                    for (SavedTrack t : track) {
                        libraryUriCache.add(t.getTrack().getUri());
                        String a = TrackUtils.getArtists(t.getTrack().getArtists());
                        librarySongList.addModifyAction(() -> ((DefaultTableModel) librarySongList.getModel()).addRow(new Object[]{t.getTrack().getName() + " - " + a, TrackUtils.calculateFileSizeKb(t.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(t.getTrack().getDurationMs())}));
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
    }, "Library thread");

    public Library() {
        setVisible(false);
        final boolean[] inProg = {false};
        addMouseWheelListener(e -> {
            if (!inProg[0]) {
                inProg[0] = true;
                BoundedRangeModel m = getVerticalScrollBar().getModel();
                int extent = m.getExtent();
                int maximum = m.getMaximum();
                int value = m.getValue();
                if (value + extent >= maximum / 2) {
                    if (ContentPanel.currentView == Views.LIBRARY) {
                        if (!libraryLoadingInProgress) {
                            Thread thread = new Thread(Library::loadNext, "Library load next");
                            thread.start();
                        }
                    }
                }
                inProg[0] = false;
            }
        });

        librarySongList = new DefTable();
        librarySongList.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.library.songlist.songname"), PublicValues.language.translate("ui.library.songlist.filesize"), PublicValues.language.translate("ui.library.songlist.bitrate"), PublicValues.language.translate("ui.library.songlist.length")}));
        librarySongList.getTableHeader().setForeground(PublicValues.globalFontColor);
        librarySongList.setForeground(PublicValues.globalFontColor);
        librarySongList.getColumnModel().getColumn(0).setPreferredWidth(347);
        librarySongList.getColumnModel().getColumn(3).setPreferredWidth(51);
        librarySongList.setFillsViewportHeight(true);
        librarySongList.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(libraryUriCache.get(librarySongList.getSelectedRow()), true, PublicValues.shuffle);
                    Thread thread1 = new Thread(() -> TrackUtils.addAllToQueue(libraryUriCache, librarySongList), "Library add to queue");
                    thread1.start();
                }
            }
        }));
        setViewportView(librarySongList);
        
        createcontextMenu();

        Events.subscribe(SpotifyXPEvents.libraryupdate.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                if(libraryUriCache.size() == 0) return;
                // Parameters
                // Type
                // uri
                if(data[0] instanceof Integer && (Integer) data[0] == 1) {
                    for(int i = 0; i < libraryUriCache.size(); i++) {
                        String uri = libraryUriCache.get(i);
                        if(uri == data[1]) {
                            int counter = i;
                            librarySongList.addModifyAction(() -> ((DefaultTableModel) librarySongList.getModel()).removeRow(counter));
                            libraryUriCache.remove(counter);
                            break;
                        }
                    }
                }else {
                    try {
                        Track track = InstanceManager.getSpotifyApi().getTrack(((String) data[1]).split(":")[2]).build().execute();
                        String artists = TrackUtils.getArtists(track.getArtists());
                        librarySongList.addModifyAction(() -> ((DefaultTableModel) librarySongList.getModel()).insertRow(0, new Object[]{track.getName() + " - " + artists, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getDurationMs())}));
                        libraryUriCache.add(0, (String) data[0]);
                    } catch (IOException e) {
                        ConsoleLogging.Throwable(e);
                    }
                }
            }
        });
    }

    void createcontextMenu() {
        contextMenu = new ContextMenu(librarySongList, libraryUriCache, getClass());
        contextMenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(libraryUriCache.get(librarySongList.getSelectedRow())));
        contextMenu.addItem(PublicValues.language.translate("ui.general.refresh"), () -> {
            libraryUriCache.clear();
            librarySongList.removeAll();
            libraryThread.start();
        });
        contextMenu.addItem("Add to queue", () -> {
            if(librarySongList.getSelectedRow() == -1) return;
            Events.triggerEvent(SpotifyXPEvents.addtoqueue.getName(), libraryUriCache.get(librarySongList.getSelectedRow()));
        });
        contextMenu.addItem(PublicValues.language.translate("ui.general.remove"), () -> {
            InstanceManager.getSpotifyApi().removeUsersSavedTracks(libraryUriCache.get(librarySongList.getSelectedRow()).split(":")[2]);
            libraryUriCache.remove(librarySongList.getSelectedRow());
            ((DefaultTableModel) librarySongList.getModel()).removeRow(librarySongList.getSelectedRow());
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
            if (total != libraryUriCache.size()) {
                while (parsed != 19) {
                    SavedTrack[] track = InstanceManager.getSpotifyApi().getUsersSavedTracks().limit(visibleCount).offset(libraryUriCache.size()).build().execute().getItems();
                    for (SavedTrack t : track) {
                        libraryUriCache.add(t.getTrack().getUri());
                        String a = TrackUtils.getArtists(t.getTrack().getArtists());
                        librarySongList.addModifyAction(() -> ((DefaultTableModel) librarySongList.getModel()).addRow(new Object[]{t.getTrack().getName() + " - " + a, TrackUtils.calculateFileSizeKb(t.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(t.getTrack().getDurationMs())}));
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

    @Override
    public void makeVisible() {
        setVisible(true);
    }

    @Override
    public void makeInvisible() {
        setVisible(false);
    }
}
