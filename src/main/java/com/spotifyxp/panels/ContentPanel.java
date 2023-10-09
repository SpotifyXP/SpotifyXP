package com.spotifyxp.panels;

import com.neovisionaries.i18n.CountryCode;
import com.spotifyxp.api.Player;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.*;
import com.spotifyxp.dialogs.HTMLDialog;
import com.spotifyxp.dialogs.LyricsDialog;
import com.spotifyxp.dpi.JComponentFactory;
import com.spotifyxp.dummy.DummyCanvasPlayer;
import com.spotifyxp.engine.EnginePanel;
import com.spotifyxp.events.LoggerEvent;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.injector.InjectingPoints;
import com.spotifyxp.injector.InjectorStore;
import com.spotifyxp.lastfm.LastFMDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.*;
import com.spotifyxp.theming.themes.DarkGreen;
import com.spotifyxp.theming.themes.Legacy;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.PublicValues;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.*;
import com.spotifyxp.video.CanvasPlayer;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"CanBeFinal", "rawtypes", "Convert2Lambda"})
public class ContentPanel extends JPanel {
    public static JTable librarysonglist;
    public static Player player = null;
    public static JTable searchsonglist;
    public static JTextField searchartistfield;
    public static JTextField searchsongtitlefield;
    public static JTable hotlistplayliststable;
    public static JTable hotlistsongstable;
    public static JTable playlistsplayliststable;
    public static JTable playlistssongtable;
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
    public static JToggleButton playlistsbutton;
    public static JToggleButton librarybutton;
    public static JToggleButton searchbutton;
    public static JToggleButton hotlistbutton;
    public static JToggleButton queuebutton;
    public static JToggleButton feedbackbutton;
    public static JToggleButton homebutton;
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
    public static JList queuelist;
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
    public static JTabbedPane legacyswitch = (JTabbedPane) JComponentFactory.createJComponent(new JTabbedPane());  //For Legacy theme
    public static JMenuBar bar = (JMenuBar) JComponentFactory.createJComponent(new JMenuBar()); //For Legacy theme
    public static JSVGPanel heart;
    public static ArrayList<String> searchsonglistcache = new ArrayList<String>();
    public static ArrayList<String> hotlistplaylistlistcache = new ArrayList<String>();
    public static ArrayList<String> hotlistsonglistcache = new ArrayList<String>();
    public static ArrayList<String> libraryuricache = new ArrayList<String>();
    public static ArrayList<String> queueuricache = new ArrayList<String>();
    public static DefaultListModel<String> queuelistmodel = new DefaultListModel<>();
    public static ArrayList<String> playlistsuricache = new ArrayList<String>();
    public static ArrayList<String> playlistssonguricache = new ArrayList<String>();
    public static ArrayList<String> searchplaylistsongscache = new ArrayList<String>();
    public static JSVGPanel userbutton;
    public static JSVGPanel settingsbutton;
    public static JSVGPanel threepointbutton;
    public static DropDownMenu userdropdown;
    public static DropDownMenu threepointdropdown;
    public static JSVGPanel playerareavolumeicon;
    public static JSlider playerareavolumeslider;
    public static JLabel playerareavolumecurrent;
    public static JPanel searchplaylistpanel;
    public static JButton searchbackbutton;
    public static JTable searchplaylisttable;
    public static JRadioButton searchfilterplaylist;
    public static JRadioButton searchfilteralbum;
    public static JRadioButton searchfiltershow;
    public static JRadioButton searchfiltertrack;
    public static JScrollPane searchplaylistscrollpanel;
    public static JRadioButton searchfilterartist;
    public static ArtistPanel artistPanel;
    public static boolean isLastArtist = false;
    public static CountryCode countryCode;
    public static JTable advancedsongtable;
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
    private static int libraryOffset = 28;
    private static boolean libraryLoadingInProgress = false;
    public static void loadNext() {
        if(libraryLoadingInProgress) {
            return;
        }
        libraryLoadingInProgress = true;
        int visibleCount = 19;
        JSONObject list = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/tracks", new NameValuePair[]{new NameValuePair("limit", String.valueOf(visibleCount)), new NameValuePair("offset", String.valueOf(libraryOffset))}));
        if(list.getInt("total") > libraryOffset) {
            for (Object o : list.getJSONArray("items")) {
                libraryuricache.add(new JSONObject(new JSONObject(o.toString()).getJSONObject("track").toString()).getString("uri"));
            }
            int i = 0;
            for (String s : libraryuricache) {
                if (i != libraryOffset) {
                    i++;
                    continue;
                }
                try {
                    Track track = Factory.getSpotifyApi().getTrack(s.split(":")[2]).build().execute();
                    String a = TrackUtils.getArtists(track.getArtists());
                    ((DefaultTableModel) librarysonglist.getModel()).addRow(new Object[]{track.getName() + " - " + a, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            libraryOffset += 19;
        }
        libraryLoadingInProgress = false;
    }
    static boolean steamdeck = false;
    public static DefThread librarythread = new DefThread(new Runnable() {
        public void run() {
            libraryLoadingInProgress = true;
            int visibleCount = 28;
            JSONObject list = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/tracks", new NameValuePair[]{new NameValuePair("limit", String.valueOf(visibleCount))}));
            for(Object o : list.getJSONArray("items")) {
                libraryuricache.add(new JSONObject(new JSONObject(o.toString()).getJSONObject("track").toString()).getString("uri"));
            }
            for(String s : libraryuricache) {
                try {
                    Track track = Factory.getSpotifyApi().getTrack(s.split(":")[2]).build().execute();
                    String a = TrackUtils.getArtists(track.getArtists());
                    ((DefaultTableModel)librarysonglist.getModel()).addRow(new Object[]{track.getName() + " - " + a, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
                } catch (IOException | ParseException | SpotifyWebApiException e) {
                    hotlistbutton.setEnabled(true);
                    searchbutton.setEnabled(true);
                    queuebutton.setEnabled(true);
                    playlistsbutton.setEnabled(true);
                }
            }
            libraryLoadingInProgress = false;
        }
    });
    public static boolean shuffle = false;
    enum LastTypes {
        Playlists,
        Library,
        Search,
        HotList,
        Queue,
        Feedback,
        Home
    }
    public static LastTypes lastmenu = LastTypes.HotList;
    void createSettingsButton() {
        settingsbutton = new JSVGPanel();
        settingsbutton.getJComponent().setBounds(669, 11, 23, 23);
        add(settingsbutton.getJComponent());

        settingsbutton.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!settingsPanel.isVisible()) {
                    setNothingVisible();
                    settingsPanel.setVisible(true);
                    settingsbutton.setRotation(10);
                }else {
                    SettingsPanel.applySettings();
                    settingsPanel.setVisible(false);
                    switch (lastmenu) {
                        case Queue:
                            setQueueVisible();
                            setButtonsVisible();
                            break;
                        case Search:
                            setSearchVisible();
                            setButtonsVisible();
                            break;
                        case Library:
                            setLibraryVisible();
                            setButtonsVisible();
                            break;
                        case Feedback:
                            setFeedbackVisible();
                            setButtonsVisible();
                            break;
                        case Playlists:
                            setPlaylistsVisible();
                            setButtonsVisible();
                            break;
                        case HotList:
                            setHotlistVisible();
                            setButtonsVisible();
                            break;
                    }
                    playerarea.setVisible(true);
                    setButtonsVisible();
                    settingsbutton.setRotation(0);
                }
            }
        });

        JComponentFactory.addJComponent(settingsbutton.getJComponent());
    }
    public static void blockTabSwitch() {
        if(PublicValues.theme.hasLegacyUI()) {
            legacyswitch.setEnabled(false);
        }else{
            makeButtonsHidden();
        }
    }
    public static void enableTabSwitch() {
        if(PublicValues.theme.hasLegacyUI()) {
            legacyswitch.setEnabled(true);
        }else{
            makeButtonsVisible();
        }
    }

    void createUserButton() {
        userbutton = new JSVGPanel();
        userbutton.getJComponent().setBounds(702, 11, 23, 23);
        add(userbutton.getJComponent());

        userdropdown = new DropDownMenu(userbutton, false);
        userdropdown.addItem(PublicValues.language.translate("ui.menu.logout"), new Runnable() {
            @Override
            public void run() {
                PublicValues.config.write(ConfigValues.username.name, "");
                PublicValues.config.write(ConfigValues.password.name, "");
                JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.logout.text"), PublicValues.language.translate("ui.logout.title"), JOptionPane.OK_CANCEL_OPTION);
                System.exit(0);
            }
        });
        JComponentFactory.addJComponent(userbutton.getJComponent());
    }
    void createThreePointButton() {
        threepointbutton = new JSVGPanel();
        threepointbutton.getJComponent().setBounds(735, 11, 23, 23);
        add(threepointbutton.getJComponent());

        threepointdropdown = new DropDownMenu(threepointbutton, false);
        threepointdropdown.addItem(PublicValues.language.translate("ui.menu.help.about"), new Runnable() {
            @Override
            public void run() {
                openAbout();
            }
        });
        threepointdropdown.addItem(PublicValues.language.translate("ui.menu.file.exit"), new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        });
        JComponentFactory.addJComponent(threepointbutton.getJComponent());
    }
    void openAbout() {
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
            for(String s : out.split("\n")) {
                if(s.contains("(TRANSLATE)")) {
                    s = s.replace(s.split("\\(TRANSLATE\\)")[1].replace("(TRANSLATE)", ""), PublicValues.language.translate(s.split("\\(TRANSLATE\\)")[1].replace("(TRANSLATE)", "")));
                    s = s.replace("(TRANSLATE)", "");
                }
                cache.append(s);
            }
            String opensourcelist = URLUtils.getURLResponseAsString("https://raw.githubusercontent.com/werwolf2303/SpotifyXP/main/opensourcelist.txt");
            StringBuilder finalhtml = new StringBuilder();
            finalhtml.append(cache.toString().split("<insertOpenSourceList>")[0]);
            finalhtml.append(opensourcelist);
            finalhtml.append(cache.toString().split("</insertOpenSourceList>")[1]);
            dialog.open(frame, PublicValues.language.translate("ui.menu.help.about"), finalhtml.toString());
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
        homebutton = new JToggleButton(PublicValues.language.translate("ui.navigation.home"));
        homebutton.setBounds(5, 111, 107, 23);
        homebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setHomeVisible();
            }
        });
        add(homebutton);
    }
    void createPlayerArea() {
        playerarea = new EnginePanel();
        playerarea.setBounds(72, 0, 565, 100);
        add(playerarea);
        playerarea.setLayout(null);

        playerareashufflebutton = new JSVGPanel();
        playerareashufflebutton.getJComponent().setBounds(510, 75, 20, 20);
        //playerarea.add(playerareashufflebutton.getJComponent());

        JComponentFactory.addJComponent(playerareashufflebutton.getJComponent());

        playerarearepeatingbutton = new JSVGPanel();
        playerarearepeatingbutton.getJComponent().setBounds(540, 75, 20, 20);
        playerarea.add(playerarearepeatingbutton.getJComponent());

        playerareashufflebutton.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(playerareashufflebutton.isFilled) {
                    shuffle = false;
                    player.getPlayer().setShuffle(false);
                    try {
                        PublicValues.spotifyplayer.tracks(true).next.clear();
                        for(String s : Shuffle.before) {
                            PublicValues.spotifyplayer.addToQueue(s);
                        }
                    }catch (Exception e2) {
                        ConsoleLogging.Throwable(e2);
                        ExceptionDialog.open(e2);
                    }
                    if(!PublicValues.theme.isLight()) {
                        playerareashufflebutton.setImage(new Resources().readToInputStream("icons/shufflewhite.svg"));
                    }else{
                        playerareashufflebutton.setImage(new Resources().readToInputStream("icons/shuffledark.svg"));
                    }
                    playerareashufflebutton.isFilled = false;
                }else{
                    shuffle = true;
                    player.getPlayer().setShuffle(true);
                    Shuffle.makeShuffle();
                    playerareashufflebutton.isFilled = true;
                    playerareashufflebutton.setImage(new Resources().readToInputStream("icons/shuffleselected.svg"));
                }
            }
        });
        playerarearepeatingbutton.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(playerarearepeatingbutton.isFilled) {
                    player.getPlayer().setRepeat(false, false);
                    if(!PublicValues.theme.isLight()) {
                        playerarearepeatingbutton.setImage(new Resources().readToInputStream("icons/repeatwhite.svg"));
                    }else{
                        playerarearepeatingbutton.setImage(new Resources().readToInputStream("icons/repeatdark.svg"));
                    }
                    playerarearepeatingbutton.isFilled = false;
                }else{
                    player.getPlayer().setRepeat(true, false);
                    playerarearepeatingbutton.isFilled = true;
                    playerarearepeatingbutton.setImage(new Resources().readToInputStream("icons/repeatselected.svg"));
                }
            }
        });

        JComponentFactory.addJComponent(playerarearepeatingbutton.getJComponent());

        playerimage = new JImagePanel();
        playerimage.setBounds(10, 11, 78, 78);
        playerarea.add(playerimage);

        playerarealyricsbutton = new JSVGPanel();
        playerarealyricsbutton.getJComponent().setBounds(280, 75, 14, 14);
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
                        if(PublicValues.theme.isLight()) {
                            playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonedark.svg"));
                        }else{
                            playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonewhite.svg"));
                        }
                        playerarealyricsbutton.isFilled = false;
                    } else {
                        if(PublicValues.lyricsDialog.open(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri())) {
                            playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphoneselected.svg"));
                            playerarealyricsbutton.isFilled = true;
                        }
                    }
                }catch (NullPointerException e2) {
                    if(PublicValues.theme.isLight()) {
                        playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonedark.svg"));
                    }else{
                        playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonewhite.svg"));
                    }
                    playerarealyricsbutton.isFilled = false;
                }
            }
        });

        JComponentFactory.addJComponent(playerarealyricsbutton.getJComponent());

        playerareavolumeicon = new JSVGPanel();
        if(PublicValues.theme instanceof Legacy) {
            playerareavolumeicon.setSVG(false);
        }
        playerareavolumeicon.getJComponent().setBounds(306, 75, 14, 14);
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

        playerareavolumeslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(PublicValues.theme instanceof Legacy) {
                    playerareavolumecurrent.setText(String.valueOf(playerareavolumeslider.getValue()));
                    if (playerareavolumeslider.getValue() == 0) {
                        playerareavolumeicon.setImage(new Resources().readToInputStream("legacyicons/volumemuted.png"));
                    } else {
                        playerareavolumeicon.setImage(new Resources().readToInputStream("legacyicons/volumefull.png"));
                    }
                }else {
                    if (PublicValues.theme.isLight()) {
                        playerareavolumecurrent.setText(String.valueOf(playerareavolumeslider.getValue()));
                        if (playerareavolumeslider.getValue() == 0) {
                            playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumemutedark.svg"));
                        } else {
                            if (playerareavolumeslider.getValue() == 10) {
                                playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumefulldark.svg"));
                            } else {
                                if (playerareavolumeslider.getValue() < 10 && playerareavolumeslider.getValue() > 4) {
                                    playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumehalfdark.svg"));
                                }
                            }
                        }
                    } else {
                        playerareavolumecurrent.setText(String.valueOf(playerareavolumeslider.getValue()));
                        if (playerareavolumeslider.getValue() == 0) {
                            playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumemutewhite.svg"));
                        } else {
                            if (playerareavolumeslider.getValue() == 10) {
                                playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumefullwhite.svg"));
                            } else {
                                if (playerareavolumeslider.getValue() < 10 && playerareavolumeslider.getValue() > 4) {
                                    playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumehalfwhite.svg"));
                                }
                            }
                        }
                    }
                }
                switch (playerareavolumeslider.getValue()) {
                    case 0:
                        player.getPlayer().setVolume(0);
                        break;
                    case 1:
                        player.getPlayer().setVolume(6553);
                        break;
                    case 2:
                        player.getPlayer().setVolume(13107);
                        break;
                    case 3:
                        player.getPlayer().setVolume(19660);
                        break;
                    case 4:
                        player.getPlayer().setVolume(26214);
                        break;
                    case 5:
                        player.getPlayer().setVolume(32768);
                        break;
                    case 6:
                        player.getPlayer().setVolume(39321);
                        break;
                    case 7:
                        player.getPlayer().setVolume(45875);
                        break;
                    case 8:
                        player.getPlayer().setVolume(52428);
                        break;
                    case 9:
                        player.getPlayer().setVolume(58982);
                        break;
                    case 10:
                        player.getPlayer().setVolume(65536);
                        break;
                }
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
        playerarea.add(playerplaypreviousbutton);

        playerplaypausebutton = (JImageButton) JComponentFactory.createJComponent(new JImageButton());
        playerplaypausebutton.setBounds(369, 11, 69, 36);
        playerplaypausebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.getPlayer().playPause();
            }
        });
        playerarea.add(playerplaypausebutton);

        playerplaynextbutton = (JImageButton) JComponentFactory.createJComponent(new JImageButton());
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
        heart.getJComponent().setBounds(525, 20, 24, 24);


        heart.getJComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(heart.isFilled) {
                    PublicValues.elevated.makeDelete("https://api.spotify.com/v1/me/tracks?ids=" + Objects.requireNonNull(player.getPlayer().currentPlayable()).toSpotifyUri().split(":")[2]);
                    heart.setImage(new Resources().readToInputStream("icons/heart.svg"));

                    heart.isFilled = false;
                }else {
                    PublicValues.elevated.makePut("https://api.spotify.com/v1/me/tracks?ids=" + Objects.requireNonNull(player.getPlayer().currentPlayable()).toSpotifyUri().split(":")[2]);
                    heart.setImage(new Resources().readToInputStream("icons/heartfilled.svg"));
                    heart.isFilled = true;
                }
            }
        });

        heart.setImage(new Resources().readToInputStream("icons/heart.svg"));

        JComponentFactory.addJComponent(heart.getJComponent());

        playerarea.add(heart.getJComponent());

        playercurrenttime.setForeground(PublicValues.globalFontColor);

        playercurrenttime.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                playerplaytime.setText(TrackUtils.getHHMMSSOfTrack(playercurrenttime.getValue()* 1000L));
            }
        });

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
                player.getPlayer().seek(playercurrenttime.getValue()*1000);
                player.getPlayer().play();
            }
        });
        playerplaynextbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.getPlayer().next();
            }
        });

        playerplaypreviousbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicValues.spotifyplayer.previous();
            }
        });
        playercurrenttime.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ContentPanel.playerplaytime.setText(TrackUtils.getHHMMSSOfTrack(player.getPlayer().time()));
            }
        });

        ContextMenu menu = new ContextMenu(playerarea);
        menu.addItem("Open Canvas", new Runnable() {
            @Override
            public void run() {
                DefThread thread = new DefThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (Objects.requireNonNull(PublicValues.spotifyplayer.currentMetadata()).isTrack() && !Objects.requireNonNull(PublicValues.spotifyplayer.currentMetadata()).getName().equals("")) {
                                PublicValues.canvasPlayer.show();
                                PublicValues.canvasPlayer.switchMedia(UnofficialSpotifyAPI.getCanvasURLForTrack(PublicValues.spotifyplayer.currentMetadata().getArtist(), PublicValues.spotifyplayer.currentMetadata().getAlbumName(), PublicValues.spotifyplayer.currentMetadata().getName()));
                                if(!PublicValues.spotifyplayer.isPaused()) {
                                    PublicValues.canvasPlayer.play();
                                }
                            }
                        }catch (Exception ignored) {
                        }
                    }
                });
                thread.start();
            }
        });
    }
    void createLibrary() {
        librarypane = (JPanel) JComponentFactory.createJComponent(new JPanel());
        librarypane.setBounds(0, 0, 784, 421);
        tabpanel.add(librarypane);
        librarypane.setLayout(null);

        librarybutton = (JToggleButton) JComponentFactory.createJComponent(new JToggleButton(PublicValues.language.translate("ui.navigation.library")));
        librarybutton.setBounds(230, 111, 107, 23);
        add(librarybutton);

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

        libraryscrollpane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(!inProg[0]) {
                    inProg[0] = true;
                    BoundedRangeModel m = (BoundedRangeModel) libraryscrollpane.getVerticalScrollBar().getModel();
                    int extent = m.getExtent();
                    int maximum = m.getMaximum();
                    int value = m.getValue();
                    if (value + extent >= maximum / 2) {
                        if (libraryVisble) {
                            if (!libraryLoadingInProgress) {
                                DefThread thread = new DefThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadNext();
                                    }
                                });
                                thread.start();
                            }
                        }
                    }
                    inProg[0] = false;
                }
            }
        });

        librarysonglist = (JTable) JComponentFactory.createJComponent(new DefTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        librarysonglist.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.library.songlist.songname"), PublicValues.language.translate("ui.library.songlist.filesize"), PublicValues.language.translate("ui.library.songlist.bitrate"), PublicValues.language.translate("ui.library.songlist.length")
                }
        ));
        librarysonglist.getTableHeader().setForeground(PublicValues.globalFontColor);
        librarysonglist.setForeground(PublicValues.globalFontColor);
        librarydefaulttablemodel = (DefaultTableModel) librarysonglist.getModel();
        librarysonglist.getColumnModel().getColumn(0).setPreferredWidth(347);
        librarysonglist.getColumnModel().getColumn(3).setPreferredWidth(51);
        librarysonglist.setFillsViewportHeight(true);
        libraryscrollpane.setViewportView(librarysonglist);
        librarybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLibraryVisible();
            }
        });
        librarybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(librarysonglist.getModel().getRowCount()==0) {
                    librarythread.start();
                }
            }
        });
        librarybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TrackUtils.addAllToQueue(libraryuricache, librarysonglist);
            }
        });
        libraryshufflebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefThread thread1 = new DefThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        player.getPlayer().load(libraryuricache.get(0), true, true, false);
                        for(ContextTrackOuterClass.ContextTrack track : player.getPlayer().tracks(true).next) {
                            player.getPlayer().removeFromQueue(track.getUri());
                        }
                        for(String s : libraryuricache) {
                            if(i==0) {
                                i++;
                                continue;
                            }
                            player.getPlayer().addToQueue(s);
                        }
                    }
                });
                thread1.start();
            }
        });

        librarysonglist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) {
                    player.getPlayer().load(libraryuricache.get(librarysonglist.getSelectedRow()), true, shuffle, true);
                    DefThread thread1 = new DefThread(new Runnable() {
                        @Override
                        public void run() {
                            TrackUtils.addAllToQueue(libraryuricache, librarysonglist);
                        }
                    });
                    thread1.start();
                }
            }
        });
        ContextMenu librarymenu = new ContextMenu(librarysonglist);
        librarymenu.addItem(PublicValues.language.translate("ui.general.copyuri"), new Runnable() {
            @Override
            public void run() {
                ClipboardUtil.set(libraryuricache.get(librarysonglist.getSelectedRow()));
            }
        });
        librarymenu.addItem(PublicValues.language.translate("ui.general.refresh"), new Runnable() {
            @Override
            public void run() {
                libraryuricache.clear();
                librarysonglist.removeAll();
                librarythread.start();
            }
        });
        librarymenu.addItem(PublicValues.language.translate("ui.general.remove"), new Runnable() {
            @Override
            public void run() {
                PublicValues.elevated.makeDelete("https://api.spotify.com/v1/me/tracks?ids=" + libraryuricache.get(librarysonglist.getSelectedRow()).split(":")[2]);
                libraryuricache.remove(librarysonglist.getSelectedRow());
                ((DefaultTableModel)librarysonglist.getModel()).removeRow(librarysonglist.getSelectedRow());
            }
        });
    }
    void createPlaylist() {
        playlistsbutton = (JToggleButton) JComponentFactory.createJComponent(new JToggleButton(PublicValues.language.translate("ui.navigation.playlists")));
        playlistsbutton.setBounds(118, 111, 107, 23);
        add(playlistsbutton);
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

        playlistsplayliststable = (JTable) JComponentFactory.createJComponent(new DefTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        playlistsplayliststable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.playlists.playlists.playlistname")
                }
        ));
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

        playlistssongtable = (JTable) JComponentFactory.createJComponent(new DefTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        playlistssongtable.getTableHeader().setForeground(PublicValues.globalFontColor);
        playlistssongtable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.playlists.songslist.songtitle"), PublicValues.language.translate("ui.playlists.songslist.filesize"), PublicValues.language.translate("ui.playlists.songslist.bitrate"), PublicValues.language.translate("ui.playlists.songslist.length")
                }
        ));
        playlistssongtable.setForeground(PublicValues.globalFontColor);
        playlistssongtable.getColumnModel().getColumn(0).setPreferredWidth(363);
        playlistssongtable.getColumnModel().getColumn(1).setPreferredWidth(89);
        playlistssongtable.getColumnModel().getColumn(3).setPreferredWidth(96);
        playlistssongtable.setFillsViewportHeight(true);
        playlistssongtable.setColumnSelectionAllowed(true);
        playlistssongsscroll.setViewportView(playlistssongtable);
        playlistsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefThread thread = new DefThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject list = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/playlists", new NameValuePair[]{new NameValuePair("limit", "50")}));
                        for(Object o : list.getJSONArray("items")) {
                            playlistsuricache.add(new JSONObject(o.toString()).getString("uri"));
                        }

                        boolean con = true;

                        while(con) {
                            try {
                                String url = list.getString("next");
                                list = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/playlists", new NameValuePair[]{new NameValuePair("offset", url.split("\\?")[1].split("&")[0].replace("offset=", "")), new NameValuePair("limit", url.split("\\?")[1].split("&")[1].replace("limit=", ""))}));
                                for (Object o : list.getJSONArray("items")) {
                                    playlistsuricache.add(new JSONObject(o.toString()).getString("uri"));
                                }
                            }catch (JSONException ignored) {
                            }
                            try {
                                if(list.isNull("next")) {
                                    con = false;
                                }
                            }catch (JSONException exc) {
                                con = false;
                            }
                        }
                        try {
                            for (String s : playlistsuricache) {
                                ((DefaultTableModel) playlistsplayliststable.getModel()).addRow(new Object[]{Factory.getSpotifyApi().getPlaylist(s.split(":")[2]).build().execute().getName()});
                            }
                        } catch (ParseException | IOException | SpotifyWebApiException ignored) {

                        }
                    }
                });
                if(playlistsplayliststable.getModel().getRowCount()==0) {
                    thread.start();
                }
            }
        });

        ContextMenu menu = new ContextMenu(playlistsplayliststable);
        menu.addItem(PublicValues.language.translate("ui.general.remove.playlist"), new Runnable() {
            @Override
            public void run() {
                Factory.getSpotifyApi().unfollowPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]);
                playlistsuricache.remove(playlistsuricache.get(playlistsplayliststable.getSelectedRow()));
                ((DefaultTableModel)playlistsplayliststable.getModel()).removeRow(playlistsplayliststable.getSelectedRow());
            }
        });

        menu.addItem(PublicValues.language.translate("ui.general.remove.playlist"), new Runnable() {
            @Override
            public void run() {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Clipboard clipboard = toolkit.getSystemClipboard();
                StringSelection strSel = new StringSelection(playlistsuricache.get(playlistsplayliststable.getSelectedRow()));
                clipboard.setContents(strSel, null);
            }
        });

        playlistsplayliststable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) {
                    DefThread thread = new DefThread(new Runnable() {
                        @Override
                        public void run() {
                            playlistssonguricache.clear();
                            ((DefaultTableModel)playlistssongtable.getModel()).setRowCount(0);
                            try {
                                Playlist pl = Factory.getSpotifyApi().getPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).build().execute();
                                if(pl.getTracks().getTotal() > 100) {
                                    int parsed = 0;
                                    int offset = 100;
                                    for (PlaylistTrack track : pl.getTracks().getItems()) {
                                        if(!playlistsplayliststable.isVisible()) {
                                            break;
                                        }
                                        playlistssonguricache.add(track.getTrack().getUri());
                                        ((DefaultTableModel)playlistssongtable.getModel()).addRow(new Object[] {track.getTrack().getName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                    }
                                    while(true) {
                                        if(!searchplaylisttable.isVisible()) {
                                            break;
                                        }
                                        if(parsed == pl.getTracks().getTotal()) {
                                            break;
                                        }
                                        Paging<PlaylistTrack> tracks = Factory.getSpotifyApi().getPlaylistsItems(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).offset(offset).build().execute();
                                        for (PlaylistTrack track : tracks.getItems()) {
                                            playlistssonguricache.add(track.getTrack().getUri());
                                            ((DefaultTableModel)playlistssongtable.getModel()).addRow(new Object[] {track.getTrack().getName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                            parsed++;
                                        }
                                        offset = offset + 100;
                                    }
                                }else {
                                    for (PlaylistTrack track : Factory.getSpotifyApi().getPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).build().execute().getTracks().getItems()) {
                                        playlistssonguricache.add(track.getTrack().getUri());
                                        ((DefaultTableModel)playlistssongtable.getModel()).addRow(new Object[] {track.getTrack().getName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                    }
                                }
                            } catch (IOException | ParseException | SpotifyWebApiException ex) {
                                ConsoleLogging.Throwable(ex);
                            }
                        }
                    });
                    thread.start();
                }
            }
        });

        playlistssongtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) {
                    player.getPlayer().load(playlistssonguricache.get(playlistssongtable.getSelectedRow()), true, shuffle, false);
                    TrackUtils.addAllToQueue(playlistssonguricache, playlistssongtable);
                }
            }
        });
        playlistsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPlaylistsVisible();
            }
        });
    }
    void createSearch() {
        searchbutton = (JToggleButton) JComponentFactory.createJComponent(new JToggleButton(PublicValues.language.translate("ui.navigation.search")));
        searchbutton.setBounds(338, 111, 107, 23);
        add(searchbutton);
        searchpane = (JPanel) JComponentFactory.createJComponent(new JPanel());
        searchpane.setBounds(0, 0, 784, 421);
        tabpanel.add(searchpane);
        searchpane.setLayout(null);
        searchbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSearchVisible();
            }
        });
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

        searchclearfieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchartistfield.setText("");
                searchsongtitlefield.setText("");
            }
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
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
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
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
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

        searchfilterartist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchfiltertrack.setSelected(false);
                searchfilteralbum.setSelected(false);
                searchfiltershow.setSelected(false);
                searchfilterplaylist.setSelected(false);
            }
        });

        searchfilteralbum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchfiltertrack.setSelected(false);
                searchfiltershow.setSelected(false);
                searchfilterplaylist.setSelected(false);
                searchfilterartist.setSelected(false);
            }
        });

        searchfilterplaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchfiltertrack.setSelected(false);
                searchfilteralbum.setSelected(false);
                searchfiltershow.setSelected(false);
                searchfilterartist.setSelected(false);
            }
        });

        searchfiltershow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchfiltertrack.setSelected(false);
                searchfilteralbum.setSelected(false);
                searchfilterplaylist.setSelected(false);
                searchfilterartist.setSelected(false);
            }
        });

        searchfiltertrack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchfilteralbum.setSelected(false);
                searchfiltershow.setSelected(false);
                searchfilterplaylist.setSelected(false);
                searchfilterartist.setSelected(false);
            }
        });
        searchfinditbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefThread thread1 = new DefThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean excludeExplicit = searchfilterexcludeexplicit.isSelected();
                        String searchartist = searchartistfield.getText();
                        String searchtitle = searchsongtitlefield.getText();
                        boolean track = searchfiltertrack.isSelected();
                        boolean artist = searchfilterartist.isSelected();
                        boolean album = searchfilteralbum.isSelected();
                        boolean show = searchfiltershow.isSelected();
                        boolean playlist = searchfilterplaylist.isSelected();
                        searchsonglistcache.clear();
                        ((DefaultTableModel)searchsonglist.getModel()).setRowCount(0);
                        if(searchtitle.equals("") && searchartist.equals("")) {
                            return; //User didn't type anything in so we just return
                        }
                        try {
                            if(track) {
                                for (Track t : Factory.getSpotifyApi().searchTracks(searchtitle + " " + searchartist).limit(50).build().execute().getItems()) {
                                    String artists = TrackUtils.getArtists(t.getArtists());
                                    if(!searchartist.equalsIgnoreCase("")) {
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
                            if(artist) {
                                artistPanel.artistalbumuricache.clear();
                                artistPanel.artistpopularuricache.clear();
                                if(searchtitle.equals("")) {
                                    searchtitle = searchartist;
                                }
                                for(Artist a : Factory.getSpotifyApi().searchArtists(searchtitle).build().execute().getItems()) {
                                    searchsonglistcache.add(a.getUri());
                                    Factory.getSpotifyAPI().addArtistToList(a, searchsonglist);
                                }
                            }
                            if(album) {
                                for(AlbumSimplified a : Factory.getSpotifyApi().searchAlbums(searchtitle).build().execute().getItems()) {
                                    if(!searchartist.equals("")) {
                                        if(!TrackUtils.trackHasArtist(a.getArtists(), searchartist, true)) {
                                            continue;
                                        }
                                    }
                                    searchsonglistcache.add(a.getUri());
                                    ((DefaultTableModel)searchsonglist.getModel()).addRow(new Object[] {a.getName()});
                                }
                            }
                            if(show) {
                                for(ShowSimplified s : Factory.getSpotifyApi().searchShows(searchtitle).build().execute().getItems()) {
                                    if(!searchartist.equals("")) {
                                        if(!s.getPublisher().equalsIgnoreCase(searchartist)) {
                                            continue;
                                        }
                                    }
                                    searchsonglistcache.add(s.getUri());
                                    ((DefaultTableModel)searchsonglist.getModel()).addRow(new Object[]{s.getName()});
                                }
                            }
                            if(playlist) {
                                for (PlaylistSimplified t : Factory.getSpotifyApi().searchPlaylists(searchtitle).build().execute().getItems()) {
                                    if(!searchartist.equals("")) {
                                        if(!t.getOwner().getDisplayName().equalsIgnoreCase(searchartist)) {
                                            continue;
                                        }
                                    }
                                    searchsonglistcache.add(t.getUri());
                                    Factory.getSpotifyAPI().addPlaylistToList(t, searchsonglist);
                                }
                            }
                        } catch (IOException | SpotifyWebApiException | ParseException ex) {
                            ConsoleLogging.Throwable(ex);
                        }
                    }
                });
                thread1.start();
            }
        });
        searchscrollpanel = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
        searchscrollpanel.setBounds(0, 139, 784, 282);
        searchpane.add(searchscrollpanel);

        searchsonglist = (JTable) JComponentFactory.createJComponent(new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        searchsonglist.getTableHeader().setReorderingAllowed(false);
        artistPanelBackButton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.back")));
        artistPanelBackButton.setBounds(0, 0, 89, 23);
        artistPanelBackButton.setForeground(PublicValues.globalFontColor);
        artistPanelBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(PublicValues.blockArtistPanelBackButton) {
                    return;
                }
                switch (lastmenu) {
                    case Search:
                        if(isLastArtist) {
                            ContentPanel.artistPanel.openPanel();
                            ContentPanel.artistPanel.isFirst = true;
                            ContentPanel.artistPanel.contentPanel.setVisible(true);
                            ContentPanel.searchplaylistpanel.setVisible(false);
                            artistPanelVisible = true;
                            isLastArtist = false;
                        }else {
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
                if(e.getClickCount()==2) {
                    switch (searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[1]) {
                        case "playlist":
                        case "album":
                        case "show":
                            searchpane.setVisible(false);
                            ContentPanel.blockTabSwitch();
                            searchplaylistpanel.setVisible(true);
                            searchplaylistsongscache.clear();
                            ((DefaultTableModel)searchplaylisttable.getModel()).setRowCount(0);
                            break;
                        case "artist":
                            artistPanel.artistpopularuricache.clear();
                            artistPanel.artistalbumuricache.clear();
                            ((DefaultTableModel)artistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                            ((DefaultTableModel)artistPanel.artistpopularsonglist.getModel()).setRowCount(0);
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
                    DefThread thread = new DefThread(new Runnable() {
                        @Override
                        public void run() {
                            if(player.getPlayer().isReady()) {
                                if(searchsonglistcache.get(searchsonglist.getSelectedRow()).contains("playlist")) {
                                    try {
                                        Playlist pl = Factory.getSpotifyApi().getPlaylist(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute();
                                        if(pl.getTracks().getTotal() > 100) {
                                            int parsed = 0;
                                            int offset = 100;
                                            for (PlaylistTrack track : pl.getTracks().getItems()) {
                                                if(!searchplaylisttable.isVisible()) {
                                                    break;
                                                }
                                                ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{track.getTrack().getName() + " - " + pl.getName() + " - " + pl.getOwner().getDisplayName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                                searchplaylistsongscache.add(track.getTrack().getUri());
                                                parsed++;
                                            }
                                            while(true) {
                                                if(!searchplaylisttable.isVisible()) {
                                                    break;
                                                }
                                                if(parsed == pl.getTracks().getTotal()) {
                                                    break;
                                                }
                                                Paging<PlaylistTrack> tracks = Factory.getSpotifyApi().getPlaylistsItems(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).build().execute();
                                                for (PlaylistTrack track : tracks.getItems()) {
                                                    ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{track.getTrack().getName(), TrackUtils.calculateFileSizeKb(track.getTrack().getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                                    searchplaylistsongscache.add(track.getTrack().getUri());
                                                    parsed++;
                                                }
                                                offset = offset + 100;
                                            }
                                        }else {
                                            for (PlaylistTrack track : pl.getTracks().getItems()) {
                                                if(!searchplaylisttable.isVisible()) {
                                                    break;
                                                }
                                                ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{track.getTrack().getName() + " - " + pl.getName() + " - " + pl.getOwner().getDisplayName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                                                searchplaylistsongscache.add(track.getTrack().getUri());
                                            }
                                        }
                                    } catch (IOException | ParseException | SpotifyWebApiException ex) {
                                        ConsoleLogging.Throwable(ex);
                                    }
                                }else {
                                    if(searchsonglistcache.get(searchsonglist.getSelectedRow()).contains("artist")) {
                                        try {
                                            Artist a = Factory.getSpotifyApi().getArtist(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute();
                                            try {
                                                artistPanel.artistimage.setImage(new URL(a.getImages()[0].getUrl()).openStream());
                                            } catch (ArrayIndexOutOfBoundsException exception) {
                                                //No artist image (when this is raised it's a bug)
                                            }
                                            artistPanel.artisttitle.setText(a.getName());
                                            DefThread trackthread = new DefThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        for (Track t : Factory.getSpotifyApi().getArtistsTopTracks(a.getUri().split(":")[2], countryCode).build().execute()) {
                                                            if(!artistPanel.isVisible()) {
                                                                break;
                                                            }
                                                            artistPanel.artistpopularuricache.add(t.getUri());
                                                            Factory.getSpotifyAPI().addSongToList(TrackUtils.getArtists(t.getArtists()), t, artistPanel.artistpopularsonglist);
                                                        }
                                                    } catch (IOException | ParseException | SpotifyWebApiException ex) {
                                                        ConsoleLogging.Throwable(ex);
                                                    }
                                                }
                                            });
                                            DefThread albumthread = new DefThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Factory.getSpotifyAPI().addAllAlbumsToList(artistPanel.artistalbumuricache, a.getUri(), artistPanel.artistalbumalbumtable);
                                                }
                                            });
                                            albumthread.start();
                                            trackthread.start();
                                        } catch (IOException | ParseException | SpotifyWebApiException ex) {
                                            ConsoleLogging.Throwable(ex);
                                        }
                                    }else {
                                        if(searchsonglistcache.get(searchsonglist.getSelectedRow()).contains("show")) {
                                            try {
                                                Show show = Factory.getSpotifyApi().getShow(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute();
                                                if(show.getEpisodes().getTotal() > 50) {
                                                    int parsed = 0;
                                                    int offset = 50;
                                                    for (EpisodeSimplified episode : show.getEpisodes().getItems()) {
                                                        if(!searchplaylisttable.isVisible()) {
                                                            break;
                                                        }
                                                        ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{episode.getName(), TrackUtils.calculateFileSizeKb(episode.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(episode.getDurationMs())});
                                                        searchplaylistsongscache.add(episode.getUri());
                                                        parsed++;
                                                    }
                                                    while(parsed != show.getEpisodes().getTotal()) {
                                                        if(!searchplaylisttable.isVisible()) {
                                                            break;
                                                        }
                                                        Paging<EpisodeSimplified> tracks = Factory.getSpotifyApi().getShowEpisodes(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).build().execute();
                                                        for (EpisodeSimplified episode : tracks.getItems()) {
                                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{episode.getName(), TrackUtils.calculateFileSizeKb(episode.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(episode.getDurationMs())});
                                                            searchplaylistsongscache.add(episode.getUri());
                                                            parsed++;
                                                        }
                                                        offset = offset + 50;
                                                    }
                                                }else {
                                                    for (EpisodeSimplified episode : show.getEpisodes().getItems()) {
                                                        if(!searchplaylisttable.isVisible()) {
                                                            break;
                                                        }
                                                        ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{episode.getName(), TrackUtils.calculateFileSizeKb(episode.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(episode.getDurationMs())});
                                                        searchplaylistsongscache.add(episode.getUri());
                                                    }
                                                }
                                            } catch (IOException | ParseException | SpotifyWebApiException ex) {
                                                ConsoleLogging.Throwable(ex);
                                            }
                                        }else{
                                            if(searchsonglistcache.get(searchsonglist.getSelectedRow()).contains("album")) {
                                                try {
                                                    Album album = Factory.getSpotifyApi().getAlbum(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).build().execute();
                                                    int parsed = 0;
                                                    int offset = 50;
                                                    if(album.getTracks().getTotal() > 50) {
                                                        for(TrackSimplified simplified : album.getTracks().getItems()) {
                                                            if(!searchplaylisttable.isVisible()) {
                                                                break;
                                                            }
                                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                                                            searchplaylistsongscache.add(simplified.getUri());
                                                            parsed++;
                                                        }
                                                        while(parsed != album.getTracks().getTotal()) {
                                                            if(!searchplaylisttable.isVisible()) {
                                                                break;
                                                            }
                                                            Paging<TrackSimplified> tracks = Factory.getSpotifyApi().getAlbumsTracks(searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]).offset(offset).build().execute();
                                                            for(TrackSimplified simplified : tracks.getItems()) {
                                                                ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                                                                searchplaylistsongscache.add(simplified.getUri());
                                                                parsed++;
                                                            }
                                                            offset = offset + 50;
                                                        }
                                                    }else {
                                                        for (TrackSimplified simplified : album.getTracks().getItems()) {
                                                            if(!searchplaylisttable.isVisible()) {
                                                                break;
                                                            }
                                                            ((DefaultTableModel) searchplaylisttable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                                                            searchplaylistsongscache.add(simplified.getUri());
                                                        }
                                                    }
                                                } catch (IOException | ParseException | SpotifyWebApiException ex) {
                                                    ConsoleLogging.Throwable(ex);
                                                }
                                            }else{
                                                player.getPlayer().load(searchsonglistcache.get(searchsonglist.getSelectedRow()), true, shuffle, false);
                                            }
                                        }
                                    }
                                }
                            }else{
                                player.retry();
                                if(player.getPlayer().isReady()) {
                                    player.getPlayer().load(searchsonglistcache.get(searchsonglist.getSelectedRow()), true, shuffle, false);
                                }else{
                                    GraphicalMessage.sorryError();
                                    System.exit(0);
                                }
                            }
                        }
                    });
                    thread.start();
                    searchsonglist.setColumnSelectionInterval(0, searchsonglist.getColumnCount() - 1);
                }else{
                    searchsonglist.setColumnSelectionInterval(0, searchsonglist.getColumnCount() - 1);
                }
            }
        });
        searchsonglist.getTableHeader().setForeground(PublicValues.globalFontColor);
        searchsonglist.setForeground(PublicValues.globalFontColor);
        searchsonglist.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")
                }
        ));
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

        searchplaylisttable = (JTable) JComponentFactory.createJComponent(new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        searchplaylistscrollpanel.setViewportView(searchplaylisttable);
        searchplaylisttable.setForeground(PublicValues.globalFontColor);
        searchplaylisttable.getTableHeader().setForeground(PublicValues.globalFontColor);

        searchplaylisttable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2) {
                    player.getPlayer().load(searchplaylistsongscache.get(searchplaylisttable.getSelectedRow()), true, shuffle, false);
                    searchplaylisttable.setColumnSelectionInterval(0, searchplaylisttable.getColumnCount() - 1);
                    TrackUtils.addAllToQueue(searchplaylistsongscache, searchplaylisttable);
                }
            }
        });

        searchplaylisttable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")
                }
        ));

        searchplaylistpanel.setVisible(false);

        searchplaylistsongscontextmenu = new ContextMenu(searchplaylisttable);
        searchplaylistsongscontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), new Runnable() {
            @Override
            public void run() {
                ClipboardUtil.set(searchplaylistsongscache.get(searchplaylisttable.getSelectedRow()));
            }
        });


        searchbackbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchplaylistpanel.setVisible(false);
                searchpane.setVisible(true);
                if(!artistPanelVisible) {
                    enableTabSwitch();
                }
            }
        });
        searchcontextmenu = new ContextMenu(searchsonglist);
        searchcontextmenu.addItem(PublicValues.language.translate("ui.general.addtolibrary"), new Runnable() {
            @Override
            public void run() {
                heart.isFilled = true;
                heart.setImage(new Resources().readToInputStream("icons/heartfilled.svg"));
                PublicValues.elevated.makePut("https://api.spotify.com/v1/me/tracks?ids=" + searchsonglistcache.get(searchsonglist.getSelectedRow()).split(":")[2]);
                if(!(libraryuricache.size()==0)) {
                    fetchOnlyFirstSongsFromUserLibrary();
                }
            }
        });
        searchcontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), new Runnable() {
            @Override
            public void run() {
                ClipboardUtil.set(searchsonglistcache.get(searchsonglist.getSelectedRow()));
            }
        });
    }
    void createErrorDisplay() {
        errorDisplay = (JButton) JComponentFactory.createJComponent(new JButton() {
            @Override
            public void setText(String text) {
                if(text.equals("Default")) {
                    text = PublicValues.language.translate("ui.errorqueue.button");
                    super.setText(text);
                    return;
                }
                if(!text.contains(PublicValues.language.translate("ui.errorqueue.button"))) {
                    text = PublicValues.language.translate("ui.errorqueue.button") + " " + text;
                }
                if(errorDisplay != null) errorDisplay.setVisible(true);
                if(text.equals(String.valueOf(0))) {
                    errorDisplay.setVisible(false);
                }
                super.setText(text);
                if(errorDisplay != null) errorDisplay.setBounds(10, 10, errorDisplay.getWidth(), errorDisplay.getHeight());
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
                JDialog dialog = new JDialog();
                dialog.setTitle(PublicValues.language.translate("ui.errorqueue.title"));
                JScrollPane pane = (JScrollPane) JComponentFactory.createJComponent(new JScrollPane());
                JTable table = (JTable) JComponentFactory.createJComponent(new DefTable()  {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                table.setModel(new DefaultTableModel(
                        new Object[][] {
                        },
                        new String[] {
                                ""
                        }
                ));
                pane.setViewportView(table);
                int i = 10;
                for(ExceptionDialog exd : errorQueue) {
                    ((DefaultTableModel) table.getModel()).addRow(new Object[]{exd.getPreview()});
                    i += 10;
                }
                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        errorQueue.get(table.getSelectedRow()).openReal();
                        errorQueue.remove(errorQueue.size() - 1);
                        errorDisplay.setText(String.valueOf(errorQueue.size()));
                        ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
                        if(((DefaultTableModel) table.getModel()).getRowCount() == 0) {
                            errorDisplay.setVisible(false);
                        }
                    }
                });
                JButton remove = new JButton(PublicValues.language.translate("ui.errorqueue.clear"));
                remove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        errorQueue.clear();
                        ((DefaultTableModel) table.getModel()).setRowCount(0);
                        errorDisplay.setVisible(false);
                    }
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
        hotlistbutton = (JToggleButton) JComponentFactory.createJComponent(new JToggleButton(PublicValues.language.translate("ui.navigation.hotlist")));
        hotlistbutton.setBounds(447, 111, 107, 23);
        add(hotlistbutton);
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

        hotlistplayliststable = (JTable) JComponentFactory.createJComponent(new DefTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        hotlistplayliststable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.hotlist.playlistlist.playlists")
                }
        ));
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
        hotlistplaylistspanelrightclickmenu.addItem(PublicValues.language.translate("ui.general.refresh"), new Runnable() {
            @Override
            public void run() {
                hotlistplaylistlistcache.clear();
                hotlistsonglistcache.clear();
                ((DefaultTableModel)hotlistsongstable.getModel()).setRowCount(0);
                ((DefaultTableModel)hotlistplayliststable.getModel()).setRowCount(0);
                fetchHotlist();
            }
        });

        hotlistsongstable = (JTable) JComponentFactory.createJComponent(new DefTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        hotlistsongstable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.hotlist.songlist.songtitle"), PublicValues.language.translate("ui.hotlist.songlist.filesize"), PublicValues.language.translate("ui.hotlist.songlist.bitrate"), PublicValues.language.translate("ui.hotlist.songlist.length")
                }
        ));

        hotlistsongstablecontextmenu = new ContextMenu(hotlistsongstable);
        hotlistsongstablecontextmenu.addItem(PublicValues.language.translate("ui.general.copyuri"), new Runnable() {
            @Override
            public void run() {
                ClipboardUtil.set(hotlistsonglistcache.get(hotlistsongstable.getSelectedRow()));
            }
        });

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
                if(e.getClickCount()==2) {
                    //player.getPlayer().tracks(true).next.clear();
                    player.getPlayer().load(hotlistsonglistcache.get(hotlistsongstable.getSelectedRow()), true, shuffle, false);
                    TrackUtils.addAllToQueue(hotlistsonglistcache, hotlistsongstable);
                }
            }
        });
        hotlistplayliststable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((DefaultTableModel)hotlistsongstable.getModel()).setRowCount(0);
                hotlistsonglistcache.clear();
                if(e.getClickCount()==2) {
                    try {
                        for (TrackSimplified track : Factory.getSpotifyApi().getAlbum(hotlistplaylistlistcache.get(hotlistplayliststable.getSelectedRow())).build().execute().getTracks().getItems()) {
                            String a = TrackUtils.getArtists(track.getArtists());
                            hotlistsonglistcache.add(track.getUri());
                            ((DefaultTableModel) hotlistsongstable.getModel()).addRow(new Object[]{track.getName() + " - " + a, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
                        }
                    } catch (IOException | SpotifyWebApiException | ParseException ex) {
                        ConsoleLogging.Throwable(ex);
                    }
                }
            }
        });
        hotlistbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setHotlistVisible();
            }
        });
    }
    void createQueue() {
        queuebutton = (JToggleButton) JComponentFactory.createJComponent(new JToggleButton(PublicValues.language.translate("ui.navigation.queue")));
        queuebutton.setBounds(557, 111, 107, 23);
        add(queuebutton);
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

        //noinspection unchecked
        queuelist = new JList(queuelistmodel);
        queuescrollpane.setViewportView(queuelist);
        queuebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setQueueVisible();
            }
        });
        queuebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) queuelist.getModel()).removeAllElements();
                queueuricache.clear();
                DefThread queueworker = new DefThread(new Runnable() {
                    @Override
                    public void run() {
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
                            //Nothing in queue
                        }
                    }
                });
                queueworker.start();
            }
        });

        queueremovebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.getPlayer().removeFromQueue(queueuricache.get(queuelist.getSelectedIndex()));
                queueuricache.remove(queueuricache.get(queuelist.getSelectedIndex()));
                ((DefaultListModel) queuelist.getModel()).remove(queuelist.getSelectedIndex());
            }
        });
    }
    void createFeedback() {
        feedbackbutton = (JToggleButton) JComponentFactory.createJComponent(new JToggleButton(PublicValues.language.translate("ui.navigation.feedback")));
        feedbackbutton.setBounds(667, 111, 107, 23);
        add(feedbackbutton);
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
        feedbackbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFeedbackVisible();
            }
        });
        feedbackupdaterversionfield.setEditable(false);
        feedbackupdaterdownloadbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionUtils.openBrowser(new Updater().updateAvailable().url);
            }
        });
        feedbackgithubbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionUtils.openBrowser("https://github.com/werwolf2303/SpotifyXP");
            }
        });
        feedbackviewissuesbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionUtils.openBrowser("https://github.com/werwolf2303/SpotifyXP/issues");
            }
        });
        feedbackcreateissuebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionUtils.openBrowser("https://github.com/werwolf2303/SpotifyXP/issues/new");
            }
        });
    }
    public static void steamDeck() {
        steamdeck = true;
    }
    void createLegacy() {
        JFrame dialog = new JFrame();
        playerarea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2) {
                    if(!dialog.isVisible()) {
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
                    }else{
                        windowWasOpened = false;
                        dialog.dispose();
                        playerarea.setBounds(784/2-playerareawidth/2, 8, playerareawidth, playerareaheight);
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
                playerarea.setBounds(784/2-playerareawidth/2, 8, playerareawidth, playerareaheight);
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
                if(windowWasOpened) {
                    dialog.setVisible(true);
                }
            }
        });
        if(PublicValues.theme instanceof DarkGreen) {
            legacyswitch.setBackground(new Color(63, 63, 63));
        }
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
                return frame.getWidth() / legacyswitch.getTabCount();
            }
        });
        if(!(PublicValues.theme instanceof DarkGreen)) {
            legacyswitch.setBackgroundAt(0, Color.white);
            legacyswitch.setBackgroundAt(1, Color.white);
            legacyswitch.setBackgroundAt(2, Color.white);
            legacyswitch.setBackgroundAt(3, Color.white);
            legacyswitch.setBackgroundAt(4, Color.white);
            legacyswitch.setBackgroundAt(5, Color.white);
            legacyswitch.setBackgroundAt(6, Color.white);
        }
        add(legacyswitch);
        legacyswitch.setSelectedIndex(0);
        homebutton.doClick();
        preventBugLegacySwitch();
        legacyswitch.setComponentAt(0, tabpanel);
        setHomeVisible();
        legacyswitch.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (legacyswitch.getSelectedIndex()) {
                    case 0:
                        homebutton.doClick();
                        preventBugLegacySwitch();
                        legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                        setHomeVisible();
                        break;
                    case 1:
                        playlistsbutton.doClick();
                        preventBugLegacySwitch();
                        legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                        setPlaylistsVisible();
                        break;
                    case 2:
                        if(librarysonglist.getModel().getRowCount()==0) {
                            librarythread.start();
                        }
                        preventBugLegacySwitch();
                        legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                        setLibraryVisible();
                        break;
                    case 3:
                        searchbutton.doClick();
                        preventBugLegacySwitch();
                        legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                        setSearchVisible();
                        break;
                    case 4:
                        hotlistbutton.doClick();
                        preventBugLegacySwitch();
                        legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                        setHotlistVisible();
                        break;
                    case 5:
                        queuebutton.doClick();
                        preventBugLegacySwitch();
                        legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                        setQueueVisible();
                        break;
                    case 6:
                        feedbackbutton.doClick();
                        preventBugLegacySwitch();
                        legacyswitch.setComponentAt(legacyswitch.getSelectedIndex(), tabpanel);
                        setFeedbackVisible();
                        break;
                    default:
                        GraphicalMessage.bug("Legacy JTabbedPane changeListener");
                        break;
                }
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
        file.add(playuri);
        file.add(exit);
        edit.add(settings);
        view.add(audiovisualizer);
        lastfm.add(lastfmdashboard);
        account.add(logout);
        help.add(extensions);
        help.add(about);
        audiovisualizer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicValues.visualizer.open();
            }
        });
        extensions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InjectorStore().open();
            }
        });
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
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicValues.config.write(ConfigValues.username.name, "");
                PublicValues.config.write(ConfigValues.password.name, "");
                JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.logout.text"), PublicValues.language.translate("ui.logout.title"), JOptionPane.OK_CANCEL_OPTION);
                System.exit(0);
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAbout();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        playuri.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uri = JOptionPane.showInputDialog(frame, PublicValues.language.translate("ui.playtrackuri.message"), PublicValues.language.translate("ui.playtrackuri.title"), JOptionPane.PLAIN_MESSAGE);
                PublicValues.spotifyplayer.load(uri, true, false, false);
            }
        });
        lastfmdashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LastFMDialog().open();
            }
        });
        if(steamdeck) {

            for (int i = 0; i < bar.getMenuCount(); i++) {
                JMenu menu1 = bar.getMenu(i);
                JFrame window = new JFrame();
                window.setTitle(menu1.getText());
                ArrayList<JMenuItem> items = new ArrayList<>();
                JTable table = new DefTable() {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                table.setModel(new DefaultTableModel(
                        new Object[][]{
                        },
                        new String[]{
                                ""
                        }
                ));
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
        }
    }
    public static ArrayList<String> advanceduricache = new ArrayList<String>();
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

        advancedbackbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advancedsongpanel.setVisible(false);
                homepane.getComponent().setVisible(true);
                ContentPanel.enableTabSwitch();
            }
        });


        advancedsongtable = (JTable) JComponentFactory.createJComponent(new DefTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        advancedsongtable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")
                }
        ));
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
                if(e.getClickCount()==2) {
                    player.getPlayer().load(advanceduricache.get(advancedsongtable.getSelectedRow()), true, shuffle, false);
                    advancedsongtable.setColumnSelectionInterval(0, advancedsongtable.getColumnCount() - 1);
                    TrackUtils.addAllToQueue(advanceduricache, advancedsongtable);
                }
            }
        });
    }
    public static boolean pressedCTRL = false;
    boolean clicked = false;
    int w = 0;
    int h = 0;
    boolean visible = false;
    boolean dragging = false;
    boolean toggle = false;

    public static void showArtistPanel(String fromuri) {
        if(advancedSongPanelVisible) {
            homepane.getComponent().setVisible(true);
            advancedSongPanelVisible = false;
            advancedsongpanel.setVisible(false);
        }
        switch(lastmenu) {
            case Home:
                artistPanel.artistpopularuricache.clear();
                artistPanel.artistalbumuricache.clear();
                ((DefaultTableModel)artistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                ((DefaultTableModel)artistPanel.artistpopularsonglist.getModel()).setRowCount(0);
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
                ((DefaultTableModel)artistPanel.artistalbumalbumtable.getModel()).setRowCount(0);
                ((DefaultTableModel)artistPanel.artistpopularsonglist.getModel()).setRowCount(0);
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
                artistPanel.artistimage.setImage(new URL(a.getImages()[0].getUrl()).openStream());
            } catch (ArrayIndexOutOfBoundsException exception) {
                //No artist image (when this is raised it's a bug)
            }
            artistPanel.artisttitle.setText(a.getName());
            DefThread trackthread = new DefThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (Track t : Factory.getSpotifyApi().getArtistsTopTracks(a.getUri().split(":")[2], countryCode).build().execute()) {
                            if(!artistPanel.isVisible()) {
                                break;
                            }
                            artistPanel.artistpopularuricache.add(t.getUri());
                            Factory.getSpotifyAPI().addSongToList(TrackUtils.getArtists(t.getArtists()), t, artistPanel.artistpopularsonglist);
                        }
                    } catch (IOException | ParseException | SpotifyWebApiException ex) {
                        ConsoleLogging.Throwable(ex);
                    }
                }
            });
            DefThread albumthread = new DefThread(new Runnable() {
                @Override
                public void run() {
                    Factory.getSpotifyAPI().addAllAlbumsToList(artistPanel.artistalbumuricache, a.getUri(), artistPanel.artistalbumalbumtable);
                }
            });
            albumthread.start();
            trackthread.start();
            artistPanel.openPanel();
        } catch (IOException | ParseException | SpotifyWebApiException ex) {
            ConsoleLogging.Throwable(ex);
        }
    }

    public ContentPanel(Player p) {
        ConsoleLogging.info(PublicValues.language.translate("debug.buildcontentpanelbegin"));
        player = p;
        SplashPanel.linfo.setText("Setting window size...");
        setPreferredSize(new Dimension(783, 600));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);

        SplashPanel.linfo.setText("Creating errorDisplay...");
        createErrorDisplay();
        SplashPanel.linfo.setText("Creating tabpanel...");
        tabpanel = new JPanel();
        tabpanel.setLayout(null);

        if(PublicValues.theme.hasLegacyUI()) {
            tabpanel.setBounds(0, 140, 784, 450);
        }else{
            tabpanel.setBounds(0, 140, 784, 421);
        }

        SplashPanel.linfo.setText("Creating playerarea...");
        createPlayerArea();
        SplashPanel.linfo.setText("Creating settingsbutton...");
        createSettingsButton();
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
        SplashPanel.linfo.setText("Creating dot menus...");
        createThreePointButton();
        createUserButton();
        createHome();
        SplashPanel.linfo.setText("Creating CanvasPlayer...");
        if(System.getProperty("os.name").toLowerCase().contains("win")) {
            PublicValues.canvasPlayer = new CanvasPlayer();
        }else{
            PublicValues.canvasPlayer = new DummyCanvasPlayer(false);
        }
        SplashPanel.linfo.setText("Creating advancedPanel...");
        createAdvancedPanel();
        SplashPanel.linfo.setText("Changing component visibility...");
        searchpane.setVisible(false); //Not show searchpane when window is opened
        librarypane.setVisible(false); //Not show librarypane when window is opened
        playlistspane.setVisible(false); //Not show playlistspane when window is opened
        queuepane.setVisible(false); //Not show queuepane when window is opened
        feedbackpane.setVisible(false); //Now show feedbackpane when window is opened
        hotlistpane.setVisible(false); //Not show hotlistpane when window is opened
        homepane.getComponent().setVisible(false); //Not show homepane when window is opened


        SplashPanel.linfo.setText("Adding window mouse listener...");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(pressedCTRL) {
                    PublicValues.injector.openInjectWindow("");
                }
            }
        });

        if(!(PublicValues.theme instanceof Legacy) && !(PublicValues.theme instanceof DarkGreen)) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    setHotlistVisible();
                }
            });
            t.start();
            add(tabpanel);
        }else{
            createLegacy();
        }

        try {
            if(!(Factory.getSpotifyApi().getCurrentUsersProfile() == null)) {
                countryCode = Factory.getSpotifyApi().getCurrentUsersProfile().build().execute().getCountry();
            }
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            ConsoleLogging.Throwable(e);
            //Defaulting to German
            countryCode = CountryCode.DE;
        }
        artistPanelBackButton.setVisible(false);
        SplashPanel.linfo.setText("Init Theme...");
        updateTheme();
        SplashPanel.linfo.setText("Trying to restore play state...");
        if(new File(PublicValues.fileslocation, "play.state").exists()) {
            parseLastPlayState();
            try {
                if (!lastPlayState.uri.equals("")) {
                    playerplaytime.setText(lastPlayState.playtime);
                    playerplaytimetotal.setText(lastPlayState.playtimetotal);
                    playercurrenttime.setMaximum(Integer.parseInt(lastPlayState.playerslidermax));
                    PublicValues.spotifyplayer.load(lastPlayState.uri, false, shuffle, false);
                    PlayerListener.locked = true;
                    while(PlayerListener.locked) {
                        Thread.sleep(99);
                    }
                    PublicValues.spotifyplayer.seek(Integer.parseInt(lastPlayState.playerslider)*1000);
                    while(PlayerListener.locked) {
                        Thread.sleep(99);
                    }
                    playerareavolumeslider.setValue(Integer.parseInt(lastPlayState.playervolume));
                }
            }catch (Exception e) {
                new RuntimeException(e);
            }
        }
        SplashPanel.linfo.setText("Done building contentPanel");
        ConsoleLogging.info(PublicValues.language.translate("debug.buildcontentpanelend"));
    }
    class LastPlayState {
        public String uri;
        public String playtimetotal;
        public String playtime;
        public String playerslider;
        public String playerslidermax;
        public String playervolume;
    }
    LastPlayState lastPlayState;
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
            scan.close();
            lastPlayState = state;
        } catch (FileNotFoundException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        }
    }
    int playerareawidth = 0;
    int playerareaheight = 0;

    //These are the positions for the popup window
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

    //These are the positions for the normal player style
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

    static void preventBugLegacySwitch() {
        for(int i = 0; i < legacyswitch.getTabCount(); i++) {
            legacyswitch.setComponentAt(i, new JPanel());
        }
    }

    void fetchOnlyFirstSongsFromUserLibrary() {
        DefaultTableModel model = (DefaultTableModel) librarysonglist.getModel();
        try {
            int count = 0;
            for(SavedTrack track : Factory.getSpotifyApi().getUsersSavedTracks().limit(10).build().execute().getItems()) {
                if(!libraryuricache.contains(track.getTrack().getUri())) {
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

    public static void updateTheme() {
        if(PublicValues.theme.isLight()) {
            playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumefulldark.svg"));
            playerarea.setBorder(new LineBorder(Color.black));
            settingsbutton.setImage(new Resources(true).readToInputStream("icons/settingsdark.svg"));
            userbutton.setImage(new Resources(true).readToInputStream("icons/userdark.svg"));
            threepointbutton.setImage(new Resources(true).readToInputStream("icons/dotsdark.svg"));
            playerplaypreviousbutton.setImage(new Resources().readToInputStream("icons/playerplaypreviousdark.svg"));
            playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerplaydark.svg"));
            playerplaynextbutton.setImage(new Resources().readToInputStream("icons/playerplaynextdark.svg"));
            playerareashufflebutton.setImage(new Resources().readToInputStream("icons/shuffledark.svg"));
            playerarearepeatingbutton.setImage(new Resources().readToInputStream("icons/repeatdark.svg"));
            playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonedark.svg"));
        }else{
            settingsbutton.setImage(new Resources(true).readToInputStream("icons/settingswhite.svg"));
            userbutton.setImage(new Resources(true).readToInputStream("icons/userwhite.svg"));
            threepointbutton.setImage(new Resources(true).readToInputStream("icons/dotswhite.svg"));
            playerarea.setBorder(new LineBorder(Color.gray));
            playerplaypreviousbutton.setImage(new Resources().readToInputStream("icons/playerplaypreviouswhite.svg"));
            playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerplaywhite.svg"));
            playerplaynextbutton.setImage(new Resources().readToInputStream("icons/playerplaynextwhite.svg"));
            playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumefullwhite.svg"));
            playerareashufflebutton.setImage(new Resources().readToInputStream("icons/shufflewhite.svg"));
            playerarearepeatingbutton.setImage(new Resources().readToInputStream("icons/repeatwhite.svg"));
            playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonewhite.svg"));
        }
        if(PublicValues.theme instanceof Legacy) {
            playerareavolumeicon.setImage(new Resources().readToInputStream("legacyicons/volumefull.png"));
            playerplaypreviousbutton.setImage(new Resources().readToInputStream("legacyicons/playerplayprevious.svg"));
            playerplaynextbutton.setImage(new Resources().readToInputStream("legacyicons/playerplaynext.svg"));
            playerplaypausebutton.setImage(new Resources().readToInputStream("legacyicons/playerplay.svg"));
            playerareavolumecurrent.setText(String.valueOf(playerareavolumeslider.getValue()));
            if (playerareavolumeslider.getValue() == 0) {
                playerareavolumeicon.setImage(new Resources().readToInputStream("legacyicons/volumemuted.png"));
            } else {
                playerareavolumeicon.setImage(new Resources().readToInputStream("legacyicons/volumefull.png"));
            }
        }else{
            playerplaynextbutton.setBorderPainted(true);
            playerplaypreviousbutton.setBorderPainted(true);
            playerplaypausebutton.setBorderPainted(true);
        }
        if(PublicValues.theme instanceof Legacy || PublicValues.theme instanceof DarkGreen) {
            //Hide stylish buttons
            settingsbutton.getJComponent().setVisible(false);
            userbutton.getJComponent().setVisible(false);
            threepointbutton.getJComponent().setVisible(false);
            //---
            //Set toggle buttons hidden
            librarybutton.setVisible(false);
            playlistsbutton.setVisible(false);
            searchbutton.setVisible(false);
            hotlistbutton.setVisible(false);
            queuebutton.setVisible(false);
            feedbackbutton.setVisible(false);
            homebutton.setVisible(false);
            //---
            //Set style of components
            //INFO: No need to change the color of the jmenubar because under windowsxp it has the right color
            playerplaypausebutton.setBorderPainted(false);
            playerplaypausebutton.setContentAreaFilled(false);
            playerplaynextbutton.setBorderPainted(false);
            playerplaynextbutton.setContentAreaFilled(false);
            playerplaypreviousbutton.setBorderPainted(false);
            playerplaypreviousbutton.setContentAreaFilled(false);
            playerimage.setImage(new Resources().readToInputStream("legacyicons/nothingplaying.png"));
            playerarea.setBorder(null);
            //---
            //Resize components
            playerarea.setBounds(784/2-playerarea.getWidth()/2, 8, playerarea.getWidth(), playerarea.getHeight()-3);
            //---
            //Add JTabbedPane
            legacyswitch.setVisible(true);
            //---
        }else{
            playerimage.setImage(new Resources().readToInputStream("icons/nothingplaying.png"));
        }
    }
    boolean windowWasOpened = false;

    @SuppressWarnings("SameParameterValue")
    static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }
    public void setLibraryVisible() {
        lastmenu = LastTypes.Library;
        librarypane.setVisible(true);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
        hotlistbutton.setSelected(false);
        searchbutton.setSelected(false);
        librarybutton.setSelected(true);
        feedbackbutton.setSelected(false);
        playlistsbutton.setSelected(false);
        queuebutton.setSelected(false);
        homebutton.setSelected(false);
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
        hotlistbutton.setSelected(false);
        searchbutton.setSelected(true);
        librarybutton.setSelected(false);
        feedbackbutton.setSelected(false);
        playlistsbutton.setSelected(false);
        queuebutton.setSelected(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        homebutton.setSelected(false);
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = true;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
    }
    public void setButtonsHidden() {
        playerarea.setVisible(false);
        librarybutton.setVisible(false);
        playlistsbutton.setVisible(false);
        searchbutton.setVisible(false);
        hotlistbutton.setVisible(false);
        queuebutton.setVisible(false);
        feedbackbutton.setVisible(false);
        homebutton.setVisible(false);
    }
    public static void makeButtonsHidden() {
        librarybutton.setVisible(false);
        playlistsbutton.setVisible(false);
        searchbutton.setVisible(false);
        hotlistbutton.setVisible(false);
        queuebutton.setVisible(false);
        feedbackbutton.setVisible(false);
        homebutton.setVisible(false);
    }
    public static void makeButtonsVisible() {
        librarybutton.setVisible(true);
        playlistsbutton.setVisible(true);
        searchbutton.setVisible(true);
        hotlistbutton.setVisible(true);
        queuebutton.setVisible(true);
        feedbackbutton.setVisible(true);
        homebutton.setVisible(true);
    }
    public void setButtonsVisible() {
        playerarea.setVisible(true);
        librarybutton.setVisible(true);
        playlistsbutton.setVisible(true);
        searchbutton.setVisible(true);
        hotlistbutton.setVisible(true);
        queuebutton.setVisible(true);
        feedbackbutton.setVisible(true);
        homebutton.setVisible(true);
    }
    public void setNothingVisible() {
        setButtonsHidden();
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
        hotlistbutton.setSelected(true);
        searchbutton.setSelected(false);
        librarybutton.setSelected(false);
        feedbackbutton.setSelected(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        homebutton.setSelected(false);
        playlistsbutton.setSelected(false);
        queuebutton.setSelected(false);
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
        hotlistbutton.setSelected(true);
        searchbutton.setSelected(false);
        librarybutton.setSelected(false);
        feedbackbutton.setSelected(false);
        playlistsbutton.setSelected(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        homebutton.setSelected(false);
        queuebutton.setSelected(false);
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = true;
        feedbackVisible = false;
        if(hotlistplayliststable.getRowCount()==0) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    fetchHotlist();
                }
            });
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
        homebutton.setSelected(false);
        hotlistbutton.setSelected(false);
        searchbutton.setSelected(false);
        librarybutton.setSelected(false);
        feedbackbutton.setSelected(true);
        playlistsbutton.setSelected(false);
        queuebutton.setSelected(false);
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
        hotlistbutton.setSelected(false);
        searchbutton.setSelected(false);
        librarybutton.setSelected(false);
        feedbackbutton.setSelected(false);
        playlistsbutton.setSelected(true);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        homebutton.setSelected(false);
        queuebutton.setSelected(false);
        queueVisible = false;
        playlistsVisible = true;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
    }
    public void setQueueVisible() {
        lastmenu = LastTypes.Queue;
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(false);
        playlistspane.setVisible(false);
        queuepane.setVisible(true);
        hotlistbutton.setSelected(false);
        searchbutton.setSelected(false);
        librarybutton.setSelected(false);
        feedbackbutton.setSelected(false);
        playlistsbutton.setSelected(false);
        homepane.getComponent().setVisible(false);
        homeVisible = false;
        homebutton.setSelected(false);
        queuebutton.setSelected(true);
        queueVisible = true;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
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
        homebutton.setSelected(true);
        hotlistbutton.setSelected(false);
        searchbutton.setSelected(false);
        librarybutton.setSelected(false);
        feedbackbutton.setSelected(false);
        playlistsbutton.setSelected(false);
        queuebutton.setSelected(false);
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
    }
    public static void saveCurrentState() {
        try {
            PublicValues.spotifyplayer.currentPlayable().toSpotifyUri();
        }catch (Exception e) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(new File(PublicValues.fileslocation, "play.state"));
            writer.write(PublicValues.spotifyplayer.currentPlayable().toSpotifyUri() + "\n" + playercurrenttime.getValue() + "\n" + playerplaytime.getText() + "\n" + playerplaytimetotal.getText() + "\n" + playercurrenttime.getMaximum() + "\n" + playerareavolumecurrent.getText());
            writer.close();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
        try {
            new File(PublicValues.appLocation, "LOCK").delete();
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
    }
    void fetchHotlist() {
        JSONObject stuff = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/top/artists"));
        StringBuilder genres = new StringBuilder();
        StringBuilder tracks = new StringBuilder();
        StringBuilder artists = new StringBuilder();
        System.out.println("Line 3153: " + stuff);
        for(Object o : new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/player/recently-played?limit=10")).getJSONArray("items")) {
            System.err.println("Line 3154: " + o.toString());
            JSONObject track = new JSONObject(new JSONObject(o.toString()).get("track").toString());
            tracks.append(track.getString("uri").split(":")[2]).append(",");
        }
        for(Object o : stuff.getJSONArray("items")) {
            JSONObject found = new JSONObject(o.toString());
            for(Object o2 : found.getJSONArray("genres")) {
                genres.append(o2.toString()).append(",");
            }
            String likeduri = found.getString("uri").split(":")[2];
            artists.append(likeduri).append(",");
        }
        StringBuilder fivegenres = new StringBuilder();
        StringBuilder fiveartists = new StringBuilder();
        StringBuilder fivetracks = new StringBuilder();
        int gcr = new Random().nextInt(genres.toString().split(",").length);
        int gc = 0;
        int gcs = 0;
        for(String s : genres.toString().split(",")) {
            if(gcs!=gcr) {
                gcs++;
                continue;
            }
            if(gc==2) {
                break;
            }
            if(fivegenres.toString().equals("")) {
                fivegenres.append(s);
            }else{
                fivegenres.append(",").append(s);
            }
            gc++;
        }
        int ac = 0;
        for(String s : artists.toString().split(",")) {
            if(ac==2) {
                break;
            }
            if(fiveartists.toString().equals("")) {
                fiveartists.append(s);
            }else{
                fiveartists.append(",").append(s);
            }
            ac++;
        }
        int tc = 0;
        for(String s : tracks.toString().split(",")) {
            if(tc==1) {
                break;
            }
            if(fivetracks.toString().equals("")) {
                fivetracks.append(s);
            }else{
                fivetracks.append(",").append(s);
            }
            tc++;
        }
        try {
            JSONObject recommendations = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/recommendations", new NameValuePair[]{new NameValuePair("seed_artists", fiveartists.toString()), new NameValuePair("seed_genres", fivegenres.toString()), new NameValuePair("seed_tracks", fivetracks.toString())}));
            for (Object o : recommendations.getJSONArray("tracks")) {
                JSONObject album = new JSONObject(new JSONObject(o.toString()).get("album").toString());
                String uri = album.getString("uri").split(":")[2];
                hotlistplaylistlistcache.add(uri);
                try {
                    Album albumreq = Factory.getSpotifyApi().getAlbum(uri).build().execute();
                    String a = TrackUtils.getArtists(albumreq.getArtists());
                    ((DefaultTableModel) hotlistplayliststable.getModel()).addRow(new Object[]{albumreq.getName() + " - " + a});
                } catch (IOException | ParseException | SpotifyWebApiException e) {
                    ConsoleLogging.Throwable(e);
                }
            }
        }catch (JSONException exception) {
            ExceptionDialog.open(exception);
        }
    }

    static boolean advancedSongPanelVisible = false;

    public static void showAdvancedSongPanel(String foruri, HomePanel.ContentTypes contentType) {
        homepane.getComponent().setVisible(false);
        ((DefaultTableModel) advancedsongtable.getModel()).setRowCount(0);
        advanceduricache.clear();

        advancedSongPanelVisible = true;

        if(artistPanelVisible) {
            artistPanelBackButton.doClick();
        }

        try {
            switch (contentType) {
                case playlist:
                    for(PlaylistTrack simplified : Factory.getSpotifyApi().getPlaylist(foruri.split(":")[2]).build().execute().getTracks().getItems()) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getTrack().getName(), TrackUtils.calculateFileSizeKb(simplified.getTrack().getDurationMs()), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(simplified.getTrack().getDurationMs())});
                        advanceduricache.add(simplified.getTrack().getUri());
                    }
                    break;
                case show:
                    for(EpisodeSimplified simplified : Factory.getSpotifyApi().getShow(foruri.split(":")[2]).build().execute().getEpisodes().getItems()) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                        advanceduricache.add(simplified.getUri());
                    }
                    break;
                case album:
                    for(TrackSimplified simplified : Factory.getSpotifyApi().getAlbum(foruri.split(":")[2]).build().execute().getTracks().getItems()) {
                        ((DefaultTableModel) advancedsongtable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                        advanceduricache.add(simplified.getUri());
                    }
                    break;
                default:
                    GraphicalMessage.bug("tried to invoke showAdvancedSongPanel with incompatible type -> " + contentType.toString());
                    break;
            }
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.bug("ContentPanel showAdvancedSongPanel");
        }

        advancedsongpanel.setVisible(true);
        blockTabSwitch();
    }
    public static JFrame2 frame = new JFrame2("SpotifyXP - v" + ApplicationUtils.getVersion() + " " + ApplicationUtils.getReleaseCandidate());
    public static SettingsPanel settingsPanel = null;
    public void open()
    {
        PublicValues.contentPanel = this;
        JFrame2 mainframe;
        mainframe = frame;
        try {
            mainframe.setIconImage(ImageIO.read(new Resources(false).readToInputStream("spotifyxp.png")));
        } catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        }
        settingsPanel = new SettingsPanel();
        settingsPanel.setBounds(0, 52, 784, 509);
        settingsPanel.setVisible(false);
        if(PublicValues.theme.hasLegacyUI()) {
            mainframe.setJMenuBar(bar);
            mainframe.setPreferredSize(new Dimension(784, 590));
        }else{
            mainframe.setPreferredSize(new Dimension(793, 600));
        }
        mainframe.add(settingsPanel, BorderLayout.CENTER);
        mainframe.add(this, BorderLayout.CENTER);
        mainframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainframe.dispose();
            }
        });
        mainframe.setForeground(Color.blue);
        DefThread thread = new DefThread(new Runnable() {
            @Override
            public void run() {
                InjectingPoints.INTERNALinvokeOnFrameReady();
            }
        });
        thread.start();
        mainframe.setVisible(true);
        mainframe.setResizable(false);
        mainframe.pack();
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
        //JComponentFactory.enableResizing();
    }
}
