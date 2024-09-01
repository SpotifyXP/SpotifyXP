package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Artist;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.utils.*;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HotList extends JPanel {
    public static DefTable hotlistplayliststable;
    public static DefTable hotlistsongstable;
    public static JPanel hotlistplaylistspanel;
    public static JScrollPane hotlistplaylistsscrollpanel;
    public static JPanel hotlistsonglistpanel;
    public static JScrollPane hotslistsongscrollpanel;
    public static final ArrayList<String> hotlistplaylistlistcache = new ArrayList<>();
    public static final ArrayList<String> hotlistsonglistcache = new ArrayList<>();
    public static ContextMenu hotlistplaylistspanelrightclickmenu;
    public static ContextMenu hotlistsongstablecontextmenu;

    public HotList() {
        setBounds(0, 0, 784, 421);
        ContentPanel.tabpanel.add(this);
        setLayout(null);
        hotlistplaylistspanel = new JPanel();
        hotlistplaylistspanel.setBounds(0, 0, 259, 421);
        add(hotlistplaylistspanel);
        hotlistplaylistspanel.setLayout(null);
        hotlistplaylistsscrollpanel = new JScrollPane();
        hotlistplaylistsscrollpanel.setBounds(0, 0, 259, 421);
        hotlistplaylistspanel.add(hotlistplaylistsscrollpanel);
        hotlistplayliststable = new DefTable();
        hotlistplayliststable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.hotlist.playlistlist.playlists")}));
        hotlistplayliststable.setForeground(PublicValues.globalFontColor);
        hotlistplayliststable.getTableHeader().setForeground(PublicValues.globalFontColor);
        hotlistplayliststable.getColumnModel().getColumn(0).setPreferredWidth(623);
        hotlistplayliststable.setFillsViewportHeight(true);
        hotlistplayliststable.setColumnSelectionAllowed(true);
        hotlistplaylistsscrollpanel.setViewportView(hotlistplayliststable);
        hotlistsonglistpanel = new JPanel();
        hotlistsonglistpanel.setBounds(260, 0, 524, 421);
        add(hotlistsonglistpanel);
        hotlistsonglistpanel.setLayout(null);
        hotslistsongscrollpanel = new JScrollPane();
        hotslistsongscrollpanel.setBounds(0, 0, 524, 421);
        hotlistsonglistpanel.add(hotslistsongscrollpanel);
        hotlistplaylistspanelrightclickmenu = new ContextMenu(hotlistplayliststable);
        hotlistplaylistspanelrightclickmenu.addItem(PublicValues.language.translate("ui.general.refresh"), () -> {
            hotlistplaylistlistcache.clear();
            hotlistsonglistcache.clear();
            ((DefaultTableModel) hotlistsongstable.getModel()).setRowCount(0);
            ((DefaultTableModel) hotlistplayliststable.getModel()).setRowCount(0);
            fetchHotlist();
        });
        hotlistsongstable = new DefTable();
        hotlistsongstable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.hotlist.songlist.songtitle"), PublicValues.language.translate("ui.hotlist.songlist.filesize"), PublicValues.language.translate("ui.hotlist.songlist.bitrate"), PublicValues.language.translate("ui.hotlist.songlist.length")}));
        hotlistsongstablecontextmenu = new ContextMenu(hotlistsongstable);
        hotlistsongstablecontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(hotlistsonglistcache.get(hotlistsongstable.getSelectedRow())));
        hotlistsongstable.getColumnModel().getColumn(0).setPreferredWidth(363);
        hotlistsongstable.getColumnModel().getColumn(1).setPreferredWidth(89);
        hotlistsongstable.getColumnModel().getColumn(3).setPreferredWidth(96);
        hotlistsongstable.setFillsViewportHeight(true);
        hotlistsongstable.setColumnSelectionAllowed(true);
        hotlistsongstable.setForeground(PublicValues.globalFontColor);
        hotlistsongstable.getTableHeader().setForeground(PublicValues.globalFontColor);
        hotslistsongscrollpanel.setViewportView(hotlistsongstable);
        hotlistplayliststable.getTableHeader().setReorderingAllowed(false);
        hotlistsongstable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hotlistsongstable.setColumnSelectionInterval(0, hotlistsongstable.getColumnCount() - 1);
                if (e.getClickCount() == 2) {
                    // Factory.getPlayer().getPlayer().tracks(true).next.clear();
                    InstanceManager.getPlayer().getPlayer().load(hotlistsonglistcache.get(hotlistsongstable.getSelectedRow()), true, PublicValues.shuffle);
                    TrackUtils.addAllToQueue(hotlistsonglistcache, hotlistsongstable);
                }
            }
        }));
        hotlistplayliststable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((DefaultTableModel) hotlistsongstable.getModel()).setRowCount(0);
                hotlistsonglistcache.clear();
                if (e.getClickCount() == 2) {
                    for (TrackSimplified track : SpotifyUtils.getAllTracksAlbum(hotlistplaylistlistcache.get(hotlistplayliststable.getSelectedRow()))) {
                        String a = TrackUtils.getArtists(track.getArtists());
                        hotlistsonglistcache.add(track.getUri());
                        ((DefaultTableModel) hotlistsongstable.getModel()).addRow(new Object[]{track.getName() + " - " + a, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
                    }
                }
            }
        }));
    }

    public static void fetchHotlist() {
        try {
            Artist[] artist = InstanceManager.getSpotifyApi().getUsersTopArtists().build().execute().getItems();
            PlayHistory[] history = InstanceManager.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks().limit(10).build().execute().getItems();
            ArrayList<String> genres = new ArrayList<>();
            ArrayList<String> tracks = new ArrayList<>();
            ArrayList<String> artists = new ArrayList<>();
            for (PlayHistory h : history) {
                tracks.add(h.getTrack().getUri().split(":")[2]);
            }
            for (Artist a : artist) {
                genres.addAll(Arrays.asList(a.getGenres()));
                artists.add(a.getUri().split(":")[2]);
            }
            String[] fiveartists = getRandomValues(artists, 2);
            String[] fivegenres = getRandomValues(genres, 2);
            String[] fivetracks = getRandomValues(tracks, 1);
            TrackSimplified[] finaltracks = InstanceManager.getSpotifyApi().getRecommendations().seed_artists(Arrays.toString(fiveartists).replace("  ", ",").replace(" ", "")).seed_genres(Arrays.toString(fivegenres).replace("  ", ",").replace(" ", "")).seed_tracks(Arrays.toString(fivetracks).replace("  ", ",").replace(" ", "")).build().execute().getTracks();
            for (TrackSimplified t : finaltracks) {
                try {
                    AlbumSimplified albumreq = InstanceManager.getSpotifyApi().getTrack(t.getUri().split(":")[2]).build().execute().getAlbum();
                    String a = TrackUtils.getArtists(albumreq.getArtists());
                    ((DefaultTableModel) hotlistplayliststable.getModel()).addRow(new Object[]{albumreq.getName() + " - " + a});
                    hotlistplaylistlistcache.add(albumreq.getUri());
                } catch (IOException | ParseException | SpotifyWebApiException e) {
                    ConsoleLogging.Throwable(e);
                }
            }
        } catch (Exception exception) {
            GraphicalMessage.openException(exception);
        }
    }

    private static String[] getRandomValues(ArrayList<String> inputList, int count) {
        if (count <= 0) {
            return new String[]{}; // Return an empty list if count is zero or negative.
        }
        // Create a copy of the input list to avoid modifying the original list.
        ArrayList<String> copyList = new ArrayList<>(inputList);
        // Shuffle the copy list to randomize the order.
        Collections.shuffle(copyList);
        // Ensure that count does not exceed the size of the copy list.
        count = Math.min(count, copyList.size());
        // Create a sublist containing the first 'count' elements from the shuffled list.
        return new String[]{copyList.subList(0, count).toString().replaceAll("\\[", "").replaceAll("]", "")};
    }
}
