package com.spotifyxp.configuration;

public enum ConfigValues {
    sendanalytics("settings.analytics"),
    audioquality("settings.playback.quality"),
    theme("settings.ui.theme"),
    mypalpath("settings.mypal.path"),
    showallrecommendations("settings.performance.showallrecommendations"),
    disableplayerstats("settings.performance.displayplayerstats"),
    username("user.settings.username"),
    password("user.settings.password"),
    hideExceptions("general.exception.visibility");
    public final String name;
    ConfigValues(String n) {
        name = n;
    }
}
