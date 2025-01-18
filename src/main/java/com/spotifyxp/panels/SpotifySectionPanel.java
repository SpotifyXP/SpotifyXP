package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.guielements.SpotifyBrowseSection;
import com.spotifyxp.utils.AsyncActionListener;

import javax.swing.*;
import java.awt.*;

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
        for(UnofficialSpotifyAPI.SpotifyBrowseEntry entry : section.getBody()) {
            if(entry.getComponent().getId().contains("carousel")) {
                JLabel titleOfEntry = new JLabel(entry.getText().getTitle());
                titleOfEntry.setForeground(PublicValues.globalFontColor);
                titleOfEntry.setBounds(xCache, yCache - titleHeight - titleSpacing, width, titleHeight);
                SpotifyBrowseSection spotifyBrowseSection = new SpotifyBrowseSection(entry.getChildren().get(), xCache, yCache, width, height);

                contentPanel.add(titleOfEntry);
                contentPanel.add(spotifyBrowseSection);

                yCache += height + spacing;
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
