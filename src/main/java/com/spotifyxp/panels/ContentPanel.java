package com.spotifyxp.panels;

import com.neovisionaries.i18n.CountryCode;
import com.spotifyxp.PublicValues;
import com.spotifyxp.api.Player;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.*;
import com.spotifyxp.dev.ErrorSimulator;
import com.spotifyxp.dev.LocationFinder;
import com.spotifyxp.dialogs.HTMLDialog;
import com.spotifyxp.dialogs.LyricsDialog;
import com.spotifyxp.dpi.JComponentFactory;
import com.spotifyxp.dummy.DummyCanvasPlayer;
import com.spotifyxp.engine.EnginePanel;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.LoggerEvent;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.history.PlaybackHistory;
import com.spotifyxp.injector.InjectorStore;
import com.spotifyxp.lastfm.LastFMDialog;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.*;
import com.spotifyxp.theming.themes.DarkGreen;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.*;
import com.spotifyxp.video.CanvasPlayer;
import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ContentPanel extends JPanel {
    public static DefTable librarysonglist;
    public static Player player = null;
    public static DefTable searchsonglist;
    public static JTextField searchartistfield;
    public static JTextField searchsongtitlefield;
    public static DefTable hotlistplayliststable;
    public static DefTable hotlistsongstable;
    public static DefTable playlistsplayliststable;
    public static DefTable playlistssongtable;
    public static JTextField feedbackupdaterversionfield;
    public static EnginePanel playerarea;
    public static JImagePanel playerimage;
    public static JScrollText playertitle;
    public static JLabel playerdescription;
    public static JImageButton playerplaypreviousbutton;
    public static JImageButton playerplaypausebutton;
    public static JImageButton playerplaynextbutton;
    public static JSlider playercurrenttime;
    public static JLabel playerplaytime;
    public static JLabel playerplaytimetotal;
    public static JPanel tabpanel;
    public static JPanel librarypane;
    public static JButton libraryshufflebutton;
    public static JButton libraryplaybutton;
    public static JScrollPane libraryscrollpane;
    public static HomePanel homepane;
    public static DefaultTableModel librarydefaulttablemodel;
    public static JPanel searchpane;
    public static JPanel hotlistpane;
    public static JPanel hotlistplaylistspanel;
    public static JScrollPane hotlistplaylistsscrollpanel;
    public static JPanel hotlistsonglistpanel;
    public static JScrollPane hotslistsongscrollpanel;
    public static JPanel playlistspane;
    public static JPanel playlistsplaylistslist;
    public static JScrollPane playlistsplaylistsscroll;
    public static JPanel playlistssonglist;
    public static JScrollPane playlistssongsscroll;
    public static JPanel queuepane;
    public static JButton queueremovebutton;
    public static boolean homeVisible = false;
    public static JScrollPane queuescrollpane;
    public static JList<String> queuelist;
    public static JPanel feedbackpane;
    public static JLabel feedbackmakesurelabel;
    public static JPanel feedbackissuepanel;
    public static JButton feedbackviewissuesbutton;
    public static JButton feedbackcreateissuebutton;
    public static JButton feedbackgithubbutton;
    public static JPanel feedbackupdatespanel;
    public static JLabel feedbackwillbemovedlabel;
    public static JButton feedbackupdaterdownloadbutton;
    public static JPanel searchsearchfieldspanel;
    public static JLabel searchartistlabel;
    public static JLabel searchsongtitlelabel;
    public static JButton searchclearfieldsbutton;
    public static JButton searchfinditbutton;
    public static JPanel searchsearchfilterpanel;
    public static JRadioButton searchfilterexcludeexplicit;
    public static JScrollPane searchscrollpanel;
    public static boolean searchVisible = false;
    public static boolean playlistsVisible = false;
    public static boolean feedbackVisible = false;
    public static boolean queueVisible = false;
    public static boolean hotlistVisible = false;
    public static boolean libraryVisble = false;
    public static final JTabbedPane legacyswitch = (JTabbedPane) JComponentFactory.createJComponent(new JTabbedPane());
    public static final JMenuBar bar = (JMenuBar) JComponentFactory.createJComponent(new JMenuBar());
    public static JSVGPanel heart;
    public static final ArrayList<String> searchsonglistcache = new ArrayList<>();
    public static final ArrayList<String> hotlistplaylistlistcache = new ArrayList<>();
    public static final ArrayList<String> hotlistsonglistcache = new ArrayList<>();
    public static final ArrayList<String> libraryuricache = new ArrayList<>();
    public static final ArrayList<String> queueuricache = new ArrayList<>();
    public static final DefaultListModel<String> queuelistmodel = new DefaultListModel<>();
    public static final ArrayList<String> playlistsuricache = new ArrayList<>();
    public static final ArrayList<String> playlistssonguricache = new ArrayList<>();
    public static final ArrayList<String> searchplaylistsongscache = new ArrayList<>();
    public static JSVGPanel userbutton;
    public static JSVGPanel settingsbutton;
    public static JSVGPanel threepointbutton;
    public static JSVGPanel historybutton;
    public static DropDownMenu userdropdown;
    public static DropDownMenu threepointdropdown;
    public static JSVGPanel playerareavolumeicon;
    public static JSlider playerareavolumeslider;
    public static JLabel playerareavolumecurrent;
    public static JPanel searchplaylistpanel;
    public static JButton searchbackbutton;
    public static DefTable searchplaylisttable;
    public static JRadioButton searchfilterplaylist;
    public static JRadioButton searchfilteralbum;
    public static JRadioButton searchfiltershow;
    public static JRadioButton searchfiltertrack;
    public static JScrollPane searchplaylistscrollpanel;
    public static JRadioButton searchfilterartist;
    public static ArtistPanel artistPanel;
    public static boolean isLastArtist = false;
    public static CountryCode countryCode;
    public static DefTable advancedsongtable;
    public static JButton artistPanelBackButton;
    public static JSVGPanel playerareashufflebutton;
    public static JPanel advancedsongpanel;
    public static JScrollPane advancedscrollpanel;
    public static JButton advancedbackbutton;
    public static JSVGPanel playerarearepeatingbutton;
    public static JSVGPanel playerarealyricsbutton;
    public static JTextField noconnectionmessage;
    public static ContextMenu searchplaylistsongscontextmenu;
    public static ContextMenu searchcontextmenu;
    public static ContextMenu hotlistplaylistspanelrightclickmenu;
    public static ContextMenu hotlistsongstablecontextmenu;
    public static JButton errorDisplay;
    public static boolean artistPanelVisible = false;
    public static ArrayList<ExceptionDialog> errorQueue;
    public static boolean shuffle = false;
    public static final ArrayList<String> advanceduricache = new ArrayList<>();
    public static boolean pressedCTRL = false;
    public static final JFrame2 frame = new JFrame2("SpotifyXP - v" + ApplicationUtils.getVersion() + " " + ApplicationUtils.getReleaseCandidate());
    public static SettingsPanel settingsPanel = null;
    static boolean steamdeck = false;
    static LastTypes lastmenu = LastTypes.HotList;
    static boolean advancedSongPanelVisible = false;
    private static boolean libraryLoadingInProgress = false;
    private static boolean doneLastParsing = false;

    public static final DefThread librarythread = new DefThread(new Runnable() {
        public void run() {
            try {
                libraryLoadingInProgress = true;
                int visibleCount = 28;
                int total = Factory.getSpotifyApi().getUsersSavedTracks().limit(visibleCount).build().execute().getTotal();
                int counter = 0;
                int last = 0;
                int parsed = 0;
                while (parsed != visibleCount) {
                    SavedTrack[] track = Factory.getSpotifyApi().getUsersSavedTracks().limit(visibleCount).build().execute().getItems();
                    for (SavedTrack t : track) {
                        libraryuricache.add(t.getTrack().getUri());
                        String a = TrackUtils.getArtists(t.getTrack().getArtists());
                        librarysonglist.addModifyAction(() -> ((DefaultTableModel) librarysonglist.getModel()).addRow(new Object[]{t.getTrack().getName() + " - " + a, TrackUtils.calculateFileSizeKb(t.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(t.getTrack().getDurationMs())}));
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
                }
                libraryLoadingInProgress = false;
            } catch (Exception e) {
                ConsoleLogging.error("Error loading users library! Library now locked");
                libraryLoadingInProgress = true;
                throw new RuntimeException(e);
            }
        }
    });
    boolean clicked = false;
    int w = 0;
    int h = 0;
    boolean visible = false;
    boolean dragging = false;
    boolean toggle = false;
    LastPlayState lastPlayState;
    int playerareawidth = 0;
    int playerareaheight = 0;
    boolean windowWasOpened = false;
    int count = 0;

    @SuppressWarnings("Busy")
    public ContentPanel(Player p) {
        ConsoleLogging.info(PublicValues.language.translate("debug.buildcontentpanelbegin"));
        player = p;
        SplashPanel.linfo.setText("Setting window size...");
        setPreferredSize(new Dimension(783, 600));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);
        SplashPanel.linfo.setText("Creating errorDisplay...");
        createErrorDisplay();
        SplashPanel.linfo.setText("Creating playback history...");
        PublicValues.history = new PlaybackHistory();
        Events.registerOnTrackNextEvent(() -> {
            if(PublicValues.spotifyplayer.currentPlayable() == null) return;
            if(!doneLastParsing) return;
            if(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri().split(":")[1].equals("track")) {
                try {
                    PublicValues.history.addSong(Factory.getSpotifyApi().getTrack(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri().split(":")[2]).build().execute());
                } catch (SQLException | IOException | SpotifyWebApiException | ParseException e) {
                    ConsoleLogging.Throwable(e);
                }
            }
        });
        Events.registerOnTrackLoadFinished(() -> PublicValues.blockLoading = false);
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
        SplashPanel.linfo.setText("Creating search");
        createSearch();
        createHome();
        SplashPanel.linfo.setText("Creating CanvasPlayer...");
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            PublicValues.canvasPlayer = new CanvasPlayer();
        } else {
            PublicValues.canvasPlayer = new DummyCanvasPlayer(false);
        }
        SplashPanel.linfo.setText("Creating advancedPanel...");
        createAdvancedPanel();
        SplashPanel.linfo.setText("Changing component visibility...");
        searchpane.setVisible(false); // Not show searchpane when window is opened
        librarypane.setVisible(false); // Not show librarypane when window is opened
        playlistspane.setVisible(false); // Not show playlistspane when window is opened
        queuepane.setVisible(false); // Not show queuepane when window is opened
        feedbackpane.setVisible(false); // Now show feedbackpane when window is opened
        hotlistpane.setVisible(false); // Not show hotlistpane when window is opened
        homepane.getComponent().setVisible(false); // Not show homepane when window is opened
        SplashPanel.linfo.setText("Adding window mouse listener...");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (pressedCTRL) {
                    PublicValues.injector.openInjectWindow("");
                }
            }
        });
        if (PublicValues.autoLoadHotList) {
            Thread t = new Thread(this::setHotlistVisible);
            t.start();
            add(tabpanel);
        }
        createLegacy();
        try {
            if (!(Factory.getSpotifyApi().getCurrentUsersProfile() == null)) {
                countryCode = Factory.getSpotifyApi().getCurrentUsersProfile().build().execute().getCountry();
            }
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            ConsoleLogging.Throwable(e);
            // Defaulting to German
            countryCode = CountryCode.DE;
        }
        artistPanelBackButton.setVisible(false);
        SplashPanel.linfo.setText("Init Theme...");
        updateTheme();
        SplashPanel.linfo.setText("Trying to restore play state...");
        if (new File(PublicValues.fileslocation, "play.state").exists()) {
            parseLastPlayState();
            try {
                if (!lastPlayState.uri.isEmpty()) {
                    playerplaytime.setText(lastPlayState.playtime);
                    playerplaytimetotal.setText(lastPlayState.playtimetotal);
                    playercurrenttime.setMaximum(Integer.parseInt(lastPlayState.playerslidermax));
                    PublicValues.spotifyplayer.load(lastPlayState.uri, false, shuffle, false);
                    Runnable event = new Runnable() {
                        @Override
                        public void run() {
                            PublicValues.spotifyplayer.seek(Integer.parseInt(lastPlayState.playerslider) * 1000);
                            playerareavolumeslider.setValue(Integer.parseInt(lastPlayState.playervolume));
                            Events.remove(this);
                            doneLastParsing = true;
                        }
                    };
                    Events.registerPlayerLockReleaseEvent(event);
                }
                if(!lastPlayState.queue.isEmpty()) {
                    try {
                        PublicValues.spotifyplayer.clearQueue();
                        for(String s : lastPlayState.queue) {
                            PublicValues.spotifyplayer.addToQueue(s);
                        }
                    }catch (Exception ignored) {
                    }
                }
            } catch (Exception e) {
                //Failed to load last play state! Dont notify user because its not that important
            }
        }
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

    public static void loadNext() {
        if (libraryLoadingInProgress) {
            return;
        }
        libraryLoadingInProgress = true;
        try {
            int visibleCount = 19;
            int total = Factory.getSpotifyApi().getUsersSavedTracks().build().execute().getTotal();
            int parsed = 0;
            int counter = 0;
            int last = 0;
            if (total != libraryuricache.size()) {
                while (parsed != 19) {
                    SavedTrack[] track = Factory.getSpotifyApi().getUsersSavedTracks().limit(visibleCount).offset(libraryuricache.size()).build().execute().getItems();
                    for (SavedTrack t : track) {
                        libraryuricache.add(t.getTrack().getUri());
                        String a = TrackUtils.getArtists(t.getTrack().getArtists());
                        librarysonglist.addModifyAction(() -> ((DefaultTableModel) librarysonglist.getModel()).addRow(new Object[]{t.getTrack().getName() + " - " + a, TrackUtils.calculateFileSizeKb(t.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(t.getTrack().getDurationMs())}));
                        parsed++;
                    }
                    if (last == parsed) {
                        if (counter > 1) {
                            break;
                        }
                        counter++;
                    } else {
                        counter = 0;
                    }
                    last = parsed;
                }
            }
        } catch (Exception e) {
            libraryLoadingInProgress = true;
            ConsoleLogging.error("Error loading users library! Library now locked");
            throw new RuntimeException(e);
        }
        libraryLoadingInProgress = false;
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
        if (advancedSongPanelVisible) {
            homepane.getComponent().setVisible(true);
            advancedSongPanelVisible = false;
            advancedsongpanel.setVisible(false);
        }
        switch (lastmenu) {
            case Home:
                artistPanel.artistpopularuricache.clear();
                artistPanel.artistalbumuricache.clear();
                ((DefaultTableModel) artistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                ((DefaultTableModel) artistPanel.artistpopularsonglist.getModel()).setRowCount(0);
                artistPanel.artisttitle.setText("");
                ContentPanel.artistPanel.openPanel();
                ContentPanel.artistPanel.isFirst = true;
                artistPanel.contentPanel.setVisible(true);
                artistPanelBackButton.setVisible(true);
                artistPanelVisible = true;
                homepane.getComponent().setVisible(false);
                ContentPanel.blockTabSwitch();
                break;
            case Search:
                artistPanel.artistpopularuricache.clear();
                artistPanel.artistalbumuricache.clear();
                ((DefaultTableModel) artistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                ((DefaultTableModel) artistPanel.artistpopularsonglist.getModel()).setRowCount(0);
                artistPanel.artisttitle.setText("");
                ContentPanel.artistPanel.openPanel();
                ContentPanel.artistPanel.isFirst = true;
                artistPanel.contentPanel.setVisible(true);
                artistPanelBackButton.setVisible(true);
                artistPanelVisible = true;
                searchpane.setVisible(false);
                ContentPanel.blockTabSwitch();
                break;
        }
        try {
            Artist a = Factory.getSpotifyApi().getArtist(fromuri.split(":")[2]).build().execute();
            try {
                artistPanel.artistimage.setImage(new URL(SpotifyUtils.getImageForSystem(a.getImages()).getUrl()).openStream());
            } catch (ArrayIndexOutOfBoundsException exception) {
                // No artist image (when this is raised it's a bug)
            }
            artistPanel.artisttitle.setText(a.getName());
            DefThread trackthread = new DefThread(() -> {
                try {
                    for (Track t : Factory.getSpotifyApi().getArtistsTopTracks(a.getUri().split(":")[2], countryCode).build().execute()) {
                        if (!artistPanel.isVisible()) {
                            break;
                        }
                        artistPanel.artistpopularuricache.add(t.getUri());
                        Factory.getSpotifyAPI().addSongToList(TrackUtils.getArtists(t.getArtists()), t, artistPanel.artistpopularsonglist);
                    }
                } catch (IOException | ParseException | SpotifyWebApiException ex) {
                    ConsoleLogging.Throwable(ex);
                }
            });
            DefThread albumthread = new DefThread(() -> {
                for(AlbumSimplified album : SpotifyUtils.getAllAlbumsArtist(a.getUri())) {
                    artistPanel.artistalbumuricache.add(album.getUri());
                    ((DefaultTableModel) artistPanel.artistalbumalbumtable.getModel()).addRow(new Object[]{album.getName()});
                }
            });
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
        playerareavolumeicon.setImage(Graphics.VOLUMEFULL.getPath());
        playerarea.setBorder(new LineBorder(PublicValues.borderColor));
        playerplaypreviousbutton.setImage(Graphics.PLAYERPLAYPREVIOUS.getPath());
        playerplaypausebutton.setImage(Graphics.PLAYERPlAY.getPath());
        playerplaynextbutton.setImage(Graphics.PLAYERPLAYNEXT.getPath());
        playerareashufflebutton.setImage(Graphics.SHUFFLE.getPath());
        playerarearepeatingbutton.setImage(Graphics.REPEAT.getPath());
        playerarealyricsbutton.setImage(Graphics.MICROPHONE.getPath());
        playerplaypausebutton.setBorderPainted(false);
        playerplaypausebutton.setContentAreaFilled(false);
        playerplaynextbutton.setBorderPainted(false);
        playerplaynextbutton.setContentAreaFilled(false);
        playerplaypreviousbutton.setBorderPainted(false);
        playerplaypreviousbutton.setContentAreaFilled(false);
        playerimage.setImage(Graphics.NOTHINGPLAYING.getPath());
        playerarea.setBorder(null);
        //---
        // Resize components
        playerarea.setBounds(784 / 2 - playerarea.getWidth() / 2, 8, playerarea.getWidth(), playerarea.getHeight() - 3);
        //---
        // Add JTabbedPane
        legacyswitch.setVisible(true);
        //---
    }

    @SuppressWarnings("SameParameterValue")
    static Color hex2Rgb(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static void saveCurrentState() {
        try {
            Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri();
        } catch (Exception e) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(new File(PublicValues.fileslocation, "play.state"));
            StringBuilder queue = new StringBuilder();
            int counter = 0;
            for(ContextTrackOuterClass.ContextTrack t : PublicValues.spotifyplayer.tracks(true).next) {
                if(counter == 50) break;
                queue.append(t.getUri()).append("\n");
                counter++;
            }
            writer.write(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri() + "\n" + playercurrenttime.getValue() + "\n" + playerplaytime.getText() + "\n" + playerplaytimetotal.getText() + "\n" + playercurrenttime.getMaximum() + "\n" + playerareavolumecurrent.getText() + "\n" + StringUtils.replaceLast(queue.toString(), "\n", ""));
            writer.close();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
        try {
            new File(PublicValues.appLocation, "LOCK").delete();
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
    }

    public static void showAdvancedSongPanel(String foruri, HomePanel.ContentTypes contentType) {
        homepane.getComponent().setVisible(false);
        ((DefaultTableModel) advancedsongtable.getModel()).setRowCount(0);
        advanceduricache.clear();
        advancedSongPanelVisible = true;
        if (artistPanelVisible) {
            artistPanelBackButton.doClick();
        }
        try {
            switch (contentType) {
                case playlist:
                    for (PlaylistTrack simplified : SpotifyUtils.getAllTracksPlaylist(foruri)) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getTrack().getName(), TrackUtils.calculateFileSizeKb(simplified.getTrack().getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getTrack().getDurationMs())});
                        advanceduricache.add(simplified.getTrack().getUri());
                    }
                    break;
                case show:
                    for (EpisodeSimplified simplified : SpotifyUtils.getAllEpisodesShow(foruri)) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                        advanceduricache.add(simplified.getUri());
                    }
                    break;
                case album:
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
        HTMLDialog dialog = new HTMLDialog(new LoggerEvent() {
            @Override
            public void log(String s) {
            }

            @Override
            public void err(String s) {
            }

            @Override
            public void info(String s) {
            }

            @Override
            public void crit(String s) {
            }
        });
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
            String opensourcelist = URLUtils.getURLResponseAsString("https://raw.githubusercontent.com/SpotifyXP/SpotifyXP/main/opensourcelist.txt");
            String finalhtml = cache.toString().split("<insertOpenSourceList>")[0] + opensourcelist + cache.toString().split("</insertOpenSourceList>")[1];
            dialog.open(frame, PublicValues.language.translate("ui.menu.help.about"), finalhtml);
        } catch (Exception ex) {
            ExceptionDialog.open(ex);
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
        homepane = new HomePanel();
        tabpanel.add(homepane.getComponent());
    }

    void createPlayerArea() {
        playerarea = (EnginePanel) JComponentFactory.createJComponent(new EnginePanel());
        playerarea.setBounds(72, 0, 565, 100);
        add(playerarea);
        playerarea.setLayout(null);
        playerareashufflebutton = new JSVGPanel();
        playerareashufflebutton.getJComponent().setBounds(510, 75, 20, 20);
        playerareashufflebutton.getJComponent().setBackground(frame.getBackground());
        // playerarea.add(playerareashufflebutton.getJComponent());
        JComponentFactory.addJComponent(playerareashufflebutton.getJComponent());
        playerarearepeatingbutton = new JSVGPanel();
        playerarearepeatingbutton.getJComponent().setBounds(540, 75, 20, 20);
        playerarearepeatingbutton.getJComponent().setBackground(frame.getBackground());
        playerarea.add(playerarearepeatingbutton.getJComponent());
        playerareashufflebutton.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (playerareashufflebutton.isFilled) {
                    shuffle = false;
                    player.getPlayer().setShuffle(false);
                    try {
                        PublicValues.spotifyplayer.tracks(true).next.clear();
                        for (String s : Shuffle.before) {
                            PublicValues.spotifyplayer.addToQueue(s);
                        }
                    } catch (Exception e2) {
                        ConsoleLogging.Throwable(e2);
                        ExceptionDialog.open(e2);
                    }
                    playerareashufflebutton.setImage(Graphics.SHUFFLE.getPath());
                    playerareashufflebutton.isFilled = false;
                } else {
                    shuffle = true;
                    player.getPlayer().setShuffle(true);
                    Shuffle.makeShuffle();
                    playerareashufflebutton.isFilled = true;
                    playerareashufflebutton.setImage(Graphics.SHUFFLESELECTED.getPath());
                }
            }
        });
        playerarearepeatingbutton.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (playerarearepeatingbutton.isFilled) {
                    player.getPlayer().setRepeat(false, false);
                    playerarearepeatingbutton.setImage(Graphics.REPEAT.getPath());
                    playerarearepeatingbutton.isFilled = false;
                } else {
                    player.getPlayer().setRepeat(true, false);
                    playerarearepeatingbutton.isFilled = true;
                    playerarearepeatingbutton.setImage(Graphics.REPEATSELECTED.getPath());
                }
            }
        });
        JComponentFactory.addJComponent(playerarearepeatingbutton.getJComponent());
        playerimage = (JImagePanel) JComponentFactory.createJComponent(new JImagePanel());
        playerimage.setBounds(10, 11, 78, 78);
        playerarea.add(playerimage);
        playerarealyricsbutton = new JSVGPanel();
        playerarealyricsbutton.getJComponent().setBounds(280, 75, 14, 14);
        playerarealyricsbutton.getJComponent().setBackground(frame.getBackground());
        playerarea.add(playerarealyricsbutton.getJComponent());
        playerarealyricsbutton.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    if (PublicValues.lyricsDialog == null) {
                        PublicValues.lyricsDialog = new LyricsDialog();
                    }
                    if (playerarealyricsbutton.isFilled) {
                        PublicValues.lyricsDialog.close();
                        playerarealyricsbutton.setImage(Graphics.MICROPHONE.getPath());
                        playerarealyricsbutton.isFilled = false;
                    } else {
                        if (PublicValues.lyricsDialog.open(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri())) {
                            playerarealyricsbutton.setImage(Graphics.MICROPHONESELECTED.getPath());
                            playerarealyricsbutton.isFilled = true;
                        }
                    }
                } catch (NullPointerException e2) {
                    playerarealyricsbutton.setImage(Graphics.MICROPHONE.getPath());
                    playerarealyricsbutton.isFilled = false;
                }
            }
        });
        JComponentFactory.addJComponent(playerarealyricsbutton.getJComponent());
        playerareavolumeicon = new JSVGPanel();
        playerareavolumeicon.getJComponent().setBounds(306, 75, 14, 14);
        playerareavolumeicon.getJComponent().setBackground(frame.getBackground());
        playerarea.add(playerareavolumeicon.getJComponent());
        JComponentFactory.addJComponent(playerareavolumeicon.getJComponent());
        playerareavolumecurrent = (JLabel) JComponentFactory.createJComponent(new JLabel());
        playerareavolumecurrent.setBounds(489, 75, 35, 14);
        playerarea.add(playerareavolumecurrent);
        playerareavolumeslider = (JSlider) JComponentFactory.createJComponent(new JSlider());
        playerareavolumeslider.setBounds(334, 76, 145, 13);
        playerarea.add(playerareavolumeslider);
        playerareavolumeslider.setForeground(PublicValues.globalFontColor);
        playerareavolumecurrent.setText("10");
        playerareavolumeslider.setMinimum(0);
        playerareavolumeslider.setMaximum(10);
        playerareavolumeslider.setValue(10);
        player.getPlayer().setVolume(65536);
        playerareavolumeslider.addChangeListener(e -> {
            playerareavolumecurrent.setText(String.valueOf(playerareavolumeslider.getValue()));
            switch (playerareavolumeslider.getValue()) {
                case 0:
                    player.getPlayer().setVolume(0);
                    playerareavolumeicon.setImage(Graphics.VOLUMEMUTE.getPath());
                    break;
                case 1:
                    player.getPlayer().setVolume(6553);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 2:
                    player.getPlayer().setVolume(13107);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 3:
                    player.getPlayer().setVolume(19660);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 4:
                    player.getPlayer().setVolume(26214);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 5:
                    player.getPlayer().setVolume(32768);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 6:
                    player.getPlayer().setVolume(39321);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 7:
                    player.getPlayer().setVolume(45875);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 8:
                    player.getPlayer().setVolume(52428);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 9:
                    player.getPlayer().setVolume(58982);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 10:
                    player.getPlayer().setVolume(65536);
                    playerareavolumeicon.setImage(Graphics.VOLUMEFULL.getPath());
                    break;
            }
        });
        playertitle = (JScrollText) JComponentFactory.createJComponent(new JScrollText(PublicValues.language.translate("ui.player.title")));
        playertitle.setBounds(109, 11, 168, 14);
        playerarea.add(playertitle);
        playertitle.setForeground(PublicValues.globalFontColor);
        playerdescription = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.player.description")));
        playerdescription.setBounds(109, 40, 138, 20);
        playerarea.add(playerdescription);
        playerdescription.setForeground(PublicValues.globalFontColor);
        playerarea.setDebug(true);
        playerplaypreviousbutton = (JImageButton) JComponentFactory.createJComponent(new JImageButton());
        playerplaypreviousbutton.setBounds(287, 11, 70, 36);
        playerplaypreviousbutton.setColor(frame.getBackground());
        playerarea.add(playerplaypreviousbutton);
        playerplaypausebutton = (JImageButton) JComponentFactory.createJComponent(new JImageButton());
        playerplaypausebutton.setColor(frame.getBackground());
        playerplaypausebutton.setBounds(369, 11, 69, 36);
        playerplaypausebutton.addActionListener(e -> player.getPlayer().playPause());
        playerarea.add(playerplaypausebutton);
        playerplaynextbutton = (JImageButton) JComponentFactory.createJComponent(new JImageButton());
        playerplaynextbutton.setColor(frame.getBackground());
        playerplaynextbutton.setBounds(448, 11, 69, 36);
        playerarea.add(playerplaynextbutton);
        playercurrenttime = (JSlider) JComponentFactory.createJComponent(new JSlider());
        playercurrenttime.setValue(0);
        playercurrenttime.setBounds(306, 54, 200, 13);
        playerarea.add(playercurrenttime);
        playerplaytime = (JLabel) JComponentFactory.createJComponent(new JLabel("00:00"));
        playerplaytime.setHorizontalAlignment(SwingConstants.RIGHT);
        playerplaytime.setBounds(244, 54, 57, 14);
        playerarea.add(playerplaytime);
        playerplaytime.setForeground(PublicValues.globalFontColor);
        playerplaytimetotal = (JLabel) JComponentFactory.createJComponent(new JLabel("00:00"));
        playerplaytimetotal.setBounds(506, 54, 49, 14);
        playerarea.add(playerplaytimetotal);
        playerplaytimetotal.setForeground(PublicValues.globalFontColor);
        heart = new JSVGPanel();
        heart.getJComponent().setBackground(frame.getBackground());
        heart.getJComponent().setBounds(525, 20, 24, 24);
        heart.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (heart.isFilled) {
                    Factory.getSpotifyApi().removeUsersSavedTracks(Objects.requireNonNull(player.getPlayer().currentPlayable()).toSpotifyUri().split(":")[2]);
                    heart.setImage(Graphics.HEART.getPath());
                    heart.isFilled = false;
                } else {
                    Factory.getSpotifyApi().saveTracksForUser(Objects.requireNonNull(player.getPlayer().currentPlayable()).toSpotifyUri().split(":")[2]);
                    heart.setImage(Graphics.HEARTFILLED.getPath());
                    heart.isFilled = true;
                }
            }
        });
        heart.setImage(Graphics.HEART.getPath());
        JComponentFactory.addJComponent(heart.getJComponent());
        playerarea.add(heart.getJComponent());
        playercurrenttime.setForeground(PublicValues.globalFontColor);
        playercurrenttime.addChangeListener(e -> playerplaytime.setText(TrackUtils.getHHMMSSOfTrack(playercurrenttime.getValue() * 1000L)));
        playercurrenttime.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                player.getPlayer().pause();
                PlayerListener.pauseTimer = true;
            }
        });
        playercurrenttime.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                PlayerListener.pauseTimer = false;
                player.getPlayer().seek(playercurrenttime.getValue() * 1000);
                player.getPlayer().play();
            }
        });
        playerplaynextbutton.addActionListener(e -> player.getPlayer().next());
        playerplaypreviousbutton.addActionListener(e -> PublicValues.spotifyplayer.previous());
        playercurrenttime.addChangeListener(e -> ContentPanel.playerplaytime.setText(TrackUtils.getHHMMSSOfTrack(player.getPlayer().time())));
        ContextMenu menu = new ContextMenu(playerarea);
        menu.addItem("Open Canvas", () -> {
            DefThread thread = new DefThread(() -> {
                try {
                    if (Objects.requireNonNull(PublicValues.spotifyplayer.currentMetadata()).isTrack() && !Objects.requireNonNull(PublicValues.spotifyplayer.currentMetadata()).getName().isEmpty()) {
                        PublicValues.canvasPlayer.show();
                        PublicValues.canvasPlayer.switchMedia(UnofficialSpotifyAPI.getCanvasURLForTrack(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri()));
                        if (!PublicValues.spotifyplayer.isPaused()) {
                            PublicValues.canvasPlayer.play();
                        }
                    }
                } catch (Exception ignored) {
                }
            });
            thread.start();
        });

        historybutton = new JSVGPanel();
        historybutton.setImage(Graphics.HISTORY.getPath());
        historybutton.getJComponent().setBounds(720, 50, 20, 20);
        historybutton.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(historybutton.isFilled) {
                    historybutton.isFilled = false;
                    historybutton.setImage(Graphics.HISTORY.getPath());
                    PublicValues.history.dispose();
                }else{
                    historybutton.isFilled = true;
                    historybutton.setImage(Graphics.HISTORYSELECTED.getPath());
                    PublicValues.history.open();
                }
            }
        });
        JComponentFactory.addJComponent(historybutton.getJComponent());
        add(historybutton.getJComponent());
    }

    void createLibrary() {
        librarypane = (JPanel) JComponentFactory.createJComponent(new JPanel());
        librarypane.setBounds(0, 0, 784, 421);
        tabpanel.add(librarypane);
        librarypane.setLayout(null);
        libraryshufflebutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.library.shuffle")));
        libraryshufflebutton.setBounds(41, 398, 321, 23);
        librarypane.add(libraryshufflebutton);
        libraryshufflebutton.setForeground(PublicValues.globalFontColor);
        libraryplaybutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.library.play")));
        libraryplaybutton.setBounds(408, 398, 321, 23);
        librarypane.add(libraryplaybutton);
        libraryplaybutton.setForeground(PublicValues.globalFontColor);
        libraryscrollpane = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        libraryscrollpane.setBounds(0, 0, 784, 398);
        librarypane.add(libraryscrollpane);
        final boolean[] inProg = {false};
        libraryscrollpane.addMouseWheelListener(e -> {
            if (!inProg[0]) {
                inProg[0] = true;
                BoundedRangeModel m = libraryscrollpane.getVerticalScrollBar().getModel();
                int extent = m.getExtent();
                int maximum = m.getMaximum();
                int value = m.getValue();
                if (value + extent >= maximum / 2) {
                    if (libraryVisble) {
                        if (!libraryLoadingInProgress) {
                            DefThread thread = new DefThread(ContentPanel::loadNext);
                            thread.start();
                        }
                    }
                }
                inProg[0] = false;
            }
        });
        librarysonglist = (DefTable) JComponentFactory.createJComponent(new DefTable());
        librarysonglist.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.library.songlist.songname"), PublicValues.language.translate("ui.library.songlist.filesize"), PublicValues.language.translate("ui.library.songlist.bitrate"), PublicValues.language.translate("ui.library.songlist.length")}));
        librarysonglist.getTableHeader().setForeground(PublicValues.globalFontColor);
        librarysonglist.setForeground(PublicValues.globalFontColor);
        librarydefaulttablemodel = (DefaultTableModel) librarysonglist.getModel();
        librarysonglist.getColumnModel().getColumn(0).setPreferredWidth(347);
        librarysonglist.getColumnModel().getColumn(3).setPreferredWidth(51);
        librarysonglist.setFillsViewportHeight(true);
        libraryscrollpane.setViewportView(librarysonglist);
        libraryshufflebutton.addActionListener(e -> {
            DefThread thread1 = new DefThread(() -> {
                ArrayList<String> random = new ArrayList<>();
                Collections.copy(random, libraryuricache);
                PublicValues.spotifyplayer.load(libraryuricache.get(0), true, false, false);
                Collections.shuffle(random);
                for (String s : random) {
                    player.getPlayer().addToQueue(s);
                }
            });
            thread1.start();
        });
        librarysonglist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    player.getPlayer().load(libraryuricache.get(librarysonglist.getSelectedRow()), true, shuffle, true);
                    DefThread thread1 = new DefThread(() -> TrackUtils.addAllToQueue(libraryuricache, librarysonglist));
                    thread1.start();
                }
            }
        });
        ContextMenu librarymenu = new ContextMenu(librarysonglist);
        librarymenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(libraryuricache.get(librarysonglist.getSelectedRow())));
        librarymenu.addItem(PublicValues.language.translate("ui.general.refresh"), () -> {
            libraryuricache.clear();
            librarysonglist.removeAll();
            librarythread.start();
        });
        librarymenu.addItem(PublicValues.language.translate("ui.general.remove"), () -> {
            Factory.getSpotifyApi().removeUsersSavedTracks(libraryuricache.get(librarysonglist.getSelectedRow()).split(":")[2]);
            libraryuricache.remove(librarysonglist.getSelectedRow());
            ((DefaultTableModel) librarysonglist.getModel()).removeRow(librarysonglist.getSelectedRow());
        });
    }

    void createPlaylist() {
        playlistspane = (JPanel) JComponentFactory.createJComponent(new JPanel());
        playlistspane.setBounds(0, 0, 784, 421);
        tabpanel.add(playlistspane);
        playlistspane.setLayout(null);
        playlistsplaylistslist = (JPanel) JComponentFactory.createJComponent(new JPanel());
        playlistsplaylistslist.setBounds(0, 0, 259, 421);
        playlistspane.add(playlistsplaylistslist);
        playlistsplaylistslist.setLayout(null);
        playlistsplaylistsscroll = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        playlistsplaylistsscroll.setBounds(0, 0, 259, 421);
        playlistsplaylistslist.add(playlistsplaylistsscroll);
        playlistsplayliststable = (DefTable) JComponentFactory.createJComponent(new DefTable());
        playlistsplayliststable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.playlists.playlists.playlistname")}));
        playlistsplayliststable.setForeground(PublicValues.globalFontColor);
        playlistsplayliststable.getColumnModel().getColumn(0).setPreferredWidth(623);
        playlistsplayliststable.setFillsViewportHeight(true);
        playlistsplayliststable.setColumnSelectionAllowed(true);
        playlistsplayliststable.getTableHeader().setForeground(PublicValues.globalFontColor);
        playlistsplaylistsscroll.setViewportView(playlistsplayliststable);
        playlistssonglist = (JPanel) JComponentFactory.createJComponent(new JPanel());
        playlistssonglist.setBounds(260, 0, 524, 421);
        playlistspane.add(playlistssonglist);
        playlistssonglist.setLayout(null);
        playlistssongsscroll = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        playlistssongsscroll.setBounds(0, 0, 524, 421);
        playlistssonglist.add(playlistssongsscroll);
        playlistssongtable = (DefTable) JComponentFactory.createJComponent(new DefTable());
        playlistssongtable.getTableHeader().setForeground(PublicValues.globalFontColor);
        playlistssongtable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.playlists.songslist.songtitle"), PublicValues.language.translate("ui.playlists.songslist.filesize"), PublicValues.language.translate("ui.playlists.songslist.bitrate"), PublicValues.language.translate("ui.playlists.songslist.length")}));
        playlistssongtable.setForeground(PublicValues.globalFontColor);
        playlistssongtable.getColumnModel().getColumn(0).setPreferredWidth(363);
        playlistssongtable.getColumnModel().getColumn(1).setPreferredWidth(89);
        playlistssongtable.getColumnModel().getColumn(3).setPreferredWidth(96);
        playlistssongtable.setFillsViewportHeight(true);
        playlistssongtable.setColumnSelectionAllowed(true);
        playlistssongsscroll.setViewportView(playlistssongtable);
        ContextMenu menu = new ContextMenu(playlistsplayliststable);
        menu.addItem(PublicValues.language.translate("ui.general.remove.playlist"), () -> {
            Factory.getSpotifyApi().unfollowPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]);
            playlistsuricache.remove(playlistsuricache.get(playlistsplayliststable.getSelectedRow()));
            ((DefaultTableModel) playlistsplayliststable.getModel()).removeRow(playlistsplayliststable.getSelectedRow());
        });
        menu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            StringSelection strSel = new StringSelection(playlistsuricache.get(playlistsplayliststable.getSelectedRow()));
            clipboard.setContents(strSel, null);
        });
        playlistsplayliststable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefThread thread = new DefThread(() -> {
                        playlistssonguricache.clear();
                        ((DefaultTableModel) playlistssongtable.getModel()).setRowCount(0);
                        try {
                            int offset = 0;
                            int parsed = 0;
                            int counter = 0;
                            int last = 0;
                            int total = Factory.getSpotifyApi().getPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).build().execute().getTracks().getTotal();
                            while (parsed != total) {
                                Paging<PlaylistTrack> ptracks = Factory.getSpotifyApi().getPlaylistsItems(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).offset(offset).limit(100).build().execute();
                                for (PlaylistTrack track : ptracks.getItems()) {
                                    ((DefaultTableModel) playlistssongtable.getModel()).addRow(new Object[]{track.getTrack().getName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                    playlistssonguricache.add(track.getTrack().getUri());
                                    parsed++;
                                }
                                if (last == parsed) {
                                    if (counter > 1) {
                                        break;
                                    }
                                    counter++;
                                } else {
                                    counter = 0;
                                }
                                last = parsed;
                                offset += 100;
                            }
                        } catch (Exception e1) {
                            throw new RuntimeException(e1);
                        }
                    });
                    thread.start();
                }
            }
        });
        playlistssongtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    player.getPlayer().load(playlistssonguricache.get(playlistssongtable.getSelectedRow()), true, shuffle, false);
                    TrackUtils.addAllToQueue(playlistssonguricache, playlistssongtable);
                }
            }
        });
    }

    private String searchCacheTitle = "";
    private String searchCacheArtist = "";
    private boolean excludeExplicit = false;

    void createSearch() {
        searchpane = (JPanel) JComponentFactory.createJComponent(new JPanel());
        searchpane.setBounds(0, 0, 784, 421);
        tabpanel.add(searchpane);
        searchpane.setLayout(null);
        searchsearchfieldspanel = (JPanel) JComponentFactory.createJComponent(new JPanel());
        searchsearchfieldspanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.search.searchfield.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchsearchfieldspanel.setBounds(0, 0, 784, 128);
        searchpane.add(searchsearchfieldspanel);
        searchsearchfieldspanel.setLayout(null);
        searchsearchfieldspanel.setForeground(PublicValues.globalFontColor);
        searchartistlabel = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.search.searchfield.artist")));
        searchartistlabel.setHorizontalAlignment(SwingConstants.RIGHT);
        searchartistlabel.setBounds(30, 25, 46, 14);
        searchsearchfieldspanel.add(searchartistlabel);
        searchartistlabel.setForeground(PublicValues.globalFontColor);
        searchsongtitlelabel = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.search.searchfield.title")));
        searchsongtitlelabel.setHorizontalAlignment(SwingConstants.RIGHT);
        searchsongtitlelabel.setBounds(10, 62, 66, 14);
        searchsearchfieldspanel.add(searchsongtitlelabel);
        searchsongtitlelabel.setForeground(PublicValues.globalFontColor);
        searchclearfieldsbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.search.searchfield.button.clear")));
        searchclearfieldsbutton.setBounds(30, 94, 194, 23);
        searchsearchfieldspanel.add(searchclearfieldsbutton);
        searchclearfieldsbutton.setForeground(PublicValues.globalFontColor);
        searchclearfieldsbutton.addActionListener(e -> {
            searchartistfield.setText("");
            searchsongtitlefield.setText("");
        });
        searchfinditbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.search.searchfield.button.findit")));
        searchfinditbutton.setBounds(234, 94, 194, 23);
        searchsearchfieldspanel.add(searchfinditbutton);
        searchfinditbutton.setForeground(PublicValues.globalFontColor);
        searchartistfield = (JTextField) JComponentFactory.createJComponent(new JTextField());
        searchartistfield.setBounds(86, 22, 356, 20);
        searchsearchfieldspanel.add(searchartistfield);
        searchartistfield.setColumns(10);
        searchartistfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchfinditbutton.doClick();
                }
            }
        });
        searchsongtitlefield = (JTextField) JComponentFactory.createJComponent(new JTextField());
        searchsongtitlefield.setColumns(10);
        searchsongtitlefield.setBounds(86, 59, 356, 20);
        searchsearchfieldspanel.add(searchsongtitlefield);
        searchsongtitlefield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchfinditbutton.doClick();
                }
            }
        });
        searchsearchfilterpanel = (JPanel) JComponentFactory.createJComponent(new JPanel());
        searchsearchfilterpanel.setLayout(null);
        searchsearchfilterpanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.search.searchfield.filters.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchsearchfilterpanel.setBounds(452, 11, 322, 106);
        searchsearchfieldspanel.add(searchsearchfilterpanel);
        searchfilterexcludeexplicit = (JRadioButton) JComponentFactory.createJComponent(new JRadioButton(PublicValues.language.translate("ui.search.searchfield.filters.excludeexplicit")));
        searchfilterexcludeexplicit.setBounds(6, 24, 130, 23);
        searchsearchfilterpanel.add(searchfilterexcludeexplicit);
        searchfilterexcludeexplicit.setForeground(PublicValues.globalFontColor);
        searchfilterartist = (JRadioButton) JComponentFactory.createJComponent(new JRadioButton(PublicValues.language.translate("ui.search.filter.artist")));
        searchfilterartist.setBounds(160, 23, 130, 23);
        searchsearchfilterpanel.add(searchfilterartist);
        searchfilterartist.setForeground(PublicValues.globalFontColor);
        searchfiltertrack = (JRadioButton) JComponentFactory.createJComponent(new JRadioButton(PublicValues.language.translate("ui.search.filter.track")));
        searchfiltertrack.setBounds(6, 50, 130, 23);
        searchsearchfilterpanel.add(searchfiltertrack);
        searchfiltertrack.setForeground(PublicValues.globalFontColor);
        searchfiltertrack.setSelected(true);
        searchfilteralbum = (JRadioButton) JComponentFactory.createJComponent(new JRadioButton(PublicValues.language.translate("ui.search.filter.album")));
        searchfilteralbum.setBounds(160, 50, 130, 23);
        searchsearchfilterpanel.add(searchfilteralbum);
        searchfilteralbum.setForeground(PublicValues.globalFontColor);
        searchfilterplaylist = (JRadioButton) JComponentFactory.createJComponent(new JRadioButton(PublicValues.language.translate("ui.search.filter.playlist")));
        searchfilterplaylist.setBounds(6, 75, 130, 23);
        searchsearchfilterpanel.add(searchfilterplaylist);
        searchfilterplaylist.setForeground(PublicValues.globalFontColor);
        searchfiltershow = (JRadioButton) JComponentFactory.createJComponent(new JRadioButton(PublicValues.language.translate("ui.search.filter.show")));
        searchfiltershow.setBounds(160, 75, 130, 23);
        searchsearchfilterpanel.add(searchfiltershow);
        searchfiltershow.setForeground(PublicValues.globalFontColor);
        searchfilterartist.addActionListener(e -> {
            searchfiltertrack.setSelected(false);
            searchfilteralbum.setSelected(false);
            searchfiltershow.setSelected(false);
            searchfilterplaylist.setSelected(false);
        });
        searchfilteralbum.addActionListener(e -> {
            searchfiltertrack.setSelected(false);
            searchfiltershow.setSelected(false);
            searchfilterplaylist.setSelected(false);
            searchfilterartist.setSelected(false);
        });
        searchfilterplaylist.addActionListener(e -> {
            searchfiltertrack.setSelected(false);
            searchfilteralbum.setSelected(false);
            searchfiltershow.setSelected(false);
            searchfilterartist.setSelected(false);
        });
        searchfiltershow.addActionListener(e -> {
            searchfiltertrack.setSelected(false);
            searchfilteralbum.setSelected(false);
            searchfilterplaylist.setSelected(false);
            searchfilterartist.setSelected(false);
        });
        searchfiltertrack.addActionListener(e -> {
            searchfilteralbum.setSelected(false);
            searchfiltershow.setSelected(false);
            searchfilterplaylist.setSelected(false);
            searchfilterartist.setSelected(false);
        });
        searchfinditbutton.addActionListener(e -> {
            DefThread thread1 = new DefThread(() -> {
                String searchartist = searchartistfield.getText();
                String searchtitle = searchsongtitlefield.getText();
                boolean track = searchfiltertrack.isSelected();
                boolean artist = searchfilterartist.isSelected();
                boolean album = searchfilteralbum.isSelected();
                boolean show = searchfiltershow.isSelected();
                boolean playlist = searchfilterplaylist.isSelected();
                searchCacheTitle = searchtitle;
                searchCacheArtist = searchartist;
                excludeExplicit = searchfilterexcludeexplicit.isSelected();
                searchsonglistcache.clear();
                ((DefaultTableModel) searchsonglist.getModel()).setRowCount(0);
                if (searchtitle.isEmpty() && searchartist.isEmpty()) {
                    return; // User didn't type anything in so we just return
                }
                try {
                    if (track) {
                        for (Track t : Factory.getSpotifyApi().searchTracks(searchtitle + " " + searchartist).limit(50).build().execute().getItems()) {
                            String artists = TrackUtils.getArtists(t.getArtists());
                            if (!searchartist.equalsIgnoreCase("")) {
                                if (!TrackUtils.trackHasArtist(t.getArtists(), searchartist, true)) {
                                    continue;
                                }
                            }
                            if (excludeExplicit) {
                                if (!t.getIsExplicit()) {
                                    searchsonglistcache.add(t.getUri());
                                    Factory.getSpotifyAPI().addSongToList(artists, t, searchsonglist);
                                }
                            } else {
                                searchsonglistcache.add(t.getUri());
                                Factory.getSpotifyAPI().addSongToList(artists, t, searchsonglist);
                            }
                        }
                    }
                    if (artist) {
                        artistPanel.artistalbumuricache.clear();
                        artistPanel.artistpopularuricache.clear();
                        if (searchtitle.isEmpty()) {
                            searchtitle = searchartist;
                        }
                        for (Artist a : Factory.getSpotifyApi().searchArtists(searchtitle).build().execute().getItems()) {
                            searchsonglistcache.add(a.getUri());
                            Factory.getSpotifyAPI().addArtistToList(a, searchsonglist);
                        }

                    }
                    if (album) {
                        for (AlbumSimplified a : Factory.getSpotifyApi().searchAlbums(searchtitle).build().execute().getItems()) {
                            if (!searchartist.isEmpty()) {
                                if (!TrackUtils.trackHasArtist(a.getArtists(), searchartist, true)) {
                                    continue;
                                }
                            }
                            searchsonglistcache.add(a.getUri());
                            ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{a.getName()});
                        }
                    }
                    if (show) {
                        for (ShowSimplified s : Factory.getSpotifyApi().searchShows(searchtitle).build().execute().getItems()) {
                            if (!searchartist.isEmpty()) {
                                if (!s.getPublisher().equalsIgnoreCase(searchartist)) {
                                    continue;
                                }
                            }
                            searchsonglistcache.add(s.getUri());
                            ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{s.getName()});
                        }
                    }
                    if (playlist) {
                        for (PlaylistSimplified t : Factory.getSpotifyApi().searchPlaylists(searchtitle).build().execute().getItems()) {
                            if (!searchartist.isEmpty()) {
                                if (!t.getOwner().getDisplayName().equalsIgnoreCase(searchartist)) {
                                    continue;
                                }
                            }
                            searchsonglistcache.add(t.getUri());
                            Factory.getSpotifyAPI().addPlaylistToList(t, searchsonglist);
                        }
                    }
                    ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore")});
                } catch (IOException | SpotifyWebApiException | ParseException ex) {
                    ConsoleLogging.Throwable(ex);
                }
            });
            thread1.start();
        });
        searchscrollpanel = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        searchscrollpanel.setBounds(0, 139, 784, 282);
        searchpane.add(searchscrollpanel);
        searchsonglist = (DefTable) JComponentFactory.createJComponent(new DefTable());
        searchsonglist.getTableHeader().setReorderingAllowed(false);
        artistPanelBackButton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.back")));
        artistPanelBackButton.setBounds(0, 0, 89, 23);
        artistPanelBackButton.setForeground(PublicValues.globalFontColor);
        artistPanelBackButton.addActionListener(e -> {
            if (PublicValues.blockArtistPanelBackButton) {
                return;
            }
            switch (lastmenu) {
                case Search:
                    if (isLastArtist) {
                        ContentPanel.artistPanel.openPanel();
                        ContentPanel.artistPanel.isFirst = true;
                        ContentPanel.artistPanel.contentPanel.setVisible(true);
                        ContentPanel.searchplaylistpanel.setVisible(false);
                        artistPanelVisible = true;
                        isLastArtist = false;
                    } else {
                        artistPanel.contentPanel.setVisible(false);
                        artistPanelBackButton.setVisible(false);
                        searchpane.setVisible(true);
                        artistPanelVisible = false;
                        ContentPanel.enableTabSwitch();
                    }
                    break;
                case Home:
                    homepane.getComponent().setVisible(true);
                    artistPanelBackButton.setVisible(false);
                    artistPanel.contentPanel.setVisible(false);
                    artistPanelVisible = false;
                    ContentPanel.enableTabSwitch();
            }
        });
        tabpanel.add(artistPanelBackButton);
        artistPanel = new ArtistPanel();
        artistPanel.contentPanel.setBounds(0, 21, 784, 400);
        tabpanel.add(artistPanel.contentPanel);
        artistPanel.contentPanel.setVisible(false);
        JComponentFactory.addJComponent(artistPanel.contentPanel);
        searchsonglist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        if (searchsonglist.getModel().getValueAt(searchsonglist.getSelectedRow(), 2).toString().equals(PublicValues.language.translate("ui.general.loadmore"))) {
                            ((DefaultTableModel) searchsonglist.getModel()).setRowCount(searchsonglist.getRowCount() - 1);
                            DefThread thread1 = new DefThread(() -> {
                                String searchartist = searchCacheArtist;
                                String searchtitle = searchCacheTitle;
                                boolean track = searchsonglistcache.get(0).split(":")[1].equals("track");
                                boolean artist = searchsonglistcache.get(0).split(":")[1].equals("artist");
                                boolean album = searchsonglistcache.get(0).split(":")[1].equals("album");
                                boolean show = searchsonglistcache.get(0).split(":")[1].equals("show");
                                boolean playlist = searchsonglistcache.get(0).split(":")[1].equals("playlist");
                                try {
                                    if (track) {
                                        for (Track t : Factory.getSpotifyApi().searchTracks(searchtitle + " " + searchartist).limit(50).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            String artists = TrackUtils.getArtists(t.getArtists());
                                            if (!searchartist.equalsIgnoreCase("")) {
                                                if (!TrackUtils.trackHasArtist(t.getArtists(), searchartist, true)) {
                                                    continue;
                                                }
                                            }
                                            if (excludeExplicit) {
                                                if (!t.getIsExplicit()) {
                                                    searchsonglistcache.add(t.getUri());
                                                    Factory.getSpotifyAPI().addSongToList(artists, t, searchsonglist);
                                                }
                                            } else {
                                                searchsonglistcache.add(t.getUri());
                                                Factory.getSpotifyAPI().addSongToList(artists, t, searchsonglist);
                                            }
                                        }
                                    }
                                    if (artist) {
                                        artistPanel.artistalbumuricache.clear();
                                        artistPanel.artistpopularuricache.clear();
                                        if (searchtitle.isEmpty()) {
                                            searchtitle = searchartist;
                                        }
                                        for (Artist a : Factory.getSpotifyApi().searchArtists(searchtitle).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            searchsonglistcache.add(a.getUri());
                                            Factory.getSpotifyAPI().addArtistToList(a, searchsonglist);
                                        }
                                    }
                                    if (album) {
                                        for (AlbumSimplified a : Factory.getSpotifyApi().searchAlbums(searchtitle).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            if (!searchartist.isEmpty()) {
                                                if (!TrackUtils.trackHasArtist(a.getArtists(), searchartist, true)) {
                                                    continue;
                                                }
                                            }
                                            searchsonglistcache.add(a.getUri());
                                            ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{a.getName()});
                                        }
                                    }
                                    if (show) {
                                        for (ShowSimplified s : Factory.getSpotifyApi().searchShows(searchtitle).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            if (!searchartist.isEmpty()) {
                                                if (!s.getPublisher().equalsIgnoreCase(searchartist)) {
                                                    continue;
                                                }
                                            }
                                            searchsonglistcache.add(s.getUri());
                                            ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{s.getName()});
                                        }
                                    }
                                    if (playlist) {
                                        for (PlaylistSimplified t : Factory.getSpotifyApi().searchPlaylists(searchtitle).offset(searchsonglistcache.size()).build().execute().getItems()) {
                                            if (!searchartist.isEmpty()) {
                                                if (!t.getOwner().getDisplayName().equalsIgnoreCase(searchartist)) {
                                                    continue;
                                                }
                                            }
                                            searchsonglistcache.add(t.getUri());
                                            Factory.getSpotifyAPI().addPlaylistToList(t, searchsonglist);
                                        }
                                    }
                                    ((DefaultTableModel) searchsonglist.getModel()).addRow(new Object[]{PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore"), PublicValues.language.translate("ui.general.loadmore")});
                                } catch (IOException | SpotifyWebApiException | ParseException ex) {
                                    ConsoleLogging.Throwable(ex);
                                }
                            });
                            thread1.start();
                            return;
                        }
                    }catch (NullPointerException ignored) {
                    }
                    switch (searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[1]) {
                        case "playlist":
                        case "album":
                        case "show":
                            searchpane.setVisible(false);
                            ContentPanel.blockTabSwitch();
                            searchplaylistpanel.setVisible(true);
                            searchplaylistsongscache.clear();
                            ((DefaultTableModel) searchplaylisttable.getModel()).setRowCount(0);
                            break;
                        case "artist":
                            artistPanel.artistpopularuricache.clear();
                            artistPanel.artistalbumuricache.clear();
                            ((DefaultTableModel) artistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                            ((DefaultTableModel) artistPanel.artistpopularsonglist.getModel()).setRowCount(0);
                            artistPanel.artisttitle.setText("");
                            ContentPanel.artistPanel.openPanel();
                            ContentPanel.artistPanel.isFirst = true;
                            artistPanel.contentPanel.setVisible(true);
                            artistPanelBackButton.setVisible(true);
                            artistPanelVisible = true;
                            searchpane.setVisible(false);
                            ContentPanel.blockTabSwitch();
                            break;
                    }
                    DefThread thread = new DefThread(() -> {
                        switch (searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[1].toLowerCase()) {
                            case "playlist":
                                try {
                                    Playlist pl = Factory.getSpotifyApi().getPlaylist(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute();
                                    int parsed = 0;
                                    int offset = 0;
                                    int counter = 0;
                                    int last = 0;
                                    int total = pl.getTracks().getTotal();
                                    while (parsed != total) {
                                        for (PlaylistTrack track : Factory.getSpotifyApi().getPlaylistsItems(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).limit(100).build().execute().getItems()) {
                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{track.getTrack().getName() + " - " + pl.getName() + " - " + pl.getOwner().getDisplayName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                            searchplaylistsongscache.add(track.getTrack().getUri());
                                            parsed++;
                                        }
                                        if (last == parsed) {
                                            if (counter > 1) {
                                                break;
                                            }
                                            counter++;
                                        } else {
                                            counter = 0;
                                        }
                                        last = parsed;
                                        offset += 100;
                                    }
                                } catch (Exception e1) {
                                    ConsoleLogging.Throwable(e1);
                                }
                                break;
                            case "artist":
                                try {
                                    Artist a = Factory.getSpotifyApi().getArtist(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute();
                                    try {
                                        artistPanel.artistimage.setImage(new URL(SpotifyUtils.getImageForSystem(a.getImages()).getUrl()).openStream());
                                    } catch (ArrayIndexOutOfBoundsException exception) {
                                        // No artist image (when this is raised it's a bug)
                                    }
                                    artistPanel.artisttitle.setText(a.getName());
                                    DefThread trackthread = new DefThread(() -> {
                                        try {
                                            for (Track t : Factory.getSpotifyApi().getArtistsTopTracks(a.getUri().split(":")[2], countryCode).build().execute()) {
                                                if (!artistPanel.isVisible()) {
                                                    break;
                                                }
                                                artistPanel.artistpopularuricache.add(t.getUri());
                                                Factory.getSpotifyAPI().addSongToList(TrackUtils.getArtists(t.getArtists()), t, artistPanel.artistpopularsonglist);
                                            }
                                        } catch (IOException | ParseException | SpotifyWebApiException ex) {
                                            ConsoleLogging.Throwable(ex);
                                        }
                                    });
                                    DefThread albumthread = new DefThread(() -> Factory.getSpotifyAPI().addAllAlbumsToList(artistPanel.artistalbumuricache, a.getUri(), artistPanel.artistalbumalbumtable));
                                    albumthread.start();
                                    trackthread.start();
                                } catch (Exception e1) {
                                    throw new RuntimeException(e1);
                                }
                                break;
                            case "show":
                                try {
                                    int parsed = 0;
                                    int offset = 0;
                                    int last = 0;
                                    int counter = 0;
                                    int total = Factory.getSpotifyApi().getShow(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute().getEpisodes().getTotal();
                                    while (parsed != total) {
                                        for (EpisodeSimplified episode : Factory.getSpotifyApi().getShowEpisodes(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).limit(50).build().execute().getItems()) {
                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{episode.getName(), TrackUtils.calculateFileSizeKb(episode.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(episode.getDurationMs())});
                                            searchplaylistsongscache.add(episode.getUri());
                                            parsed++;
                                        }
                                        if (last == parsed) {
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
                                } catch (Exception e1) {
                                    throw new RuntimeException(e1);
                                }
                                break;
                            case "album":
                                try {
                                    int parsed = 0;
                                    int offset = 0;
                                    int last = 0;
                                    int counter = 0;
                                    int total = Factory.getSpotifyApi().getAlbumsTracks(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute().getTotal();
                                    while (parsed != total) {
                                        for (TrackSimplified simplified : Factory.getSpotifyApi().getAlbumsTracks(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).limit(50).build().execute().getItems()) {
                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                                            searchplaylistsongscache.add(simplified.getUri());
                                            parsed++;
                                        }
                                        if (last == parsed) {
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
                                } catch (Exception e1) {
                                    throw new RuntimeException(e1);
                                }
                                break;
                            case "track":
                                player.getPlayer().load(searchsonglistcache.get(searchsonglist.getSelectedRow()), true, shuffle, false);
                                break;
                            default:
                                throw new RuntimeException("Invalid uri '" + searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[1].toLowerCase() + "'");
                        }
                    });
                    thread.start();
                    searchsonglist.setColumnSelectionInterval(0, searchsonglist.getColumnCount() - 1);
                } else {
                    searchsonglist.setColumnSelectionInterval(0, searchsonglist.getColumnCount() - 1);
                }
            }
        });
        searchsonglist.getTableHeader().setForeground(PublicValues.globalFontColor);
        searchsonglist.setForeground(PublicValues.globalFontColor);
        searchsonglist.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")}));
        searchsonglist.getColumnModel().getColumn(0).setPreferredWidth(342);
        searchsonglist.getColumnModel().getColumn(1).setPreferredWidth(130);
        searchsonglist.setFillsViewportHeight(true);
        searchsonglist.setColumnSelectionAllowed(true);
        searchscrollpanel.setViewportView(searchsonglist);
        searchplaylistpanel = (JPanel) JComponentFactory.createJComponent(new JPanel());
        searchplaylistpanel.setBounds(0, 0, 784, 421);
        tabpanel.add(searchplaylistpanel);
        searchplaylistpanel.setLayout(null);
        searchbackbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.back")));
        searchbackbutton.setBounds(0, 0, 89, 23);
        searchplaylistpanel.add(searchbackbutton);
        searchbackbutton.setForeground(PublicValues.globalFontColor);
        searchplaylistscrollpanel = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        searchplaylistscrollpanel.setBounds(0, 22, 784, 399);
        searchplaylistpanel.add(searchplaylistscrollpanel);
        searchplaylisttable = (DefTable) JComponentFactory.createJComponent(new DefTable());
        searchplaylistscrollpanel.setViewportView(searchplaylisttable);
        searchplaylisttable.setForeground(PublicValues.globalFontColor);
        searchplaylisttable.getTableHeader().setForeground(PublicValues.globalFontColor);
        searchplaylisttable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    player.getPlayer().load(searchplaylistsongscache.get(searchplaylisttable.getSelectedRow()), true, shuffle, false);
                    searchplaylisttable.setColumnSelectionInterval(0, searchplaylisttable.getColumnCount() - 1);
                    TrackUtils.addAllToQueue(searchplaylistsongscache, searchplaylisttable);
                }
            }
        });
        searchplaylisttable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")}));
        searchplaylistpanel.setVisible(false);
        searchplaylistsongscontextmenu = new ContextMenu(searchplaylisttable);
        searchplaylistsongscontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(searchplaylistsongscache.get(searchplaylisttable.getSelectedRow())));
        searchbackbutton.addActionListener(e -> {
            searchplaylistpanel.setVisible(false);
            searchpane.setVisible(true);
            if (!artistPanelVisible) {
                enableTabSwitch();
            }
        });
        searchcontextmenu = new ContextMenu(searchsonglist);
        searchcontextmenu.addItem(PublicValues.language.translate("ui.general.addtolibrary"), () -> {
            heart.isFilled = true;
            heart.setImage(Graphics.HEARTFILLED.getPath());
            Factory.getSpotifyApi().saveTracksForUser("https://api.spotify.com/v1/me/tracks?ids=" + searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]);
            if (!libraryuricache.isEmpty()) {
                fetchOnlyFirstSongsFromUserLibrary();
            }
        });
        searchcontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(searchsonglistcache.get(searchsonglist.getSelectedRow())));
    }

    void createErrorDisplay() {
        errorDisplay = (JButton) JComponentFactory.createJComponent(new JButton() {
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
        });
        errorDisplay.setText("Default");
        errorQueue = new ArrayList<ExceptionDialog>() {
            @Override
            public boolean add(ExceptionDialog exceptionDialog) {
                super.add(exceptionDialog);
                errorDisplay.setText(String.valueOf(errorQueue.size()));
                return true;
            }
        };
        errorDisplay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(errorQueue.size() > 100) {
                    if(!(SystemUtils.getUsableRAMmb() > 512)) {
                        errorQueue.clear();
                        GraphicalMessage.sorryError("Too many errors! Out of memory prevention");
                        return;
                    }
                }
                JDialog dialog = new JDialog();
                dialog.setTitle(PublicValues.language.translate("ui.errorqueue.title"));
                JScrollPane pane = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
                DefTable table = (DefTable) JComponentFactory.createJComponent(new DefTable());
                table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{""}));
                pane.setViewportView(table);
                int i = 10;
                for (ExceptionDialog exd : errorQueue) {
                    ((DefaultTableModel) table.getModel()).addRow(new Object[]{exd.getPreview()});
                    i += 10;
                }
                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if(SwingUtilities.isRightMouseButton(e)) return;
                        if(e.getClickCount() != 2) return;
                        errorQueue.get(table.getSelectedRow()).openReal();
                    }
                });
                ContextMenu menu = new ContextMenu(pane, table);
                menu.addItem(PublicValues.language.translate("ui.general.copy"), () -> ClipboardUtil.set(errorQueue.get(table.getSelectedRow()).getAsFormattedText()));
                menu.addItem(PublicValues.language.translate("ui.general.remove"), () -> {
                    errorQueue.remove(errorQueue.size() - 1);
                    errorDisplay.setText(String.valueOf(errorQueue.size()));
                    ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
                    if (table.getModel().getRowCount() == 0) {
                        errorDisplay.setVisible(false);
                    }
                });
                JButton remove = new JButton(PublicValues.language.translate("ui.errorqueue.clear"));
                remove.addActionListener(e1 -> {
                    errorQueue.clear();
                    ((DefaultTableModel) table.getModel()).setRowCount(0);
                    errorDisplay.setVisible(false);
                });
                dialog.add(remove, BorderLayout.SOUTH);
                dialog.setPreferredSize(new Dimension(ContentPanel.frame.getWidth() / 2, ContentPanel.frame.getHeight() / 2));
                dialog.add(pane, BorderLayout.CENTER);
                dialog.setVisible(true);
                dialog.pack();
            }
        });
        errorDisplay.setVisible(false);
        add(errorDisplay);
        errorDisplay.setBackground(Color.decode("#BB0000"));
        errorDisplay.setBounds(5, 5, 100, 40);
    }

    void createHotList() {
        hotlistpane = (JPanel) JComponentFactory.createJComponent(new JPanel());
        hotlistpane.setBounds(0, 0, 784, 421);
        tabpanel.add(hotlistpane);
        hotlistpane.setLayout(null);
        hotlistplaylistspanel = (JPanel) JComponentFactory.createJComponent(new JPanel());
        hotlistplaylistspanel.setBounds(0, 0, 259, 421);
        hotlistpane.add(hotlistplaylistspanel);
        hotlistplaylistspanel.setLayout(null);
        hotlistplaylistsscrollpanel = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        hotlistplaylistsscrollpanel.setBounds(0, 0, 259, 421);
        hotlistplaylistspanel.add(hotlistplaylistsscrollpanel);
        hotlistplayliststable = (DefTable) JComponentFactory.createJComponent(new DefTable());
        hotlistplayliststable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.hotlist.playlistlist.playlists")}));
        hotlistplayliststable.setForeground(PublicValues.globalFontColor);
        hotlistplayliststable.getTableHeader().setForeground(PublicValues.globalFontColor);
        hotlistplayliststable.getColumnModel().getColumn(0).setPreferredWidth(623);
        hotlistplayliststable.setFillsViewportHeight(true);
        hotlistplayliststable.setColumnSelectionAllowed(true);
        hotlistplaylistsscrollpanel.setViewportView(hotlistplayliststable);
        hotlistsonglistpanel = (JPanel) JComponentFactory.createJComponent(new JPanel());
        hotlistsonglistpanel.setBounds(260, 0, 524, 421);
        hotlistpane.add(hotlistsonglistpanel);
        hotlistsonglistpanel.setLayout(null);
        hotslistsongscrollpanel = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
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
        hotlistsongstable = (DefTable) JComponentFactory.createJComponent(new DefTable());
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
        hotlistsongstable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hotlistsongstable.setColumnSelectionInterval(0, hotlistsongstable.getColumnCount() - 1);
                if (e.getClickCount() == 2) {
                    // player.getPlayer().tracks(true).next.clear();
                    player.getPlayer().load(hotlistsonglistcache.get(hotlistsongstable.getSelectedRow()), true, shuffle, false);
                    TrackUtils.addAllToQueue(hotlistsonglistcache, hotlistsongstable);
                }
            }
        });
        hotlistplayliststable.addMouseListener(new MouseAdapter() {
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
        });
    }

    void createQueue() {
        queuepane = new JPanel();
        queuepane.setBounds(0, 0, 784, 421);
        tabpanel.add(queuepane);
        queuepane.setLayout(null);
        queueremovebutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.queue.remove")));
        queueremovebutton.setBounds(0, 398, 784, 23);
        queuepane.add(queueremovebutton);
        queueremovebutton.setForeground(PublicValues.globalFontColor);
        queuescrollpane = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        queuescrollpane.setBounds(0, 0, 784, 395);
        queuepane.add(queuescrollpane);
        queuelist = new JList<>(queuelistmodel);
        queuescrollpane.setViewportView(queuelist);
        Events.registerToQueueUpdateEvent(() -> {
            if (queuelistmodel.isEmpty()) {
                return;
            }
            queueuricache.clear();
            ((DefaultListModel<?>) queuelist.getModel()).clear();
            try {
                for (ContextTrackOuterClass.ContextTrack t : PublicValues.spotifyplayer.tracks(true).next) {
                    Track track = Factory.getSpotifyApi().getTrack(t.getUri().split(":")[2]).build().execute();
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
        Events.registerToQueueAdvanceEvent(() -> {
            if (queuelistmodel.isEmpty()) {
                return;
            }
            queueuricache.remove(0);
            queuelistmodel.remove(0);
        });
        Events.registerToQueueRegressEvent(() -> {
            if (queuelistmodel.isEmpty()) {
                return;
            }
            if (!queuelistmodel.get(0).equalsIgnoreCase(PublicValues.spotifyplayer.tracks(true).current.getUri())) {
                try {
                    Track t = Factory.getSpotifyApi().getTrack(PublicValues.spotifyplayer.tracks(true).current.getUri().split(":")[2]).build().execute();
                    String a = TrackUtils.getArtists(t.getArtists());
                    queueuricache.add(0, t.getUri());
                    queuelistmodel.add(0, t.getName() + " - " + a);
                } catch (Exception e) {
                    throw new RuntimeException("Cant regress queue");
                }
            }
        });
        queueremovebutton.addActionListener(e -> {
            player.getPlayer().removeFromQueue(queueuricache.get(queuelist.getSelectedIndex()));
            queueuricache.remove(queueuricache.get(queuelist.getSelectedIndex()));
            ((DefaultListModel<?>) queuelist.getModel()).remove(queuelist.getSelectedIndex());
        });
    }

    void createFeedback() {
        feedbackpane = (JPanel) JComponentFactory.createJComponent(new JPanel());
        feedbackpane.setBounds(0, 0, 784, 421);
        tabpanel.add(feedbackpane);
        feedbackpane.setLayout(null);
        feedbackmakesurelabel = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.feedback.makesure")));
        feedbackmakesurelabel.setBounds(10, 23, 690, 25);
        feedbackpane.add(feedbackmakesurelabel);
        feedbackmakesurelabel.setForeground(PublicValues.globalFontColor);
        feedbackissuepanel = (JPanel) JComponentFactory.createJComponent(new JPanel());
        feedbackissuepanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.feedback.issues.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        feedbackissuepanel.setBounds(0, 333, 426, 88);
        feedbackpane.add(feedbackissuepanel);
        feedbackissuepanel.setLayout(null);
        feedbackviewissuesbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.feedback.issues.view")));
        feedbackviewissuesbutton.setBounds(10, 21, 188, 56);
        feedbackissuepanel.add(feedbackviewissuesbutton);
        feedbackviewissuesbutton.setForeground(PublicValues.globalFontColor);
        feedbackcreateissuebutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.feedback.issues.create")));
        feedbackcreateissuebutton.setBounds(227, 21, 188, 56);
        feedbackissuepanel.add(feedbackcreateissuebutton);
        feedbackcreateissuebutton.setForeground(PublicValues.globalFontColor);
        feedbackgithubbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.feedback.github.open")));
        feedbackgithubbutton.setBounds(466, 355, 250, 55);
        feedbackpane.add(feedbackgithubbutton);
        feedbackgithubbutton.setForeground(PublicValues.globalFontColor);
        feedbackupdatespanel = (JPanel) JComponentFactory.createJComponent(new JPanel());
        feedbackupdatespanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.updater.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        feedbackupdatespanel.setBounds(10, 59, 566, 249);
        feedbackpane.add(feedbackupdatespanel);
        feedbackupdatespanel.setLayout(null);
        feedbackupdaterversionfield = (JTextField) JComponentFactory.createJComponent(new JTextField());
        feedbackupdaterversionfield.setBounds(10, 85, 230, 20);
        feedbackupdatespanel.add(feedbackupdaterversionfield);
        feedbackupdaterversionfield.setColumns(10);
        feedbackwillbemovedlabel = (JLabel) JComponentFactory.createJComponent(new JLabel("The Updater will be moved to an other place"));
        feedbackwillbemovedlabel.setBounds(10, 29, 327, 14);
        feedbackupdatespanel.add(feedbackwillbemovedlabel);
        feedbackwillbemovedlabel.setForeground(PublicValues.globalFontColor);
        feedbackupdaterdownloadbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.updater.downloadnewest")));
        feedbackupdaterdownloadbutton.setBounds(10, 149, 230, 23);
        feedbackupdatespanel.add(feedbackupdaterdownloadbutton);
        feedbackupdaterdownloadbutton.setForeground(PublicValues.globalFontColor);
        feedbackupdaterversionfield.setEditable(false);
        feedbackupdaterdownloadbutton.addActionListener(e -> ConnectionUtils.openBrowser(new Updater().updateAvailable().url));
        feedbackgithubbutton.addActionListener(e -> ConnectionUtils.openBrowser("https://github.com/werwolf2303/SpotifyXP"));
        feedbackviewissuesbutton.addActionListener(e -> ConnectionUtils.openBrowser("https://github.com/werwolf2303/SpotifyXP/issues"));
        feedbackcreateissuebutton.addActionListener(e -> ConnectionUtils.openBrowser("https://github.com/werwolf2303/SpotifyXP/issues/new"));
    }

    @SuppressWarnings("all")
    void createLegacy() {
        JFrame dialog = new JFrame();
        playerarea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    if (!dialog.isVisible()) {
                        playerareawidth = playerarea.getWidth();
                        playerareaheight = playerarea.getHeight();
                        windowWasOpened = true;
                        frame.getContentPane().remove(playerarea);
                        frame.repaint();
                        frame.getContentPane().repaint();
                        dialog.setTitle(PublicValues.language.translate("ui.player.dialog.title"));
                        dialog.setContentPane(playerarea);
                        dialog.setPreferredSize(new Dimension(338, 359));
                        dialog.setVisible(true);
                        dialog.pack();
                        changePlayerToWindowStyle();
                    } else {
                        windowWasOpened = false;
                        dialog.dispose();
                        playerarea.setBounds(784 / 2 - playerareawidth / 2, 8, playerareawidth, playerareaheight);
                        restoreDefaultPlayerStyle();
                        add(playerarea);
                        frame.repaint();
                        frame.getContentPane().repaint();
                    }
                }
            }
        });
        dialog.setResizable(false);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                windowWasOpened = false;
                dialog.dispose();
                playerarea.setBounds(784 / 2 - playerareawidth / 2, 8, playerareawidth, playerareaheight);
                restoreDefaultPlayerStyle();
                add(playerarea);
                frame.repaint();
                frame.getContentPane().repaint();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dialog.dispose();
            }
        });
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                if (windowWasOpened) {
                    dialog.setVisible(true);
                }
            }
        });
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
                if(!(PublicValues.theme instanceof DarkGreen)) {
                    return frame.getWidth() / legacyswitch.getTabCount() - 3;
                }
                return frame.getWidth() / legacyswitch.getTabCount();
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
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setHomeVisible();
                    break;
                case 1:
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setPlaylistsVisible();
                    break;
                case 2:
                    if (librarysonglist.getModel().getRowCount() == 0) {
                        librarythread.start();
                    }
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setLibraryVisible();
                    break;
                case 3:
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setSearchVisible();
                    break;
                case 4:
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setHotlistVisible();
                    break;
                case 5:
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setQueueVisible();
                    break;
                case 6:
                    preventBugLegacySwitch();
                    legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                    setFeedbackVisible();
                    break;
                default:
                    GraphicalMessage.bug("Legacy JTabbedPane changeListener");
                    break;
            }
        });
        JMenu file = new JMenu(PublicValues.language.translate("ui.legacy.file"));
        JMenu edit = new JMenu(PublicValues.language.translate("ui.legacy.edit"));
        JMenu view = new JMenu(PublicValues.language.translate("ui.legacy.view"));
        JMenu lastfm = new JMenu("Last.fm");
        JMenu account = new JMenu(PublicValues.language.translate("ui.legacy.account"));
        JMenu help = new JMenu(PublicValues.language.translate("ui.legacy.help"));
        JMenuItem exit = new JMenuItem(PublicValues.language.translate("ui.legacy.exit"));
        JMenuItem logout = new JMenuItem(PublicValues.language.translate("ui.legacy.logout"));
        JMenuItem about = new JMenuItem(PublicValues.language.translate("ui.legacy.about"));
        JMenuItem settings = new JMenuItem(PublicValues.language.translate("ui.legacy.settings"));
        JMenuItem extensions = new JMenuItem(PublicValues.language.translate("ui.legacy.extensionstore"));
        JMenuItem audiovisualizer = new JMenuItem(PublicValues.language.translate("ui.legacy.view.audiovisualizer"));
        JMenuItem playuri = new JMenuItem(PublicValues.language.translate("ui.legacy.playuri"));
        JMenuItem lastfmdashboard = new JMenuItem("Dashboard");
        bar.add(file);
        bar.add(edit);
        bar.add(view);
        bar.add(lastfm);
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
        lastfm.add(lastfmdashboard);
        account.add(logout);
        help.add(extensions);
        help.add(about);
        audiovisualizer.addActionListener(e -> PublicValues.visualizer.open());
        extensions.addActionListener(e -> new InjectorStore().open());
        settings.addActionListener(new ActionListener() {
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
        });
        logout.addActionListener(e -> {
            PublicValues.config.write(ConfigValues.username.name, "");
            PublicValues.config.write(ConfigValues.password.name, "");
            JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.logout.text"), PublicValues.language.translate("ui.logout.title"), JOptionPane.OK_CANCEL_OPTION);
            System.exit(0);
        });
        about.addActionListener(e -> openAbout());
        exit.addActionListener(e -> System.exit(0));
        playuri.addActionListener(e -> {
            String uri = JOptionPane.showInputDialog(frame, PublicValues.language.translate("ui.playtrackuri.message"), PublicValues.language.translate("ui.playtrackuri.title"), JOptionPane.PLAIN_MESSAGE);
            PublicValues.spotifyplayer.load(uri, true, false, false);
            Events.INTERNALtriggerQueueUpdateEvents();
        });
        lastfmdashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(LastFMDialog.isOpen()) {
                    return;
                }
                LastFMDialog.openWhenLoggedIn();
            }
        });
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
                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (e.getClickCount() == 2) {
                            items.get(table.getSelectedRow()).doClick();
                        }
                    }
                });
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
        advancedsongpanel = (JPanel) JComponentFactory.createJComponent(new JPanel());
        advancedsongpanel.setBounds(0, 0, 784, 421);
        tabpanel.add(advancedsongpanel);
        advancedsongpanel.setLayout(null);
        advancedbackbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.back")));
        advancedbackbutton.setBounds(0, 0, 89, 23);
        advancedsongpanel.add(advancedbackbutton);
        advancedbackbutton.setForeground(PublicValues.globalFontColor);
        advancedsongpanel.setVisible(false);
        advancedbackbutton.addActionListener(e -> {
            advancedsongpanel.setVisible(false);
            homepane.getComponent().setVisible(true);
            ContentPanel.enableTabSwitch();
        });
        advancedsongtable = (DefTable) JComponentFactory.createJComponent(new DefTable());
        advancedsongtable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")}));
        advancedsongtable.setForeground(PublicValues.globalFontColor);
        advancedsongtable.getTableHeader().setForeground(PublicValues.globalFontColor);
        advancedscrollpanel = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        advancedscrollpanel.setBounds(0, 22, 784, 399);
        advancedsongpanel.add(advancedscrollpanel);
        advancedscrollpanel.setViewportView(advancedsongtable);
        advancedsongtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    player.getPlayer().load(advanceduricache.get(advancedsongtable.getSelectedRow()), true, shuffle, false);
                    advancedsongtable.setColumnSelectionInterval(0, advancedsongtable.getColumnCount() - 1);
                    TrackUtils.addAllToQueue(advanceduricache, advancedsongtable);
                }
            }
        });
    }

    void parseLastPlayState() {
        try {
            LastPlayState state = new LastPlayState();
            File playstate = new File(PublicValues.fileslocation, "play.state");
            Scanner scan = new Scanner(playstate);
            int read = 0;
            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                switch (read) {
                    case 0:
                        state.uri = data;
                        break;
                    case 1:
                        state.playerslider = data;
                        break;
                    case 2:
                        state.playtime = data;
                        break;
                    case 3:
                        state.playtimetotal = data;
                        break;
                    case 4:
                        state.playerslidermax = data;
                        break;
                    case 5:
                        state.playervolume = data;
                        break;
                }
                read++;
            }
            String out = IOUtils.toString(new FileInputStream(new File(PublicValues.fileslocation, "play.state")), Charset.defaultCharset());
            for(int i = 0; i < out.split("\n").length; i++) {
                if(i > 5) {
                    state.queue.add(out.split("\n")[i]);
                }
            }
            scan.close();
            lastPlayState = state;
        } catch (FileNotFoundException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // These are the positions for the popup window
    void changePlayerToWindowStyle() {
        playerareashufflebutton.getJComponent().setBounds(20, 293, 20, 20);
        playerarearepeatingbutton.getJComponent().setBounds(280, 293, 20, 20);
        playertitle.setHorizontalAlignment(SwingConstants.CENTER);
        playerarealyricsbutton.getJComponent().setBounds(20, 232, 14, 14);
        playerdescription.setHorizontalAlignment(SwingConstants.CENTER);
        playerarea.add(playerdescription);
        playerimage.setBounds(91, 11, 132, 130);
        playertitle.setBounds(10, 166, 301, 14);
        playerdescription.setBounds(10, 184, 300, 20);
        playerplaypreviousbutton.setBounds(47, 222, 70, 36);
        playerplaypausebutton.setBounds(129, 222, 69, 36);
        playerplaynextbutton.setBounds(208, 222, 69, 36);
        playercurrenttime.setBounds(62, 269, 200, 13);
        playerplaytime.setBounds(0, 269, 57, 14);
        playerplaytimetotal.setBounds(262, 269, 49, 14);
        playerareavolumeicon.getJComponent().setBounds(62, 293, 14, 14);
        playerareavolumeslider.setBounds(87, 293, 145, 13);
        playerareavolumecurrent.setBounds(242, 293, 35, 14);
        heart.getJComponent().setBounds(286, 229, 24, 24);
    }

    // These are the positions for the normal player style
    void restoreDefaultPlayerStyle() {
        playerarearepeatingbutton.getJComponent().setBounds(540, 75, 20, 20);
        playerareashufflebutton.getJComponent().setBounds(510, 75, 20, 20);
        playertitle.setHorizontalAlignment(SwingConstants.LEFT);
        playerdescription.setHorizontalAlignment(SwingConstants.LEFT);
        playerimage.setBounds(10, 11, 78, 78);
        playertitle.setBounds(109, 11, 168, 14);
        playerarealyricsbutton.getJComponent().setBounds(280, 75, 14, 14);
        playerdescription.setBounds(109, 40, 138, 20);
        playerplaypreviousbutton.setBounds(287, 11, 70, 36);
        playerplaypausebutton.setBounds(369, 11, 69, 36);
        playerplaynextbutton.setBounds(448, 11, 69, 36);
        playercurrenttime.setBounds(306, 54, 200, 13);
        playerplaytime.setBounds(244, 54, 57, 14);
        playerplaytimetotal.setBounds(506, 54, 49, 14);
        playerareavolumeicon.getJComponent().setBounds(306, 75, 14, 14);
        playerareavolumeslider.setBounds(334, 76, 145, 13);
        playerareavolumecurrent.setBounds(489, 75, 35, 14);
        heart.getJComponent().setBounds(525, 20, 24, 24);
    }

    void fetchOnlyFirstSongsFromUserLibrary() {
        DefaultTableModel model = (DefaultTableModel) librarysonglist.getModel();
        try {
            int count = 0;
            for (SavedTrack track : Factory.getSpotifyApi().getUsersSavedTracks().limit(10).build().execute().getItems()) {
                if (!libraryuricache.contains(track.getTrack().getUri())) {
                    String a = TrackUtils.getArtists(track.getTrack().getArtists());
                    model.insertRow(count, new Object[]{track.getTrack().getName() + " - " + a, TrackUtils.calculateFileSizeKb(track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                    libraryuricache.add(count, track.getTrack().getUri());
                    count++;
                }
            }
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            ConsoleLogging.Throwable(e);
        }
    }

    public void setLibraryVisible() {
        lastmenu = LastTypes.Library;
        librarypane.setVisible(true);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = true;
        hotlistVisible = false;
        feedbackVisible = false;
    }

    public void setSearchVisible() {
        lastmenu = LastTypes.Search;
        librarypane.setVisible(false);
        searchpane.setVisible(true);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
        homepane.getComponent().setVisible(false);
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
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
    }

    public void setHotlistVisible() {
        lastmenu = LastTypes.HotList;
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(true);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = true;
        feedbackVisible = false;
        if (hotlistplayliststable.getRowCount() == 0) {
            Thread t = new Thread(this::fetchHotlist);
            t.start();
        }
    }

    public void setFeedbackVisible() {
        lastmenu = LastTypes.Feedback;
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(true);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = true;
    }

    public void setPlaylistsVisible() {
        lastmenu = LastTypes.Playlists;
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(true);
        queuepane.setVisible(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = false;
        playlistsVisible = true;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
        DefThread thread = new DefThread(() -> {
            try {
                int total = Factory.getSpotifyApi().getListOfCurrentUsersPlaylists().build().execute().getTotal();
                int parsed = 0;
                int counter = 0;
                int last = 0;
                int offset = 0;
                while (parsed != total) {
                    PlaylistSimplified[] playlists = Factory.getSpotifyApi().getListOfCurrentUsersPlaylists().offset(offset).limit(50).build().execute().getItems();
                    for (PlaylistSimplified simplified : playlists) {
                        playlistsuricache.add(simplified.getUri());
                        ((DefaultTableModel) playlistsplayliststable.getModel()).addRow(new Object[]{simplified.getName()});
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
        });
        if (playlistsplayliststable.getModel().getRowCount() == 0) {
            thread.start();
        }
    }

    public void setQueueVisible() {
        lastmenu = LastTypes.Queue;
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(true);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        queueVisible = true;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
        if (queuelistmodel.isEmpty()) {
            ((DefaultListModel<?>) queuelist.getModel()).removeAllElements();
            queueuricache.clear();
            DefThread queueworker = new DefThread(() -> {
                try {
                    for (ContextTrackOuterClass.ContextTrack track : player.getPlayer().tracks(true).next) {
                        Track t = Factory.getSpotifyApi().getTrack(track.getUri().split(":")[2]).build().execute();
                        String a = TrackUtils.getArtists(t.getArtists());
                        queueuricache.add(track.getUri());
                        queuelistmodel.addElement(t.getName() + " - " + a);
                    }
                } catch (IOException | SpotifyWebApiException | ParseException ex) {
                    ConsoleLogging.Throwable(ex);
                } catch (NullPointerException exc) {
                    // Nothing in queue
                }
            });
            queueworker.start();
        }
    }

    public void setHomeVisible() {
        lastmenu = LastTypes.Home;
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
        homepane.getComponent().setVisible(true);
        homeVisible = true;
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
    }

    String[] getRandomValues(ArrayList<String> inputList, int count) {
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

    void fetchHotlist() {
        try {
            Artist[] artist = Factory.getSpotifyApi().getUsersTopArtists().build().execute().getItems();
            PlayHistory[] history = Factory.getSpotifyApi().getCurrentUsersRecentlyPlayedTracks().limit(10).build().execute().getItems();
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
            TrackSimplified[] finaltracks = Factory.getSpotifyApi().getRecommendations().seed_artists(Arrays.toString(fiveartists).replace("  ", ",").replace(" ", "")).seed_genres(Arrays.toString(fivegenres).replace("  ", ",").replace(" ", "")).seed_tracks(Arrays.toString(fivetracks).replace("  ", ",").replace(" ", "")).build().execute().getTracks();
            for (TrackSimplified t : finaltracks) {
                try {
                    AlbumSimplified albumreq = Factory.getSpotifyApi().getTrack(t.getUri().split(":")[2]).build().execute().getAlbum();
                    String a = TrackUtils.getArtists(albumreq.getArtists());
                    ((DefaultTableModel) hotlistplayliststable.getModel()).addRow(new Object[]{albumreq.getName() + " - " + a});
                    hotlistplaylistlistcache.add(albumreq.getUri());
                } catch (IOException | ParseException | SpotifyWebApiException e) {
                    ConsoleLogging.Throwable(e);
                }
            }
        } catch (Exception exception) {
            ExceptionDialog.open(exception);
        }
    }

    public void open() {
        PublicValues.contentPanel = this;
        JFrame2 mainframe;
        mainframe = frame;
        try {
            mainframe.setIconImage(ImageIO.read(new Resources(false).readToInputStream("spotifyxp.png")));
        } catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        }
        if(!steamdeck) {
            mainframe.setJMenuBar(copyJMenuBar(PublicValues.menuBar));
        }
        mainframe.setPreferredSize(new Dimension(784, 590));
        mainframe.getContentPane().add(this);
        mainframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainframe.dispose();
            }
        });
        mainframe.setForeground(Color.blue);
        Events.INTERNALtriggerOnFrameReadyEvents();
        mainframe.open();
        int w = Toolkit.getDefaultToolkit().getScreenSize().width;
        int h = Toolkit.getDefaultToolkit().getScreenSize().height;
        mainframe.setLocation(w / 2 - mainframe.getWidth() / 2, h / 2 - mainframe.getHeight() / 2);
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        } catch (InterruptedException ignored) {
        }
        mainframe.requestFocus();
        mainframe.setAlwaysOnTop(false);
        JComponentFactory.applyDPI();
        // JComponentFactory.enableResizing();
    }

    enum LastTypes {Playlists, Library, Search, HotList, Queue, Feedback, Home}

    static class LastPlayState {
        public String uri;
        public String playtimetotal;
        public String playtime;
        public String playerslider;
        public String playerslidermax;
        public String playervolume;
        public final ArrayList<String> queue = new ArrayList<>();
    }

    private JMenuBar copyJMenuBar(JMenuBar originalMenuBar) {
        JMenuBar newMenuBar = new JMenuBar();
        for (int i = 0; i < originalMenuBar.getMenuCount(); i++) {
            JMenu originalMenu = originalMenuBar.getMenu(i);
            JMenu newMenu = new JMenu(originalMenu.getText());
            for (int j = 0; j < originalMenu.getItemCount(); j++) {
                JMenuItem originalMenuItem = originalMenu.getItem(j);
                JMenuItem newMenuItem = new JMenuItem(originalMenuItem.getText());
                for(ActionListener listener : originalMenuItem.getActionListeners()) {
                    newMenuItem.addActionListener(listener);
                }
                newMenu.add(newMenuItem);
            }
            newMenuBar.add(newMenu);
        }
        return newMenuBar;
    }
}