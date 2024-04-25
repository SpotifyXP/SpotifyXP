package com.spotifyxp.dialogs;

import com.spotifyxp.panels.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class HTMLDialog extends JDialog {
    private JPanel contentPane;
    private JTextPane html;

    public HTMLDialog() {
        setContentPane(contentPane);
        setModal(true);
    }

    public void open(String title, String htmlcode) {
        setTitle(title);
        setLocation(ContentPanel.frame.getCenter());
        setResizable(false);
        html.setMinimumSize(new Dimension(getHeight(), getWidth()));
        html.setEditable(false);
        html.setContentType("text/html");
        if (htmlcode.contains("<html>")) {
            html.setText(htmlcode);
        } else {
            html.setText("<html>" + htmlcode + "</html>");
        }
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                html.setMinimumSize(new Dimension(getHeight(), getWidth()));
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        pack();
        setVisible(true);

    }

    public JTextPane getHtmlComponent() {
        return html;
    }

    public JDialog getDialog() {
        return this;
    }
}
