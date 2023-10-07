package com.spotifyxp.lastfm;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.de.umass.lastfm.*;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.utils.GraphicalMessage;

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
    DefTable chartstracks;
    ArrayList<String> scrobblesuricache = new ArrayList<>();
    ArrayList<String> userchartsartistsuricache = new ArrayList<>();
    ArrayList<String> userchartsalbumsuricache = new ArrayList<>();
    ArrayList<String> userchartstracksuricache = new ArrayList<>();
    ArrayList<String> chartsartistsuricache = new ArrayList<>();
    ArrayList<String> chartstracksuricache = new ArrayList<>();
    JScrollPane scrobblestablescroll;
    JScrollPane userchartsartistsscroll;
    JScrollPane userchartsalbumsscroll;
    JScrollPane userchartstracksscroll;
    JScrollPane chartsartistsscroll;
    JScrollPane chartstracksscroll;
    int scrobblescurrent = 0;
    int userchartsartistscurrent = 0;
    int userchartsalbumscurrent = 0;
    int userchartstrackscurrent = 0;
    int chartsartistscurrent = 0;
    int chartstrackscurrent = 0;

    public LastFMDialog() {
        setPreferredSize(new Dimension(800, 600));
        setTitle("Last.fm - Dashboard v" + LFMValues.version + " " + LFMValues.candidate);
        setLayout(null);
        scrobblespanel = new JPanel();
        userchartspanel = new JPanel();
        chartspanel = new JPanel();
        userchartspanel.setPreferredSize(new Dimension(800, 1690));
        chartspanel.setPreferredSize(new Dimension(800, 1140));
        scrobbles = new JScrollPane(scrobblespanel);
        usercharts = new JScrollPane(userchartspanel);
        charts = new JScrollPane(chartspanel);
        usercharts.setPreferredSize(new Dimension(800, 1690));
        charts.setPreferredSize(new Dimension(800, 1140));
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
                    if(scrobblestable.getModel().getValueAt(scrobblestable.getSelectedRow(), 2).toString().equalsIgnoreCase("load more")) {
                        DefThread thread = new DefThread(new Runnable() {
                            @Override
                            public void run() {
                                scrobblescurrent++;
                                ((DefaultTableModel) scrobblestable.getModel()).setRowCount(scrobblestable.getRowCount() - 1);
                                for(Track t : User.getRecentTracks(LFMValues.username, scrobblescurrent, LFMValues.tracklimit, LFMValues.apikey)) {
                                    ((DefaultTableModel) scrobblestable.getModel()).addRow(new Object[] {t.getName(), t.getArtist(), formatDate(t.getPlayedWhen())});
                                    scrobblesuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                                }
                                if(User.getRecentTracks(LFMValues.username, scrobblescurrent, LFMValues.tracklimit, LFMValues.apikey).getTotalPages() != scrobblescurrent) {
                                    ((DefaultTableModel) scrobblestable.getModel()).addRow(new Object[] {"Load More", "Load More", "Load More"});
                                }
                            }
                        });
                        return;
                    }
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
                    if(userchartsartists.getModel().getValueAt(userchartsartists.getSelectedRow(), 1).toString().equalsIgnoreCase("load more")) {
                        DefThread thread = new DefThread(new Runnable() {
                            @Override
                            public void run() {
                                userchartsartistscurrent++;
                                ((DefaultTableModel) userchartsartists.getModel()).setRowCount(userchartsartists.getRowCount() - 1);
                                for(Artist a : User.getTopArtists(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartsartistscurrent, LFMValues.apikey)) {
                                    ((DefaultTableModel) userchartsartists.getModel()).addRow(new Object[]{a.getName(), a.getPlaycount()});
                                    userchartsartistsuricache.add(LastFMConverter.getArtistURIfromName(a.getName()));
                                }
                                if(User.getTopArtists(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartsartistscurrent, LFMValues.apikey).getTotalPages() != userchartsartistscurrent) {
                                    ((DefaultTableModel) userchartsartists.getModel()).addRow(new Object[]{"Load More", "Load More"});
                                }
                            }
                        });
                        thread.start();
                        return;
                    }
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
                if(userchartsalbums.getModel().getValueAt(userchartsalbums.getSelectedRow(), 2).toString().equalsIgnoreCase("load more")) {
                    DefThread thread = new DefThread(new Runnable() {
                        @Override
                        public void run() {
                            userchartsalbumscurrent++;
                            ((DefaultTableModel) userchartsalbums.getModel()).setRowCount(userchartsalbums.getRowCount() - 1);
                            for(Album a : User.getTopAlbums(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartsalbumscurrent, LFMValues.apikey)) {
                                ((DefaultTableModel) userchartsalbums.getModel()).addRow(new Object[]{a.getName(), a.getArtist(), a.getPlaycount()});
                                userchartsalbumsuricache.add(LastFMConverter.getAlbumURIfromName(a.getName()));
                            }
                            if(User.getTopAlbums(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartsalbumscurrent, LFMValues.apikey).getTotalPages() != userchartsalbumscurrent) {
                                ((DefaultTableModel) userchartsalbums.getModel()).addRow(new Object[]{"Load More", "Load More", "Load More"});
                            }
                        }
                    });
                    thread.start();
                    return;
                }
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
                    if(userchartstracks.getModel().getValueAt(userchartstracks.getSelectedRow(), 2).toString().equalsIgnoreCase("load more")) {
                        DefThread thread = new DefThread(new Runnable() {
                            @Override
                            public void run() {
                                userchartstrackscurrent++;
                                ((DefaultTableModel) userchartstracks.getModel()).setRowCount(userchartstracks.getRowCount() - 1);
                                for(Track t : User.getTopTracks(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartstrackscurrent, LFMValues.apikey)) {
                                    ((DefaultTableModel) userchartstracks.getModel()).addRow(new Object[]{t.getName(), t.getArtist(), t.getPlaycount()});
                                    userchartstracksuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                                }
                                if(User.getTopTracks(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartstrackscurrent, LFMValues.apikey).getTotalPages() != userchartstrackscurrent) {
                                    ((DefaultTableModel) userchartstracks.getModel()).addRow(new Object[]{"Load More", "Load More", "Load More"});
                                }
                            }
                        });
                        thread.start();
                        return;
                    }
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
                    if(chartsartists.getModel().getValueAt(chartsartists.getSelectedRow(), 1).toString().equalsIgnoreCase("load more")) {
                        DefThread thread = new DefThread(new Runnable() {
                            @Override
                            public void run() {
                                chartsartistscurrent++;
                                ((DefaultTableModel) chartsartists.getModel()).setRowCount(chartsartists.getRowCount() - 1);
                                for(Artist a : Chart.getTopArtists(LFMValues.tracklimit, chartsartistscurrent, LFMValues.apikey)) {
                                    ((DefaultTableModel) chartsartists.getModel()).addRow(new Object[]{a.getName(), a.getPlaycount()});
                                    chartsartistsuricache.add(LastFMConverter.getArtistURIfromName(a.getName()));
                                }
                                if(Chart.getTopArtists(LFMValues.tracklimit, chartsartistscurrent, LFMValues.apikey).getTotalPages() != chartsartistscurrent) {
                                    ((DefaultTableModel) chartsartists.getModel()).addRow(new Object[]{"Load More", "Load More"});
                                }
                            }
                        });
                        thread.start();
                        return;
                    }
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
        JLabel chartstrackslabel = new JLabel(PublicValues.language.translate("ui.lastfm.chartstracks"));
        chartstrackslabel.setBounds(6, 570, 788, 23);
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
                    if(chartstracks.getModel().getValueAt(chartstracks.getSelectedRow(), 2).toString().equalsIgnoreCase("load more")) {
                        DefThread thread = new DefThread(new Runnable() {
                            @Override
                            public void run() {
                                chartstrackscurrent++;
                                ((DefaultTableModel) chartstracks.getModel()).setRowCount(chartstracks.getRowCount() - 1);
                                for(Track t : Chart.getTopTracks(LFMValues.tracklimit, chartstrackscurrent, LFMValues.apikey)) {
                                    ((DefaultTableModel) chartstracks.getModel()).addRow(new Object[]{t.getName(), t.getArtist(), t.getPlaycount()});
                                    chartstracksuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                                }
                                if(Chart.getTopTracks(LFMValues.tracklimit, chartstrackscurrent, LFMValues.apikey).getTotalPages() != chartstrackscurrent) {
                                    ((DefaultTableModel) chartstracks.getModel()).addRow(new Object[]{"Load More", "Load More", "Load More"});
                                }
                            }
                        });
                        thread.start();
                        return;
                    }
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
        chartstracksscroll.setBounds(6, 600, 778, 503);
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
                scrobblescurrent++;
                for(Track t : User.getRecentTracks(LFMValues.username, scrobblescurrent, LFMValues.tracklimit, LFMValues.apikey)) {
                    try {
                        ((DefaultTableModel) scrobblestable.getModel()).addRow(new Object[]{t.getName(), t.getArtist(), formatDate(t.getPlayedWhen())});
                        scrobblesuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                    }catch (NullPointerException e) {
                        GraphicalMessage.bug(User.getRawRecentTracks(LFMValues.username, scrobblescurrent, LFMValues.tracklimit, LFMValues.apikey));
                    }
                }
                if(User.getRecentTracks(LFMValues.username, scrobblescurrent, LFMValues.tracklimit, LFMValues.apikey).getTotalPages() != scrobblescurrent) {
                    ((DefaultTableModel) scrobblestable.getModel()).addRow(new Object[] {"Load More", "Load More", "Load More"});
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
                userchartsartistscurrent++;
                for(Artist a : User.getTopArtists(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartsartistscurrent, LFMValues.apikey)) {
                    ((DefaultTableModel) userchartsartists.getModel()).addRow(new Object[]{a.getName(), a.getPlaycount()});
                    userchartsartistsuricache.add(LastFMConverter.getArtistURIfromName(a.getName()));
                }
                if(User.getTopArtists(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartsartistscurrent, LFMValues.apikey).getTotalPages() != userchartsartistscurrent) {
                    ((DefaultTableModel) userchartsartists.getModel()).addRow(new Object[]{"Load More", "Load More"});
                }
            }
        });
        artistthread.start();

        DefThread albumthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                userchartsalbumscurrent++;
                for(Album a : User.getTopAlbums(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartsalbumscurrent, LFMValues.apikey)) {
                    ((DefaultTableModel) userchartsalbums.getModel()).addRow(new Object[]{a.getName(), a.getArtist(), a.getPlaycount()});
                    userchartsalbumsuricache.add(LastFMConverter.getAlbumURIfromName(a.getName()));
                }
                if(User.getTopAlbums(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartsalbumscurrent, LFMValues.apikey).getTotalPages() != userchartsalbumscurrent) {
                    ((DefaultTableModel) userchartsalbums.getModel()).addRow(new Object[]{"Load More", "Load More", "Load More"});
                }
            }
        });
        albumthread.start();

        DefThread trackthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                userchartstrackscurrent++;
                for(Track t : User.getTopTracks(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartstrackscurrent, LFMValues.apikey)) {
                    ((DefaultTableModel) userchartstracks.getModel()).addRow(new Object[]{t.getName(), t.getArtist(), t.getPlaycount()});
                    userchartstracksuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                }
                if(User.getTopTracks(LFMValues.username, Period.OVERALL, LFMValues.tracklimit, userchartstrackscurrent, LFMValues.apikey).getTotalPages() != userchartstrackscurrent) {
                    ((DefaultTableModel) userchartstracks.getModel()).addRow(new Object[]{"Load More", "Load More", "Load More"});
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
                chartsartistscurrent++;
                for(Artist a : Chart.getTopArtists(LFMValues.tracklimit, chartsartistscurrent, LFMValues.apikey)) {
                    ((DefaultTableModel) chartsartists.getModel()).addRow(new Object[]{a.getName(), a.getPlaycount()});
                    chartsartistsuricache.add(LastFMConverter.getArtistURIfromName(a.getName()));
                }
                if(Chart.getTopArtists(LFMValues.tracklimit, chartsartistscurrent, LFMValues.apikey).getTotalPages() != chartsartistscurrent) {
                    ((DefaultTableModel) chartsartists.getModel()).addRow(new Object[]{"Load More", "Load More"});
                }
            }
        });
        artistthread.start();

        DefThread trackthread = new DefThread(new Runnable() {
            @Override
            public void run() {
                chartstrackscurrent++;
                for(Track t : Chart.getTopTracks(LFMValues.tracklimit, chartstrackscurrent, LFMValues.apikey)) {
                    ((DefaultTableModel) chartstracks.getModel()).addRow(new Object[]{t.getName(), t.getArtist(), t.getPlaycount()});
                    chartstracksuricache.add(LastFMConverter.getTrackURIfromName(t.getName()));
                }
                if(Chart.getTopTracks(LFMValues.tracklimit, chartstrackscurrent, LFMValues.apikey).getTotalPages() != chartstrackscurrent) {
                    ((DefaultTableModel) chartstracks.getModel()).addRow(new Object[]{"Load More", "Load More", "Load More"});
                }
            }
        });
        trackthread.start();
    }
}
