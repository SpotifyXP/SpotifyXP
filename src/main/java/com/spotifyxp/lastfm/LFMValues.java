package com.spotifyxp.lastfm;

import java.util.Base64;

public class LFMValues {
    public static final String apikey = new String(Base64.getDecoder().decode("YTUxMmFjYjMyMjA1MTMyZTkxOWUzMGVjMmNlYTE4ZDc="));
    public static final String apisecret = new String(Base64.getDecoder().decode("MmQzMGM3ZmJjYzVkMDhkYmVkMDgzOGU5NTU0ZTAwYTk="));
    public static String username = "";
    public static int artistlimit = 10;
    public static int tracklimit = 20;
}
