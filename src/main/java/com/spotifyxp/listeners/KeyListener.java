package com.spotifyxp.listeners;

import com.spotifyxp.PublicValues;
import com.spotifyxp.panels.ContentPanel;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

import java.awt.event.ActionEvent;

@SuppressWarnings("CanBeFinal")
public class KeyListener {
    int playpause = 179;
    int next = 176;
    int previous = 177;
    public static boolean playpausepressed = false;
    public static boolean nextpressed = false;
    public static boolean previouspressed = false;
    public void start() {
        if(PublicValues.appLocation.startsWith("/")) {
            return; //Operating system is Linux KeyListener not supported > Missing library files
        }
        if(PublicValues.isMacOS) {
            return; //Same as Linux
        }
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
                        }catch (IllegalStateException ignored) {

                        }
                    }
                    if (globalKeyEvent.isControlPressed()) {
                        if(!ContentPanel.pressedCTRL) ContentPanel.frame.setResizable(true);
                        ContentPanel.pressedCTRL = true;
                    }
                }

                @Override
                public void keyReleased(GlobalKeyEvent globalKeyEvent) {
                    if (globalKeyEvent.getVirtualKeyCode() == playpause) {
                        playpausepressed = false;

                    }else {
                        if (globalKeyEvent.getVirtualKeyCode() == next) {
                            nextpressed = false;

                        }else{
                            if (globalKeyEvent.getVirtualKeyCode() == previous) {
                                previouspressed = false;

                            }else {
                                if(ContentPanel.pressedCTRL) ContentPanel.frame.setResizable(false);
                                ContentPanel.pressedCTRL = false;
                            }
                        }
                    }
                }
            });
        }catch (IllegalStateException ex) {
            keyboardHook.shutdownHook();
            start();
        }
    }
}
