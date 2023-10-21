package com.spotifyxp.deps.de.werwolf2303.javasetuptool.components;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.PublicValues;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.Setup;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.utils.StreamUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class HTMLComponent extends JPanel implements com.spotifyxp.deps.de.werwolf2303.javasetuptool.components.Component {
    final String defaultHTML = "<a>Load HTML with the 'load()' function</a>";
    final JEditorPane pane;

    public HTMLComponent() {
        pane = new JEditorPane();
        pane.setEditable(false);
        pane.setContentType("text/html");
        pane.setText(defaultHTML);
        add(pane, BorderLayout.CENTER);
    }

    public String getName() {
        return "HTMLComponent";
    }

    public JPanel drawable() {
        return this;
    }

    public void init() {
        setPreferredSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height));
        pane.setPreferredSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height));
    }

    public void nowVisible() {

    }

    public void onLeave() {

    }

    public void giveComponents(JButton next, JButton previous, JButton cancel, JButton custom1, JButton custom2, Runnable fin, Setup.SetupBuilder builder) {

    }

    public void load(String html) {
        pane.setText(html);
    }

    public void load(URL url) {
        try {
            pane.setText(StreamUtils.inputStreamToString(url.openStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
