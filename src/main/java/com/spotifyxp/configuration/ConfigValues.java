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
    language("user.settings.language"),
    hideExceptions("general.exception.visibility"),
    lastfmusername("lastfm.user.settings.username"),
    lastfmpassword("lastfm.user.settings.password"),
    lastfmtracklimit("lastfm.user.settings.tracklimit"),
    facebook("user.settings.isfacebook"),
    lastfmartistlimit("lastfm.user.settings.artistlimit");
    public final String name;
    ConfigValues(String n) {
        name = n;
    }
}
