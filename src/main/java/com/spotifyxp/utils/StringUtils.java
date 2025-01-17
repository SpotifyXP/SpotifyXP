package com.spotifyxp.utils;


public class StringUtils {
    public static String replaceLast(String original, String target, String replacement) {
        int lastIndex = original.lastIndexOf(target);

        if (lastIndex == -1) {
            // Target substring not found
            return original;
        }

        String beforeLast = original.substring(0, lastIndex);
        String afterLast = original.substring(lastIndex + target.length());

        return beforeLast + replacement + afterLast;
    }
}
