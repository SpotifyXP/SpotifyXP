package com.spotifyxp.injector;

import java.util.ArrayList;

public class InjectingPoints {
    private static ArrayList<Runnable> onFrameReady = new ArrayList<>();


    /**
     * Registers an onFrameReady. This code gets executed when the SpotifyXP window is done drawing
     * @param runnable Runnable of code to execute
     */
    public static void registerOnFrameReady(Runnable runnable) {
        onFrameReady.add(runnable);
    }

    /**
     * <a style="color:red">(NTERNAL)</a> Do not use these
     */
    public static void INTERNALinvokeOnFrameReady() {
        for(Runnable runnable : onFrameReady) {
            runnable.run();
        }
    }
}
