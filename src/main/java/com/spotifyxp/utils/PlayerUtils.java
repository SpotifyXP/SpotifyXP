package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.com.spotify.Authentication;
import com.spotifyxp.deps.com.spotify.Keyexchange;
import com.spotifyxp.deps.com.spotify.connectstate.Connect;
import com.spotifyxp.deps.xyz.gianlu.librespot.ZeroconfServer;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.decoders.AudioQuality;
import com.spotifyxp.deps.xyz.gianlu.librespot.common.Utils;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.PlayerConfiguration;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLogging;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

public class PlayerUtils {
    private byte[] savedBlob;
    private String savedUsername;

    public Player buildPlayer() {
        Session.Builder builder = new Session.Builder()
                .setPreferredLocale(PublicValues.config.getString(ConfigValues.other_preferredlocale.name))
                .setDeviceType(Connect.DeviceType.COMPUTER)
                .setDeviceName(PublicValues.deviceName)
                .setDeviceId(Utils.randomHexString(new SecureRandom(), 40).toLowerCase());
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

            if (!PublicValues.config.getBoolean(ConfigValues.facebook.name)) {
                if(new File(PublicValues.fileslocation, "credentials.json").exists()) {
                    session = new Session.Builder()
                            .setPreferredLocale(PublicValues.config.getString(ConfigValues.other_preferredlocale.name))
                            .setDeviceType(Connect.DeviceType.COMPUTER)
                            .setDeviceName(PublicValues.deviceName)
                            .setDeviceId(Utils.randomHexString(new SecureRandom(), 40).toLowerCase())
                            .stored(new File(PublicValues.fileslocation, "credentials.json"))
                            .create();
                }else {
                    CompletableFuture<Session> sessionFuture = new CompletableFuture<>();
                    try (ZeroconfServer zeroconfServer = new ZeroconfServer.Builder()
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
                        zeroconfServer.close();
                    } catch (IOException e) {
                        ConsoleLogging.Throwable(e);
                        GraphicalMessage.sorryError("Failed to build player");
                        System.exit(0);
                    }
                }
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
        }catch (IllegalArgumentException e) {
            try {
                Session session = builder.blob(savedUsername, savedBlob).create();
                Player player = new Player(playerconfig, session);
                PublicValues.session = session;
                Events.subscribe(SpotifyXPEvents.internetConnectionDropped.getName(), connectionDroppedListener());
                Events.subscribe(SpotifyXPEvents.internetConnectionReconnected.getName(), connectionReconnectedListener());
                return player;
            }catch (Exception ex) {
                ConsoleLogging.Throwable(ex);
                GraphicalMessage.sorryError("Failed to build player");
                System.exit(0);
            }
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
}
