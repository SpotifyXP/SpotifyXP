package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.guielements.SpotifyBrowseSection;
import com.spotifyxp.utils.AsyncActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SpotifySectionPanel extends JScrollPane implements View {
    public static JButton backButton;
    public static JLayeredPane contentPanel;
    public static JLabel title;
    private UnofficialSpotifyAPI.SpotifyBrowseSection section;

    public SpotifySectionPanel() {
        contentPanel = new JLayeredPane();

        javax.swing.SwingUtilities.invokeLater(() -> getVerticalScrollBar().setValue(0));

        setVisible(false);
        setViewportView(contentPanel);

        contentPanel.setLayout(null);
        contentPanel.setBackground(getBackground());

        backButton = new JButton(PublicValues.language.translate("ui.back"));
        backButton.setBounds(0, 0, 89, 23);
        backButton.setForeground(PublicValues.globalFontColor);
        backButton.addActionListener(new AsyncActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(title);
            contentPanel.add(backButton, JLayeredPane.PALETTE_LAYER);
            contentPanel.revalidate();
            contentPanel.repaint();
            ContentPanel.switchView(ContentPanel.lastView);
            ContentPanel.enableTabSwitch();

            // Back button is still visible. Redrawing the entire window should fix it
            ContentPanel.frame.revalidate();
            ContentPanel.frame.repaint();
        }));
        contentPanel.add(backButton, JLayeredPane.PALETTE_LAYER);

        title = new JLabel();
        title.setBounds(0, 0, 784, 50);
        title.setBackground(contentPanel.getBackground());
        title.setForeground(PublicValues.globalFontColor);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(null);
        title.setFont(title.getFont().deriveFont(Font.BOLD).deriveFont(23f));

        contentPanel.add(title);
    }

    void fillIt() {
        title.setText(section.getHeader().getText().getTitle());

        int yCache = 80;
        int xCache = 10;
        int width = getWidth() - 32;
        int height = 261;
        int spacing = 70;
        int titleHeight = getFontMetrics(title.getFont()).getHeight();
        int titleSpacing = 5;
        ArrayList<Integer> skip = new ArrayList<>();
        for(int i = 0; i < section.getBody().size(); i++) {
            if(skip.contains(i)) {
                continue;
            }
            UnofficialSpotifyAPI.SpotifyBrowseEntry entry = section.getBody().get(i);
            if(entry.getComponent().getId().contains("carousel")) {
                JLabel titleOfEntry = new JLabel(entry.getText().getTitle());
                titleOfEntry.setForeground(PublicValues.globalFontColor);
                titleOfEntry.setBounds(xCache, yCache - titleHeight - titleSpacing, width, titleHeight);
                SpotifyBrowseSection spotifyBrowseSection = new SpotifyBrowseSection(entry.getChildren().get(), xCache, yCache, width, height);

                contentPanel.add(titleOfEntry);
                contentPanel.add(spotifyBrowseSection);

                yCache += height + spacing;
            }
            if(entry.getComponent().getCategory().contains("card") && !entry.getChildren().isPresent()) {
                JLabel titleOfEntry = new JLabel(section.getBody().get(i-1).getText().getTitle());
                titleOfEntry.setForeground(PublicValues.globalFontColor);
                titleOfEntry.setBounds(xCache, yCache - titleHeight - titleSpacing, width, titleHeight);
                ArrayList<ArrayList<String>> entries = new ArrayList<>();
                for(int j = 0; j < section.getBody().subList(i, section.getBody().size()).size(); j++) {
                    UnofficialSpotifyAPI.SpotifyBrowseEntry cardEntry = section.getBody().subList(i, section.getBody().size()).get(j);
                    if(cardEntry.getComponent().getCategory().contains("card")) {
                        entries.add(new ArrayList<>(Arrays.asList(cardEntry.getText().getTitle(), cardEntry.getText().getDescription().orElse(""), cardEntry.getText().getSubtitle().orElse(""), cardEntry.getEvents().get().getEvents().get(0).getData_uri().get().getUri())));
                        skip.add(i + j);
                    } else {
                        break;
                    }
                }
                SpotifyBrowseSection spotifyBrowseSection = new SpotifyBrowseSection(entries, xCache, yCache, width, height);

                contentPanel.add(titleOfEntry);
                contentPanel.add(spotifyBrowseSection);
            }
        }

        contentPanel.setPreferredSize(new Dimension(width, yCache + 10));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void fillWith(UnofficialSpotifyAPI.SpotifyBrowseSection section) {
        this.section = section;
        fillIt();
    }

    @Override
    public void makeVisible() {
        setVisible(true);
    }

    @Override
    public void makeInvisible() {
        setVisible(false);
    }
}
