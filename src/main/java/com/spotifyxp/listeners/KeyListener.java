package com.spotifyxp.listeners;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;

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
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
                @Override
                public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
                    switch (nativeEvent.getKeyCode()) {
                        case NativeKeyEvent.VC_MEDIA_PLAY:
                            //PlayPause
                            playpausepressed = true;
                            PublicValues.spotifyplayer.playPause();
                            break;
                        case NativeKeyEvent.VC_MEDIA_NEXT:
                            //Next
                            nextpressed = true;
                            PublicValues.spotifyplayer.next();
                            break;
                        case NativeKeyEvent.VC_MEDIA_PREVIOUS:
                            //Previous
                            previouspressed = true;
                            PublicValues.spotifyplayer.previous();
                            break;
                    }
                }

                @Override
                public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
                    switch (nativeEvent.getKeyCode()) {
                        case NativeKeyEvent.VC_MEDIA_PLAY:
                            //PlayPause
                            playpausepressed = false;
                            break;
                        case NativeKeyEvent.VC_MEDIA_NEXT:
                            //Next
                            nextpressed = false;
                            break;
                        case NativeKeyEvent.VC_MEDIA_PREVIOUS:
                            //Previous
                            previouspressed = false;
                            break;
                    }
                }
            });
        } catch (Exception ex) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                throw new RuntimeException(e);
            }
            ConsoleLogging.Throwable(ex);
        }
    }
}
