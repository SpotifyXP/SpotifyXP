package com.spotifyxp.graphics;

import com.spotifyxp.PublicValues;

public enum Graphics {
    DOTS("dots"),
    HEART("heart"),
    HEARTFILLED("heartfilled"),
    HISTORY("history"),
    HISTORYSELECTED("historyselected"),
    MICROPHONE("microphone"),
    MICROPHONESELECTED("microphoneselected"),
    NOTHINGPLAYING("nothingplaying"),
    PLAYERPAUSE("playerpause"),
    PLAYERPlAY("playerplay"),
    PLAYERPLAYNEXT("playerplaynext"),
    PLAYERPLAYPREVIOUS("playerplayprevious"),
    REPEAT("repeat"),
    REPEATSELECTED("repeatselected"),
    SETTINGS("settings"),
    SHUFFLE("shuffle"),
    SHUFFLESELECTED("shuffleselected"),
    USER("user"),
    VOLUMEFULL("volumefull"),
    VOLUMEHALF("volumehalf"),
    VOLUMEMUTE("volumemute");
    final String path;
    Graphics(String resourcePath) {
        String fullPath = "/icons/" + resourcePath;
        switch (resourcePath) {
            case "historyselected":
            case "heartfilled":
            case "microphonefilled":
            case "microphoneselected":
            case "repeatselected":
            case "shuffleselected":
            case "nothingplaying":
            case "heart":
                path = fullPath + ".svg";
                break;
            default:
                if(PublicValues.theme.isLight()) {
                    path = fullPath + "dark.svg";
                }else{
                    path = fullPath + "white.svg";
                }
                break;
        }
    }

    public String getPath() {
        return path;
    }
}