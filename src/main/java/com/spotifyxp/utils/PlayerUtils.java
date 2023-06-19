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
import java.io.File;
import java.net.UnknownHostException;

public class PlayerUtils {
    public static Player buildPlayer() {
        Session.Builder builder = new Session.Builder()
                .setPreferredLocale("en")
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .setDeviceName(PublicValues.deviceName)
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
            Session session = builder.userPass(PublicValues.config.get(ConfigValues.username.name), PublicValues.config.get(ConfigValues.password.name)).create();
            Player player = new Player(playerconfig, session);
            PublicValues.session = session;
            return player;
        }catch (Session.SpotifyAuthenticationException e) {
            new LoginDialog().openWithInvalidAuth();
        }catch (UnknownHostException e) {
            GraphicalMessage.sorryError();
            System.exit(0);
        }catch(Exception e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
        return null;
    }
}
