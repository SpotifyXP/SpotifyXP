package com.spotifyxp.swingextension;

import com.spotifyxp.threading.DefThread;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("BusyWait")
public class JScrollText extends JLabel implements Runnable {
    String text;
    public JScrollText(String text) {
        this.text = text;
        super.setText(text);
    }

    DefThread t;
    boolean animate = true;

    public void run() {
        FontMetrics metrics = null;
        try {
            metrics = getFontMetrics(getFont());
        }catch (NullPointerException ignored) {
        }
        if(metrics != null) {
            while (animate) {
                if (!isVisible()) {
                    return;
                }
                int hgt = metrics.getHeight();
                int adv = metrics.stringWidth(getText());

                //Check if the text is completely visible (Don't need to scroll)
                Dimension size = new Dimension(adv + 2, hgt + 2);
                if (!(size.width > getWidth())) {
                    animate = false;
                    break;
                }
                //----

                String oldText = getText();
                String newText = oldText.substring(1) + oldText.charAt(0);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {
                }
                super.setText(newText);
            }
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        text = text + "       ";
        super.setText(text);
        animate = true;
        t = new DefThread(this);
        t.start();
    }
}
