package com.spotifyxp.swingextension;

import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.ClipboardUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public class RAWTextArea extends JPanel {
    private String content = "";

    public void copyText() {
        ClipboardUtil.set(content);
    }

    public void append(String text) {
        content = content + text;
        repaint();
    }

    public void setText(String text) {
        content = text;
        repaint();
    }

    public String getText() {
        return content;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(PublicValues.globalFontColor);
        Rectangle2D r = g.getFontMetrics().getStringBounds(content, g);
        int ycache = getFontMetrics(getFont()).getHeight() + 5;
        ArrayList<String> toDraw = new ArrayList<>(Arrays.asList(content.split("\n")));
        for (String s : toDraw) {
            g.drawString(s, 5, ycache);
            ycache += getFontMetrics(getFont()).getHeight() + 5;
        }
        setPreferredSize(new Dimension(getWidth(), ycache + 10));
    }
}
