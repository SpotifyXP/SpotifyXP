package com.spotifyxp.listeners;

import com.spotifyxp.PublicValues;
import com.spotifyxp.panels.ContentPanel;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

@SuppressWarnings("CanBeFinal")
public class KeyListener {
    public static boolean playpausepressed = false;
    public static boolean nextpressed = false;
    public static boolean previouspressed = false;

    /**
     * Starts a key listener
     * <br> Listens for playpause, previous and next
     */
    public void start() {
        if(PublicValues.appLocation.startsWith("/")) {
            return; //Operating system is not supported
        }
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
        try {
            keyboardHook.addKeyListener(new GlobalKeyListener() {
                @Override
                public void keyPressed(GlobalKeyEvent globalKeyEvent) {
                    switch (globalKeyEvent.getVirtualKeyCode()) {
                        case 179:
                            //PlayPause
                            playpausepressed = true;
                            PublicValues.spotifyplayer.playPause();
                            break;
                        case 176:
                            //Next
                            nextpressed = true;
                            PublicValues.spotifyplayer.next();
                            break;
                        case 177:
                            //Previous
                            previouspressed = true;
                            PublicValues.spotifyplayer.previous();
                            break;
                    }
                    if (globalKeyEvent.isControlPressed()) {
                        if(!ContentPanel.pressedCTRL) ContentPanel.frame.setResizable(true);
                        ContentPanel.pressedCTRL = true;
                    }
                }

                @Override
                public void keyReleased(GlobalKeyEvent globalKeyEvent) {
                    switch (globalKeyEvent.getVirtualKeyCode()) {
                        case 179:
                            //PlayPause
                            playpausepressed = false;
                            break;
                        case 176:
                            //Next
                            nextpressed = false;
                            break;
                        case 177:
                            //Previous
                            previouspressed = false;
                            break;
                    }
                    if(ContentPanel.pressedCTRL) {
                        ContentPanel.frame.setResizable(false);
                        ContentPanel.pressedCTRL = false;
                    }
                }
            });
        }catch (IllegalStateException ex) {
            keyboardHook.shutdownHook();
            start();
        }
    }
}
