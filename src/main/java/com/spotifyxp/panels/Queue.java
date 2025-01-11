package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.DDReorderList;
import com.spotifyxp.utils.AsyncActionListener;
import com.spotifyxp.utils.TrackUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Queue extends JPanel implements View {
    public static JScrollPane queuescrollpane;
    public static DDReorderList<String> queuelist;
    public static final ArrayList<String> queueuricache = new ArrayList<>();
    public static final DefaultListModel<String> queuelistmodel = new DefaultListModel<>();
    public static ContextMenu contextMenu;


    //ToDo: Implement the reordering of the queue after dragging
    public Queue() throws IOException {
        setBounds(0, 0, 784, 421);
        ContentPanel.tabpanel.add(this);
        setLayout(null);
        setVisible(false);
        queuescrollpane = new JScrollPane();
        queuescrollpane.setBounds(0, 0, 784, 395);
        add(queuescrollpane);
        queuelist = new DDReorderList<>(queuelistmodel);
        // ToDo: Fix multiple firing of onReordered when dragged
        queuelist.setOnReordered(new DDReorderList.OnReordered<String>() {
            @Override
            public void run(int newIndex, int oldIndex, String name) {
                String uri = queueuricache.get(oldIndex);
                queueuricache.remove(oldIndex);
                queueuricache.add(newIndex, uri);
                try {
                    InstanceManager.getPlayer().getPlayer().clearQueue();
                } catch (Exception exc) {
                    ConsoleLogging.warning("Couldn't queue tracks");
                    ConsoleLogging.Throwable(exc);
                    return;
                }
                ArrayList<ContextTrackOuterClass.ContextTrack> tracks = new ArrayList<>();
                for(String s : queueuricache) {
                    tracks.add(ContextTrackOuterClass.ContextTrack.newBuilder().setUri(s).build());
                }
                InstanceManager.getPlayer().getPlayer().setQueue(tracks);
            }
        });
        queuelist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int selectedIndex = queuelist.getSelectedIndex();
                    ((DefaultListModel<String>) queuelist.getModel()).removeRange(0, selectedIndex);
                    String uri = queueuricache.get(selectedIndex);
                    queueuricache.subList(0, selectedIndex).clear();
                    InstanceManager.getPlayer().getPlayer().load(uri, true, PublicValues.shuffle);
                    InstanceManager.getPlayer().getPlayer().clearQueue();
                    ArrayList<ContextTrackOuterClass.ContextTrack> tracks = new ArrayList<>();
                    for(String s : queueuricache) {
                        tracks.add(ContextTrackOuterClass.ContextTrack.newBuilder().setUri(s).build());
                    }
                    InstanceManager.getPlayer().getPlayer().setQueue(tracks);
                }
            }
        });
        queuelist.setForeground(PublicValues.globalFontColor);
        queuelist.setBackground(getBackground());
        queuescrollpane.setViewportView(queuelist);
        Events.subscribe(SpotifyXPEvents.addtoqueue.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                if(!queueuricache.isEmpty()) {
                    queueuricache.add((String) data[0]);
                    try {
                        Track track = InstanceManager.getSpotifyApi().getTrack(((String) data[0]).split(":")[2]).build().execute();
                        String a = TrackUtils.getArtists(track.getArtists());
                        queuelistmodel.addElement(track.getName() + " - " + a);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // This happens when (psst... i dont know)
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to list tracks in queue");
                    }
                }
            }
        });
        Events.subscribe(SpotifyXPEvents.queueUpdate.getName(), (Object... data) -> {
            if(InstanceManager.getPlayer().getPlayer().tracks(true).next.size() > queueuricache.size()) {
                queueuricache.clear();
                ((DefaultListModel<?>) queuelist.getModel()).clear();
                try {
                    for (ContextTrackOuterClass.ContextTrack t : PublicValues.spotifyplayer.tracks(true).next) {
                        Track track = InstanceManager.getSpotifyApi().getTrack(t.getUri().split(":")[2]).build().execute();
                        queueuricache.add(t.getUri());
                        String a = TrackUtils.getArtists(track.getArtists());
                        queuelistmodel.addElement(track.getName() + " - " + a);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // This happens when (psst... i dont know)
                } catch (Exception e) {
                    throw new RuntimeException("Failed to list tracks in queue");
                }
                return;
            }
            if (!queuelistmodel.isEmpty() && !queueuricache.isEmpty() && data[0] != null && (data[0] instanceof String)) {
                if(queueuricache.get(0).equals(data[0])) {
                    Events.triggerEvent(SpotifyXPEvents.queueAdvance.getName());
                    return;
                } else {
                    Events.triggerEvent(SpotifyXPEvents.queueRegress.getName());
                    return;
                }
            }
            if (queuelistmodel.isEmpty()) {
                return;
            }
            queueuricache.clear();
            ((DefaultListModel<?>) queuelist.getModel()).clear();
            try {
                for (ContextTrackOuterClass.ContextTrack t : PublicValues.spotifyplayer.tracks(true).next) {
                    Track track = InstanceManager.getSpotifyApi().getTrack(t.getUri().split(":")[2]).build().execute();
                    queueuricache.add(t.getUri());
                    String a = TrackUtils.getArtists(track.getArtists());
                    queuelistmodel.addElement(track.getName() + " - " + a);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // This happens when (psst... i dont know)
            } catch (Exception e) {
                throw new RuntimeException("Failed to list tracks in queue");
            }
        });
        Events.subscribe(SpotifyXPEvents.queueAdvance.getName(), (Object... data) -> {
            if (queuelistmodel.isEmpty()) {
                return;
            }
            queueuricache.remove(0);
            queuelistmodel.remove(0);
        });
        Events.subscribe(SpotifyXPEvents.queueRegress.getName(), (Object... data) -> {
            if (queuelistmodel.isEmpty()) {
                return;
            }
            queueuricache.clear();
            ((DefaultListModel<?>) queuelist.getModel()).clear();
            try {
                for (ContextTrackOuterClass.ContextTrack t : PublicValues.spotifyplayer.tracks(true).next) {
                    Track track = InstanceManager.getSpotifyApi().getTrack(t.getUri().split(":")[2]).build().execute();
                    queueuricache.add(t.getUri());
                    String a = TrackUtils.getArtists(track.getArtists());
                    queuelistmodel.addElement(track.getName() + " - " + a);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // This happens when (psst... i dont know)
            } catch (Exception e) {
                throw new RuntimeException("Failed to list tracks in queue");
            }
        });
        contextMenu = new ContextMenu(queuelist);
        contextMenu.addItem("Remove", () -> {
            String uri = queueuricache.get(queuelist.getSelectedIndex());
            ArrayList<ContextTrackOuterClass.ContextTrack> tracks = new ArrayList<>();
            for(ContextTrackOuterClass.ContextTrack track : InstanceManager.getPlayer().getPlayer().tracks(true).next) {
                if(track.hasUri() && track.getUri().equals(uri)) {
                    continue;
                }
                tracks.add(track);
            }
            InstanceManager.getPlayer().getPlayer().setQueue(tracks);
            queueuricache.remove(queueuricache.get(queuelist.getSelectedIndex()));
            ((DefaultListModel<?>) queuelist.getModel()).remove(queuelist.getSelectedIndex());
        });
    }

    @Override
    public void makeVisible() {
        if (Queue.queuelistmodel.isEmpty()) {
            ((DefaultListModel<?>) Queue.queuelist.getModel()).removeAllElements();
            Queue.queueuricache.clear();
            Thread queueworker = new Thread(() -> {
                try {
                    for (ContextTrackOuterClass.ContextTrack track : InstanceManager.getPlayer().getPlayer().tracks(true).next) {
                        Track t = InstanceManager.getSpotifyApi().getTrack(track.getUri().split(":")[2]).build().execute();
                        String a = TrackUtils.getArtists(t.getArtists());
                        Queue.queueuricache.add(track.getUri());
                        Queue.queuelistmodel.addElement(t.getName() + " - " + a);
                    }
                } catch (IOException ex) {
                    ConsoleLogging.Throwable(ex);
                } catch (NullPointerException exc) {
                    // Nothing in queue
                }
            }, "Queue worker (ContentPanel)");
            queueworker.start();
        }
        setVisible(true);
    }

    @Override
    public void makeInvisible() {
        setVisible(false);
    }
}
