package com.spotifyxp.utils;


import com.spotifyxp.lib.libDetect;

public class UniversalPath {
    public static String getTempDirectory() {
        if(libDetect.isLinux()) {
            return "/tmp";
        }else{
            if(libDetect.isMacOS()) {
                return "/tmp";
            }else{
                if(libDetect.isWindows()) {
                    return System.getProperty("java.io.tmpdir");
                }else{
                    if(libDetect.isOther()) {
                        return "/tmp"; //Defaulting to Linux
                    }else{
                        return "";
                    }
                }
            }
        }
    }
    public static String getUserDirectory() {
        return System.getProperty("user.home");
    }
}
