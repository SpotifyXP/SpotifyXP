package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.utils.PlayerUtils;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
        new CustomSaveDir().runArgument(new File("data").getAbsolutePath()).run();
        PublicValues.config = new Config();

        for (SpotifyXPEvents s : SpotifyXPEvents.values()) {
            Events.register(s.getName(), true);
        }

        new PlayerUtils().buildPlayer();
    }
}
