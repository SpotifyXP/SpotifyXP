package com.spotifyxp.lastfm;

import java.util.Base64;

public class LFMValues {
    public static String apikey = new String(Base64.getDecoder().decode("YTUxMmFjYjMyMjA1MTMyZTkxOWUzMGVjMmNlYTE4ZDc="));
    public static String apisecret = new String(Base64.getDecoder().decode("MmQzMGM3ZmJjYzVkMDhkYmVkMDgzOGU5NTU0ZTAwYTk="));
    public static String username = "";
    public static int artistlimit = 10;
    public static int tracklimit = 20;
    public static String candidate = "ALPHA";
    public static String version = "0.0.1";
}
