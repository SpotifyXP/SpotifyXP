package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.com.spotify.Keyexchange;
import com.spotifyxp.deps.com.spotify.connectstate.Connect;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.decoders.AudioQuality;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.PlayerConfiguration;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.PlayerDefine;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLogging;

import java.io.File;
import java.net.UnknownHostException;

public class PlayerUtils {
    public PlayerDefine buildPlayer() {
        Session.Builder builder = new Session.Builder()
                .setPreferredLocale(PublicValues.config.getString(ConfigValues.other_preferredlocale.name))
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .setDeviceName(PublicValues.deviceName)
                .setClientToken("");
        PlayerConfiguration playerconfig = new PlayerConfiguration.Builder()
                .setAutoplayEnabled(PublicValues.config.getBoolean(ConfigValues.other_autoplayenabled.name))
                .setCrossfadeDuration(PublicValues.config.getInt(ConfigValues.other_crossfadeduration.name))
                .setEnableNormalisation(PublicValues.config.getBoolean(ConfigValues.other_enablenormalization.name))
                .setInitialVolume(65536)
                .setLogAvailableMixers(true)
                .setMetadataPipe(new File(""))
                .setMixerSearchKeywords(PublicValues.config.getString(ConfigValues.other_mixersearchkeywords.name).split(","))
                .setNormalisationPregain(PublicValues.config.getInt(ConfigValues.other_normalizationpregain.name))
                .setOutput(PlayerConfiguration.AudioOutput.MIXER)
                .setOutputClass("")
                .setOutputPipe(new File(""))
                .setPreferredQuality(AudioQuality.valueOf(PublicValues.config.getString(ConfigValues.audioquality.name)))
                .setPreloadEnabled(PublicValues.config.getBoolean(ConfigValues.other_preloadenabled.name))
                .setReleaseLineDelay(PublicValues.config.getInt(ConfigValues.other_releaselinedelay.name))
                .setVolumeSteps(64)
                .setBypassSinkVolume(PublicValues.config.getBoolean(ConfigValues.other_bypasssinkvolume.name))
                .setLocalFilesPath(new File(PublicValues.fileslocation))
                .build();
        try {
            Session session;
            if (!PublicValues.config.getBoolean(ConfigValues.facebook.name)) {
                session = builder.userPass(PublicValues.config.getString(ConfigValues.username.name), PublicValues.config.getString(ConfigValues.password.name)).create();
            } else {
                session = builder.facebook().create();
            }
            Player player = new Player(playerconfig, session);
            PublicValues.session = session;
            Events.subscribe(SpotifyXPEvents.internetConnectionDropped.getName(), connectionDroppedListener());
            Events.subscribe(SpotifyXPEvents.internetConnectionReconnected.getName(), connectionReconnectedListener());
            return player;
        }catch (Session.SpotifyAuthenticationException e) {
            new LoginDialog().openWithInvalidAuth();
            return buildPlayer();
        }catch (UnknownHostException offline) {
            GraphicalMessage.sorryErrorExit("No internet connection!");
        }catch(Exception e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.sorryError("Failed to build player");
            System.exit(0);
        }
        return null;
    }

    Runnable connectionReconnectedListener() {
        return new Runnable() {
            @Override
            public void run() {
                PublicValues.wasOffline = false;
                System.out.println("Connection re established!");
            }
        };
    }

    Runnable connectionDroppedListener() {
        return new Runnable() {
            @Override
            public void run() {
                PublicValues.wasOffline = true;
                System.out.println("Connection dropped! Reconnecting...");
            }
        };
    }

    public PlayerDefine buildPlayer(boolean ignore) throws Session.SpotifyAuthenticationException {
        Session.Builder builder = new Session.Builder()
                .setPreferredLocale(PublicValues.config.getString(ConfigValues.other_preferredlocale.name))
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .setDeviceName(PublicValues.deviceName)
                .setClientToken("");
        PlayerConfiguration playerconfig = new PlayerConfiguration.Builder()
                .setAutoplayEnabled(PublicValues.config.getBoolean(ConfigValues.other_autoplayenabled.name))
                .setCrossfadeDuration(PublicValues.config.getInt(ConfigValues.other_crossfadeduration.name))
                .setEnableNormalisation(PublicValues.config.getBoolean(ConfigValues.other_enablenormalization.name))
                .setInitialVolume(65536)
                .setLogAvailableMixers(true)
                .setMetadataPipe(new File(""))
                .setMixerSearchKeywords(PublicValues.config.getString(ConfigValues.other_mixersearchkeywords.name).split(","))
                .setNormalisationPregain(PublicValues.config.getInt(ConfigValues.other_normalizationpregain.name))
                .setOutput(PlayerConfiguration.AudioOutput.MIXER)
                .setOutputClass("")
                .setOutputPipe(new File(""))
                .setPreferredQuality(AudioQuality.valueOf(PublicValues.config.getString(ConfigValues.audioquality.name)))
                .setPreloadEnabled(PublicValues.config.getBoolean(ConfigValues.other_preloadenabled.name))
                .setReleaseLineDelay(PublicValues.config.getInt(ConfigValues.other_releaselinedelay.name))
                .setVolumeSteps(64)
                .setBypassSinkVolume(PublicValues.config.getBoolean(ConfigValues.other_bypasssinkvolume.name))
                .setLocalFilesPath(new File(PublicValues.fileslocation))
                .build();
        try {
            Session session;
            if (PublicValues.config.getString(ConfigValues.facebook.name).equalsIgnoreCase("false") || PublicValues.config.getString(ConfigValues.facebook.name).equalsIgnoreCase("")) {
                session = builder.userPass(PublicValues.config.getString(ConfigValues.username.name), PublicValues.config.getString(ConfigValues.password.name)).create();
            } else {
                session = builder.facebook().create();
            }
            Player player = new Player(playerconfig, session);
            PublicValues.session = session;
            return player;
        }catch (Session.SpotifyAuthenticationException e) {
            throw new Session.SpotifyAuthenticationException(Keyexchange.APLoginFailed.newBuilder().setErrorCode(Keyexchange.ErrorCode.BadCredentials).build());
        }catch (UnknownHostException e) {
            GraphicalMessage.sorryErrorExit("No internet connection!");
        }catch(Exception e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.sorryError("Failed to build player");
            System.exit(0);
        }
        return null;
    }
}
