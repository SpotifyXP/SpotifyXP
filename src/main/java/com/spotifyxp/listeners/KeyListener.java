package com.spotifyxp.listeners;

import com.spotifyxp.PublicValues;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

public class KeyListener {
    int playpause = 179;
    int next = 176;
    int previous = 177;
    public static boolean playpausepressed = false;
    public static boolean nextpressed = false;
    public static boolean previouspressed = false;
    public void start() {
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
        try {
            keyboardHook.addKeyListener(new GlobalKeyListener() {
                @Override
                public void keyPressed(GlobalKeyEvent globalKeyEvent) {
                    if (globalKeyEvent.getVirtualKeyCode() == playpause) {
                        playpausepressed = true;
                        PublicValues.spotifyplayer.playPause();
                    }
                    if (globalKeyEvent.getVirtualKeyCode() == next) {
                        nextpressed = true;
                        PublicValues.spotifyplayer.next();
                    }
                    if (globalKeyEvent.getVirtualKeyCode() == previous) {
                        previouspressed = true;
                        try {
                            PublicValues.spotifyplayer.previous();
                        }catch (IllegalStateException exc) {

                        }
                    }
                }

                @Override
                public void keyReleased(GlobalKeyEvent globalKeyEvent) {
                    if (globalKeyEvent.getVirtualKeyCode() == playpause) {
                        playpausepressed = false;

                    }
                    if (globalKeyEvent.getVirtualKeyCode() == next) {
                        nextpressed = false;

                    }
                    if (globalKeyEvent.getVirtualKeyCode() == previous) {
                        previouspressed = false;

                    }
                }
            });
        }catch (IllegalStateException ex) {
            keyboardHook.shutdownHook();
            start();
        }
    }
}
