package com.spotifyxp.utils;


import com.spotify.connectstate.Connect;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.logging.ConsoleLogging;
import xyz.gianlu.librespot.api.ApiServer;
import xyz.gianlu.librespot.api.PlayerApiServer;
import xyz.gianlu.librespot.api.PlayerWrapper;
import xyz.gianlu.librespot.api.SessionWrapper;
import xyz.gianlu.librespot.audio.decoders.AudioQuality;
import xyz.gianlu.librespot.core.Session;
import xyz.gianlu.librespot.mercury.MercuryClient;
import xyz.gianlu.librespot.player.Player;
import xyz.gianlu.librespot.player.PlayerConfiguration;
import xyz.gianlu.librespot.player.ShellEvents;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class PlayerUtils {
    public static Player buildPlayer() {
        Session.Builder builder2 = new Session.Builder()
                .setPreferredLocale("de")
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .setDeviceName("XPS")
                .setClientToken("")
                .setDeviceId(null);
        PlayerConfiguration playerconfig = new PlayerConfiguration.Builder()
                .setAutoplayEnabled(true)
                .setCrossfadeDuration(0)
                .setEnableNormalisation(true)
                .setInitialVolume(65536)
                .setLogAvailableMixers(true)
                .setMetadataPipe(new File(""))
                .setMixerSearchKeywords(new String[] {})
                .setNormalisationPregain(3)
                .setOutput(PlayerConfiguration.AudioOutput.MIXER)
                .setOutputClass("")
                .setOutputPipe(new File(""))
                .setPreferredQuality(AudioQuality.valueOf(PublicValues.config.get(ConfigValues.audioquality.name)))
                .setPreloadEnabled(true)
                .setReleaseLineDelay(20)
                .setVolumeSteps(64)
                .setBypassSinkVolume(false)
                .setLocalFilesPath(new File(""))
                .build();
        try {
            Session session = builder2.userPass(new Encryption().decrypt(PublicValues.config.get(ConfigValues.username.name)), new Encryption().decrypt(PublicValues.config.get(ConfigValues.password.name))).create();
            Player player = new Player(playerconfig, session);
            PublicValues.session = session;
            return player;
        } catch (IOException | MercuryClient.MercuryException | GeneralSecurityException |
                 Session.SpotifyAuthenticationException e) {
            ConsoleLogging.Throwable(e);
        }
        return null;
    }
}
