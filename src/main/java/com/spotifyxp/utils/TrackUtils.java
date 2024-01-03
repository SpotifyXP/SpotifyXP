package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.logging.ConsoleLogging;

import javax.swing.table.DefaultTableModel;
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
            if(!Factory.getPlayer().getPlayer().tracks(true).previous.isEmpty()) {
                Factory.getPlayer().getPlayer().tracks(true).previous.clear();
            }
            if(!Factory.getPlayer().getPlayer().tracks(true).next.isEmpty()) {
                Factory.getPlayer().getPlayer().tracks(true).next.clear();
            }
        }catch (Exception exc) {
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
                if(counter==addintable.getRowCount()) {
                    break; //User is on the last song
                }
                Factory.getPlayer().getPlayer().addToQueue(s);
            }
        }catch (ArrayIndexOutOfBoundsException exception) {
            GraphicalMessage.bug("TrackUtils line 112");
        } catch (NullPointerException exc) {
            //Factory.getPlayer().getPlayer().load("spotify:track:40aG6sP7TMy3x1J1zGW8su", true, false);
            for (String s : cache) {
                if (!(counter == addintable.getSelectedRow() + 1)) {
                    counter++;
                    continue;
                }
                if(counter==addintable.getRowCount()) {
                    break; //User is on the last song
                }
                Factory.getPlayer().getPlayer().addToQueue(s);
            }
        }
        Factory.getPlayer().getPlayer().setShuffle(PublicValues.shuffle);
        if(PublicValues.shuffle) {
            Shuffle.makeShuffle();
        }
        Events.triggerEvent(SpotifyXPEvents.queueUpdate.getName());
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
    public static void removeLovedTrack(DefTable table, ArrayList<String> uricache) {
        Factory.getSpotifyApi().removeUsersSavedTracks(uricache.get(table.getSelectedRow()).split(":")[2]);
        uricache.remove(table.getSelectedRow());
        table.addModifyAction(() -> ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow()));
    }
    public static void removeFollowedPlaylist(DefTable table, ArrayList<String> uricache) {
        Factory.getSpotifyApi().unfollowPlaylist(uricache.get(table.getSelectedRow()).split(":")[2]);
        uricache.remove(table.getSelectedRow());
        table.addModifyAction(() -> ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow()));
    }
}
