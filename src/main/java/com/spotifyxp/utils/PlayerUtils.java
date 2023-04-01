package com.spotifyxp.utils;


import com.spotifyxp.deps.com.spotify.connectstate.Connect;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.decoders.AudioQuality;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.PlayerConfiguration;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class PlayerUtils {
    public static Player buildPlayer() throws EOFException, Session.SpotifyAuthenticationException {
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
                .setLocalFilesPath(new File(PublicValues.fileslocation))
                .build();
        try {
            Session session = builder2.userPass(PublicValues.config.get(ConfigValues.username.name), PublicValues.config.get(ConfigValues.password.name)).create();
            Player player = new Player(playerconfig, session);
            PublicValues.session = session;
            return player;
        } catch (IOException | MercuryClient.MercuryException | GeneralSecurityException e) {
            //ConsoleLogging.Throwable(e);
            e.printStackTrace();
        }
        return null;
    }
}
