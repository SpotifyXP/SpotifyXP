package com.spotifyxp.injector;

import java.util.ArrayList;

public class InjectingPoints {
    private static ArrayList<Runnable> onFrameReady = new ArrayList<>();


    public static void registerOnFrameReady(Runnable runnable) {
        onFrameReady.add(runnable);
    }

    public static void INTERNALinvokeOnFrameReady() {
        for(Runnable runnable : onFrameReady) {
            runnable.run();
        }
    }
}
