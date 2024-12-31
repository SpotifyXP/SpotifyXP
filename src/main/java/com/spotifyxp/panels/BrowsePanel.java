package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.guielements.SpotifyBrowseModule;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class BrowsePanel extends JScrollPane implements View {
    public static UnofficialSpotifyAPI.SpotifyBrowse spotifyBrowse;
    public static JPanel contentPanel;
    public static JPopupMenu popupMenu;
    public static DefTable table;
    public static ArrayList<String> genreIds;

    public BrowsePanel() {
        setBounds(0, 0, 784, 421);
        setVisible(false);
        contentPanel = new JPanel();
        setViewportView(contentPanel);
        contentPanel.setLayout(null);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    spotifyBrowse = UnofficialSpotifyAPI.getSpotifyBrowse();
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(PublicValues.config.getInt(ConfigValues.browse_view_style.name) == 1) {
                    displayBrowseTable();
                } else {
                    displayBrowseMetro();
                }
            }
        });
        thread.start();
        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    xyRunnable.run(e.getX(), e.getY());
                }
            }
        });
        popupMenu = new JPopupMenu();
        JCheckBoxMenuItem metroLayout = new JCheckBoxMenuItem("Metro layout"); //ToDo: Translate
        JCheckBoxMenuItem tableLayout = new JCheckBoxMenuItem("Table layout"); //ToDo: Translate
        boolean isTableLayout = PublicValues.config.getInt(ConfigValues.browse_view_style.name) == 1;
        metroLayout.setSelected(!isTableLayout);
        tableLayout.setSelected(isTableLayout);
        metroLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableLayout.setSelected(false);
                metroLayout.setSelected(true);
                PublicValues.config.write(ConfigValues.browse_view_style.name, 0);
                PublicValues.config.save();
                contentPanel.removeAll();
                contentPanel.revalidate();
                contentPanel.repaint();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        displayBrowseMetro();
                    }
                });
                thread.start();
            }
        });
        tableLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                metroLayout.setSelected(false);
                tableLayout.setSelected(true);
                PublicValues.config.write(ConfigValues.browse_view_style.name, 1);
                PublicValues.config.save();
                contentPanel.removeAll();
                contentPanel.revalidate();
                contentPanel.repaint();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        displayBrowseTable();
                    }
                });
                thread.start();
            }
        });
        popupMenu.add(metroLayout);
        popupMenu.add(tableLayout);
    }

    @FunctionalInterface
    public interface XYRunnable  {
        void run(int x, int y);
    }

    @FunctionalInterface
    public interface IDRunnable {
        void run(String id);
    }

    void displayBrowseTable() {
        genreIds = new ArrayList<>();
        table = new DefTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Name"
                }
        ));
        table.setForeground(PublicValues.globalFontColor);
        table.getTableHeader().setForeground(PublicValues.globalFontColor);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    xyRunnable.run(table.getX() + e.getX(), table.getY() + e.getY());
                    return;
                }
                if(e.getClickCount() == 2) {
                    idRunnable.run(genreIds.get(table.getSelectedRow()).split(":")[2]);
                }
            }
        });
        scrollPane.setBounds(10, 10, getWidth() - 30, getHeight() - 40);

        for(UnofficialSpotifyAPI.SpotifyBrowseEntry entry : spotifyBrowse.getBody()) {
            if(entry.getMetadata().isPresent()) {
                if(!entry.getMetadata().get().getVideoUrl().isPresent()
                        && !entry.getComponent().getCategory().equals("row")
                        && !entry.getComponent().getCategory().toLowerCase(Locale.ENGLISH).contains("sectionheader")
                        && entry.getCustom().isPresent()
                        && entry.getCustom().get().getBackgroundColor().isPresent()) {
                    table.addModifyAction(new Runnable() {
                        @Override
                        public void run() {
                            ((DefaultTableModel) table.getModel()).addRow(new Object[] {entry.getText().getTitle()});
                        }
                    });
                    genreIds.add(entry.getEvents().get().getEvents().get(0).getData_uri().get().getUri());
                }
            }
        }

        contentPanel.add(scrollPane);
        contentPanel.setPreferredSize(new Dimension(782, 405));
    }

    void displayBrowseMetro() {
        int yCache = 10;
        int xCache = 10;
        int xCount = 0;
        int elementWidth = (784 / 4) - 23;
        int elementHeight = (421 / 4) - 5;
        for(UnofficialSpotifyAPI.SpotifyBrowseEntry entry : spotifyBrowse.getBody()) {
            if(entry.getMetadata().isPresent()) {
                if(!entry.getMetadata().get().getVideoUrl().isPresent()
                        && !entry.getComponent().getCategory().equals("row")
                        && !entry.getComponent().getCategory().toLowerCase(Locale.ENGLISH).contains("sectionheader")
                        && entry.getCustom().isPresent()
                        && entry.getCustom().get().getBackgroundColor().isPresent()) {
                    if(xCount == 4) {
                        xCount = 0;
                        xCache = 10;
                        yCache += elementHeight + 20;
                    }

                    try {
                        String uri = "";
                        for(UnofficialSpotifyAPI.SpotifyBrowseEntryImagesImage image : entry.getImages().get().getImages()) {
                            if(image.getType() == UnofficialSpotifyAPI.SpotifyBrowseEntryImagesImageTypes.MAIN) {
                                uri = image.getUri();
                                break;
                            }
                        }
                        SpotifyBrowseModule panel = new SpotifyBrowseModule(xCache, yCache, entry.getText().getTitle(), new URL(uri).openStream(), elementWidth, elementHeight, entry.getEvents().get().getEvents().get(0).getData_uri().get().getUri(), xyRunnable, idRunnable);
                        panel.setBackground(Color.decode(entry.getCustom().get().getBackgroundColor().get()));
                        panel.setBounds(xCache, yCache, elementWidth, elementHeight);
                        contentPanel.add(panel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    xCount += 1;
                    xCache += elementWidth + 20;
                }
            }
        }
        contentPanel.setPreferredSize(new Dimension(784, yCache));
        revalidate();
        repaint();
    }

    XYRunnable xyRunnable = new XYRunnable() {
        @Override
        public void run(int x, int y) {
            popupMenu.show(contentPanel, x, y);
        }
    };

    IDRunnable idRunnable = new IDRunnable() {
        @Override
        public void run(String id) {
            ContentPanel.switchView(Views.BROWSESECTION);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ContentPanel.sectionPanel.fillWith(UnofficialSpotifyAPI.getSpotifyBrowseSection(id));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
            ContentPanel.blockTabSwitch();
        }
    };

    @Override
    public void makeVisible() {
        setVisible(true);
    }

    @Override
    public void makeInvisible() {
        setVisible(false);
    }
}
