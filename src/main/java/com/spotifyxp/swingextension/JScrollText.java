package com.spotifyxp.swingextension;

import com.spotifyxp.threading.DefThread;
import javax.swing.*;
import java.awt.*;

public class JScrollText extends JLabel implements Runnable {
    public JScrollText(String text) {
        super.setText(text);
    }

    DefThread t;
    boolean animate = true;

    public void run() {
        long startofexec;
        long last = 0;
        FontMetrics metrics = getFontMetrics(getFont());
        while(animate) {
            //Hsync
            startofexec = System.currentTimeMillis();
            //---
            if (!isVisible()) {
                return;
            }
            int hgt = metrics.getHeight();
            int adv = metrics.stringWidth(getText());
            Dimension size = new Dimension(adv+2, hgt+2);
            if(!(size.width > getWidth())) {
                animate = false;
                break;
            }
            String oldText = getText();
            String newText = oldText.substring(1) + oldText.charAt(0);
            super.setText(newText);
            //HSync
            try {
                Thread.sleep(400);
            } catch (InterruptedException ignored) {
            }
            if(!(last == 0)) {
                if(System.currentTimeMillis() != startofexec + 400) {
                    try {
                        Thread.sleep(System.currentTimeMillis() - startofexec);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            last = System.currentTimeMillis() - startofexec;
            //---
        }
    }

    @Override
    public void setText(String text) {
        text = text + "       ";
        super.setText(text);
        animate = true;
        t = new DefThread(this);
        t.start();
    }
}
