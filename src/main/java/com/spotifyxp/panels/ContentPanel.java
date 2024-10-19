package com.spotifyxp.panels;

import com.google.gson.JsonArray;
import com.neovisionaries.i18n.CountryCode;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.miscellaneous.Device;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.*;
import com.spotifyxp.dev.ErrorSimulator;
import com.spotifyxp.dev.LocationFinder;
import com.spotifyxp.dialogs.HTMLDialog;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.injector.InjectorStore;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.utils.*;
import org.apache.hc.core5.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ContentPanel extends JPanel {
    public static PlayerArea playerarea;
    public static Search searchpanel;
    public static Library librarypanel;
    public static Playlists playlistspanel;
    public static HomePanel homepanel;
    public static HotList hotlistpanel;
    public static Queue queuepanel;
    public static Feedback feedbackpanel;
    public static ArtistPanel artistPanel;
    public static JPanel tabpanel;
    public static boolean searchVisible = false;
    public static boolean playlistsVisible = false;
    public static boolean feedbackVisible = false;
    public static boolean queueVisible = false;
    public static boolean hotlistVisible = false;
    public static boolean libraryVisble = false;
    public static boolean artistPanelVisible = false;
    public static boolean homeVisible = false;
    public static final JTabbedPane legacyswitch = new JTabbedPane();
    public static final JMenuBar bar = new JMenuBar();
    public static boolean isLastArtist = false;
    public static DefTable advancedsongtable;
    public static JButton artistPanelBackButton;
    public static JPanel advancedsongpanel;
    public static JScrollPane advancedscrollpanel;
    public static JButton advancedbackbutton;
    public static JButton errorDisplay;
    public static ArrayList<ExceptionDialog> errorQueue;
    public static final ArrayList<String> advanceduricache = new ArrayList<>();
    public static boolean pressedCTRL = false;
    public static final JFrame frame = new JFrame("SpotifyXP - v" + ApplicationUtils.getVersion() + " " + ApplicationUtils.getReleaseCandidate());
    public static Views currentView = Views.HOME; //The view on start is home
    public static String advancedSongPanelUri;
    static boolean steamdeck = false;
    static LastTypes lastmenu = LastTypes.HotList;
    static boolean advancedSongPanelVisible = false;
    static boolean errorDisplayVisible = false;

    public enum Views {
        HOME,
        PLAYLISTS,
        LIBRARY,
        SEARCH,
        HOTLIST,
        QUEUE,
        FEEDBACK,
        PLAYLIST,
        SHOW,
        ALBUM,
        ARTIST,
        OTHER
    }

    static class TabEntry {
        public String title;
        public JComponent component;
        public int count;

        public TabEntry(String title, JComponent component) {
            this.title = title;
            this.component = component;
            this.count = extraTabs.size() + 1;
        }
    }

    private static ArrayList<TabEntry> extraTabs = new ArrayList<>();

    public static void addComponentToTabs(String title, JComponent component) {
        extraTabs.add(new TabEntry(title, component));
        legacyswitch.addTab(title, new JPanel());
        tabpanel.add(component);
    }

    @SuppressWarnings("Busy")
    public ContentPanel() {
        ConsoleLogging.info(PublicValues.language.translate("debug.buildcontentpanelbegin"));
        SplashPanel.linfo.setText("Setting window size...");
        setPreferredSize(new Dimension(783, 600));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);
        SplashPanel.linfo.setText("Creating errorDisplay...");
        createErrorDisplay();
        Events.subscribe(SpotifyXPEvents.trackLoadFinished.getName(), (Object... data) -> PublicValues.blockLoading = false);
        SplashPanel.linfo.setText("Creating tabpanel...");
        tabpanel = new JPanel();
        tabpanel.setLayout(null);
        tabpanel.setBounds(0, 140, 784, 450);
        SplashPanel.linfo.setText("Creating playerarea...");
        createPlayerArea();
        SplashPanel.linfo.setText("Creating feedback...");
        createFeedback();
        SplashPanel.linfo.setText("Creating library...");
        createLibrary();
        SplashPanel.linfo.setText("Creating hotlist...");
        createHotList();
        SplashPanel.linfo.setText("Creating playlist...");
        createPlaylist();
        SplashPanel.linfo.setText("Creating queue...");
        createQueue();
        SplashPanel.linfo.setText("Creating search...");
        createSearch();
        SplashPanel.linfo.setText("Creating artistPanel...");
        createArtistPanel();
        SplashPanel.linfo.setText("Creating home...");
        createHome();
        SplashPanel.linfo.setText("Creating advancedPanel...");
        createAdvancedPanel();
        SplashPanel.linfo.setText("Adding window mouse listener...");
        addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (pressedCTRL) {
                    PublicValues.injector.openInjectWindow("");
                }
            }
        }));
        SplashPanel.linfo.setText("Deciding population of hotlist...");
        if (PublicValues.autoLoadHotList) {
            Thread t = new Thread(this::setHotlistVisible, "Make HotList visible");
            t.start();
        }
        SplashPanel.linfo.setText("Making window interactive...");
        createLegacy();
        try {
            if (!(InstanceManager.getSpotifyApi().getCurrentUsersProfile() == null)) {
                PublicValues.countryCode = InstanceManager.getSpotifyApi().getCurrentUsersProfile().build().execute().getCountry();
            }
        } catch (IOException | ParseException | SpotifyWebApiException | NullPointerException e) {
            ConsoleLogging.Throwable(e);
            // Defaulting to German
            PublicValues.countryCode = CountryCode.DE;
        }
        artistPanelBackButton.setVisible(false);
        SplashPanel.linfo.setText("Init Theme...");
        updateTheme();
        SplashPanel.linfo.setText("Done building contentPanel");
        ConsoleLogging.info(PublicValues.language.translate("debug.buildcontentpanelend"));
    }

    @Override
    public void paint(java.awt.Graphics g) {
        super.paint(g);
        if(getPaintOverwrite() != null) {
            getPaintOverwrite().run(g);
        }
    }

    public static void blockTabSwitch() {
        legacyswitch.setEnabled(false);
    }

    public static void enableTabSwitch() {
        legacyswitch.setEnabled(true);
    }

    public static void steamDeck() {
        steamdeck = true;
    }

    public static void showArtistPanel(String fromuri) {
        currentView = Views.ARTIST;
        if (advancedSongPanelVisible) {
            homepanel.getComponent().setVisible(true);
            advancedSongPanelVisible = false;
            advancedsongpanel.setVisible(false);
        }
        switch (lastmenu) {
            case Home:
                artistPanel.popularuricache.clear();
                artistPanel.albumuricache.clear();
                ((DefaultTableModel) artistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                ((DefaultTableModel) artistPanel.artistpopularsonglist.getModel()).setRowCount(0);
                artistPanel.artisttitle.setText("");
                ContentPanel.artistPanel.openPanel();
                ArtistPanel.isFirst = true;
                artistPanel.contentPanel.setVisible(true);
                artistPanelBackButton.setVisible(true);
                artistPanelVisible = true;
                homepanel.getComponent().setVisible(false);
                ContentPanel.blockTabSwitch();
                break;
            case Search:
                artistPanel.popularuricache.clear();
                artistPanel.albumuricache.clear();
                ((DefaultTableModel) artistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                ((DefaultTableModel) artistPanel.artistpopularsonglist.getModel()).setRowCount(0);
                artistPanel.artisttitle.setText("");
                ContentPanel.artistPanel.openPanel();
                ArtistPanel.isFirst = true;
                artistPanel.contentPanel.setVisible(true);
                artistPanelBackButton.setVisible(true);
                artistPanelVisible = true;
                searchpanel.setVisible(false);
                ContentPanel.blockTabSwitch();
                break;
        }
        try {
            Artist a = InstanceManager.getSpotifyApi().getArtist(fromuri.split(":")[2]).build().execute();
            try {
                artistPanel.artistimage.setImage(new URL(SpotifyUtils.getImageForSystem(a.getImages()).getUrl()).openStream());
            } catch (ArrayIndexOutOfBoundsException exception) {
                // No artist image (when this is raised it's a bug)
            }
            artistPanel.artisttitle.setText(a.getName());
            Thread trackthread = new Thread(() -> {
                try {
                    for (Track t : InstanceManager.getSpotifyApi().getArtistsTopTracks(a.getUri().split(":")[2], PublicValues.countryCode).build().execute()) {
                        if (!artistPanel.isVisible()) {
                            break;
                        }
                        artistPanel.popularuricache.add(t.getUri());
                        InstanceManager.getSpotifyAPI().addSongToList(TrackUtils.getArtists(t.getArtists()), t, artistPanel.artistpopularsonglist);
                    }
                } catch (IOException | ParseException | SpotifyWebApiException ex) {
                    ConsoleLogging.Throwable(ex);
                }
            }, "Get tracks");
            Thread albumthread = new Thread(() -> {
                for(AlbumSimplified album : SpotifyUtils.getAllAlbumsArtist(a.getUri())) {
                    artistPanel.albumuricache.add(album.getUri());
                    ((DefaultTableModel) artistPanel.artistalbumalbumtable.getModel()).addRow(new Object[]{album.getName()});
                }
            }, "Get albums for artist");
            albumthread.start();
            trackthread.start();
            artistPanel.openPanel();
        } catch (IOException | ParseException | SpotifyWebApiException ex) {
            ConsoleLogging.Throwable(ex);
        }
    }

    static void preventBugLegacySwitch() {
        for (int i = 0; i < legacyswitch.getTabCount(); i++) {
            legacyswitch.setComponentAt(i, new JPanel());
        }
    }

    public static void updateTheme() {
        PlayerArea.playerareavolumeicon.setImage(Graphics.VOLUMEFULL.getPath());
        playerarea.setBorder(new LineBorder(PublicValues.borderColor));
        PlayerArea.playerplaypreviousbutton.setImage(Graphics.PLAYERPLAYPREVIOUS.getPath());
        PlayerArea.playerplaypausebutton.setImage(Graphics.PLAYERPlAY.getPath());
        PlayerArea.playerplaynextbutton.setImage(Graphics.PLAYERPLAYNEXT.getPath());
        PlayerArea.playerareashufflebutton.setImage(Graphics.SHUFFLE.getPath());
        PlayerArea.playerarearepeatingbutton.setImage(Graphics.REPEAT.getPath());
        PlayerArea.playerarealyricsbutton.setImage(Graphics.MICROPHONE.getPath());
        PlayerArea.playerplaypausebutton.setBorderPainted(false);
        PlayerArea.playerplaypausebutton.setContentAreaFilled(false);
        PlayerArea.playerplaynextbutton.setBorderPainted(false);
        PlayerArea.playerplaynextbutton.setContentAreaFilled(false);
        PlayerArea.playerplaypreviousbutton.setBorderPainted(false);
        PlayerArea.playerplaypreviousbutton.setContentAreaFilled(false);
        PlayerArea.playerimage.setImage(Graphics.NOTHINGPLAYING.getPath());
        playerarea.setBorder(null);
        //---
        // Resize components
        playerarea.setBounds(784 / 2 - playerarea.getWidth() / 2, 8, playerarea.getWidth(), playerarea.getHeight() - 3);
        //---
        // Add JTabbedPane
        legacyswitch.setVisible(true);
        //---
    }

    public static void showAdvancedSongPanel(String foruri, HomePanel.ContentTypes contentType) {
        homepanel.getComponent().setVisible(false);
        advancedSongPanelUri = foruri;
        ((DefaultTableModel) advancedsongtable.getModel()).setRowCount(0);
        advanceduricache.clear();
        advancedSongPanelVisible = true;
        if (artistPanelVisible) {
            artistPanelBackButton.doClick();
        }
        try {
            switch (contentType) {
                case playlist:
                    currentView = Views.PLAYLIST;
                    for (PlaylistTrack simplified : SpotifyUtils.getAllTracksPlaylist(foruri)) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getTrack().getName(), TrackUtils.calculateFileSizeKb(simplified.getTrack().getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getTrack().getDurationMs())});
                        advanceduricache.add(simplified.getTrack().getUri());
                    }
                    break;
                case show:
                    currentView = Views.SHOW;
                    for (EpisodeSimplified simplified : SpotifyUtils.getAllEpisodesShow(foruri)) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                        advanceduricache.add(simplified.getUri());
                    }
                    break;
                case album:
                    currentView = Views.ALBUM;
                    for (TrackSimplified simplified : SpotifyUtils.getAllTracksAlbum(foruri)) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                        advanceduricache.add(simplified.getUri());
                    }
                    break;
                default:
                    GraphicalMessage.bug("tried to invoke showAdvancedSongPanel with incompatible type -> " + contentType);
                    break;
            }
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
        advancedsongpanel.setVisible(true);
        blockTabSwitch();
    }

    public static void openAbout() {
        HTMLDialog dialog = new HTMLDialog();
        dialog.getDialog().setPreferredSize(new Dimension(400, 500));
        try {
            String out = new Resources().readToString("about.html");
            StringBuilder cache = new StringBuilder();
            for (String s : out.split("\n")) {
                if (s.contains("(TRANSLATE)")) {
                    s = s.replace(s.split("\\(TRANSLATE\\)")[1].replace("(TRANSLATE)", ""), PublicValues.language.translate(s.split("\\(TRANSLATE\\)")[1].replace("(TRANSLATE)", "")));
                    s = s.replace("(TRANSLATE)", "");
                }
                cache.append(s);
            }
            String opensourcelist = new Resources().readToString("setup/thirdparty.html");
            String finalhtml = cache.toString().split("<insertOpenSourceList>")[0] + opensourcelist + cache.toString().split("</insertOpenSourceList>")[1];
            dialog.open(PublicValues.language.translate("ui.menu.help.about"), finalhtml);
        } catch (Exception ex) {
            GraphicalMessage.openException(ex);
            ConsoleLogging.Throwable(ex);
        }
        dialog.getDialog().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.getDialog().dispose();
            }
        });
    }

    void createHome() {
        homepanel = new HomePanel();
        tabpanel.add(homepanel.getComponent());
    }

    void createPlayerArea() {
        playerarea = new PlayerArea(frame);
        add(playerarea);
    }

    void createLibrary() {
        librarypanel = new Library();
        tabpanel.add(librarypanel);
    }

    void createArtistPanel() {
        artistPanelBackButton = new JButton(PublicValues.language.translate("ui.back"));
        artistPanelBackButton.setBounds(0, 0, 89, 23);
        artistPanelBackButton.setForeground(PublicValues.globalFontColor);
        artistPanelBackButton.addActionListener(new AsyncActionListener(e -> {
            if (PublicValues.blockArtistPanelBackButton) {
                return;
            }
            switch (lastmenu) {
                case Search:
                    currentView = Views.SEARCH;
                    if (isLastArtist) {
                        artistPanel.openPanel();
                        artistPanel.isFirst = true;
                        artistPanel.contentPanel.setVisible(true);
                        Search.searchplaylistpanel.setVisible(false);
                        artistPanelVisible = true;
                        isLastArtist = false;
                    } else {
                        artistPanel.contentPanel.setVisible(false);
                        artistPanelBackButton.setVisible(false);
                        searchpanel.setVisible(true);
                        artistPanelVisible = false;
                        ContentPanel.enableTabSwitch();
                    }
                    break;
                case Home:
                    currentView = Views.HOME;
                    homepanel.getComponent().setVisible(true);
                    artistPanelBackButton.setVisible(false);
                    artistPanel.contentPanel.setVisible(false);
                    artistPanelVisible = false;
                    ContentPanel.enableTabSwitch();
            }
        }));
        tabpanel.add(artistPanelBackButton);
        artistPanel = new ArtistPanel();
        artistPanel.contentPanel.setBounds(0, 21, 784, 400);
        tabpanel.add(artistPanel.contentPanel);
        artistPanel.contentPanel.setVisible(false);
    }

    void createPlaylist() {
        playlistspanel = new Playlists();
        tabpanel.add(playlistspanel);
    }

    void createSearch() {
        searchpanel = new Search();
        tabpanel.add(searchpanel);
    }

    void createErrorDisplay() {
        errorDisplay = new JButton() {
            @Override
            public void setText(String text) {
                if (text.equals("Default")) {
                    text = PublicValues.language.translate("ui.errorqueue.button");
                    super.setText(text);
                    return;
                }
                if (!text.contains(PublicValues.language.translate("ui.errorqueue.button"))) {
                    text = PublicValues.language.translate("ui.errorqueue.button") + " " + text;
                }
                if (errorDisplay != null) errorDisplay.setVisible(true);
                if (text.equals(String.valueOf(0))) {
                    try {
                        Objects.requireNonNull(errorDisplay);
                        errorDisplay.setVisible(false);
                    } catch (NullPointerException e) {
                        throw new RuntimeException(e);
                    }
                }
                super.setText(text);
                if (errorDisplay != null)
                    errorDisplay.setBounds(10, 10, errorDisplay.getWidth(), errorDisplay.getHeight());
            }
        };
        errorDisplay.setText("Default");
        errorQueue = new ArrayList<ExceptionDialog>() {
            @Override
            public boolean add(ExceptionDialog exceptionDialog) {
                super.add(exceptionDialog);
                errorDisplay.setText(String.valueOf(errorQueue.size()));
                return true;
            }
        };
        errorDisplay.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(errorQueue.size() > 100) {
                    if(!(SystemUtils.getUsableRAMmb() > 512)) {
                        errorQueue.clear();
                        GraphicalMessage.sorryError("Too many errors! Out of memory prevention");
                        return;
                    }
                }
                if(errorDisplayVisible) {
                    return;
                }
                errorDisplayVisible = true;
                JFrame dialog = new JFrame();
                dialog.setTitle(PublicValues.language.translate("ui.errorqueue.title"));
                JScrollPane pane = new JScrollPane();
                DefTable table = new DefTable();
                table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{""}));
                pane.setViewportView(table);
                for (ExceptionDialog exd : errorQueue) {
                    ((DefaultTableModel) table.getModel()).addRow(new Object[]{exd.getPreview()});
                }
                table.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if(SwingUtilities.isRightMouseButton(e)) return;
                        if(e.getClickCount() == 2) errorQueue.get(table.getSelectedRow()).openReal();
                    }
                }));
                ContextMenu menu = new ContextMenu(pane, table);
                menu.addItem(PublicValues.language.translate("ui.general.copy"), () -> ClipboardUtil.set(errorQueue.get(table.getSelectedRow()).getAsFormattedText()));
                menu.addItem(PublicValues.language.translate("ui.general.remove"), () -> {
                    errorQueue.remove(table.getSelectedRow());
                    errorDisplay.setText(String.valueOf(errorQueue.size()));
                    ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
                    if (table.getModel().getRowCount() == 0) {
                        errorDisplay.setVisible(false);
                    }
                });
                JButton remove = new JButton(PublicValues.language.translate("ui.errorqueue.clear"));
                remove.addActionListener(new AsyncActionListener(e1 -> {
                    errorQueue.clear();
                    ((DefaultTableModel) table.getModel()).setRowCount(0);
                    errorDisplay.setVisible(false);
                }));
                dialog.add(remove, BorderLayout.SOUTH);
                dialog.setPreferredSize(new Dimension(ContentPanel.frame.getWidth() / 2, ContentPanel.frame.getHeight() / 2));
                dialog.add(pane, BorderLayout.CENTER);
                dialog.setVisible(true);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        errorDisplayVisible = false;
                    }
                });
                dialog.pack();
            }
        }));
        errorDisplay.setVisible(false);
        add(errorDisplay);
        errorDisplay.setBackground(Color.decode("#BB0000"));
        errorDisplay.setBounds(5, 5, 100, 40);
    }

    void createHotList() {
        hotlistpanel = new HotList();
        tabpanel.add(hotlistpanel);
    }

    void createQueue() {
        queuepanel = new Queue();
        tabpanel.add(queuepanel);
    }

    void createFeedback() {
        feedbackpanel = new Feedback();
        tabpanel.add(feedbackpanel);
    }

    @SuppressWarnings("all")
    void createLegacy() {
        legacyswitch.setForeground(PublicValues.globalFontColor);
        legacyswitch.setBounds(0, 111, 784, 450);
        legacyswitch.addTab(PublicValues.language.translate("ui.navigation.home"), new JPanel());
        legacyswitch.addTab(PublicValues.language.translate("ui.navigation.playlists"), new JPanel());
        legacyswitch.addTab(PublicValues.language.translate("ui.navigation.library"), new JPanel());
        legacyswitch.addTab(PublicValues.language.translate("ui.navigation.search"), new JPanel());
        legacyswitch.addTab(PublicValues.language.translate("ui.navigation.hotlist"), new JPanel());
        legacyswitch.addTab(PublicValues.language.translate("ui.navigation.queue"), new JPanel());
        legacyswitch.addTab(PublicValues.language.translate("ui.navigation.feedback"), new JPanel());
        legacyswitch.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return 800 / legacyswitch.getTabCount() - 3;
            }
        });
        add(legacyswitch);
        legacyswitch.setSelectedIndex(0);
        setHomeVisible();
        preventBugLegacySwitch();
        legacyswitch.setComponentAt(0, tabpanel);
        setHomeVisible();
        legacyswitch.addChangeListener(e -> {
            switch (legacyswitch.getSelectedIndex()) {
                case 0:
                    currentView = Views.HOME;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setHomeVisible();
                    break;
                case 1:
                    currentView = Views.PLAYLIST;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setPlaylistsVisible();
                    break;
                case 2:
                    currentView = Views.LIBRARY;
                    if (Library.librarysonglist.getModel().getRowCount() == 0) {
                        Library.librarythread.start();
                    }
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setLibraryVisible();
                    break;
                case 3:
                    currentView = Views.SEARCH;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setSearchVisible();
                    break;
                case 4:
                    currentView = Views.HOTLIST;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setHotlistVisible();
                    break;
                case 5:
                    currentView = Views.QUEUE;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setQueueVisible();
                    break;
                case 6:
                    currentView = Views.FEEDBACK;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setFeedbackVisible();
                    break;
                default:
                    currentView = Views.OTHER;
                    int selected = legacyswitch.getSelectedIndex();
                    if(extraTabs.isEmpty()) {
                        ConsoleLogging.warning("JTabbedPane tried to open pane outside of the allowed range");
                        break;
                    }
                    hideAllPanel();
                    TabEntry entry = extraTabs.get(selected - 7);
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    entry.component.setVisible(true);
                    break;
            }
        });
        JMenu file = new JMenu(PublicValues.language.translate("ui.legacy.file"));
        JMenu edit = new JMenu(PublicValues.language.translate("ui.legacy.edit"));
        JMenu view = new JMenu(PublicValues.language.translate("ui.legacy.view"));
        JMenu playback = new JMenu(PublicValues.language.translate("ui.playback.menu"));
        JMenu account = new JMenu(PublicValues.language.translate("ui.legacy.account"));
        JMenu help = new JMenu(PublicValues.language.translate("ui.legacy.help"));
        JMenuItem exit = new JMenuItem(PublicValues.language.translate("ui.legacy.exit"));
        JMenuItem logout = new JMenuItem(PublicValues.language.translate("ui.legacy.logout"));
        JMenuItem about = new JMenuItem(PublicValues.language.translate("ui.legacy.about"));
        JMenuItem settings = new JMenuItem(PublicValues.language.translate("ui.legacy.settings"));
        JMenuItem extensions = new JMenuItem(PublicValues.language.translate("ui.legacy.extensionstore"));
        JMenuItem audiovisualizer = new JMenuItem(PublicValues.language.translate("ui.legacy.view.audiovisualizer"));
        JMenuItem playuri = new JMenuItem(PublicValues.language.translate("ui.legacy.playuri"));
        JMenuItem changedevice = new JMenuItem(PublicValues.language.translate("ui.playback.changedevice"));
        bar.add(file);
        bar.add(edit);
        bar.add(view);
        bar.add(account);
        bar.add(help);
        if(PublicValues.devMode) {
            JMenu developer = new JMenu("Developer");
            JMenuItem locationfinder = new JMenuItem("Location Finder");
            JMenuItem errorsimulator = new JMenuItem("Error Generator");
            bar.add(developer);
            developer.add(errorsimulator);
            developer.add(locationfinder);
            errorsimulator.addActionListener(e -> new ErrorSimulator().open());
            locationfinder.addActionListener(e -> new LocationFinder());
        }
        file.add(playuri);
        file.add(exit);
        edit.add(settings);
        view.add(audiovisualizer);
        account.add(logout);
        help.add(extensions);
        help.add(about);
        playback.add(changedevice);
        changedevice.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> devices = new ArrayList<>();
                Device[] devicelist;
                try {
                    devicelist = InstanceManager.getSpotifyApi().getUsersAvailableDevices().setHeader("Authorization", "Bearer " + Token.getToken("user-read-playback-state")).build().execute();
                    for(Device d : devicelist) {
                        devices.add(d.getName());
                    }
                    HttpClient client = HttpClients.createDefault();
                    HttpGet get = new HttpGet("https://api.spotify.com/v1/me/player/devices");
                    get.setHeader("Authorization", "Bearer " + PublicValues.session.tokens().getToken("user-read-playback-state", "user-read-private").accessToken);
                    System.out.println(EntityUtils.toString(client.execute(get).getEntity()));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                int retvalue = JOptionPane.showOptionDialog(frame, PublicValues.language.translate("ui.playback.changedevice.message"), PublicValues.language.translate("ui.playback.changedevice.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, devices.toArray(), PublicValues.session.deviceName());
                if(retvalue != JOptionPane.CLOSED_OPTION) {
                    JsonArray array = new JsonArray();
                    array.add(devicelist[retvalue].getId());
                    //try {
                    //    InstanceManager.getSpotifyApi().transferUsersPlayback(array).play(!PublicValues.spotifyplayer.).build().execute();
                    //}catch (Exception ex) {
                    //    throw new RuntimeException(ex);
                    //}
                }
            }
        }));
        audiovisualizer.addActionListener(e -> PublicValues.visualizer.open());
        extensions.addActionListener(e -> new InjectorStore().open());
        settings.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame dialog = new JFrame();
                dialog.setTitle(PublicValues.language.translate("ui.settings.title"));
                dialog.getContentPane().add(new SettingsPanel());
                dialog.setPreferredSize(new Dimension(422, 506));
                dialog.setResizable(false);
                dialog.setVisible(true);
                dialog.pack();
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        SettingsPanel.applySettings();
                    }
                });
            }
        }));
        logout.addActionListener(new AsyncActionListener(e -> {
            PublicValues.config.write(ConfigValues.username.name, "");
            PublicValues.config.write(ConfigValues.password.name, "");
            JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("ui.logout.text"), PublicValues.language.translate("ui.logout.title"), JOptionPane.OK_CANCEL_OPTION);
            System.exit(0);
        }));
        about.addActionListener(new AsyncActionListener(e -> openAbout()));
        exit.addActionListener(e -> System.exit(0));
        playuri.addActionListener(new AsyncActionListener(e -> {
            String uri = JOptionPane.showInputDialog(frame, PublicValues.language.translate("ui.playtrackuri.message"), PublicValues.language.translate("ui.playtrackuri.title"), JOptionPane.PLAIN_MESSAGE);
            PublicValues.spotifyplayer.load(uri, true, false);
            Events.triggerEvent(SpotifyXPEvents.queueUpdate.getName());
        }));
        if (steamdeck) {
            for (int i = 0; i < bar.getMenuCount(); i++) {
                JMenu menu1 = bar.getMenu(i);
                JFrame window = new JFrame();
                window.setTitle(menu1.getText());
                ArrayList<JMenuItem> items = new ArrayList<>();
                DefTable table = new DefTable();
                table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{""}));
                window.add(table, BorderLayout.CENTER);
                for (int j = 0; j < menu1.getMenuComponentCount(); j++) {
                    java.awt.Component comp = menu1.getMenuComponent(j);
                    if (comp instanceof JMenuItem) {
                        JMenuItem menuItem1 = (JMenuItem) comp;
                        items.add(menuItem1);
                        ((DefaultTableModel) table.getModel()).addRow(new Object[]{menuItem1.getText()});
                    }
                }
                table.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (e.getClickCount() == 2) {
                            items.get(table.getSelectedRow()).doClick();
                        }
                    }
                }));
                window.setPreferredSize(new Dimension(300, 300));
                window.setVisible(true);
                window.pack();
            }
        }else{
            PublicValues.menuBar = bar;
        }
    }

    @FunctionalInterface
    public interface PaintOverwrite {
        void run(java.awt.Graphics g);
    }

    private static PaintOverwrite overwrite;

    public PaintOverwrite getPaintOverwrite() {
        return overwrite;
    }

    public void removePaintOverwrite() {
        overwrite = null;
    }

    public static void addPaintOverwrite(PaintOverwrite over) {
        overwrite = over;
        PublicValues.contentPanel.repaint();
    }

    void createAdvancedPanel() {
        advancedsongpanel = new JPanel();
        advancedsongpanel.setBounds(0, 0, 784, 421);
        tabpanel.add(advancedsongpanel);
        advancedsongpanel.setLayout(null);
        advancedbackbutton = new JButton(PublicValues.language.translate("ui.back"));
        advancedbackbutton.setBounds(0, 0, 89, 23);
        advancedsongpanel.add(advancedbackbutton);
        advancedbackbutton.setForeground(PublicValues.globalFontColor);
        advancedsongpanel.setVisible(false);
        advancedbackbutton.addActionListener(new AsyncActionListener(e -> {
            advancedsongpanel.setVisible(false);
            homepanel.getComponent().setVisible(true);
            ContentPanel.enableTabSwitch();
        }));
        advancedsongtable = new DefTable();
        advancedsongtable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")}));
        advancedsongtable.setForeground(PublicValues.globalFontColor);
        advancedsongtable.getTableHeader().setForeground(PublicValues.globalFontColor);
        advancedscrollpanel = new JScrollPane();
        advancedscrollpanel.setBounds(0, 22, 784, 399);
        advancedsongpanel.add(advancedscrollpanel);
        advancedscrollpanel.setViewportView(advancedsongtable);
        advancedsongtable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(advanceduricache.get(advancedsongtable.getSelectedRow()), true, PublicValues.shuffle);
                    advancedsongtable.setColumnSelectionInterval(0, advancedsongtable.getColumnCount() - 1);
                    TrackUtils.addAllToQueue(advanceduricache, advancedsongtable);
                }
            }
        }));
    }

    public void setLibraryVisible() {
        currentView = Views.LIBRARY;
        lastmenu = LastTypes.Library;
        librarypanel.setVisible(true);
        searchpanel.setVisible(false);
        hotlistpanel.setVisible(false);
        feedbackpanel.setVisible(false);
        playlistspanel.setVisible(false);
        queuepanel.setVisible(false);
        homepanel.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = true;
        hotlistVisible = false;
        feedbackVisible = false;
    }

    public void setSearchVisible() {
        currentView = Views.SEARCH;
        lastmenu = LastTypes.Search;
        librarypanel.setVisible(false);
        searchpanel.setVisible(true);
        hotlistpanel.setVisible(false);
        feedbackpanel.setVisible(false);
        playlistspanel.setVisible(false);
        queuepanel.setVisible(false);
        homepanel.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = true;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
    }

    public void setButtonsHidden() {
        playerarea.setVisible(false);
    }

    public void setButtonsVisible() {
        playerarea.setVisible(true);
    }

    public void setNothingVisible() {
        setButtonsHidden();
        librarypanel.setVisible(false);
        searchpanel.setVisible(false);
        hotlistpanel.setVisible(false);
        feedbackpanel.setVisible(false);
        playlistspanel.setVisible(false);
        queuepanel.setVisible(false);
        homepanel.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
        for(TabEntry entry : extraTabs) {
            entry.component.setVisible(false);
        }
    }

    public void hideAllPanel() {
        librarypanel.setVisible(false);
        searchpanel.setVisible(false);
        hotlistpanel.setVisible(false);
        feedbackpanel.setVisible(false);
        playlistspanel.setVisible(false);
        queuepanel.setVisible(false);
        homepanel.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
        for(TabEntry entry : extraTabs) {
            entry.component.setVisible(false);
        }
    }

    public void setHotlistVisible() {
        currentView = Views.HOTLIST;
        lastmenu = LastTypes.HotList;
        librarypanel.setVisible(false);
        searchpanel.setVisible(false);
        hotlistpanel.setVisible(true);
        feedbackpanel.setVisible(false);
        playlistspanel.setVisible(false);
        queuepanel.setVisible(false);
        homepanel.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = true;
        feedbackVisible = false;
        if (HotList.hotlistplayliststable.getRowCount() == 0) {
            Thread t = new Thread(HotList::fetchHotlist, "Get HotList");
            t.start();
        }
    }

    public void setFeedbackVisible() {
        currentView = Views.FEEDBACK;
        lastmenu = LastTypes.Feedback;
        librarypanel.setVisible(false);
        searchpanel.setVisible(false);
        hotlistpanel.setVisible(false);
        feedbackpanel.setVisible(true);
        playlistspanel.setVisible(false);
        queuepanel.setVisible(false);
        homepanel.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = true;
    }

    public void setPlaylistsVisible() {
        currentView = Views.PLAYLISTS;
        lastmenu = LastTypes.Playlists;
        librarypanel.setVisible(false);
        searchpanel.setVisible(false);
        hotlistpanel.setVisible(false);
        feedbackpanel.setVisible(false);
        playlistspanel.setVisible(true);
        queuepanel.setVisible(false);
        homepanel.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = true;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
        Thread thread = new Thread(() -> {
            try {
                int total = InstanceManager.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute().getTotal();
                int parsed = 0;
                int counter = 0;
                int last = 0;
                int offset = 0;
                while (parsed != total) {
                    PlaylistSimplified[] playlists = InstanceManager.getSpotifyApi().getListOfCurrentUsersPlaylists().offset(offset).limit(50).build().execute().getItems();
                    for (PlaylistSimplified simplified : playlists) {
                        Playlists.playlistsuricache.add(simplified.getUri());
                        ((DefaultTableModel) Playlists.playlistsplayliststable.getModel()).addRow(new Object[]{simplified.getName()});
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
                    offset += 50;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "Playlists fetcher");
        if (Playlists.playlistsplayliststable.getModel().getRowCount() == 0) {
            thread.start();
        }
    }

    public void setQueueVisible() {
        currentView = Views.QUEUE;
        lastmenu = LastTypes.Queue;
        librarypanel.setVisible(false);
        searchpanel.setVisible(false);
        hotlistpanel.setVisible(false);
        feedbackpanel.setVisible(false);
        playlistspanel.setVisible(false);
        queuepanel.setVisible(true);
        homepanel.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = true;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
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
                } catch (IOException | SpotifyWebApiException | ParseException ex) {
                    ConsoleLogging.Throwable(ex);
                } catch (NullPointerException exc) {
                    // Nothing in queue
                }
            }, "Queue worker (ContentPanel)");
            queueworker.start();
        }
    }

    public void setHomeVisible() {
        currentView = Views.HOME;
        lastmenu = LastTypes.Home;
        librarypanel.setVisible(false);
        searchpanel.setVisible(false);
        hotlistpanel.setVisible(false);
        feedbackpanel.setVisible(false);
        playlistspanel.setVisible(false);
        queuepanel.setVisible(false);
        homepanel.getComponent().setVisible(true);
        homeVisible = true;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
    }

    public void open() {
        PublicValues.contentPanel = this;
        JFrame mainframe;
        mainframe = frame;
        try {
            mainframe.setIconImage(ImageIO.read(new Resources(false).readToInputStream("spotifyxp.png")));
        } catch (IOException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        }
        mainframe.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                PublicValues.screenNumber = Utils.getDisplayNumber(mainframe);
                super.componentMoved(e);
            }
        });
        mainframe.setPreferredSize(PublicValues.getApplicationDimensions());
        mainframe.getContentPane().add(this);
        mainframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainframe.dispose();
            }
        });
        mainframe.setForeground(Color.blue);
        Events.triggerEvent(SpotifyXPEvents.onFrameReady.getName());
        JMenu helpMenu = null;
        for(int i = 0; i < bar.getMenuCount(); i++) {
            JMenu menu = bar.getMenu(i);
            if(menu.getText().equals(PublicValues.language.translate("ui.legacy.help"))) {
                helpMenu = menu;
                break;
            }
        }
        if(helpMenu != null) {
            bar.remove(helpMenu);
            bar.add(helpMenu);
        }
        PublicValues.menuBar.setFont(getFont());
        PublicValues.menuBar.setBorder(null);
        PublicValues.menuBar.setForeground(PublicValues.globalFontColor);
        PublicValues.menuBar.setBackground(getBackground());
        mainframe.setJMenuBar(PublicValues.menuBar);
        mainframe.open();
        mainframe.setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width / 2 - PublicValues.applicationWidth / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - PublicValues.applicationHeight / 2)
        ;
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        } catch (InterruptedException ignored) {
        }
        mainframe.requestFocus();
        mainframe.setAlwaysOnTop(false);
        Events.triggerEvent(SpotifyXPEvents.onFrameVisible.getName());
    }

    enum LastTypes {Playlists, Library, Search, HotList, Queue, Feedback, Home}
}