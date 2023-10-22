package com.spotifyxp.listeners;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Episode;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.metadata.PlayableId;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.events.Events;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.theming.themes.Legacy;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.SpotifyUtils;
import com.spotifyxp.utils.TrackUtils;
import org.apache.hc.core5.http.ParseException;
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
 * This class is the binding class between the UI and librespot
 */
@SuppressWarnings("CanBeFinal")
public class PlayerListener implements Player.EventsListener {
    private final com.spotifyxp.api.Player pl;
    public static boolean pauseTimer = false;
    public static boolean locked = true;
    class PlayerThread extends TimerTask {
        public void run() {
            if(!pauseTimer) {
                if(!PublicValues.spotifyplayer.isPaused()) {
                    try {
                        ContentPanel.playercurrenttime.setMaximum(TrackUtils.getSecondsFromMS(Objects.requireNonNull(pl.getPlayer().currentMetadata()).duration()));
                        ContentPanel.playercurrenttime.setValue(TrackUtils.getSecondsFromMS(pl.getPlayer().time()));
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

    boolean fromShuffle = false;

    @Override
    public void onTrackChanged(@NotNull Player player, @NotNull PlayableId playableId, @Nullable MetadataWrapper metadataWrapper, boolean b) {
        if (!ContentPanel.libraryuricache.contains(playableId.toSpotifyUri())) {
            ContentPanel.heart.isFilled = false;
            ContentPanel.heart.setImage(new Resources().readToInputStream("icons/heart.svg"));
        } else {
            ContentPanel.heart.isFilled = true;
            ContentPanel.heart.setImage(new Resources().readToInputStream("icons/heartfilled.svg"));
        }
        if(ContentPanel.playerarealyricsbutton.isFilled) {
            PublicValues.lyricsDialog.open(playableId.toSpotifyUri());
        }
        if(!PublicValues.config.get(ConfigValues.disableplayerstats.name).equals("true")) {
            timer.schedule(new PlayerThread(), 0, 1000);
            try {
                StringBuilder artists = new StringBuilder();
                switch (playableId.toSpotifyUri().split(":")[1]) {
                    case "episode":
                        Episode episode = Factory.getSpotifyApi().getEpisode(playableId.toSpotifyUri().split(":")[2]).build().execute();
                        ContentPanel.playerplaytimetotal.setText(TrackUtils.getHHMMSSOfTrack(episode.getDurationMs()));
                        ContentPanel.playertitle.setText(episode.getName());
                        artists.append(episode.getShow().getPublisher());
                        ContentPanel.playerimage.setImage(new URL(SpotifyUtils.getImageForSystem(episode.getImages()).getUrl()).openStream());
                        break;
                    case "track":
                        Track track = Factory.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute();
                        ContentPanel.playerplaytimetotal.setText(TrackUtils.getHHMMSSOfTrack(track.getDurationMs()));
                        ContentPanel.playertitle.setText(track.getName());
                        for (ArtistSimplified artist : track.getArtists()) {
                            if (artists.toString().isEmpty()) {
                                artists.append(artist.getName());
                            } else {
                                artists.append(", ").append(artist.getName());
                            }
                        }
                        ContentPanel.playerimage.setImage(new URL(SpotifyUtils.getImageForSystem(track.getAlbum().getImages()).getUrl()).openStream());
                        try {
                            if (PublicValues.canvasPlayer.isShown()) {
                                PublicValues.canvasPlayer.play();
                            }
                        }catch (NullPointerException e) {
                            //System not supported
                        }
                        break;
                    default:
                        ConsoleLogging.warning(PublicValues.language.translate("playerlistener.playableid.unknowntype"));
                        Track t = Factory.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute();
                        ContentPanel.playerplaytimetotal.setText(String.valueOf(t.getDurationMs()));
                        ContentPanel.playertitle.setText(t.getName());
                        for (ArtistSimplified artist : t.getArtists()) {
                            if (artists.toString().isEmpty()) {
                                artists.append(artist.getName());
                            } else {
                                artists.append(", ").append(artist.getName());
                            }
                        }
                        ContentPanel.playerimage.setImage(new URL(SpotifyUtils.getImageForSystem(t.getAlbum().getImages()).getUrl()).openStream());
                        try {
                            if (PublicValues.canvasPlayer.isShown()) {
                                PublicValues.canvasPlayer.play();
                            }
                        }catch (NullPointerException e) {
                            //System not supported
                        }
                }
                ContentPanel.playerdescription.setText(artists.toString());
            } catch (IOException | ParseException | SpotifyWebApiException | JSONException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }
        locked = false;
        Events.INTERNALtriggerPlayerLockReleaseEvents();
    }

    @Override
    public void onPlaybackEnded(@NotNull Player player) {

    }

    @Override
    public void onPlaybackPaused(@NotNull Player player, long l) {
        if(PublicValues.theme instanceof Legacy) {
            ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("legacyicons/playerplay.svg"));
        }else{
            if(PublicValues.theme.isLight()) {
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerplaydark.svg"));
            }else{
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerplaywhite.svg"));
            }
        }
        try {
            if (PublicValues.canvasPlayer.isShown()) {
                PublicValues.canvasPlayer.play();
            }
        }catch (NullPointerException e) {
            //System not supported
        }
    }

    @Override
    public void onPlaybackResumed(@NotNull Player player, long l) {
        if(PublicValues.theme instanceof Legacy) {
            ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("legacyicons/playerpause.svg"));
        }else{
            if(PublicValues.theme.isLight()) {
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerpausedark.svg"));
            }else{
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerpausewhite.svg"));
            }
        }
        try {
            if (PublicValues.canvasPlayer.isShown()) {
                PublicValues.canvasPlayer.play();
            }
        }catch (NullPointerException e) {
            //System not supported
        }
    }

    @Override
    public void onPlaybackFailed(@NotNull Player player, @NotNull Exception e) {
        ConsoleLogging.error("Player failed! retry");
        pl.retry();
    }

    @Override
    public void onTrackSeeked(@NotNull Player player, long l) {
        if(!PublicValues.config.get(ConfigValues.disableplayerstats.name).equals("true")) {
            ContentPanel.playercurrenttime.setValue(TrackUtils.getSecondsFromMS(l));
        }
        try {
            if (PublicValues.canvasPlayer.isShown()) {
                PublicValues.canvasPlayer.play();
            }
        }catch (NullPointerException e) {
            //System not supported
        }
        locked = false;
        Events.INTERNALtriggerPlayerLockReleaseEvents();
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
            ContentPanel.playerareavolumeslider.setValue(TrackUtils.roundVolumeToNormal(v));
        }catch (NullPointerException ex) {
            //ContentPanel is not visible yet
        }
    }


    @Override
    public void onPanicState(@NotNull Player player) {
        ExceptionDialog.open(new UnknownError("PanicState in PlayerListener"));
    }

    @Override
    public void onStartedLoading(@NotNull Player player) {

    }

    @Override
    public void onFinishedLoading(@NotNull Player player) {

    }
}
