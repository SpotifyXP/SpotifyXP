package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Album;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.utils.ClipboardUtil;
import com.spotifyxp.utils.TrackUtils;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ArtistPanel extends JPanel {
    public JTable artistpopularsonglist;
    public JTable artistalbumalbumtable;
    public JScrollPane artistpopularscrollpane;
    public JScrollPane artistalbumscrollpanel;
    public JScrollPane contentPanel;
    public JLabel artisttitle;
    public JImagePanel artistbackgroundimage;
    public JImagePanel artistimage;
    public ArrayList<String> artistpopularuricache = new ArrayList<>();
    public ArrayList<String> artistalbumuricache = new ArrayList<>();
    public ContextMenu artistpopularsonglistcontextmenu;
    public ContextMenu artistalbumcontextmenu;
    public ArtistPanel() {
        contentPanel = new JScrollPane();
        contentPanel.setViewportView(this);
        setLayout(null);
        setPreferredSize(new Dimension(800, 975));

        JLabel artistpopularlabel = new JLabel("Popular");
        artistpopularlabel.setBounds(10, 291, 137, 27);
        add(artistpopularlabel);

        artistpopularlabel.setForeground(PublicValues.globalFontColor);

        artistpopularscrollpane = new JScrollPane();
        artistpopularscrollpane.setBounds(10, 320, 780, 277);
        add(artistpopularscrollpane);

        artistpopularsonglist = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        artistpopularscrollpane.setViewportView(artistpopularsonglist);

        artistpopularsonglistcontextmenu = new ContextMenu(artistpopularsonglist);
        artistpopularsonglistcontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), new Runnable() {
            @Override
            public void run() {
                ClipboardUtil.set(artistpopularuricache.get(artistpopularsonglist.getSelectedRow()));
            }
        });

        artistalbumscrollpanel = new JScrollPane();
        artistalbumscrollpanel.setBounds(10, 667, 780, 295);
        add(artistalbumscrollpanel);

        artistalbumalbumtable = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        artistalbumscrollpanel.setViewportView(artistalbumalbumtable);

        contentPanel.getVerticalScrollBar().setUnitIncrement(20);

        JLabel artistalbumlabel = new JLabel("Albums");
        artistalbumlabel.setBounds(10, 642, 102, 14);
        add(artistalbumlabel);

        artistalbumlabel.setForeground(PublicValues.globalFontColor);

        artistimage = new JImagePanel();
        artistimage.setBounds(288, 11, 155, 153);
        add(artistimage, new Integer(3));

        artisttitle = new JLabel("");
        artisttitle.setHorizontalAlignment(SwingConstants.CENTER);
        artisttitle.setBounds(0, 213, 780, 64);
        add(artisttitle, new Integer(2));

        artisttitle.setForeground(PublicValues.globalFontColor);

        artistbackgroundimage = new JImagePanel();
        artistbackgroundimage.setBounds(0, 0, 780, 277);
        add(artistbackgroundimage, new Integer(1));

        artistalbumcontextmenu = new ContextMenu(artistalbumalbumtable);
        artistalbumcontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), new Runnable() {
            @Override
            public void run() {
                ClipboardUtil.set(artistalbumuricache.get(artistalbumalbumtable.getSelectedRow()));
            }
        });

        artistalbumalbumtable.setForeground(PublicValues.globalFontColor);
        artistalbumalbumtable.getTableHeader().setForeground(PublicValues.globalFontColor);

        artistalbumalbumtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2) {
                    ContentPanel.isLastArtist = true;
                    ContentPanel.artistPanel.contentPanel.setVisible(false);
                    ContentPanel.searchplaylistpanel.setVisible(true);
                    ContentPanel.searchplaylistsongscache.clear();
                    ((DefaultTableModel)ContentPanel.searchplaylisttable.getModel()).setRowCount(0);
                    try {
                        Album album = ContentPanel.api.getSpotifyApi().getAlbum(artistalbumuricache.get(artistalbumalbumtable.getSelectedRow()).split(":")[2]).build().execute();
                        for(TrackSimplified simplified : album.getTracks().getItems()) {
                            ((DefaultTableModel) ContentPanel.searchplaylisttable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                            ContentPanel.searchplaylistsongscache.add(simplified.getUri());
                        }
                    } catch (IOException | ParseException | SpotifyWebApiException ex) {
                        ExceptionDialog.open(ex);
                        ConsoleLogging.Throwable(ex);
                    }
                }
            }
        });

        artistpopularsonglist.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")
                }
        ));
        artistpopularsonglist.setForeground(PublicValues.globalFontColor);
        artistpopularsonglist.getTableHeader().setForeground(PublicValues.globalFontColor);

        artistalbumalbumtable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.search.songlist.songname")
                }
        ));

        artistpopularsonglist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2) {
                    ContentPanel.player.getPlayer().load(artistpopularuricache.get(artistpopularsonglist.getSelectedRow()), true, false);
                    TrackUtils.addAllToQueue(artistpopularuricache, artistpopularsonglist);
                }
            }
        });
    }
}
