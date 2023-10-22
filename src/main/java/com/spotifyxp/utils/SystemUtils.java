package com.spotifyxp.utils;

public class SystemUtils {
    public static int getUsableRAMmb() {
        return (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024));
    }
}
