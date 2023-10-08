package com.spotifyxp.utils;

import com.spotifyxp.deps.com.spotify.connectstate.Connect;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.decoders.AudioQuality;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.PlayerConfiguration;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;

import java.io.EOFException;
import java.io.File;
import java.net.UnknownHostException;

public class PlayerUtils {
    public static Player buildPlayer() throws EOFException {
        Session.Builder builder = new Session.Builder()
                .setPreferredLocale("en")
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .setDeviceName(PublicValues.deviceName)
                .setClientToken("");
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
            Session session;
            if(PublicValues.config.get(ConfigValues.facebook.name).equalsIgnoreCase("false") || PublicValues.config.get(ConfigValues.facebook.name).equalsIgnoreCase("")) {
                session = builder.userPass(PublicValues.config.get(ConfigValues.username.name), PublicValues.config.get(ConfigValues.password.name)).create();
            }else{
                session = builder.facebook().create();
            }
            Player player = new Player(playerconfig, session);
            PublicValues.session = session;
            return player;
        }catch(Exception e) {
            e.printStackTrace();
            ConsoleLogging.Throwable(e);
            GraphicalMessage.sorryError("Failed to build player");
            System.exit(0);
        }
        return null;
    }
}
