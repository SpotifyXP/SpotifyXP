package com.spotifyxp.configuration;

import com.spotifyxp.audio.Quality;

public enum ConfigValues {
    audioquality("settings.playback.quality", ConfigValueTypes.STRING, Quality.NORMAL.configValue()),
    theme("settings.ui.theme", ConfigValueTypes.STRING, "DARKGREEN"),
    mypalpath("settings.mypal.path", ConfigValueTypes.STRING, ""),
    showallrecommendations("settings.performance.showallrecommendations", ConfigValueTypes.BOOLEAN, false),
    disableplayerstats("settings.performance.displayplayerstats", ConfigValueTypes.BOOLEAN, false),
    username("user.settings.username", ConfigValueTypes.STRING, ""),
    password("user.settings.password", ConfigValueTypes.STRING, ""),
    language("user.settings.language", ConfigValueTypes.STRING, "English"),
    hideExceptions("general.exception.visibility", ConfigValueTypes.BOOLEAN, false),
    lastfmusername("lastfm.user.settings.username", ConfigValueTypes.STRING, ""),
    lastfmpassword("lastfm.user.settings.password", ConfigValueTypes.STRING, ""),
    lastfmtracklimit("lastfm.user.settings.tracklimit", ConfigValueTypes.INT, 20),
    lastfmartistlimit("lastfm.user.settings.artistlimit", ConfigValueTypes.INT, 10),
    facebook("user.settings.isfacebook", ConfigValueTypes.BOOLEAN, false),
    spconnect("user.settings.spconnect", ConfigValueTypes.BOOLEAN, false);
    public final String name;
    public final ConfigValueTypes type;
    public final Object defaultValue;
    ConfigValues(String name, ConfigValueTypes type, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }
}
