package com.spotifyxp.configuration;

import com.spotifyxp.audio.Quality;

/**
 * Holds all registered config values with their type and default value
 */
public enum ConfigValues {
    audioquality("settings.playback.quality", ConfigValueTypes.STRING, Quality.NORMAL.configValue()),
    theme("settings.ui.theme", ConfigValueTypes.STRING, "DarkGreen"),
    mypalpath("settings.mypal.path", ConfigValueTypes.STRING, ""),
    showallrecommendations("settings.performance.showallrecommendations", ConfigValueTypes.BOOLEAN, false),
    disableplayerstats("settings.performance.displayplayerstats", ConfigValueTypes.BOOLEAN, false),
    username("user.settings.username", ConfigValueTypes.STRING, ""),
    password("user.settings.password", ConfigValueTypes.STRING, ""),
    language("user.settings.language", ConfigValueTypes.STRING, "English"),
    other_autoplayenabled("user.settings.other.autoplayenabled", ConfigValueTypes.BOOLEAN, true),
    other_crossfadeduration("user.settings.other.crossfadeduration", ConfigValueTypes.INT, 0),
    other_enablenormalization("user.settings.other.enablenormalization", ConfigValueTypes.BOOLEAN, true),
    other_normalizationpregain("user.settings.other.normalizationpregain", ConfigValueTypes.INT, 3),
    other_mixersearchkeywords("user.settings.other.mixersearchkeywords", ConfigValueTypes.STRING, ""),
    other_preloadenabled("user.settings.other.preloadenabled", ConfigValueTypes.BOOLEAN, true),
    other_releaselinedelay("user.settings.other.releaselinedelay", ConfigValueTypes.INT, 20),
    other_bypasssinkvolume("user.settings.other.bypasssinkvolume", ConfigValueTypes.BOOLEAN, false),
    other_preferredlocale("user.settings.other.preferredlocale", ConfigValueTypes.STRING, "en"),
    hideExceptions("general.exception.visibility", ConfigValueTypes.BOOLEAN, false),
    spconnect("user.settings.spconnect", ConfigValueTypes.BOOLEAN, false),
    webinteface("user.settings.webinterface", ConfigValueTypes.BOOLEAN, true),
    cache_disabled("user.settings.cache.disabled", ConfigValueTypes.BOOLEAN, false),
    disable_autoqueue("user.settings.autoqueue.disabled", ConfigValueTypes.BOOLEAN, false),
    load_all_tracks("user.settings.load_all_tracks", ConfigValueTypes.BOOLEAN, true),
    browse_view_style("user.settings.browse_view_style", ConfigValueTypes.INT, 0);

    public final String name;
    public final ConfigValueTypes type;
    public final Object defaultValue;

    ConfigValues(String name, ConfigValueTypes type, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the ConfigValues instance for the config value
     *
     * @param name name of the config value e.g. user.settings.spconnect
     * @return ConfigValues
     */
    public static ConfigValues get(String name) {
        for (ConfigValues value : ConfigValues.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
