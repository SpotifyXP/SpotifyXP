package com.spotifyxp.listeners;

import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.TrackUtils;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.json.JSONException;
import org.json.JSONObject;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.metadata.PlayableId;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("CanBeFinal")
public class PlayerListener implements Player.EventsListener {
    private final SpotifyAPI.Player pl;
    private final SpotifyAPI a;
    public static boolean pauseTimer = false;
    class PlayerThread extends TimerTask {
        public void run() {
            if(!pauseTimer) {
                try {
                    ContentPanel.playercurrenttime.setMaximum(TrackUtils.getSecondsFromMS(Objects.requireNonNull(pl.getPlayer().currentMetadata()).duration()));
                    ContentPanel.playercurrenttime.setValue(TrackUtils.getSecondsFromMS(pl.getPlayer().time()));
                } catch (NullPointerException ex) {
                    //No song is playing
                }
            }
        }
    }
    public static Timer timer = new Timer();
    public PlayerListener(SpotifyAPI.Player p, SpotifyAPI api) {
        pl = p;
        a = api;
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

        if(!PublicValues.config.get(ConfigValues.disableplayerstats.name).equals("true")) {
            timer.schedule(new PlayerThread(), 0, 1000);
            try {
                //        HH MM SS
                //Display 00:00:00

                //Removed feature announce song name
                //if(!ContentPanel.frame.isVisible()) {
                //BackgroundService.trayDialog.getTrayIcon().displayMessage("SpotifyXP: Now Playing", a.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute().getName() + " - " + TrackUtils.getArtists(a.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute().getArtists()), TrayIcon.MessageType.INFO);
                //}
                //---
                StringBuilder artists = new StringBuilder();
                if(playableId.toSpotifyUri().contains("episode")) {
                    ContentPanel.playerplaytimetotal.setText(TrackUtils.getHHMMSSOfTrack(a.getSpotifyApi().getEpisode(playableId.toSpotifyUri().split(":")[2]).build().execute().getDurationMs()));
                    ContentPanel.playertitle.setText(a.getSpotifyApi().getEpisode(playableId.toSpotifyUri().split(":")[2]).build().execute().getName());
                    artists.append(a.getSpotifyApi().getEpisode(playableId.toSpotifyUri().split(":")[2]).build().execute().getShow().getPublisher());
                    JSONObject root = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/episodes/" + playableId.toSpotifyUri().split(":")[2]));
                    for (Object object : root.getJSONArray("images")) {
                        JSONObject urls = new JSONObject(object.toString());
                        ContentPanel.playerimage.setImage(new URL(urls.getString("url")).openStream());
                        break;
                    }
                }
                else{
                    if(playableId.toSpotifyUri().contains("track")) {
                        ContentPanel.playerplaytimetotal.setText(TrackUtils.getHHMMSSOfTrack(a.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute().getDurationMs()));
                        ContentPanel.playertitle.setText(a.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute().getName());
                        for (ArtistSimplified artist : a.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute().getArtists()) {
                            if (artists.toString().equals("")) {
                                artists.append(artist.getName());
                            } else {
                                artists.append(", ").append(artist.getName());
                            }
                        }
                        JSONObject root = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/tracks/" + playableId.toSpotifyUri().split(":")[2]));
                        JSONObject album = new JSONObject(root.get("album").toString());
                        for (Object object : album.getJSONArray("images")) {
                            JSONObject urls = new JSONObject(object.toString());
                            ContentPanel.playerimage.setImage(new URL(urls.getString("url")).openStream());
                            break;
                        }


                        if(PublicValues.lyricsDialog!=null) {
                            PublicValues.lyricsDialog.open(playableId.toSpotifyUri());
                        }
                    }else{
                        ConsoleLogging.warning(PublicValues.language.translate("playerlistener.playableid.unknowntype"));
                        ContentPanel.playerplaytimetotal.setText(TrackUtils.getHHMMSSOfTrack(a.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute().getDurationMs()));
                        ContentPanel.playertitle.setText(a.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute().getName());
                        for (ArtistSimplified artist : a.getSpotifyApi().getTrack(playableId.toSpotifyUri().split(":")[2]).build().execute().getArtists()) {
                            if (artists.toString().equals("")) {
                                artists.append(artist.getName());
                            } else {
                                artists.append(", ").append(artist.getName());
                            }
                        }
                        JSONObject root = new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/tracks/" + playableId.toSpotifyUri().split(":")[2]));
                        JSONObject album = new JSONObject(root.get("album").toString());
                        for (Object object : album.getJSONArray("images")) {
                            JSONObject urls = new JSONObject(object.toString());
                            ContentPanel.playerimage.setImage(new URL(urls.getString("url")).openStream());
                            break;
                        }
                    }
                }
                ContentPanel.playerdescription.setText(artists.toString());
            } catch (IOException | ParseException | SpotifyWebApiException | JSONException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }
    }

    @Override
    public void onPlaybackEnded(@NotNull Player player) {
        //timer.cancel();
    }

    @Override
    public void onPlaybackPaused(@NotNull Player player, long l) {
        switch (PublicValues.theme) {
            case DARK:
            case MacOSDark:
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerplaywhite.svg"));
                break;
            case LIGHT:
            case MacOSLight:
            case QuaQua:
            case WINDOWS:
            case UGLY:
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerplaydark.svg"));
                break;
            case LEGACY:
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("legacyicons/playerplay.svg"));
                break;
        }
        //timer.cancel();
    }

    @Override
    public void onPlaybackResumed(@NotNull Player player, long l) {
        switch (PublicValues.theme) {
            case DARK:
            case MacOSDark:
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerpausewhite.svg"));
                break;
            case LIGHT:
            case MacOSLight:
            case QuaQua:
            case WINDOWS:
            case UGLY:
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("icons/playerpausedark.svg"));
                break;
            case LEGACY:
                ContentPanel.playerplaypausebutton.setImage(new Resources().readToInputStream("legacyicons/playerpause.svg"));
                break;
        }
        //timer.schedule(new PlayerThread(), 0, 1000);
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
        //timer.cancel();
    }

    @Override
    public void onStartedLoading(@NotNull Player player) {

    }

    @Override
    public void onFinishedLoading(@NotNull Player player) {

    }
}
