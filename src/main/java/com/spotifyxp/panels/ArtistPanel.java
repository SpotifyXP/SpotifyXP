package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Album;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.utils.AsyncMouseListener;
import com.spotifyxp.utils.ClipboardUtil;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.TrackUtils;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("BusyWait")
public class ArtistPanel extends JPanel {
    public static DefTable artistpopularsonglist;
    public static DefTable artistalbumalbumtable;
    public static JScrollPane artistpopularscrollpane;
    public static JScrollPane artistalbumscrollpanel;
    public static JScrollPane contentPanel;
    public static JLabel artisttitle;
    public static JImagePanel artistbackgroundimage;
    public static JImagePanel artistimage;
    public static ContextMenu artistpopularsonglistcontextmenu;
    public static ContextMenu artistalbumcontextmenu;
    public static boolean isFirst = false;
    public ArtistPanel() {
        contentPanel = new JScrollPane();
        contentPanel.setViewportView(this);
        setLayout(null);
        setPreferredSize(new Dimension(800, 1005));

        JLabel artistpopularlabel = new JLabel("Popular");
        artistpopularlabel.setBounds(5, 291, 137, 27);
        add(artistpopularlabel);

        artistpopularlabel.setForeground(PublicValues.globalFontColor);

        artistpopularscrollpane = new JScrollPane();
        artistpopularscrollpane.setBounds(5, 320, 760, 277);
        add(artistpopularscrollpane);

        artistpopularsonglist = new DefTable() {
        };
        artistpopularscrollpane.setViewportView(artistpopularsonglist);

        artistpopularsonglistcontextmenu = new ContextMenu(artistpopularsonglist);
        artistalbumscrollpanel = new JScrollPane();
        artistalbumscrollpanel.setBounds(5, 667, 760, 295);
        add(artistalbumscrollpanel);

        artistalbumalbumtable = new DefTable() {
        };
        artistalbumscrollpanel.setViewportView(artistalbumalbumtable);

        contentPanel.getVerticalScrollBar().setUnitIncrement(20);

        JLabel artistalbumlabel = new JLabel("Albums");
        artistalbumlabel.setBounds(5, 642, 102, 14);
        add(artistalbumlabel);

        artistalbumlabel.setForeground(PublicValues.globalFontColor);

        artistimage = new JImagePanel();
        artistimage.setBounds(288, 11, 155, 153);
        add(artistimage);

        artisttitle = new JLabel("");
        artisttitle.setHorizontalAlignment(SwingConstants.CENTER);
        artisttitle.setBounds(0, 213, 780, 64);
        add(artisttitle);

        artisttitle.setForeground(PublicValues.globalFontColor);

        artistbackgroundimage = new JImagePanel();
        artistbackgroundimage.setBounds(0, 0, 780, 277);
        add(artistbackgroundimage);

        artistalbumcontextmenu = new ContextMenu(artistalbumalbumtable);
        artistalbumcontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(albumuricache.get(artistalbumalbumtable.getSelectedRow())));

        artistalbumalbumtable.setForeground(PublicValues.globalFontColor);
        artistalbumalbumtable.getTableHeader().setForeground(PublicValues.globalFontColor);

        artistalbumalbumtable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    ContentPanel.isLastArtist = true;
                    ArtistPanel.contentPanel.setVisible(false);
                    Search.searchplaylistpanel.setVisible(true);
                    Search.searchplaylistsongscache.clear();
                    ((DefaultTableModel) Search.searchplaylisttable.getModel()).setRowCount(0);
                    try {
                        Album album = InstanceManager.getSpotifyApi().getAlbum(albumuricache.get(artistalbumalbumtable.getSelectedRow()).split(":")[2]).build().execute();
                        for (TrackSimplified simplified : album.getTracks().getItems()) {
                            artistalbumalbumtable.addModifyAction(() -> {
                                ((DefaultTableModel) Search.searchplaylisttable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                                Search.searchplaylistsongscache.add(simplified.getUri());
                            });
                        }
                    } catch (IOException | ParseException | SpotifyWebApiException ex) {
                        GraphicalMessage.openException(ex);
                        ConsoleLogging.Throwable(ex);
                    }
                }
            }
        }));

        artistpopularsonglist.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")
                }
        ));
        artistpopularsonglist.setForeground(PublicValues.globalFontColor);
        artistpopularsonglist.getTableHeader().setForeground(PublicValues.globalFontColor);

        artistalbumalbumtable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        PublicValues.language.translate("ui.search.songlist.songname")
                }
        ));

        artistpopularsonglist.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(popularuricache.get(artistpopularsonglist.getSelectedRow()), true, false, false);
                    TrackUtils.addAllToQueue(popularuricache, artistpopularsonglist);
                }
            }
        }));
    }

    public static ArrayList<String> albumuricache = new ArrayList<>();
    public static ArrayList<String> popularuricache = new ArrayList<>();

    public static ArrayList<Runnable> runWhenOpeningArtistPanel = new ArrayList<>();

    public void openPanel() {
        ContentPanel.artistPanelVisible = true;
        for(Runnable runnable : runWhenOpeningArtistPanel) {
            runnable.run();
        }
        ContentPanel.blockTabSwitch();
        javax.swing.SwingUtilities.invokeLater(() -> contentPanel.getVerticalScrollBar().setValue(0));
    }
}
