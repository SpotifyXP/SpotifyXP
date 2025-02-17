package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyHttpManager;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Playlist;
import com.spotifyxp.dialogs.AddPlaylistDialog;
import com.spotifyxp.dialogs.ChangePlaylistDialog;
import com.spotifyxp.dialogs.FollowPlaylist;
import com.spotifyxp.dialogs.SelectPlaylist;
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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException, InterruptedException {
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

        InstanceManager.getPlayer().getPlayer().waitReady();
        InstanceManager.getPkce();
        InstanceManager.getSpotifyApi();

        SelectPlaylist playlist = new SelectPlaylist(new SelectPlaylist.onPlaylistSelected() {
            @Override
            public void playlistSelected(String uri) {
                System.out.println("Selected playlist: " + uri);
            }
        });
        playlist.open();
    }
}
