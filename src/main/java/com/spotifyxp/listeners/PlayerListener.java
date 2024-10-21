package com.spotifyxp.listeners;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Episode;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.metadata.PlayableId;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.panels.PlayerArea;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.SVGUtils;
import com.spotifyxp.utils.SpotifyUtils;
import com.spotifyxp.utils.TrackUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is the binding between the UI and librespot
 */
@SuppressWarnings("CanBeFinal")
public class PlayerListener implements Player.EventsListener {
    private final com.spotifyxp.api.Player pl;
    public static boolean pauseTimer = false;
    public static boolean locked = true;

    class PlayerThread extends TimerTask {
        public void run() {
            if (!pauseTimer) {
                if (!PublicValues.spotifyplayer.isPaused()) {
                    try {
                        PlayerArea.playercurrenttime.setMaximum(TrackUtils.getSecondsFromMS(Objects.requireNonNull(pl.getPlayer().currentMetadata()).duration()));
                        PlayerArea.playercurrenttime.setValue(TrackUtils.getSecondsFromMS(pl.getPlayer().time()));
                    } catch (NullPointerException ex) {
                        //No song is playing
                    }
                }
            }
        }
    }

    public static Timer timer = new Timer();

    public PlayerListener(com.spotifyxp.api.Player p) {
        pl = p;
    }

    @Override
    public void onContextChanged(@NotNull Player player, @NotNull String s) {

    }

    @Override
    public void onTrackChanged(@NotNull Player player, @NotNull PlayableId playableId, @Nullable MetadataWrapper metadataWrapper, boolean b) {
        if (!TrackUtils.isTrackLiked(playableId.toSpotifyUri().split(":")[2])) {
            PlayerArea.heart.isFilled = false;
            PlayerArea.heart.setImage(Graphics.HEART.getPath());
        } else {
            PlayerArea.heart.isFilled = true;
            PlayerArea.heart.setImage(Graphics.HEARTFILLED.getPath());
        }
        if (PlayerArea.playerarealyricsbutton.isFilled) {
            PublicValues.lyricsDialog.open(playableId.toSpotifyUri());
        }
        Events.triggerEvent(SpotifyXPEvents.trackNext.getName());
        if (!PublicValues.config.getBoolean(ConfigValues.disableplayerstats.name)) {
            timer.schedule(new PlayerThread(), 0, 1000);
            try {
                StringBuilder artists = new StringBuilder();
                switch (playableId.toSpotifyUri().split(":")[1]) {
                    case "episode":
                        Episode episode = InstanceManager.getSpotifyApi().getEpisode(playableId.toSpotifyUri().split(":")[2]).build().execute();
                        PlayerArea.playerplaytimetotal.setText(TrackUtils.getHHMMSSOfTrack(episode.getDurationMs()));
                        PlayerArea.playertitle.setText(episode.getName());
                        artists.append(episode.getShow().getPublisher());
                        try {
                            PlayerArea.playerimage.setImage(new URL(SpotifyUtils.getImageForSystem(episode.getImages()).getUrl()).openStream());
                        } catch (Exception e) {
                            ConsoleLogging.warning("Failed to load cover for track");
                            PlayerArea.playerimage.setImage(SVGUtils.svgToImageInputStreamSameSize(Graphics.NOTHINGPLAYING.getInputStream(), PlayerArea.playerimage.getSize()));
                        }
                        break;
                    case "track":
                        Track track = InstanceManager.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute();
                        PlayerArea.playerplaytimetotal.setText(TrackUtils.getHHMMSSOfTrack(track.getDurationMs()));
                        PlayerArea.playertitle.setText(track.getName());
                        for (ArtistSimplified artist : track.getArtists()) {
                            if (artists.toString().isEmpty()) {
                                artists.append(artist.getName());
                            } else {
                                artists.append(", ").append(artist.getName());
                            }
                        }
                        try {
                            PlayerArea.playerimage.setImage(new URL(SpotifyUtils.getImageForSystem(track.getAlbum().getImages()).getUrl()).openStream());
                        } catch (Exception e) {
                            ConsoleLogging.warning("Failed to load cover for track");
                            PlayerArea.playerimage.setImage(SVGUtils.svgToImageInputStreamSameSize(Graphics.NOTHINGPLAYING.getInputStream(), PlayerArea.playerimage.getSize()));
                        }
                        break;
                    default:
                        ConsoleLogging.warning(PublicValues.language.translate("playerlistener.playableid.unknowntype"));
                        Track t = InstanceManager.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute();
                        PlayerArea.playerplaytimetotal.setText(String.valueOf(t.getDurationMs()));
                        PlayerArea.playertitle.setText(t.getName());
                        for (ArtistSimplified artist : t.getArtists()) {
                            if (artists.toString().isEmpty()) {
                                artists.append(artist.getName());
                            } else {
                                artists.append(", ").append(artist.getName());
                            }
                        }
                        try {
                            PlayerArea.playerimage.setImage(new URL(SpotifyUtils.getImageForSystem(t.getAlbum().getImages()).getUrl()).openStream());
                        } catch (Exception e) {
                            ConsoleLogging.warning("Failed to load cover for track");
                            PlayerArea.playerimage.setImage(SVGUtils.svgToImageInputStreamSameSize(Graphics.NOTHINGPLAYING.getInputStream(), PlayerArea.playerimage.getSize()));
                        }
                }
                PlayerArea.playerdescription.setText(artists.toString());
            } catch (IOException | JSONException e) {
                GraphicalMessage.openException(e);
                ConsoleLogging.Throwable(e);
            }
        }
        if (InstanceManager.getUnofficialSpotifyApi().getLyrics(playableId.toSpotifyUri()) == null) {
            PlayerArea.playerarealyricsbutton.getJComponent().setToolTipText("No lyrics found");
        } else {
            PlayerArea.playerarealyricsbutton.getJComponent().setToolTipText(null);
        }
        locked = false;
        Events.triggerEvent(SpotifyXPEvents.playerLockRelease.getName());
    }

    @Override
    public void onPlaybackEnded(@NotNull Player player) {

    }

    @Override
    public void onPlaybackPaused(@NotNull Player player, long l) {
        PlayerArea.playerplaypausebutton.setImage(Graphics.PLAYERPlAY.getPath());
    }

    @Override
    public void onPlaybackResumed(@NotNull Player player, long l) {
        PlayerArea.playerplaypausebutton.setImage(Graphics.PLAYERPAUSE.getPath());
    }

    @Override
    public void onPlaybackFailed(@NotNull Player player, @NotNull Exception e) {
        ConsoleLogging.error("Player failed! retry");
        ConsoleLogging.Throwable(e);
        pl.retry();
    }

    @Override
    public void onTrackSeeked(@NotNull Player player, long l) {
        if (PlayerArea.playercurrenttime.getValue() < TrackUtils.getSecondsFromMS(InstanceManager.getPlayer().getPlayer().time())) {
            //Backwards
            Events.triggerEvent(SpotifyXPEvents.playerSeekedBackwards.getName());
        } else {
            //Forwards
            Events.triggerEvent(SpotifyXPEvents.playerSeekedForwards.getName());
        }
        if (!PublicValues.config.getBoolean(ConfigValues.disableplayerstats.name)) {
            PlayerArea.playercurrenttime.setValue(TrackUtils.getSecondsFromMS(l));
        }
        locked = false;
        Events.triggerEvent(SpotifyXPEvents.playerLockRelease.getName());
    }

    @Override
    public void onMetadataAvailable(@NotNull Player player, @NotNull MetadataWrapper metadataWrapper) {

    }

    @Override
    public void onPlaybackHaltStateChanged(@NotNull Player player, boolean b, long l) {

    }

    @Override
    public void onInactiveSession(@NotNull Player player, boolean b) {

    }

    @Override
    public void onVolumeChanged(@NotNull Player player, @Range(from = 0L, to = 1L) float v) {
        try {
            PlayerArea.playerareavolumeslider.setValue(TrackUtils.roundVolumeToNormal(v));
        } catch (NullPointerException ex) {
            //ContentPanel is not visible yet
        }
    }


    @Override
    public void onPanicState(@NotNull Player player) {
        GraphicalMessage.openException(new UnknownError("PanicState in PlayerListener"));
        PublicValues.blockLoading = false;
        pl.retry();
    }

    @Override
    public void onStartedLoading(@NotNull Player player) {
        PublicValues.blockLoading = true;
    }

    @Override
    public void onFinishedLoading(@NotNull Player player) {
        Events.triggerEvent(SpotifyXPEvents.trackLoadFinished.getName());
    }
}
