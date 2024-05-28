package com.spotifyxp.panels;


import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Album;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import com.spotifyxp.exception.ElementNotFoundException;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.utils.AsyncMouseListener;
import com.spotifyxp.utils.ClipboardUtil;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.TrackUtils;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ArtistPanel {
    private static ArrayList<Object[]> elements;
    private JPanel contentPanel;
    private JPanel artistPanel;
    private JScrollPane artistPanelScrollPanel;
    private JImagePanel artistImage;
    private JScrollPane artistPopularSongListScrollPanel;
    private JScrollPane artistAlbumTableScrollPanel;
    private JLabel artistPopularSongListLabel;
    private JLabel artistAlbumLabel;
    private JLabel artistName;
    private JTable artistPopularSongListTable;
    private ContextMenu artistPopularSongListContextMenu;
    private JTable artistAlbumTable;
    private ContextMenu artistAlbumContextMenu;
    public static ArrayList<String> albumuricache = new ArrayList<>();
    public static ArrayList<String> popularuricache = new ArrayList<>();
    public static ArrayList<Runnable> runWhenOpeningArtistPanel = new ArrayList<>();
    public static boolean isFirst = false;


    public ArtistPanel() {
        elements = new ArrayList<>();

        artistName.setForeground(PublicValues.globalFontColor);

        artistPopularSongListLabel.setText(PublicValues.language.translate("ui.artistpanel.popular"));
        artistPopularSongListLabel.setForeground(PublicValues.globalFontColor);
        artistPopularSongListTable.setForeground(PublicValues.globalFontColor);
        artistPopularSongListTable.getTableHeader().setForeground(PublicValues.globalFontColor);
        artistPopularSongListTable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        PublicValues.language.translate("ui.search.songlist.songname"), PublicValues.language.translate("ui.search.songlist.filesize"), PublicValues.language.translate("ui.search.songlist.bitrate"), PublicValues.language.translate("ui.search.songlist.length")
                }
        ));

        artistAlbumLabel.setText(PublicValues.language.translate("ui.artistpanel.albums"));
        artistAlbumLabel.setForeground(PublicValues.globalFontColor);
        artistAlbumTable.setForeground(PublicValues.globalFontColor);
        artistAlbumTable.getTableHeader().setForeground(PublicValues.globalFontColor);
        artistAlbumTable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        PublicValues.language.translate("ui.search.songlist.songname")
                }
        ));

        initializeInteractivity();
        initializeContextMenu();
        addAllToElementList();
    }

    private void initializeContextMenu() {
        artistPopularSongListContextMenu = new ContextMenu(artistPopularSongListTable);
        artistAlbumContextMenu = new ContextMenu(artistAlbumTable);
        artistAlbumContextMenu.addItem(PublicValues.language.translate("ui.general.copyuri"), () -> ClipboardUtil.set(albumuricache.get(artistAlbumTable.getSelectedRow())));
    }

    private void initializeInteractivity() {
        artistAlbumTable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    ContentPanel.isLastArtist = true;
                    contentPanel.setVisible(false);
                    Search.searchplaylistpanel.setVisible(true);
                    Search.searchplaylistsongscache.clear();
                    ((DefaultTableModel) Search.searchplaylisttable.getModel()).setRowCount(0);
                    try {
                        Album album = InstanceManager.getSpotifyApi().getAlbum(albumuricache.get(artistAlbumTable.getSelectedRow()).split(":")[2]).build().execute();
                        for (TrackSimplified simplified : album.getTracks().getItems()) {
                            ((DefaultTableModel) Search.searchplaylisttable.getModel()).addRow(new Object[]{simplified.getName(), TrackUtils.calculateFileSizeKb(simplified.getDurationMs()), TrackUtils.getBitrate(), TrackUtils.getHHMMSSOfTrack(simplified.getDurationMs())});
                            Search.searchplaylistsongscache.add(simplified.getUri());
                        }
                    } catch (IOException | ParseException | SpotifyWebApiException ex) {
                        GraphicalMessage.openException(ex);
                        ConsoleLogging.Throwable(ex);
                    }
                }
            }
        }));

        artistPopularSongListTable.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    InstanceManager.getPlayer().getPlayer().load(popularuricache.get(artistPopularSongListTable.getSelectedRow()), true, false, false);
                    TrackUtils.addAllToQueue(popularuricache, artistPopularSongListTable);
                }
            }
        }));
    }

    private void addAllToElementList() {
        addToElementList("contentPanel", contentPanel);
        addToElementList("artistPanel", artistPanel);
        addToElementList("artistPanelScrollPanel", artistPanelScrollPanel);
        addToElementList("artistImage", artistImage);
        addToElementList("artistPopularSongListScrollPanel", artistPopularSongListScrollPanel);
        addToElementList("artistAlbumTableScrollPanel", artistAlbumTableScrollPanel);
        addToElementList("artistPopularSongListLabel", artistPopularSongListLabel);
        addToElementList("artistAlbumLabel", artistAlbumLabel);
        addToElementList("artistName", artistName);
        addToElementList("artistPopularSongListTable", artistPopularSongListTable);
        addToElementList("artistAlbumTable", artistAlbumTable);
        addToElementList("artistPopularSongListContextMenu", artistPopularSongListContextMenu);
    }

    private void addToElementList(String name, Object instance) {
        elements.add(new Object[]{name, instance});
    }

    public static JPanel getContainer() {
        return getElementByNameAutoThrow("contentPanel", JPanel.class);
    }

    public static <E> E getElementByName(String name, Class<E> elementType) throws ElementNotFoundException {
        for(Object[] element : elements) {
            if(element[0].equals(name)) {
                return elementType.cast(element[1]);
            }
        }
        throw new ElementNotFoundException(elementType);
    }

    public static <E> E getElementByNameAutoThrow(String name, Class<E> elementType) {
        for(Object[] element : elements) {
            if(element[0].equals(name)) {
                return elementType.cast(element[1]);
            }
        }
        throw new RuntimeException(new ElementNotFoundException(elementType));
    }

    private void createUIComponents() {
        artistPopularSongListTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        artistAlbumTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void openPanel() {
        ContentPanel.blockTabSwitch();
        ContentPanel.artistPanelVisible = true;
        DefThread asyncExecute = new DefThread(new Runnable() {
            @Override
            public void run() {
                for(Runnable runnable : runWhenOpeningArtistPanel) {
                    runnable.run();
                }
            }
        });
        asyncExecute.start();
        javax.swing.SwingUtilities.invokeLater(() -> artistPanelScrollPanel.getVerticalScrollBar().setValue(0));
    }
}
