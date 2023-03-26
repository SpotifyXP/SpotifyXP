package com.spotifyxp.panels;

import com.spotifyxp.Colors;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.custom.StoppableThreadRunnable;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.*;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.dialogs.HTMLDialog;
import com.spotifyxp.engine.EnginePanel;
import com.spotifyxp.events.LoggerEvent;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.DropDownMenu;
import com.spotifyxp.swingextension.JImageButton;
import com.spotifyxp.threading.StoppableThread;
import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.PublicValues;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.*;
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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"CanBeFinal", "rawtypes", "Convert2Lambda"})
public class ContentPanel extends JPanel {
    private final ContentPanel contentPanel = this;
    public static JTable librarysonglist;
    public static SpotifyAPI api = null;
    public static SpotifyAPI.Player player = null;
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
    public static JLabel playertitle;
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
    public static DefaultTableModel librarydefaulttablemodel;
    public static JToggleButton playlistsbutton;
    public static JToggleButton librarybutton;
    public static JToggleButton searchbutton;
    public static JToggleButton hotlistbutton;
    public static JToggleButton queuebutton;
    public static JToggleButton feedbackbutton;
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
    public static JImagePanel heart;
    public static ArrayList<String> searchsonglistcache = new ArrayList<>();
    public static ArrayList<String> hotlistplaylistlistcache = new ArrayList<>();
    public static ArrayList<String> hotlistsonglistcache = new ArrayList<>();
    public static ArrayList<String> libraryuricache = new ArrayList<>();
    public static ArrayList<String> queueuricache = new ArrayList<>();
    public static DefaultListModel<String> queuelistmodel = new DefaultListModel<>();
    public static ArrayList<String> playlistsuricache = new ArrayList<>();
    public static ArrayList<String> playlistssonguricache = new ArrayList<>();
    public static JImagePanel userbutton;
    public static JImagePanel settingsbutton;
    public static JImagePanel threepointbutton;
    public static DropDownMenu userdropdown;
    public static DropDownMenu threepointdropdown;
    public static JImagePanel playerareavolumeicon;
    public static JSlider playerareavolumeslider;
    public static JLabel playerareavolumecurrent;
    enum LastTypes {
        Playlists,
        Library,
        Search,
        HotList,
        Queue,
        Feedback
    }
    public static LastTypes lastmenu = LastTypes.HotList;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public ContentPanel(SpotifyAPI.Player p, SpotifyAPI a) {
        libLanguage l = PublicValues.language;
        ConsoleLogging.info(l.translate("debug.buildcontentpanelbegin"));
        api = a;
        player = p;
        setPreferredSize(new Dimension(783, 600));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);

        settingsbutton = new JImagePanel();
        settingsbutton.setBounds(669, 11, 23, 23);
        add(settingsbutton);

        settingsbutton.addMouseListener(new MouseAdapter() {
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
                    settingsbutton.setRotation(0);
                }
            }
        });


        userbutton = new JImagePanel();
        userbutton.setBounds(702, 11, 23, 23);
        add(userbutton);

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

        threepointbutton = new JImagePanel();
        threepointbutton.setBounds(735, 11, 23, 23);
        add(threepointbutton);

        threepointdropdown = new DropDownMenu(threepointbutton, false);
        threepointdropdown.addItem(PublicValues.language.translate("ui.menu.help.about"), new Runnable() {
            @Override
            public void run() {
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
                    dialog.open(frame, PublicValues.language.translate("ui.menu.help.about"), cache.toString());
                } catch (Exception ex) {
                    ConsoleLogging.Throwable(ex);
                }
                dialog.getDialog().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        dialog.getDialog().dispose();
                    }
                });
            }
        });
        threepointdropdown.addItem(PublicValues.language.translate("ui.menu.file.exit"), new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        });


        switch (PublicValues.theme) {
            case DARK:
                settingsbutton.setImage(new Resources(true).readToInputStream("icons/settingslight.png"));
                userbutton.setImage(new Resources(true).readToInputStream("icons/userlight.png"));
                threepointbutton.setImage(new Resources(true).readToInputStream("icons/dotslight.png"));
                break;
            case LIGHT:
                settingsbutton.setImage(new Resources(true).readToInputStream("icons/settingsdark.png"));
                userbutton.setImage(new Resources(true).readToInputStream("icons/userdark.png"));
                threepointbutton.setImage(new Resources(true).readToInputStream("icons/dotsdark.png"));
                break;
        }

        playerarea = new EnginePanel();
        if(PublicValues.theme == Theme.LIGHT) {
            playerarea.setBorder(new LineBorder(Color.black));
        }else {
            playerarea.setBorder(new LineBorder(Color.gray));
        }
        playerarea.setBounds(72, 0, 565, 100);
        add(playerarea);
        playerarea.setLayout(null);

        playerimage = new JImagePanel();
        playerimage.setBounds(10, 11, 78, 78);
        playerarea.add(playerimage);

        playerimage.setImage(new Resources().readToInputStream("icons/nothingplaying.png"));

        playerareavolumeicon = new JImagePanel();
        playerareavolumeicon.setBounds(306, 75, 14, 14);
        playerarea.add(playerareavolumeicon);

        playerareavolumecurrent = new JLabel();
        playerareavolumecurrent.setBounds(489, 75, 35, 14);
        playerarea.add(playerareavolumecurrent);

        playerareavolumeslider = new JSlider();
        playerareavolumeslider.setBounds(334, 76, 145, 13);
        playerarea.add(playerareavolumeslider);

        playerareavolumecurrent.setText("10");

        playerareavolumeslider.setMinimum(0);
        playerareavolumeslider.setMaximum(10);
        playerareavolumeslider.setValue(10);

        player.getPlayer().setVolume(65536);

        if(PublicValues.theme == Theme.LIGHT) {
            playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumefulldark.png"));
        }else{
            playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumefullwhite.png"));
        }

        playerareavolumeslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(PublicValues.theme == Theme.LIGHT) {
                    playerareavolumecurrent.setText(String.valueOf(playerareavolumeslider.getValue()));
                    if (playerareavolumeslider.getValue() == 0) {
                        playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumemutedark.png"));
                    }else{
                        if(playerareavolumeslider.getValue() == 10) {
                            playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumefulldark.png"));
                        }else{
                            if(playerareavolumeslider.getValue() < 10 && playerareavolumeslider.getValue() > 4 ) {
                                playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumehalfdark.png"));
                            }
                        }
                    }
                }else{
                    playerareavolumecurrent.setText(String.valueOf(playerareavolumeslider.getValue()));
                    if (playerareavolumeslider.getValue() == 0) {
                        playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumemutewhite.png"));
                    }else{
                        if(playerareavolumeslider.getValue() == 10) {
                            playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumefullwhite.png"));
                        }else{
                            if(playerareavolumeslider.getValue() < 10 && playerareavolumeslider.getValue() > 4 ) {
                                playerareavolumeicon.setImage(new Resources().readToInputStream("icons/volumehalfwhite.png"));
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

        playertitle = new JLabel(l.translate("ui.player.title"));
        playertitle.setBounds(109, 11, 168, 14);
        playerarea.add(playertitle);

        playerdescription = new JLabel(l.translate("ui.player.description"));
        playerdescription.setBounds(109, 40, 138, 20);
        playerarea.add(playerdescription);

        playerarea.setDebug(true);

        playerplaypreviousbutton = new JImageButton();
        playerplaypreviousbutton.setBounds(287, 11, 70, 36);
        playerarea.add(playerplaypreviousbutton);

        playerplaypausebutton = new JImageButton();
        playerplaypausebutton.setBounds(369, 11, 69, 36);
        playerplaypausebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.getPlayer().playPause();
            }
        });
        playerarea.add(playerplaypausebutton);

        playerplaynextbutton = new JImageButton();
        playerplaynextbutton.setBounds(448, 11, 69, 36);
        playerarea.add(playerplaynextbutton);

        playercurrenttime = new JSlider();
        playercurrenttime.setValue(0);
        playercurrenttime.setBounds(306, 54, 200, 13);
        playerarea.add(playercurrenttime);

        playerplaytime = new JLabel("00:00");
        playerplaytime.setHorizontalAlignment(SwingConstants.RIGHT);
        playerplaytime.setBounds(244, 54, 57, 14);
        playerarea.add(playerplaytime);

        playerplaytimetotal = new JLabel("00:00");
        playerplaytimetotal.setBounds(506, 54, 49, 14);
        playerarea.add(playerplaytimetotal);

        switch (PublicValues.theme) {
            case DARK:
                playerplaypreviousbutton.setImage(new Resources().readToInputStream("icons/playerplaypreviouswhite.png"));
                playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerplaywhite.png"));
                playerplaynextbutton.setImage(new Resources().readToInputStream("icons/playerplaynextwhite.png"));
                break;
            case LIGHT:
                playerplaypreviousbutton.setImage(new Resources().readToInputStream("icons/playerplaypreviousdark.png"));
                playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerplaydark.png"));
                playerplaynextbutton.setImage(new Resources().readToInputStream("icons/playerplaynextdark.png"));
                break;
        }


        heart = new JImagePanel();
        heart.setBounds(525, 20, 24, 24);


        heart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(heart.isFilled) {
                    PublicValues.elevated.makeDelete("https://api.spotify.com/v1/me/tracks?ids=" + Objects.requireNonNull(player.getPlayer().currentPlayable()).toSpotifyUri().split(":")[2]);
                    heart.setImage(new Resources().readToInputStream("icons/heart.png"));
                    heart.isFilled = false;
                }else {
                    PublicValues.elevated.makePut("https://api.spotify.com/v1/me/tracks?ids=" + Objects.requireNonNull(player.getPlayer().currentPlayable()).toSpotifyUri().split(":")[2]);
                    heart.setImage(new Resources().readToInputStream("icons/heartfilled.png"));
                    heart.isFilled = true;
                }
            }
        });

        heart.setImage(new Resources().readToInputStream("icons/heart.png"));

        playerarea.add(heart);

        tabpanel = new JPanel();
        tabpanel.setBounds(0, 140, 784, 421);
        add(tabpanel);
        tabpanel.setLayout(null);

        librarypane = new JPanel();
        librarypane.setBounds(0, 0, 784, 421);
        tabpanel.add(librarypane);
        librarypane.setLayout(null);

        libraryshufflebutton = new JButton(l.translate("ui.library.shuffle"));
        libraryshufflebutton.setBounds(41, 398, 321, 23);
        librarypane.add(libraryshufflebutton);

        libraryplaybutton = new JButton(l.translate("ui.library.play"));
        libraryplaybutton.setBounds(408, 398, 321, 23);
        librarypane.add(libraryplaybutton);

        libraryscrollpane = new JScrollPane();
        libraryscrollpane.setBounds(0, 0, 784, 398);
        librarypane.add(libraryscrollpane);

        librarysonglist = new JTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        librarysonglist.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        l.translate("ui.library.songlist.songname"), l.translate("ui.library.songlist.filesize"), l.translate("ui.library.songlist.bitrate"), l.translate("ui.library.songlist.length")
                }
        ));
        librarydefaulttablemodel = (DefaultTableModel) librarysonglist.getModel();
        librarysonglist.getColumnModel().getColumn(0).setPreferredWidth(347);
        librarysonglist.getColumnModel().getColumn(3).setPreferredWidth(51);
        librarysonglist.setFillsViewportHeight(true);
        libraryscrollpane.setViewportView(librarysonglist);

        playlistsbutton = new JToggleButton(l.translate("ui.navigation.playlists"));
        playlistsbutton.setBounds(45, 111, 107, 23);
        add(playlistsbutton);

        librarybutton = new JToggleButton(l.translate("ui.navigation.library"));
        librarybutton.setBounds(162, 111, 107, 23);
        add(librarybutton);

        searchbutton = new JToggleButton(l.translate("ui.navigation.search"));
        searchbutton.setBounds(279, 111, 107, 23);
        add(searchbutton);

        hotlistbutton = new JToggleButton(l.translate("ui.navigation.hotlist"));
        hotlistbutton.setBounds(396, 111, 107, 23);
        add(hotlistbutton);

        queuebutton = new JToggleButton(l.translate("ui.navigation.queue"));
        queuebutton.setBounds(513, 111, 107, 23);
        add(queuebutton);

        feedbackbutton = new JToggleButton(l.translate("ui.navigation.feedback"));
        feedbackbutton.setBounds(630, 111, 107, 23);
        add(feedbackbutton);

        searchpane = new JPanel();
        searchpane.setBounds(0, 0, 784, 421);
        tabpanel.add(searchpane);
        searchpane.setLayout(null);

        hotlistpane = new JPanel();
        hotlistpane.setBounds(0, 0, 784, 421);
        tabpanel.add(hotlistpane);
        hotlistpane.setLayout(null);

        hotlistplaylistspanel = new JPanel();
        hotlistplaylistspanel.setBounds(0, 0, 259, 421);
        hotlistpane.add(hotlistplaylistspanel);
        hotlistplaylistspanel.setLayout(null);

        hotlistplaylistsscrollpanel = new JScrollPane();
        hotlistplaylistsscrollpanel.setBounds(0, 0, 259, 421);
        hotlistplaylistspanel.add(hotlistplaylistsscrollpanel);

        hotlistplayliststable = new JTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        hotlistplayliststable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        l.translate("ui.hotlist.playlistlist.playlists")
                }
        ));
        hotlistplayliststable.getColumnModel().getColumn(0).setPreferredWidth(623);
        hotlistplayliststable.setFillsViewportHeight(true);
        hotlistplayliststable.setColumnSelectionAllowed(true);
        hotlistplaylistsscrollpanel.setViewportView(hotlistplayliststable);

        hotlistsonglistpanel = new JPanel();
        hotlistsonglistpanel.setBounds(260, 0, 524, 421);
        hotlistpane.add(hotlistsonglistpanel);
        hotlistsonglistpanel.setLayout(null);

        hotslistsongscrollpanel = new JScrollPane();
        hotslistsongscrollpanel.setBounds(0, 0, 524, 421);
        hotlistsonglistpanel.add(hotslistsongscrollpanel);

        ContextMenu hotlistplaylistspanelrightclickmenu = new ContextMenu(hotlistplayliststable);
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

        hotlistsongstable = new JTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        hotlistsongstable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        l.translate("ui.hotlist.songlist.songtitle"), l.translate("ui.hotlist.songlist.filesize"), l.translate("ui.hotlist.songlist.bitrate"), l.translate("ui.hotlist.songlist.length")
                }
        ));
        hotlistsongstable.getColumnModel().getColumn(0).setPreferredWidth(363);
        hotlistsongstable.getColumnModel().getColumn(1).setPreferredWidth(89);
        hotlistsongstable.getColumnModel().getColumn(3).setPreferredWidth(96);
        hotlistsongstable.setFillsViewportHeight(true);
        hotlistsongstable.setColumnSelectionAllowed(true);
        hotslistsongscrollpanel.setViewportView(hotlistsongstable);

        playlistspane = new JPanel();
        playlistspane.setBounds(0, 0, 784, 421);
        tabpanel.add(playlistspane);
        playlistspane.setLayout(null);

        playlistsplaylistslist = new JPanel();
        playlistsplaylistslist.setBounds(0, 0, 259, 421);
        playlistspane.add(playlistsplaylistslist);
        playlistsplaylistslist.setLayout(null);

        playlistsplaylistsscroll = new JScrollPane();
        playlistsplaylistsscroll.setBounds(0, 0, 259, 421);
        playlistsplaylistslist.add(playlistsplaylistsscroll);

        playlistsplayliststable = new JTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        playlistsplayliststable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        l.translate("ui.playlists.playlists.playlistname")
                }
        ));
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
        playlistsplayliststable.getColumnModel().getColumn(0).setPreferredWidth(623);
        playlistsplayliststable.setFillsViewportHeight(true);
        playlistsplayliststable.setColumnSelectionAllowed(true);
        playlistsplaylistsscroll.setViewportView(playlistsplayliststable);

        playlistssonglist = new JPanel();
        playlistssonglist.setBounds(260, 0, 524, 421);
        playlistspane.add(playlistssonglist);
        playlistssonglist.setLayout(null);

        playlistssongsscroll = new JScrollPane();
        playlistssongsscroll.setBounds(0, 0, 524, 421);
        playlistssonglist.add(playlistssongsscroll);

        playlistssongtable = new JTable()  {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        playlistssongtable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        l.translate("ui.playlists.songslist.songtitle"), l.translate("ui.playlists.songslist.filesize"), l.translate("ui.playlists.songslist.bitrate"), l.translate("ui.playlists.songslist.length")
                }
        ));
        playlistssongtable.getColumnModel().getColumn(0).setPreferredWidth(363);
        playlistssongtable.getColumnModel().getColumn(1).setPreferredWidth(89);
        playlistssongtable.getColumnModel().getColumn(3).setPreferredWidth(96);
        playlistssongtable.setFillsViewportHeight(true);
        playlistssongtable.setColumnSelectionAllowed(true);
        playlistssongsscroll.setViewportView(playlistssongtable);

        queuepane = new JPanel();
        queuepane.setBounds(0, 0, 784, 421);
        tabpanel.add(queuepane);
        queuepane.setLayout(null);

        queueremovebutton = new JButton(l.translate("ui.queue.remove"));
        queueremovebutton.setBounds(0, 398, 784, 23);
        queuepane.add(queueremovebutton);

        queuescrollpane = new JScrollPane();
        queuescrollpane.setBounds(0, 0, 784, 395);
        queuepane.add(queuescrollpane);

        //noinspection unchecked
        queuelist = new JList(queuelistmodel);
        queuescrollpane.setViewportView(queuelist);

        feedbackpane = new JPanel();
        feedbackpane.setBounds(0, 0, 784, 421);
        tabpanel.add(feedbackpane);
        feedbackpane.setLayout(null);

        feedbackmakesurelabel = new JLabel(l.translate("ui.feedback.makesure"));
        feedbackmakesurelabel.setBounds(10, 23, 690, 25);
        feedbackpane.add(feedbackmakesurelabel);

        feedbackissuepanel = new JPanel();
        feedbackissuepanel.setBorder(new TitledBorder(null, l.translate("ui.feedback.issues.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        feedbackissuepanel.setBounds(0, 333, 426, 88);
        feedbackpane.add(feedbackissuepanel);
        feedbackissuepanel.setLayout(null);

        feedbackviewissuesbutton = new JButton(l.translate("ui.feedback.issues.view"));
        feedbackviewissuesbutton.setBounds(10, 21, 188, 56);
        feedbackissuepanel.add(feedbackviewissuesbutton);

        feedbackcreateissuebutton = new JButton(l.translate("ui.feedback.issues.create"));
        feedbackcreateissuebutton.setBounds(227, 21, 188, 56);
        feedbackissuepanel.add(feedbackcreateissuebutton);

        feedbackgithubbutton = new JButton(l.translate("ui.feedback.github.open"));
        feedbackgithubbutton.setBounds(466, 355, 250, 55);
        feedbackpane.add(feedbackgithubbutton);

        feedbackupdatespanel = new JPanel();
        feedbackupdatespanel.setBorder(new TitledBorder(null, l.translate("ui.updater.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        feedbackupdatespanel.setBounds(10, 59, 566, 249);
        feedbackpane.add(feedbackupdatespanel);
        feedbackupdatespanel.setLayout(null);

        feedbackupdaterversionfield = new JTextField();
        feedbackupdaterversionfield.setBounds(10, 85, 230, 20);
        feedbackupdatespanel.add(feedbackupdaterversionfield);
        feedbackupdaterversionfield.setColumns(10);

        feedbackwillbemovedlabel = new JLabel("The Updater will be moved to an other place");
        feedbackwillbemovedlabel.setBounds(10, 29, 327, 14);
        feedbackupdatespanel.add(feedbackwillbemovedlabel);

        feedbackupdaterdownloadbutton = new JButton(l.translate("ui.updater.downloadnewest"));
        feedbackupdaterdownloadbutton.setBounds(10, 149, 230, 23);
        feedbackupdatespanel.add(feedbackupdaterdownloadbutton);


        searchpane.setVisible(false); //Not show searchpane when window is opened
        librarypane.setVisible(false); //Not show librarypane when window is opened
        playlistspane.setVisible(false); //Not show playlistspane when window is opened
        queuepane.setVisible(false); //Not show queuepane when window is opened
        feedbackpane.setVisible(false); //Now show feedbackpane when window is opened
        hotlistpane.setVisible(false); //Not show hotlistpane when window is opened

        playlistsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StoppableThread thread = new StoppableThread(new StoppableThreadRunnable() {
                    @Override
                    public void run(int i) {
                        JSONObject list = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/playlists", new NameValuePair[]{new NameValuePair("limit", "50")}));
                        for(Object o : list.getJSONArray("items")) {
                            playlistsuricache.add(new JSONObject(o.toString()).getString("uri"));
                        }

                        while(true) {
                            String url = list.getString("next");
                            list = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/playlists", new NameValuePair[]{new NameValuePair("offset", url.split("\\?")[1].split("&")[0].replace("offset=", "")), new NameValuePair("limit", url.split("\\?")[1].split("&")[1].replace("limit=", ""))}));
                            for(Object o : list.getJSONArray("items")) {
                                playlistsuricache.add(new JSONObject(o.toString()).getString("uri"));
                            }
                            try {
                                if (list.getString("next").equals("")) {
                                    break;
                                }
                            }catch (JSONException exc) {
                                break;
                            }
                        }
                        try {
                            for (String s : playlistsuricache) {
                                ((DefaultTableModel) playlistsplayliststable.getModel()).addRow(new Object[]{api.getSpotifyApi().getPlaylist(s.split(":")[2]).build().execute().getName()});
                            }
                        } catch (ParseException | IOException | SpotifyWebApiException ignored) {

                        }
                    }
                }, false);
                if(playlistsplayliststable.getModel().getRowCount()==0) {
                    thread.start();
                }
            }
        });

        ContextMenu menu = new ContextMenu(playlistsplayliststable);
        menu.addItem(PublicValues.language.translate("ui.general.remove.playlist"), new Runnable() {
            @Override
            public void run() {
                api.getSpotifyApi().unfollowPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]);
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
                    playlistssonguricache.clear();
                    ((DefaultTableModel)playlistssongtable.getModel()).setRowCount(0);
                    try {
                        for (PlaylistTrack track : api.getSpotifyApi().getPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).build().execute().getTracks().getItems()) {
                            playlistssonguricache.add(track.getTrack().getUri());
                            ((DefaultTableModel)playlistssongtable.getModel()).addRow(new Object[] {track.getTrack().getName(), TrackUtils.calculateFileSizeKb((Track) track.getTrack()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getTrack().getDurationMs())});
                        }
                    } catch (IOException | SpotifyWebApiException | ParseException ignored) {

                    }
                }
            }
        });

        playlistssongtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) {
                    player.getPlayer().load(playlistssonguricache.get(playlistssongtable.getSelectedRow()), true, false);
                    try {
                        for(PlaylistTrack track : api.getSpotifyApi().getPlaylist(playlistsuricache.get(playlistsplayliststable.getSelectedRow()).split(":")[2]).build().execute().getTracks().getItems()) {
                            player.getPlayer().addToQueue(track.getTrack().getUri());
                        }
                    } catch (IOException | SpotifyWebApiException | ParseException ignored) {

                    }
                }
            }
        });

        setHotlistVisible();

        hotlistplayliststable.getTableHeader().setReorderingAllowed(false);

        hotlistsongstable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hotlistsongstable.setColumnSelectionInterval(0, hotlistsongstable.getColumnCount() - 1);
                if(e.getClickCount()==2) {
                    //player.getPlayer().tracks(true).next.clear();
                    player.getPlayer().load(hotlistsonglistcache.get(hotlistsongstable.getSelectedRow()), true, false);
                    for(String s : hotlistsonglistcache.subList(0, hotlistsongstable.getSelectedRow())) {
                        player.getPlayer().addToQueue(s);
                    }
                }
            }
        });
        ArrayList<String> hotlistsongs = new ArrayList<>();
        hotlistplayliststable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((DefaultTableModel)hotlistsongstable.getModel()).setRowCount(0);
                hotlistsonglistcache.clear();
                if(e.getClickCount()==2) {
                    try {
                        for (TrackSimplified track : api.getSpotifyApi().getAlbum(hotlistplaylistlistcache.get(hotlistplayliststable.getSelectedRow())).build().execute().getTracks().getItems()) {
                            String a = TrackUtils.getArtists(track.getArtists());
                            hotlistsonglistcache.add(track.getUri());
                            hotlistsongs.add(track.getUri());
                            ((DefaultTableModel) hotlistsongstable.getModel()).addRow(new Object[]{track.getName() + " - " + a, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
                        }
                    } catch (IOException | SpotifyWebApiException | ParseException ex) {
                        ConsoleLogging.Throwable(ex);
                    }
                }
            }
        });
        try {
            fetchHotlist();
        }catch (RuntimeException exc) {
            JOptionPane.showConfirmDialog(frame, PublicValues.language.translate("ui.error.critical"), PublicValues.language.translate("ui.error.critical.title"), JOptionPane.OK_CANCEL_OPTION);
        }

        playlistsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPlaylistsVisible();
            }
        });
        searchbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSearchVisible();
            }
        });
        hotlistbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setHotlistVisible();
            }
        });
        feedbackbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFeedbackVisible();
            }
        });
        queuebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setQueueVisible();
            }
        });
        librarybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLibraryVisible();
            }
        });

        StoppableThread thread = new StoppableThread(new StoppableThreadRunnable() {
            @SuppressWarnings("BusyWait")
            @Override
            public void run(int i) {
                hotlistbutton.setEnabled(false); //Lock user due to spotifys api rate limit
                searchbutton.setEnabled(false);
                queuebutton.setEnabled(false);
                playlistsbutton.setEnabled(false);
                JSONObject list = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/tracks", new NameValuePair[]{new NameValuePair("limit", "50")}));
                for(Object o : list.getJSONArray("items")) {
                    libraryuricache.add(new JSONObject(new JSONObject(o.toString()).getJSONObject("track").toString()).getString("uri"));
                }

                while(true) {
                    if(i==7) {
                        try {
                            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
                        } catch (InterruptedException e) {
                            ConsoleLogging.Throwable(e);
                        }
                        i=0;
                    }
                    String url = list.getString("next");
                    list = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/tracks", new NameValuePair[]{new NameValuePair("offset", url.split("\\?")[1].split("&")[0].replace("offset=", "")), new NameValuePair("limit", url.split("\\?")[1].split("&")[1].replace("limit=", ""))}));
                    for(Object o : list.getJSONArray("items")) {
                        libraryuricache.add(new JSONObject(new JSONObject(o.toString()).getJSONObject("track").toString()).getString("uri"));
                    }
                    try {
                        if (list.getString("next").equals("")) {
                            break;
                        }
                    }catch (JSONException exc) {
                        break;
                    }
                    i++;
                }
                for(String s : libraryuricache) {
                    try {
                        Track track = api.getSpotifyApi().getTrack(s.split(":")[2]).build().execute();
                        String a = TrackUtils.getArtists(track.getArtists());
                        ((DefaultTableModel)librarysonglist.getModel()).addRow(new Object[]{track.getName() + " - " + a, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
                    } catch (IOException | ParseException | SpotifyWebApiException e) {
                        hotlistbutton.setEnabled(true);
                        searchbutton.setEnabled(true);
                        queuebutton.setEnabled(true);
                        playlistsbutton.setEnabled(true);
                    }
                }
                hotlistbutton.setEnabled(true);
                searchbutton.setEnabled(true);
                queuebutton.setEnabled(true);
                playlistsbutton.setEnabled(true);
            }
        }, false);
        librarybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(librarysonglist.getModel().getRowCount()==0) {
                    thread.start();
                }
            }
        });

        queuebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queuelist.removeAll();
                queueuricache.clear();
                StoppableThread queueworker = new StoppableThread(new StoppableThreadRunnable() {
                    @Override
                    public void run(int i) {
                        try {
                            for (ContextTrackOuterClass.ContextTrack track : player.getPlayer().tracks(true).next) {
                                Track t = api.getSpotifyApi().getTrack(track.getUri().split(":")[2]).build().execute();
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
                }, false);
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

        libraryplaybutton.setEnabled(false);
        libraryshufflebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StoppableThread thread1 = new StoppableThread(new StoppableThreadRunnable() {
                    @Override
                    public void run(int i) {
                        player.getPlayer().load(libraryuricache.get(0), true, true);
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
                }, false);
                thread1.start();
            }
        });

        librarysonglist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) {
                    player.getPlayer().load(libraryuricache.get(librarysonglist.getSelectedRow()), true, false);
                }
            }
        });

        searchsearchfieldspanel = new JPanel();
        searchsearchfieldspanel.setBorder(new TitledBorder(null, l.translate("ui.search.searchfield.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchsearchfieldspanel.setBounds(0, 0, 784, 128);
        searchpane.add(searchsearchfieldspanel);
        searchsearchfieldspanel.setLayout(null);

        searchartistlabel = new JLabel(l.translate("ui.search.searchfield.artist"));
        searchartistlabel.setHorizontalAlignment(SwingConstants.RIGHT);
        searchartistlabel.setBounds(30, 25, 46, 14);
        searchsearchfieldspanel.add(searchartistlabel);

        searchsongtitlelabel = new JLabel(l.translate("ui.search.searchfield.title"));
        searchsongtitlelabel.setHorizontalAlignment(SwingConstants.RIGHT);
        searchsongtitlelabel.setBounds(10, 62, 66, 14);
        searchsearchfieldspanel.add(searchsongtitlelabel);

        searchclearfieldsbutton = new JButton(l.translate("ui.search.searchfield.button.clear"));
        searchclearfieldsbutton.setBounds(30, 94, 194, 23);
        searchsearchfieldspanel.add(searchclearfieldsbutton);

        searchclearfieldsbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchartistfield.setText("");
                searchsongtitlefield.setText("");
            }
        });

        searchfinditbutton = new JButton(l.translate("ui.search.searchfield.button.findit"));
        searchfinditbutton.setBounds(234, 94, 194, 23);
        searchsearchfieldspanel.add(searchfinditbutton);

        searchartistfield = new JTextField();
        searchartistfield.setBounds(86, 22, 356, 20);
        searchsearchfieldspanel.add(searchartistfield);
        searchartistfield.setColumns(10);

        searchsongtitlefield = new JTextField();
        searchsongtitlefield.setColumns(10);
        searchsongtitlefield.setBounds(86, 59, 356, 20);
        searchsearchfieldspanel.add(searchsongtitlefield);

        searchsearchfilterpanel = new JPanel();
        searchsearchfilterpanel.setLayout(null);
        searchsearchfilterpanel.setBorder(new TitledBorder(null, l.translate("ui.search.searchfield.filters.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchsearchfilterpanel.setBounds(452, 11, 322, 106);
        searchsearchfieldspanel.add(searchsearchfilterpanel);

        searchfilterexcludeexplicit = new JRadioButton(l.translate("ui.search.searchfield.filters.excludeexplicit"));
        searchfilterexcludeexplicit.setBounds(6, 24, 130, 23);
        searchsearchfilterpanel.add(searchfilterexcludeexplicit);

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

        searchfinditbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean excludeExplicit = searchfilterexcludeexplicit.isSelected();
                try {
                    if(searchsongtitlefield.getText().equals("")) {
                        if(!searchartistfield.getText().equals("")) {
                            searchsonglistcache.clear();
                            ((DefaultTableModel)searchsonglist.getModel()).setRowCount(0);
                            for (Track t : api.getSpotifyApi().searchTracks(searchartistfield.getText()).limit(50).build().execute().getItems()) {
                                String artists = TrackUtils.getArtists(t.getArtists());
                                if (excludeExplicit) {
                                    if (!t.getIsExplicit()) {
                                        searchsonglistcache.add(t.getUri());
                                        api.addSongToList(artists, t, searchsonglist);
                                    }
                                } else {
                                    searchsonglistcache.add(t.getUri());
                                    api.addSongToList(artists, t, searchsonglist);
                                }
                            }
                        }
                    }else {
                        searchsonglistcache.clear();
                        ((DefaultTableModel)searchsonglist.getModel()).setRowCount(0);
                        for (Track t : api.getSpotifyApi().searchTracks(searchsongtitlefield.getText()).limit(50).build().execute().getItems()) {
                            String artists = TrackUtils.getArtists(t.getArtists());
                            if (!searchartistfield.getText().equals("")) {
                                if (!artists.toLowerCase().contains(searchartistfield.getText().toLowerCase())) {
                                    continue;
                                }
                            }
                            if (excludeExplicit) {
                                if (!t.getIsExplicit()) {
                                    searchsonglistcache.add(t.getUri());
                                    api.addSongToList(artists, t, searchsonglist);
                                }
                            } else {
                                searchsonglistcache.add(t.getUri());
                                api.addSongToList(artists, t, searchsonglist);
                            }
                        }
                    }
                } catch (IOException | SpotifyWebApiException | ParseException ex) {
                    ConsoleLogging.Throwable(ex);
                }
            }
        });

        searchscrollpanel = new JScrollPane();
        searchscrollpanel.setBounds(0, 139, 784, 282);
        searchpane.add(searchscrollpanel);

        searchsonglist = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        searchsonglist.getTableHeader().setReorderingAllowed(false);
        searchsonglist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2) {
                    if(player.getPlayer().isReady()) {
                        player.getPlayer().load(searchsonglistcache.get(searchsonglist.getSelectedRow()), true, false);
                    }else{
                        player.retry();
                        if(player.getPlayer().isReady()) {
                            player.getPlayer().load(searchsonglistcache.get(searchsonglist.getSelectedRow()), true, false);
                        }else{
                            JOptionPane.showConfirmDialog(frame, PublicValues.language.translate("ui.error.critical2.text"), PublicValues.language.translate("ui.error.critical2.title"), JOptionPane.OK_CANCEL_OPTION);
                        }
                    }
                    searchsonglist.setColumnSelectionInterval(0, searchsonglist.getColumnCount() - 1);
                }else{
                    searchsonglist.setColumnSelectionInterval(0, searchsonglist.getColumnCount() - 1);
                }
            }
        });
        searchsonglist.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        l.translate("ui.search.songlist.songname"), l.translate("ui.search.songlist.filesize"), l.translate("ui.search.songlist.bitrate"), l.translate("ui.search.songlist.length")
                }
        ));
        searchsonglist.getColumnModel().getColumn(0).setPreferredWidth(342);
        searchsonglist.getColumnModel().getColumn(1).setPreferredWidth(130);
        searchsonglist.setFillsViewportHeight(true);
        searchsonglist.setColumnSelectionAllowed(true);
        searchscrollpanel.setViewportView(searchsonglist);
        playercurrenttime.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ContentPanel.playerplaytime.setText(TrackUtils.getHHMMSSOfTrack(player.getPlayer().time()));
            }
        });
        feedbackgithubbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Open GitHub
            }
        });
        feedbackcreateissuebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Create an issue on GitHub
            }
        });
        feedbackviewissuesbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //View issues on GitHub
            }
        });
        feedbackupdaterversionfield.setEditable(false);
        feedbackupdaterdownloadbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DoubleArrayList updater = new Updater().updateAvailable();
                String version = ((GitHubAPI.Release)updater.getSecond(0)).version;
                try (BufferedInputStream in = new BufferedInputStream(new URL(((GitHubAPI.Release) updater.getSecond(0)).downloadURL).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream("SpotifyXP.jar")) {
                    byte[] dataBuffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                } catch (IOException e2) {
                    ConsoleLogging.Throwable(e2);
                }
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
        ContextMenu searchcontextmenu = new ContextMenu(searchsonglist);
        searchcontextmenu.addItem(PublicValues.language.translate("ui.general.addtolibrary"), new Runnable() {
            @Override
            public void run() {
                heart.isFilled = true;
                heart.setImage(new Resources().readToInputStream("icons/heartfilled.png"));
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
                thread.start();
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
        ConsoleLogging.info(l.translate("debug.buildcontentpanelend"));
    }

    void fetchOnlyFirstSongsFromUserLibrary() {
        DefaultTableModel model = (DefaultTableModel) librarysonglist.getModel();
        try {
            int count = 0;
            for(SavedTrack track : api.getSpotifyApi().getUsersSavedTracks().limit(10).build().execute().getItems()) {
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

    int counter = 0;

    void setThemeDark(Component co) {
        JPanel c = (JPanel) co;
        for(Component component : c.getComponents()) {
            component.setBackground(Colors.black);
            component.setForeground(Color.white);
            if(counter==0) {
                if (!(component.getParent().getBackground() == Colors.black)) {
                    component.getParent().setBackground(Colors.black);
                }
                counter++;
            }
            if(component instanceof JTable) {
                JTable table = (JTable) component;
                table.setGridColor(Colors.black);
                if(table.getBorder() instanceof  TitledBorder) {
                    TitledBorder border = (TitledBorder) table.getBorder();
                    border.setTitleColor(Color.white);
                }
            }
            if(component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                setThemeDark(panel);
            }
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
        hotlistbutton.setSelected(false);
        searchbutton.setSelected(false);
        librarybutton.setSelected(true);
        feedbackbutton.setSelected(false);
        playlistsbutton.setSelected(false);
        queuebutton.setSelected(false);
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
    }
    public void setButtonsVisible() {
        playerarea.setVisible(true);
        librarybutton.setVisible(true);
        playlistsbutton.setVisible(true);
        searchbutton.setVisible(true);
        hotlistbutton.setVisible(true);
        queuebutton.setVisible(true);
        feedbackbutton.setVisible(true);
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
        queuebutton.setSelected(false);
        queueVisible = false;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = true;
        feedbackVisible = false;
    }
    public void setFeedbackVisible() {
        lastmenu = LastTypes.Feedback;
        librarypane.setVisible(false);
        searchpane.setVisible(false);
        hotlistpane.setVisible(false);
        feedbackpane.setVisible(true);
        playlistspane.setVisible(false);
        queuepane.setVisible(false);
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
        queuebutton.setSelected(true);
        queueVisible = true;
        playlistsVisible = false;
        searchVisible = false;
        libraryVisble = false;
        hotlistVisible = false;
        feedbackVisible = false;
    }
    void fetchHotlist() {
        JSONObject stuff = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/top/artists"));
        StringBuilder genres = new StringBuilder();
        StringBuilder tracks = new StringBuilder();
        StringBuilder artists = new StringBuilder();
        for(Object o : new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/me/player/recently-played?limit=10")).getJSONArray("items")) {
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
        JSONObject recommendations = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/recommendations", new NameValuePair[] {new NameValuePair("seed_artists", fiveartists.toString()), new NameValuePair("seed_genres", fivegenres.toString()), new NameValuePair("seed_tracks", fivetracks.toString())}));
        for(Object o : recommendations.getJSONArray("tracks")) {
            JSONObject album = new JSONObject(new JSONObject(o.toString()).get("album").toString());
            String uri = album.getString("uri").split(":")[2];
            hotlistplaylistlistcache.add(uri);
            try {
                Album albumreq = api.getSpotifyApi().getAlbum(uri).build().execute();
                String a = TrackUtils.getArtists(albumreq.getArtists());
                ((DefaultTableModel)hotlistplayliststable.getModel()).addRow(new Object[] {albumreq.getName() + " - " + a});
            } catch (IOException | ParseException | SpotifyWebApiException e) {
                ConsoleLogging.Throwable(e);
            }
        }
    }
    public static JFrame frame = null;
    public static SettingsPanel settingsPanel = null;
    public void open() {
        JFrame mainframe = new JFrame("SpotifyXP - v" + PublicValues.version);
        frame = mainframe;
        try {
            mainframe.setIconImage(ImageIO.read(new Resources(false).readToInputStream("spotifyxp.png")));
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        settingsPanel = new SettingsPanel();
        settingsPanel.setBounds(0, 52, 784, 509);
        settingsPanel.setVisible(false);
        mainframe.add(settingsPanel, BorderLayout.CENTER);
        mainframe.add(this, BorderLayout.CENTER);
        mainframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainframe.setResizable(false);
        mainframe.setPreferredSize(new Dimension(793, 600));
        mainframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainframe.dispose();
            }
        });
        mainframe.setVisible(true);
        mainframe.pack();
    }
}
