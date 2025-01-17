package com.spotifyxp.panels;

import com.neovisionaries.i18n.CountryCode;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Artist;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.dev.ErrorSimulator;
import com.spotifyxp.dev.LocationFinder;
import com.spotifyxp.dialogs.HTMLDialog;
import com.spotifyxp.events.EventSubscriber;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class ContentPanel extends JPanel {
    public static PlayerArea playerarea;
    public static Search searchpanel;
    public static Library librarypanel;
    public static Playlists playlistspanel;
    public static BrowsePanel browsepanel;
    public static HomePanel homepanel;
    public static HotList hotlistpanel;
    public static Queue queuepanel;
    public static Feedback feedbackpanel;
    public static ArtistPanel artistPanel;
    public static JPanel tabpanel;
    public static final JTabbedPane legacyswitch = new JTabbedPane();
    public static final JMenuBar bar = new JMenuBar();
    public static JButton errorDisplay;
    public static ArrayList<ExceptionDialog> errorQueue;
    public static boolean pressedCTRL = false;
    public static final JFrame frame = new JFrame("SpotifyXP - v" + ApplicationUtils.getVersion() + " " + ApplicationUtils.getReleaseCandidate());
    public static Views currentView = Views.HOME; //The view on start is home
    public static Views lastView = Views.HOME;
    public static View currentViewPanel;
    public static View lastViewPanel;
    static boolean steamdeck = false;
    static boolean errorDisplayVisible = false;
    public static SettingsPanel settingsPanel;
    public static TrackPanel trackPanel;
    public static SpotifySectionPanel sectionPanel;

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

    @Deprecated
    public static void addComponentToTabs(String title, JComponent component) {
        extraTabs.add(new TabEntry(title, component));
        legacyswitch.addTab(title, new JPanel());
        tabpanel.add(component);
    }

    @SuppressWarnings("Busy")
    public ContentPanel() throws IOException {
        ConsoleLogging.info(PublicValues.language.translate("debug.buildcontentpanelbegin"));
        SplashPanel.linfo.setText("Creating menu bar...");
        createMenuBar();
        SplashPanel.linfo.setText("Setting window size...");
        setPreferredSize(PublicValues.getApplicationDimensions());
        setLayout(null);
        SplashPanel.linfo.setText("Creating errorDisplay...");
        createErrorDisplay();
        Events.subscribe(SpotifyXPEvents.trackLoadFinished.getName(), (Object... data) -> PublicValues.blockLoading = false);
        SplashPanel.linfo.setText("Creating tabpanel...");
        tabpanel = new JPanel();
        tabpanel.setLayout(new BoxLayout(tabpanel, BoxLayout.Y_AXIS));
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
        SplashPanel.linfo.setText("Creating browse...");
        createBrowse();
        SplashPanel.linfo.setText("Creating browse section...");
        createSectionPanel();
        SplashPanel.linfo.setText("Creating home...");
        createHome();
        SplashPanel.linfo.setText("Creating track panel...");
        createTrackPanel();
        SplashPanel.linfo.setText("Creating settingsPanel...");
        createSettings();
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
        SplashPanel.linfo.setText("Making window interactive...");
        createLegacy();
        try {
            if (!(InstanceManager.getSpotifyApi().getCurrentUsersProfile() == null)) {
                PublicValues.countryCode = InstanceManager.getSpotifyApi().getCurrentUsersProfile().build().execute().getCountry();
            }
        } catch (IOException | NullPointerException e) {
            ConsoleLogging.Throwable(e);
            // Defaulting to German
            PublicValues.countryCode = CountryCode.DE;
        }
        Events.subscribe(SpotifyXPEvents.addtoqueue.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                InstanceManager.getPlayer().getPlayer().addToQueue((String)data[0]);
            }
        });
        SplashPanel.linfo.setText("Init Theme...");
        updateTheme();
        SplashPanel.linfo.setText("Done building contentPanel");
        ConsoleLogging.info(PublicValues.language.translate("debug.buildcontentpanelend"));
    }

    private void createTrackPanel() {
        trackPanel = new TrackPanel();
        tabpanel.add(trackPanel);
    }

    void createSettings() {
        settingsPanel = new SettingsPanel();
    }

    @Override
    public void paint(java.awt.Graphics g) {
        super.paint(g);
        if (getPaintOverwrite() != null) {
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
        switch (currentView) {
            case BROWSE:
                browsepanel.makeInvisible();
                break;
            case HOME:
                homepanel.makeInvisible();
                break;
            case SEARCH:
                searchpanel.makeInvisible();
                break;
        }
        switchView(Views.ARTIST);
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
                } catch (IOException ex) {
                    ConsoleLogging.Throwable(ex);
                }
            }, "Get tracks");
            Thread albumthread = new Thread(() -> {
                for (AlbumSimplified album : SpotifyUtils.getAllAlbumsArtist(a.getUri())) {
                    artistPanel.albumuricache.add(album.getUri());
                    ((DefaultTableModel) artistPanel.artistalbumalbumtable.getModel()).addRow(new Object[]{album.getName()});
                }
            }, "Get albums for artist");
            albumthread.start();
            trackthread.start();
            artistPanel.openPanel();
        } catch (IOException ex) {
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

    void createBrowse() {
        browsepanel = new BrowsePanel();
        tabpanel.add(browsepanel);
    }

    void createPlayerArea() {
        playerarea = new PlayerArea(frame);
        add(playerarea);
    }

    void createLibrary() {
        librarypanel = new Library();
        tabpanel.add(librarypanel);
    }

    void createSectionPanel() {
        sectionPanel = new SpotifySectionPanel();
        tabpanel.add(sectionPanel);
    }

    void createArtistPanel() {
        artistPanel = new ArtistPanel();
        ArtistPanel.contentPanel.setPreferredSize(new Dimension(784, 400));
        tabpanel.add(ArtistPanel.contentPanel);
        ArtistPanel.contentPanel.setVisible(false);
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
                if (errorQueue.size() > 100) {
                    if (!(SystemUtils.getUsableRAMmb() > 512)) {
                        errorQueue.clear();
                        GraphicalMessage.sorryError("Too many errors! Out of memory prevention");
                        return;
                    }
                }
                if (errorDisplayVisible) {
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
                        if (SwingUtilities.isRightMouseButton(e)) return;
                        if (e.getClickCount() == 2) errorQueue.get(table.getSelectedRow()).openReal();
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

    void createQueue() throws IOException {
        queuepanel = new Queue();
        tabpanel.add(queuepanel);
    }

    void createFeedback() {
        feedbackpanel = new Feedback();
        tabpanel.add(feedbackpanel);
    }

    //ToDo: Remove this code. This is a horrible way to use a JTabbedPane
    // This is left over code from the pre JTabbedPane era, where
    // the views were switched by JButtons
    @SuppressWarnings("all")
    void createLegacy() {
        legacyswitch.setForeground(PublicValues.globalFontColor);
        legacyswitch.setBounds(0, 111, PublicValues.applicationWidth, PublicValues.contentContainerHeight());
        legacyswitch.addTab(PublicValues.language.translate("ui.navigation.home"), new JPanel());
        legacyswitch.addTab("Browse", new JPanel());
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
        preventBugLegacySwitch();
        legacyswitch.setComponentAt(0, tabpanel);
        switchView(Views.HOME);
        legacyswitch.addChangeListener(e -> {
            switch (legacyswitch.getSelectedIndex()) {
                case 0:
                    currentView = Views.HOME;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    switchView(Views.HOME);
                    break;
                case 1:
                    currentView = Views.BROWSE;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    switchView(Views.BROWSE);
                    break;
                case 2:
                    currentView = Views.PLAYLIST;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    switchView(Views.PLAYLIST);
                    break;
                case 3:
                    currentView = Views.LIBRARY;
                    if (Library.librarysonglist.getModel().getRowCount() == 0) {
                        Library.librarythread.start();
                    }
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    switchView(Views.LIBRARY);
                    break;
                case 4:
                    currentView = Views.SEARCH;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    switchView(Views.SEARCH);
                    break;
                case 5:
                    currentView = Views.HOTLIST;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    switchView(Views.HOTLIST);
                    break;
                case 6:
                    currentView = Views.QUEUE;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    switchView(Views.QUEUE);
                    break;
                case 7:
                    currentView = Views.FEEDBACK;
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    switchView(Views.FEEDBACK);
                    break;
                default:
                    GraphicalMessage.bug("JTabbedPane: Clicked outsite of allowed range");
            }
        });
    }

    void createMenuBar() {
        JMenu file = new JMenu(PublicValues.language.translate("ui.legacy.file"));
        JMenu edit = new JMenu(PublicValues.language.translate("ui.legacy.edit"));
        JMenu view = new JMenu(PublicValues.language.translate("ui.legacy.view"));
        JMenu account = new JMenu(PublicValues.language.translate("ui.legacy.account"));
        JMenu help = new JMenu(PublicValues.language.translate("ui.legacy.help"));
        JMenuItem exit = new JMenuItem(PublicValues.language.translate("ui.legacy.exit"));
        JMenuItem logout = new JMenuItem(PublicValues.language.translate("ui.legacy.logout"));
        JMenuItem about = new JMenuItem(PublicValues.language.translate("ui.legacy.about"));
        JMenuItem settings = new JMenuItem(PublicValues.language.translate("ui.legacy.settings"));
        JMenuItem extensions = new JMenuItem(PublicValues.language.translate("ui.legacy.extensionstore"));
        JMenuItem audiovisualizer = new JMenuItem(PublicValues.language.translate("ui.legacy.view.audiovisualizer"));
        JMenuItem playuri = new JMenuItem(PublicValues.language.translate("ui.legacy.playuri"));
        bar.add(file);
        bar.add(edit);
        bar.add(view);
        bar.add(account);
        bar.add(help);
        if (PublicValues.devMode) {
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
        audiovisualizer.addActionListener(e -> PublicValues.visualizer.open());
        extensions.addActionListener(e -> {
            try {
                new InjectorStore().open();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        settings.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame dialog = new JFrame();
                dialog.setTitle(PublicValues.language.translate("ui.settings.title"));
                dialog.getContentPane().add(settingsPanel);
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
            PublicValues.spotifyplayer.load(uri, true, PublicValues.shuffle);
            Events.triggerEvent(SpotifyXPEvents.queueUpdate.getName());
        }));
        PublicValues.menuBar = bar;
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

    public static void switchView(Views view) {
        if(currentViewPanel != null) {
            lastView = currentView;
            lastViewPanel = currentViewPanel;
        }
        if(lastViewPanel != null) {
            lastViewPanel.makeInvisible();
        }
        currentView = view;
        switch (view) {
            case HOME:
                currentViewPanel = homepanel;
                break;
            case BROWSE:
                currentViewPanel = browsepanel;
                break;
            case TRACKPANEL:
                currentViewPanel = trackPanel;
                break;
            case PLAYLIST:
                currentViewPanel = playlistspanel;
                break;
            case ARTIST:
                currentViewPanel = artistPanel;
                break;
            case SEARCH:
                currentViewPanel = searchpanel;
                break;
            case LIBRARY:
                currentViewPanel = librarypanel;
                break;
            case QUEUE:
                currentViewPanel = queuepanel;
                break;
            case HOTLIST:
                currentViewPanel = hotlistpanel;
                break;
            case FEEDBACK:
                currentViewPanel = feedbackpanel;
                break;
            case BROWSESECTION:
                currentViewPanel = sectionPanel;
                break;
        }
        currentViewPanel.makeVisible();
    }

    void fixSize() {
        legacyswitch.setSize(new Dimension(legacyswitch.getWidth(), getHeight() - 111));
    }

    public void open() {
        PublicValues.contentPanel = this;
        JFrame mainframe = frame;
        try {
            mainframe.setIconImage(ImageIO.read(new Resources().readToInputStream("spotifyxp.png")));
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
        mainframe.setPreferredSize(new Dimension(800, 600));
        mainframe.setMinimumSize(new Dimension(800, 600));
        mainframe.setContentPane(this);
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
        for (int i = 0; i < bar.getMenuCount(); i++) {
            JMenu menu = bar.getMenu(i);
            if (menu.getText().equals(PublicValues.language.translate("ui.legacy.help"))) {
                helpMenu = menu;
                break;
            }
        }
        if (helpMenu != null) {
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
        Events.subscribe(SpotifyXPEvents.recalculateSizes.getName(), (Object... data) -> fixSize());
        Events.triggerEvent(SpotifyXPEvents.recalculateSizes.getName());
        mainframe.requestFocus();
        mainframe.setAlwaysOnTop(false);
        Events.triggerEvent(SpotifyXPEvents.onFrameVisible.getName());
    }

}