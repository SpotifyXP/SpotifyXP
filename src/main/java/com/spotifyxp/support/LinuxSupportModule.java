package com.spotifyxp.support;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.org.mpris.MPRISMediaPlayer;
import com.spotifyxp.deps.org.mpris.Metadata;
import com.spotifyxp.deps.org.mpris.TypeRunnable;
import com.spotifyxp.deps.org.mpris.mpris.PlaybackStatus;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed.NotFoundException;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.metadata.PlayableId;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

public class LinuxSupportModule implements SupportModule {
    @Override
    public String getOSName() {
        return "Linux";
    }

    @Override
    public void run() {
        PublicValues.enableMediaControl = false;
        if (!PublicValues.customSaveDir) {
            PublicValues.fileslocation = System.getProperty("user.home") + "/.local/share/SpotifyXP";
            PublicValues.appLocation = PublicValues.fileslocation;
            PublicValues.configfilepath = PublicValues.fileslocation + "/config.json";
            PublicValues.tempPath = System.getProperty("java.io.tmpdir");
        }
        try {
            MPRISMediaPlayer mediaPlayer = new MPRISMediaPlayer(
                    DBusConnection.newConnection(DBusConnection.DBusBusType.SESSION),
                    "spotifyxp"
            );
            PublicValues.mpris = mediaPlayer.buildMPRISMediaPlayer2None(
                    new MPRISMediaPlayer.MediaPlayer2Builder()
                            .setIdentity("SpotifyXP")
                            .setDesktopEntry("/home/werwolf2303/.local/share/applications/SpotifyXP.desktop"),
                    new MPRISMediaPlayer.PlayerBuilder()
                            .setCanControl(true)
                            .setCanPlay(true)
                            .setCanPause(true)
                            .setCanGoNext(true)
                            .setCanGoPrevious(true)
                            .setOnPlayPause(new TypeRunnable<Object>() {
                                @Override
                                public void run(Object value) {
                                    InstanceManager.getPlayer().getPlayer().playPause();
                                }
                            })
                            .setOnPlay(new TypeRunnable<Object>() {
                                @Override
                                public void run(Object value) {
                                    InstanceManager.getPlayer().getPlayer().play();
                                }
                            })
                            .setOnPause(new TypeRunnable<Object>() {
                                @Override
                                public void run(Object value) {
                                    InstanceManager.getPlayer().getPlayer().pause();
                                }
                            })
                            .setOnNext(new TypeRunnable<Object>() {
                                @Override
                                public void run(Object value) {
                                    InstanceManager.getPlayer().getPlayer().next();
                                }
                            })
                            .setOnPrevious(new TypeRunnable<Object>() {
                                @Override
                                public void run(Object value) {
                                    InstanceManager.getPlayer().getPlayer().previous();
                                }
                            })
                            .setMetadata(new Metadata.Builder()
                                    .setLength(0)
                                    .setTrackID(new DBusPath("/"))
                                    .build())
            );
            mediaPlayer.create();
        } catch (DBusException e) {
            ConsoleLogging.warning("Failed to initialize MPRIS support");
        }
        Events.subscribe(SpotifyXPEvents.onFrameReady.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                InstanceManager.getPlayer().getPlayer().addEventsListener(new Player.EventsListener() {
                    @Override
                    public void onContextChanged(@NotNull Player player, @NotNull String newUri) {

                    }

                    @Override
                    public void onTrackChanged(@NotNull Player player, @NotNull PlayableId id, @Nullable MetadataWrapper metadata, boolean userInitiated) {

                    }

                    @Override
                    public void onPlaybackEnded(@NotNull Player player) {

                    }

                    @Override
                    public void onPlaybackPaused(@NotNull Player player, long trackTime) {
                        try {
                            PublicValues.mpris.setPlaybackStatus(PlaybackStatus.PAUSED);
                        } catch (DBusException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onPlaybackResumed(@NotNull Player player, long trackTime) {
                        try {
                            PublicValues.mpris.setPlaybackStatus(PlaybackStatus.PLAYING);
                        } catch (DBusException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onPlaybackFailed(@NotNull Player player, @NotNull Exception e) {
                        try {
                            PublicValues.mpris.setPlaybackStatus(PlaybackStatus.STOPPED);
                        } catch (DBusException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    public void onTrackSeeked(@NotNull Player player, long trackTime) {

                    }

                    @Override
                    public void onMetadataAvailable(@NotNull Player player, @NotNull MetadataWrapper metadata) {
                        try {
                            PublicValues.mpris.setPlaybackStatus(PlaybackStatus.PLAYING);
                            assert metadata.id != null;
                            PublicValues.mpris.setMetadata(new Metadata.Builder()
                                    .setTrackID(new DBusPath("/"))
                                    .setTitle(metadata.getName())
                                    .setArtURL(URI.create(InstanceManager.getSpotifyApi().getTrack(metadata.id.toSpotifyUri().split(":")[2]).build().execute().getAlbum().getImages()[0].getUrl()))
                                    .setLength(metadata.duration())
                                    .setArtists(Collections.singletonList(metadata.getArtist()))
                                    .setAlbumName(metadata.getAlbumName())
                                    .build());
                        } catch (NotFoundException e) {
                            ConsoleLogging.warning("Resource not found in onMetadataAvailable");
                        } catch (DBusException | IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    public void onPlaybackHaltStateChanged(@NotNull Player player, boolean halted, long trackTime) {
                        if (halted) {
                            try {
                                PublicValues.mpris.setPlaybackStatus(PlaybackStatus.STOPPED);
                            } catch (DBusException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }

                    @Override
                    public void onInactiveSession(@NotNull Player player, boolean timeout) {

                    }

                    @Override
                    public void onVolumeChanged(@NotNull Player player, @Range(from = 0, to = 1) float volume) {

                    }

                    @Override
                    public void onPanicState(@NotNull Player player) {
                        try {
                            PublicValues.mpris.setPlaybackStatus(PlaybackStatus.STOPPED);
                        } catch (DBusException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    public void onStartedLoading(@NotNull Player player) {

                    }

                    @Override
                    public void onFinishedLoading(@NotNull Player player) {

                    }
                });
            }
        });
    }
}