package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.dialogs.AddPlaylistDialog;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.theming.themes.DarkGreen;
import com.spotifyxp.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
        new CustomSaveDir().runArgument(new File("data").getAbsolutePath()).run();
        PublicValues.config = new Config();
        PublicValues.language = new libLanguage();
        PublicValues.language.setNoAutoFindLanguage("en");
        PublicValues.language.setLanguageFolder("lang");

        PublicValues.theme = new DarkGreen();
        PublicValues.theme.initTheme();

        for (SpotifyXPEvents s : SpotifyXPEvents.values()) {
            Events.register(s.getName(), true);
        }

        AddPlaylistDialog dialog = new AddPlaylistDialog();
        dialog.show((data) -> {}, () -> {}, () -> {});
    }
}
