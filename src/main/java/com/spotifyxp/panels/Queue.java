package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.utils.AsyncActionListener;
import com.spotifyxp.utils.TrackUtils;

import javax.swing.*;
import java.util.ArrayList;

public class Queue extends JPanel {
    public static JButton queueremovebutton;
    public static JScrollPane queuescrollpane;
    public static JList<String> queuelist;
    public static final ArrayList<String> queueuricache = new ArrayList<>();
    public static final DefaultListModel<String> queuelistmodel = new DefaultListModel<>();


    public Queue() {
        setBounds(0, 0, 784, 421);
        ContentPanel.tabpanel.add(this);
        setLayout(null);
        queueremovebutton = new JButton(PublicValues.language.translate("ui.queue.remove"));
        queueremovebutton.setBounds(0, 398, 784, 23);
        add(queueremovebutton);
        queueremovebutton.setForeground(PublicValues.globalFontColor);
        queuescrollpane = new JScrollPane();
        queuescrollpane.setBounds(0, 0, 784, 395);
        add(queuescrollpane);
        queuelist = new JList<>(queuelistmodel);
        queuelist.setForeground(PublicValues.globalFontColor);
        queuescrollpane.setViewportView(queuelist);
        Events.subscribe(SpotifyXPEvents.queueUpdate.getName(), () -> {
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
        Events.subscribe(SpotifyXPEvents.queueAdvance.getName(), () -> {
            if (queuelistmodel.isEmpty()) {
                return;
            }
            queueuricache.remove(0);
            queuelistmodel.remove(0);
        });
        Events.subscribe(SpotifyXPEvents.queueRegress.getName(), () -> {
            if (queuelistmodel.isEmpty()) {
                return;
            }
            if (!queuelistmodel.get(0).equalsIgnoreCase(PublicValues.spotifyplayer.tracks(true).current.getUri())) {
                try {
                    Track t = InstanceManager.getSpotifyApi().getTrack(PublicValues.spotifyplayer.tracks(true).current.getUri().split(":")[2]).build().execute();
                    String a = TrackUtils.getArtists(t.getArtists());
                    queueuricache.add(0, t.getUri());
                    queuelistmodel.add(0, t.getName() + " - " + a);
                } catch (Exception e) {
                    throw new RuntimeException("Cant regress queue");
                }
            }
        });
        queueremovebutton.addActionListener(new AsyncActionListener(e -> {
            InstanceManager.getPlayer().getPlayer().removeFromQueue(queueuricache.get(queuelist.getSelectedIndex()));
            queueuricache.remove(queueuricache.get(queuelist.getSelectedIndex()));
            ((DefaultListModel<?>) queuelist.getModel()).remove(queuelist.getSelectedIndex());
        }));
    }
}
