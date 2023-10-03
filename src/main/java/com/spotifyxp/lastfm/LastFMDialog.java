package com.spotifyxp.lastfm;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.de.umass.lastfm.*;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.threading.DefThread;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;

public class LastFMDialog extends JFrame2 {
    JPanel scrobblespanel;
    JPanel userchartspanel;
    JPanel chartspanel;
    JScrollPane scrobbles;
    JScrollPane usercharts;
    JScrollPane charts;
    JTabbedPane tabs = new JTabbedPane(SwingConstants.TOP);
    int tabHeight = 27;
    DefTable scrobblestable;
    DefTable userchartsartists;
    DefTable userchartsalbums;
    DefTable userchartstracks;
    DefTable chartsartists;
    DefTable chartsalbums;
    DefTable chartstracks;
    ArrayList<String> scrobblesuricache = new ArrayList<>();
    ArrayList<String> userchartsartistsuricache = new ArrayList<>();
    ArrayList<String> userchartsalbumsuricache = new ArrayList<>();
    ArrayList<String> userchartstracksuricache = new ArrayList<>();
    ArrayList<String> chartsartistsuricache = new ArrayList<>();
    ArrayList<String> chartsalbumsuricache = new ArrayList<>();
    ArrayList<String> chartstracksuricache = new ArrayList<>();
    JScrollPane scrobblestablescroll;
    JScrollPane userchartsartistsscroll;
    JScrollPane userchartsalbumsscroll;
    JScrollPane userchartstracksscroll;
    JScrollPane chartsartistsscroll;
    JScrollPane chartsalbumsscroll;
    JScrollPane chartstracksscroll;

    public LastFMDialog() {
        setPreferredSize(new Dimension(800, 600));
        setTitle("Last.fm - Dashboard v" + LFMValues.version + " " + LFMValues.candidate);
        setLayout(null);
        scrobblespanel = new JPanel();
        userchartspanel = new JPanel();
        chartspanel = new JPanel();
        userchartspanel.setPreferredSize(new Dimension(800, 1690));
        chartspanel.setPreferredSize(new Dimension(800, 1690));
        scrobbles = new JScrollPane(scrobblespanel);
        usercharts = new JScrollPane(userchartspanel);
        charts = new JScrollPane(chartspanel);
        usercharts.setPreferredSize(new Dimension(800, 1690));
        charts.setPreferredSize(new Dimension(800, 1690));
        scrobblespanel.setLayout(null);
        userchartspanel.setLayout(null);
        chartspanel.setLayout(null);
        tabs.setForeground(PublicValues.globalFontColor);
        tabs.addTab(PublicValues.language.translate("ui.lastfm.scrobbles"), scrobbles);
        tabs.addTab(PublicValues.language.translate("ui.lastfm.usercharts"), usercharts);
        tabs.addTab(PublicValues.language.translate("ui.lastfm.charts"), charts);
        tabs.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return 800 / tabs.getTabCount();
            }
        });
        add(tabs);
        tabs.setBounds(0, 0, 800, 600);
        JLabel scrobbleslabel = new JLabel(PublicValues.language.translate("ui.lastfm.scrobbles"));
        scrobbleslabel.setBounds(6, 6, 788, 23);
        scrobbleslabel.setForeground(PublicValues.globalFontColor);
        scrobblespanel.add(scrobbleslabel);
        scrobblestable = new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrobblestable.setForeground(PublicValues.globalFontColor);
        scrobblestable.getTableHeader().setForeground(PublicValues.globalFontColor);
        scrobblestable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    PublicValues.spotifyplayer.load(scrobblesuricache.get(scrobblestable.getSelectedRow()), true, false, false);
                }
            }
        });
        scrobblestablescroll = new JScrollPane(scrobblestable);
        scrobblestable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.lastfm.scrobbles.name"), PublicValues.language.translate("ui.lastfm.scrobbles.artist"), PublicValues.language.translate("ui.lastfm.scrobbles.at")
                }
        ));
        scrobblestablescroll.setBounds(6, 32, 778, 503);
        scrobblespanel.add(scrobblestablescroll);

        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(tabs.getSelectedIndex() == 0) {
                    parseScrobbles();
                }
                if(tabs.getSelectedIndex() == 1) {
                    parseUserCharts();
                }
                if(tabs.getSelectedIndex() == 2) {
                    parseCharts();
                }
            }
        });
        JLabel userchartsartistslabel = new JLabel(PublicValues.language.translate("ui.lastfm.userchartsartists"));
        userchartsartistslabel.setBounds(6, 6, 788, 23);
        userchartsartistslabel.setForeground(PublicValues.globalFontColor);
        userchartspanel.add(userchartsartistslabel);
        userchartsartists = new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userchartsartists.setForeground(PublicValues.globalFontColor);
        userchartsartists.getTableHeader().setForeground(PublicValues.globalFontColor);
        userchartsartists.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    ContentPanel.showArtistPanel(userchartsartistsuricache.get(userchartsartists.getSelectedRow()));
                }
            }
        });
        userchartsartists.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.lastfm.charts.artist"), PublicValues.language.translate("ui.lastfm.multiuse.scrobbles")
                }
        ));
        userchartsartistsscroll = new JScrollPane(userchartsartists);
        userchartsartistsscroll.setBounds(6, 32, 778, 503);
        userchartspanel.add(userchartsartistsscroll);
        JLabel userchartsalbumlabel = new JLabel(PublicValues.language.translate("ui.lastfm.userchartsalbums"));
        userchartsalbumlabel.setBounds(6, 570, 788, 23);
        userchartsalbumlabel.setForeground(PublicValues.globalFontColor);
        userchartspanel.add(userchartsalbumlabel);
        userchartsalbums = new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userchartsalbums.setForeground(PublicValues.globalFontColor);
        userchartsalbums.getTableHeader().setForeground(PublicValues.globalFontColor);
        userchartsalbums.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.lastfm.charts.album"), PublicValues.language.translate("ui.lastfm.charts.artist"), PublicValues.language.translate("ui.lastfm.multiuse.scrobbles")
                }
        ));
        userchartsalbums.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    ContentPanel.showAdvancedSongPanel(userchartsalbumsuricache.get(userchartsalbums.getSelectedRow()), HomePanel.ContentTypes.album);
                }
            }
        });
        userchartsalbumsscroll = new JScrollPane(userchartsalbums);
        userchartsalbumsscroll.setBounds(6, 600, 778, 503);
        userchartspanel.add(userchartsalbumsscroll);
        JLabel userchartstrackslabel = new JLabel(PublicValues.language.translate("ui.lastfm.userchartstracks"));
        userchartstrackslabel.setBounds(6, 1138, 788, 23);
        userchartstrackslabel.setForeground(PublicValues.globalFontColor);
        userchartspanel.add(userchartstrackslabel);
        userchartstracks = new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userchartstracks.setForeground(PublicValues.globalFontColor);
        userchartstracks.getTableHeader().setForeground(PublicValues.globalFontColor);
        userchartstracks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    PublicValues.spotifyplayer.load(userchartstracksuricache.get(userchartstracks.getSelectedRow()), true, false, false);
                }
            }
        });
        userchartstracks.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.lastfm.charts.track"), PublicValues.language.translate("ui.lastfm.charts.artist"), PublicValues.language.translate("ui.lastfm.multiuse.scrobbles")
                }
        ));
        userchartstracksscroll = new JScrollPane(userchartstracks);
        userchartstracksscroll.setBounds(6, 1164, 778, 503);
        userchartspanel.add(userchartstracksscroll);

        JLabel chartsartistslabel = new JLabel(PublicValues.language.translate("ui.lastfm.chartsartists"));
        chartsartistslabel.setBounds(6, 6, 788, 23);
        chartsartistslabel.setForeground(PublicValues.globalFontColor);
        chartspanel.add(chartsartistslabel);
        chartsartists = new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        chartsartists.setForeground(PublicValues.globalFontColor);
        chartsartists.getTableHeader().setForeground(PublicValues.globalFontColor);
        chartsartists.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    ContentPanel.showArtistPanel(chartsartistsuricache.get(chartsartists.getSelectedRow()));
                }
            }
        });
        chartsartists.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.lastfm.charts.artist"), PublicValues.language.translate("ui.lastfm.multiuse.scrobbles")
                }
        ));
        chartsartistsscroll = new JScrollPane(chartsartists);
        chartsartistsscroll.setBounds(6, 32, 778, 503);
        chartspanel.add(chartsartistsscroll);
        JLabel chartsalbumlabel = new JLabel(PublicValues.language.translate("ui.lastfm.chartsalbums"));
        chartsalbumlabel.setBounds(6, 570, 788, 23);
        chartsalbumlabel.setForeground(PublicValues.globalFontColor);
        chartspanel.add(chartsalbumlabel);
        chartsalbums = new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        chartsalbums.setForeground(PublicValues.globalFontColor);
        chartsalbums.getTableHeader().setForeground(PublicValues.globalFontColor);
        chartsalbums.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    ContentPanel.showAdvancedSongPanel(chartsalbumsuricache.get(chartsalbums.getSelectedRow()), HomePanel.ContentTypes.album);
                }
            }
        });
        chartsalbums.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.lastfm.charts.track"), PublicValues.language.translate("ui.lastfm.charts.artist"), PublicValues.language.translate("ui.lastfm.multiuse.scrobbles")
                }
        ));
        chartsalbumsscroll = new JScrollPane(chartsalbums);
        chartsalbumsscroll.setBounds(6, 600, 778, 503);
        chartspanel.add(chartsalbumsscroll);
        JLabel chartstrackslabel = new JLabel(PublicValues.language.translate("ui.lastfm.chartstracks"));
        chartstrackslabel.setBounds(6, 1138, 788, 23);
        chartstrackslabel.setForeground(PublicValues.globalFontColor);
        chartspanel.add(chartstrackslabel);
        chartstracks = new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        chartstracks.setForeground(PublicValues.globalFontColor);
        chartstracks.getTableHeader().setForeground(PublicValues.globalFontColor);
        chartstracks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    PublicValues.spotifyplayer.load(chartstracksuricache.get(chartstracks.getSelectedRow()), true, false, false);
                }
            }
        });
        chartstracks.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.lastfm.charts.track"), PublicValues.language.translate("ui.lastfm.charts.artist"), PublicValues.language.translate("ui.lastfm.multiuse.scrobbles")
                }
        ));
        chartstracksscroll = new JScrollPane(chartstracks);
        chartstracksscroll.setBounds(6, 1164, 778, 503);
        chartspanel.add(chartstracksscroll);
        JMenuBar bar = new JMenuBar();
        JMenu account = new JMenu(PublicValues.language.translate("ui.lastfm.account"));
        JMenuItem open = new JMenuItem(PublicValues.language.translate("ui.lastfm.account.open"));
        account.add(open);
        bar.add(account);
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LastFMUserDialog dialog = new LastFMUserDialog();
                dialog.open();
            }
        });
        setJMenuBar(bar);
        parseScrobbles();
    }

    public static String formatDate(Date d) {
        return d.getDay() + " " + Month.of(d.getMonth()).getDisplayName(TextStyle.FULL, PublicValues.language.getLocale()) + " " + d.getHours() + ":" + d.getMinutes();
    }

    void parseScrobbles() {
        if(scrobblestable.getModel().getRowCount() != 0) {
            return;
        }
        DefThread t = new DefThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <= User.getRecentTracks(LFMValues.username, LFMValues.apikey).getTotalPages(); i++) {
                    for(Track t : User.getRecentTracks(LFMValues.username, i, LFMValues.tracklimit, LFMValues.apikey)) {
                        ((DefaultTableModel) scrobblestable.getModel()).addRow(new Object[] {t.getName(), t.getArtist(), formatDate(t.getPlayedWhen())});
                        scrobblesuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                    }
                }
            }
        });
        t.start();
    }

    void parseUserCharts() {
        if(userchartsartists.getModel().getRowCount() != 0) {
            return;
        }
        DefThread artistthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <= User.getTopArtists(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, 1, LFMValues.apikey).getTotalPages(); i++) {
                    for(Artist a : User.getTopArtists(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, i, LFMValues.apikey)) {
                        ((DefaultTableModel) userchartsartists.getModel()).addRow(new Object[]{a.getName(), a.getPlaycount()});
                        userchartsartistsuricache.add(LastFMConverter.getArtistURIfromName(a.getName()));
                    }
                }
            }
        });
        artistthread.start();

        DefThread albumthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <= User.getTopAlbums(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, 1, LFMValues.apikey).getTotalPages(); i++) {
                    for(Album a : User.getTopAlbums(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, i, LFMValues.apikey)) {
                        ((DefaultTableModel) userchartsalbums.getModel()).addRow(new Object[]{a.getName(), a.getArtist(), a.getPlaycount()});
                        userchartsalbumsuricache.add(LastFMConverter.getAlbumURIfromName(a.getName()));
                    }
                }
            }
        });
        albumthread.start();

        DefThread trackthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <= User.getTopTracks(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, 1, LFMValues.apikey).getTotalPages(); i++) {
                    for(Track t : User.getTopTracks(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, i, LFMValues.apikey)) {
                        ((DefaultTableModel) userchartstracks.getModel()).addRow(new Object[]{t.getName(), t.getArtist(), t.getPlaycount()});
                        userchartstracksuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                    }
                }
            }
        });
        trackthread.start();
    }

    void parseCharts() {
        if(chartsartists.getModel().getRowCount() != 0) {
            return;
        }
        DefThread artistthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <= User.getTopArtists(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, 1, LFMValues.apikey).getTotalPages(); i++) {
                    for(Artist a : User.getTopArtists(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, i, LFMValues.apikey)) {
                        ((DefaultTableModel) chartsartists.getModel()).addRow(new Object[]{a.getName(), a.getPlaycount()});
                        chartsartistsuricache.add(LastFMConverter.getArtistURIfromName(a.getName()));
                    }
                }
            }
        });
        artistthread.start();

        DefThread albumthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <= User.getTopAlbums(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, 1, LFMValues.apikey).getTotalPages(); i++) {
                    for(Album a : User.getTopAlbums(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, i, LFMValues.apikey)) {
                        ((DefaultTableModel) chartsalbums.getModel()).addRow(new Object[]{a.getName(), a.getArtist(), a.getPlaycount()});
                        chartsalbumsuricache.add(LastFMConverter.getAlbumURIfromName(a.getName()));
                    }
                }
            }
        });
        albumthread.start();

        DefThread trackthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <= User.getTopTracks(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, 1, LFMValues.apikey).getTotalPages(); i++) {
                    for(Track t : User.getTopTracks(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, i, LFMValues.apikey)) {
                        ((DefaultTableModel) chartstracks.getModel()).addRow(new Object[]{t.getName(), t.getArtist(), t.getPlaycount()});
                        chartstracksuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                    }
                }
            }
        });
        trackthread.start();
    }
}
