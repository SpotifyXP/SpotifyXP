package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.com.spotify.connectstate.Connect;
import com.spotifyxp.deps.xyz.gianlu.librespot.ZeroconfServer;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.decoders.AudioQuality;
import com.spotifyxp.deps.xyz.gianlu.librespot.common.Utils;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.PlayerConfiguration;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLogging;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;

public class PlayerUtils {
    private LoginDialog dialog;

    public Player buildPlayer() {
        PlayerConfiguration playerconfig = new PlayerConfiguration.Builder()
                .setAutoplayEnabled(PublicValues.config.getBoolean(ConfigValues.other_autoplayenabled.name))
                .setCrossfadeDuration(PublicValues.config.getInt(ConfigValues.other_crossfadeduration.name))
                .setEnableNormalisation(PublicValues.config.getBoolean(ConfigValues.other_enablenormalization.name))
                .setInitialVolume(65536)
                .setLogAvailableMixers(true)
                .setMetadataPipe(new File(PublicValues.fileslocation, "metapipe"))
                .setMixerSearchKeywords(PublicValues.config.getString(ConfigValues.other_mixersearchkeywords.name).split(","))
                .setNormalisationPregain(PublicValues.config.getInt(ConfigValues.other_normalizationpregain.name))
                .setOutput(PlayerConfiguration.AudioOutput.MIXER)
                .setOutputClass("")
                .setOutputPipe(new File(PublicValues.fileslocation, "outputpipe"))
                .setPreferredQuality(AudioQuality.valueOf(PublicValues.config.getString(ConfigValues.audioquality.name)))
                .setPreloadEnabled(PublicValues.config.getBoolean(ConfigValues.other_preloadenabled.name))
                .setReleaseLineDelay(PublicValues.config.getInt(ConfigValues.other_releaselinedelay.name))
                .setVolumeSteps(64)
                .setBypassSinkVolume(PublicValues.config.getBoolean(ConfigValues.other_bypasssinkvolume.name))
                .setLocalFilesPath(new File(PublicValues.fileslocation))
                .build();
        try {
            Session session = null;
            if (new File(PublicValues.fileslocation, "credentials.json").exists()) {
                Session.Configuration configuration = new Session.Configuration.Builder()
                        .setStoredCredentialsFile(new File(PublicValues.fileslocation, "credentials.json"))
                        .build();
                session = new Session.Builder(configuration)
                        .setPreferredLocale(PublicValues.config.getString(ConfigValues.other_preferredlocale.name))
                        .setDeviceType(Connect.DeviceType.COMPUTER)
                        .setDeviceName(PublicValues.deviceName)
                        .setDeviceId(Utils.randomHexString(new SecureRandom(), 40).toLowerCase())
                        .stored(new File(PublicValues.fileslocation, "credentials.json"))
                        .create();
            } else {
                if (dialog == null) {
                    dialog = new LoginDialog();
                    dialog.open();
                }
                Session.Configuration configuration = new Session.Configuration.Builder()
                        .setStoredCredentialsFile(new File(PublicValues.fileslocation, "credentials.json"))
                        .build();
                CompletableFuture<Session> sessionFuture = new CompletableFuture<>();
                try (ZeroconfServer zeroconfServer = new ZeroconfServer.Builder(configuration)
                        .setPreferredLocale(PublicValues.config.getString(ConfigValues.other_preferredlocale.name))
                        .setDeviceType(Connect.DeviceType.COMPUTER)
                        .setDeviceName(PublicValues.deviceName)
                        .setDeviceId(Utils.randomHexString(new SecureRandom(), 40).toLowerCase())
                        .setListenAll(true).create()) {
                    zeroconfServer.addSessionListener(new ZeroconfServer.SessionListener() {
                        @Override
                        public void sessionClosing(@NotNull Session var1) {
                            ConsoleLogging.warning("sessionClosing in zeroconf server! Unimplemented");
                        }

                        @Override
                        public void sessionChanged(@NotNull Session var1) {
                            sessionFuture.complete(var1);
                        }
                    });
                    session = sessionFuture.get();
                } catch (IOException e) {
                    ConsoleLogging.Throwable(e);
                    GraphicalMessage.sorryError("Failed to build player");
                    System.exit(0);
                }
            }
            Player player = new Player(playerconfig, session);
            PublicValues.session = session;
            Events.subscribe(SpotifyXPEvents.internetConnectionDropped.getName(), connectionDroppedListener());
            Events.subscribe(SpotifyXPEvents.internetConnectionReconnected.getName(), connectionReconnectedListener());
            if (dialog != null) dialog.close();
            return player;
        } catch (ConnectException | Session.SpotifyAuthenticationException | IllegalArgumentException e) {
            return buildPlayer();
        } catch (UnknownHostException offline) {
            GraphicalMessage.sorryErrorExit("No internet connection!");
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.sorryError("Failed to build player");
            System.exit(0);
        }
        return null;
    }

    EventSubscriber connectionReconnectedListener() {
        return new EventSubscriber() {
            @Override
            public void run(Object... data) {
                PublicValues.wasOffline = false;
                System.out.println("Connection re established!");
            }
        };
    }

    EventSubscriber connectionDroppedListener() {
        return new EventSubscriber() {
            @Override
            public void run(Object... data) {
                PublicValues.wasOffline = true;
                System.out.println("Connection dropped! Reconnecting...");
            }
        };
    }
}
