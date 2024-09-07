package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.com.spotify.metadata.Metadata;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed.NotFoundException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.DecodedAudioStream;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.HaltListener;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.PlayableContentFeeder;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.cdn.CdnManager;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.decoders.AudioQuality;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.decoders.VorbisOnlyAudioQuality;
import com.spotifyxp.deps.xyz.gianlu.librespot.common.Utils;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.deps.xyz.gianlu.librespot.metadata.PlayableId;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings({"SameReturnValue", "IntegerDivisionInFloatingPointContext", "BooleanMethodIsAlwaysInverted"})
public class TrackUtils {
    public static String calculateFileSizeKb(Track t) {
        return calculateFileSizeKb(t.getDurationMs());
    }
    public static String calculateFileSizeKb(long milliseconds) {
        long minutes = getMMofTrack(milliseconds);
        //720kb per minute if normal 96kbps
        //1200kb per minute if high 160kbps
        //2400kb per minute if extremely high 320kbps
        String toret = "";
        switch (PublicValues.quality) {
            case NORMAL:
                toret = String.valueOf(minutes*720);
                break;
            case HIGH:
                toret = String.valueOf(minutes*1200);
                break;
            case VERY_HIGH:
                toret = String.valueOf(minutes*2400);
                break;
        }
        if(toret.isEmpty() || toret.equals("0")) {
            toret = "N/A";
        }
        return toret + " KB";
    }
    public static String calculateFileSizeKb(TrackSimplified t) {
        return calculateFileSizeKb(t.getDurationMs());
    }
    public static long getMMofTrack(long milliseconds) {
        return milliseconds/60000;
    }
    public static String getHHMMSSOfTrack(long milliseconds) {
        int seconds = Math.round(milliseconds / 1000);
        int hh = seconds / 3600;
        int mm = (seconds % 3600) / 60;
        int ss = seconds % 60;
        String formattedTime = String.format("%02d:%02d", mm, ss);
        if (hh > 0) {
            formattedTime = String.format("%02d:%s", hh, formattedTime);
        }
        return formattedTime;
    }
    public static void addAllToQueue(ArrayList<String> cache, DefTable addintable) {
        try {
            try {
                if (!InstanceManager.getPlayer().getPlayer().tracks(true).previous.isEmpty()) {
                    InstanceManager.getPlayer().getPlayer().tracks(true).previous.clear();
                }
                if (!InstanceManager.getPlayer().getPlayer().tracks(true).next.isEmpty()) {
                    InstanceManager.getPlayer().getPlayer().tracks(true).next.clear();
                }
            } catch (Exception exc) {
                ConsoleLogging.warning("Couldn't queue tracks");
                ConsoleLogging.Throwable(exc);
                return;
            }
            int counter = 0;
            try {
                for (String s : cache) {
                    if (!(counter == addintable.getSelectedRow() + 1)) {
                        counter++;
                        continue;
                    }
                    if (counter == addintable.getRowCount()) {
                        break; //User is on the last song
                    }
                    InstanceManager.getPlayer().getPlayer().addToQueue(s);
                }
            } catch (ArrayIndexOutOfBoundsException exception) {
                GraphicalMessage.bug("TrackUtils line 112");
            } catch (NullPointerException exc) {
                //Factory.getPlayer().getPlayer().load("spotify:track:40aG6sP7TMy3x1J1zGW8su", true, false);
                for (String s : cache) {
                    if (!(counter == addintable.getSelectedRow() + 1)) {
                        counter++;
                        continue;
                    }
                    if (counter == addintable.getRowCount()) {
                        break; //User is on the last song
                    }
                    InstanceManager.getPlayer().getPlayer().addToQueue(s);
                }
            }
            InstanceManager.getPlayer().getPlayer().setShuffle(PublicValues.shuffle);
            if (PublicValues.shuffle) {
                Shuffle.makeShuffle();
            }
            Events.triggerEvent(SpotifyXPEvents.queueUpdate.getName());
        }catch (IndexOutOfBoundsException ignored) {
        }
    }
    public static Integer roundVolumeToNormal(float volume) {
        return Integer.parseInt(String.valueOf(Math.round(volume*10)));
    }
    public static int getSecondsFromMS(long milliseconds) {
        return Math.round(milliseconds/1000);
    }
    public static String getBitrate() {
        switch (PublicValues.quality) {
            case NORMAL:
                return "96kbps";
            case HIGH:
                return "160kbps";
            case VERY_HIGH:
                return "320kbps";
        }
        return "Unknown (BUG)";
    }

    public static String getArtists(Metadata.Artist[] artists) {
        StringBuilder builder = new StringBuilder();
        for (Metadata.Artist artist : artists) {
            if (!(builder.length() == artists.length - 1)) {
                builder.append(artist.getName()).append(", ");
            } else {
                builder.append(artist.getName());
            }
        }
        return builder.toString();
    }

    public static String getArtists(ArtistSimplified[] artists) {
        StringBuilder builder = new StringBuilder();
        for (ArtistSimplified artist : artists) {
            if (!(builder.length() == artists.length - 1)) {
                builder.append(artist.getName()).append(", ");
            } else {
                builder.append(artist.getName());
            }
        }
        return builder.toString();
    }

    public static boolean trackHasArtist(ArtistSimplified[] artists, String tosearchfor, boolean ignoreCase) {
        for(ArtistSimplified artist : artists) {
            if(ignoreCase) {
                if (artist.getName().equalsIgnoreCase(tosearchfor)) {
                    return true;
                }
            }else {
                if (artist.getName().equals(tosearchfor)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTrackLiked(String id) {
        try {
            return InstanceManager.getSpotifyApi().checkUsersSavedTracks(id).build().execute()[0];
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            return false;
        }
    }

    public static void removeLovedTrack(DefTable table, ArrayList<String> uricache) {
        InstanceManager.getSpotifyApi().removeUsersSavedTracks(uricache.get(table.getSelectedRow()).split(":")[2]);
        uricache.remove(table.getSelectedRow());
        table.addModifyAction(() -> ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow()));
    }
    public static void removeFollowedPlaylist(DefTable table, ArrayList<String> uricache) {
        InstanceManager.getSpotifyApi().unfollowPlaylist(uricache.get(table.getSelectedRow()).split(":")[2]);
        uricache.remove(table.getSelectedRow());
        table.addModifyAction(() -> ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow()));
    }

    public static void download(PlayableId id) throws CdnManager.CdnException, IOException, MercuryClient.MercuryException, PlayableContentFeeder.ContentRestrictedException, UnsupportedOperationException, NotFoundException {
        PublicValues.disableChunkDebug = true;
        PlayableContentFeeder.LoadedStream stream = PublicValues.session.contentFeeder().load(id, new VorbisOnlyAudioQuality(AudioQuality.valueOf(PublicValues.quality.toString())), false, new HaltListener() {
            @Override
            public void streamReadHalted(int chunk, long time) {
            }

            @Override
            public void streamReadResumed(int chunk, long time) {
            }
        });

        MetadataWrapper metadata = stream.metadata;
        DecodedAudioStream audioStream = stream.in;

        if (metadata.isEpisode() && metadata.episode != null) {
            ConsoleLogging.info("Downloading episode. {name: '{}', duration: {}, uri: {}, id: {}}", metadata.episode.getName(),
                    metadata.episode.getDuration(), id.toSpotifyUri(), id);
        } else if (metadata.isTrack() && metadata.track != null) {
            ConsoleLogging.info("Downloading track. {name: '{}', artists: '{}', duration: {}, uri: {}, id: {}}", metadata.track.getName(),
                    Utils.artistsToString(metadata.track.getArtistList()), metadata.track.getDuration(), id.toSpotifyUri(), id);
        }

        OverwriteFactory.run(audioStream.stream());
        PublicValues.disableChunkDebug = false;
    }
}
