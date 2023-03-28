package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.utils.TrackUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

//FixMe: First Artist works the second one not (not reacting to mouse events)

public class ArtistPanel extends JPanel {
    public JTable artistpopularsonglist;
    public JTable artistalbumalbumtable;
    public JScrollPane artistpopularscrollpane;
    public JScrollPane artistalbumscrollpanel;
    public JLayeredPane artistimagelayer;
    public JScrollPane contentPanel = new JScrollPane();
    public JLabel artisttitle;
    public JImagePanel artistbackgroundimage;
    public JImagePanel artistimage;
    public ArrayList<String> artistpopularuricache = new ArrayList<>();
    public ArrayList<String> artistalbumuricache = new ArrayList<>();
    public ArtistPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 975));

        JLabel artistpopularlabel = new JLabel("Popular");
        artistpopularlabel.setBounds(10, 291, 137, 27);
        add(artistpopularlabel);

        artistpopularscrollpane = new JScrollPane();
        artistpopularscrollpane.setBounds(10, 320, 780, 277);
        add(artistpopularscrollpane);

        artistpopularsonglist = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        artistpopularscrollpane.setViewportView(artistpopularsonglist);

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

        artistimagelayer = new JLayeredPane();
        artistimagelayer.setBounds(10, 11, 780, 277);
        add(artistimagelayer);

        artistimage = new JImagePanel();
        artistimage.setBounds(288, 11, 155, 153);
        artistimagelayer.add(artistimage, new Integer(3));

        artisttitle = new JLabel("");
        artisttitle.setHorizontalAlignment(SwingConstants.CENTER);
        artisttitle.setBounds(0, 213, 780, 64);
        artistimagelayer.add(artisttitle, new Integer(2));

        artistbackgroundimage = new JImagePanel();
        artistbackgroundimage.setBounds(0, 0, 780, 277);
        artistimagelayer.add(artistbackgroundimage, new Integer(1));

        contentPanel.setViewportView(this);

        artistpopularsonglist.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")
                }
        ));

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
                System.out.println("Reacting click");
                super.mouseClicked(e);
                if(e.getClickCount()==2) {
                    ContentPanel.player.getPlayer().load(artistpopularuricache.get(artistpopularsonglist.getSelectedRow()), true, false);
                    TrackUtils.addAllToQueue(artistpopularuricache, artistpopularsonglist);
                }
            }
        });
    }

    public JScrollPane getPanel() {
        return contentPanel;
    }
}
