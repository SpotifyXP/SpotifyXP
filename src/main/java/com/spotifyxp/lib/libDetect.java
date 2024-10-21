package com.spotifyxp.lib;

import java.util.Locale;

public class libDetect {
    public enum OSType {
        Windows, MacOS, Linux, Other, Steamos
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

    public static OSType getDetectedOS() {
        detectedOS = getOperatingSystemType();
        return detectedOS;
    }

    /**
     * Returns true if the operating system is Windows
     */
    public static boolean isWindows() {
        detectedOS = getOperatingSystemType();
        return detectedOS == OSType.Windows;
    }

    /**
     * Returns true if the operating system is Linux
     */
    public static boolean isLinux() {
        detectedOS = getOperatingSystemType();
        return detectedOS == OSType.Linux;
    }

    /**
     * Returns true if the operating system is MacOS
     */
    public static boolean isMacOS() {
        detectedOS = getOperatingSystemType();
        return detectedOS == OSType.MacOS;
    }

    /**
     * Returns true if the operating system is Other (unspecified)
     */
    public static boolean isOther() {
        detectedOS = getOperatingSystemType();
        return detectedOS == OSType.Other;
    }
}
