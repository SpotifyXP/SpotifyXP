package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.support.LinuxSupportModule;
import com.spotifyxp.theming.themes.DarkGreen;
import com.spotifyxp.updater.Updater;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException, InterruptedException, URISyntaxException, ClassNotFoundException {
        //new CustomSaveDir().runArgument(new File("data").getAbsolutePath()).run();

        for(SpotifyXPEvents events : SpotifyXPEvents.values()) {
            Events.register(events.getName(), true);
        }

        new LinuxSupportModule().run();

        PublicValues.config = new Config();
        PublicValues.language = new libLanguage();
        PublicValues.language.setNoAutoFindLanguage("en");
        PublicValues.language.setLanguageFolder("lang");

        PublicValues.theme = new DarkGreen();
        PublicValues.theme.initTheme();
        PublicValues.defaultHttpClient = new OkHttpClient();

        Updater.UpdateInfo updateInfo = new Updater.UpdateInfo();
        updateInfo.commit_id = "cd0fbdc7736f45dc6569e205e773a9ce77147796";
        updateInfo.url = "https://api.github.com/repos/SpotifyXP/SpotifyXP/actions/artifacts/2699340620/zip";
        new Updater().invoke(updateInfo);
    }
}
