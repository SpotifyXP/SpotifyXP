package com.spotifyxp.swingextension;

import javax.swing.*;
import java.awt.*;

public class JScrollText extends JLabel implements Runnable {
    public JScrollText(String text) {
        super.setText(text);
    }

    Thread t = new Thread(this);
    boolean animate = true;

    public void run() {
        long startofexec;
        long last = 0;
        while(animate) {
            //Vsync
            startofexec = System.currentTimeMillis();
            //---
            if (!isVisible()) {
                return;
            }
            FontMetrics metrics = getFontMetrics(getFont());
            int hgt = metrics.getHeight();
            int adv = metrics.stringWidth(getText());
            Dimension size = new Dimension(adv+2, hgt+2);
            if(!(size.width > getWidth())) {
                continue;
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
        if(t==null) {
            t = new Thread(this);
        }
        if(!t.isAlive()) {
            t.start();
        }
    }
}
