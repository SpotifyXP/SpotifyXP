package com.spotifyxp.configuration;

public enum ConfigValues {
    showallrecommendations("settings.performance.showallrecommendations"),
    disableplayerstats("settings.performance.displayplayerstats"),
    username("user.settings.username"),
    password("user.settings.password");
    public final String name;
    ConfigValues(String n) {
        name = n;
    }
}
