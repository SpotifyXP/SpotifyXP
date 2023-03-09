package com.spotifyxp.lib;

import java.util.Locale;

public class libDetect {
    enum OSType {
        Windows, MacOS, Linux, Other
    }
    protected static OSType detectedOS;
    static OSType getOperatingSystemType() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.contains("mac")) || (OS.contains("darwin"))) {
            detectedOS = OSType.MacOS;
        } else if (OS.contains("win")) {
            detectedOS = OSType.Windows;
        } else if (OS.contains("nux")) {
            detectedOS = OSType.Linux;
        } else {
            detectedOS = OSType.Other;
        }
        return detectedOS;
    }
    public static boolean isWindows() {
        detectedOS = getOperatingSystemType();
        return detectedOS == OSType.Windows;
    }
    public static boolean isLinux() {
        detectedOS = getOperatingSystemType();
        return detectedOS == OSType.Linux;
    }
    public static boolean isMacOS() {
        detectedOS = getOperatingSystemType();
        return detectedOS == OSType.MacOS;
    }
    public static boolean isOther() {
        detectedOS = getOperatingSystemType();
        return detectedOS == OSType.Other;
    }
}
