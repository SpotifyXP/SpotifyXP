package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

@SuppressWarnings({"SameReturnValue", "IntegerDivisionInFloatingPointContext"})
public class TrackUtils {
    public static String calculateFileSizeKb(Track t) {
        long minutes = getMMofTrack(t.getDurationMs());
        //720kb per minute if normal 96kbps
        //1200kb per minute if high 160kbps
        //2400kb per minute if extremely high 320kbps
        switch (PublicValues.quality) {
            case NORMAL:
                return Integer.parseInt(String.valueOf(minutes*720)) + "KB";
            case HIGH:
                return Integer.parseInt(String.valueOf(minutes*1200)) + "KB";
            case VERY_HIGH:
                return Integer.parseInt(String.valueOf(minutes*2400)) + "KB";
        }
        return "Unknown (BUG)";
    }
    public static String calculateFileSizeKb(long milliseconds) {
        long minutes = getMMofTrack(milliseconds);
        //720kb per minute if normal 96kbps
        //1200kb per minute if high 160kbps
        //2400kb per minute if extremely high 320kbps
        switch (PublicValues.quality) {
            case NORMAL:
                return Integer.parseInt(String.valueOf(minutes*720)) + "KB";
            case HIGH:
                return Integer.parseInt(String.valueOf(minutes*1200)) + "KB";
            case VERY_HIGH:
                return Integer.parseInt(String.valueOf(minutes*2400)) + "KB";
        }
        return "Unknown (BUG)";
    }
    public static String calculateFileSizeKb(TrackSimplified t) {
        long minutes = getMMofTrack(t.getDurationMs());
        //720kb per minute if normal 96kbps
        //1200kb per minute if high 160kbps
        //2400kb per minute if extremely high 320kbps
        switch (PublicValues.quality) {
            case NORMAL:
                return Integer.parseInt(String.valueOf(minutes*720)) + "KB";
            case HIGH:
                return Integer.parseInt(String.valueOf(minutes*1200)) + "KB";
            case VERY_HIGH:
                return Integer.parseInt(String.valueOf(minutes*2400)) + "KB";
        }
        return "Unknown (BUG)";
    }
    public static long getMMofTrack(long milliseconds) {
        return milliseconds/60000;
    }
    public static String getHHMMSSOfTrack(long milliseconds) {
        boolean ddh = false;
        int seconds = Math.round(milliseconds/1000);
        int hh = seconds/60/60;
        seconds = seconds - hh * 3600;
        int mm = seconds/60;
        seconds = seconds - mm * 60;
        int ss = seconds;
        String h = String.valueOf(hh);
        String m = String.valueOf(mm);
        String s = String.valueOf(ss);
        if(hh<10) {
            h = "0" + h;
        }
        if(mm<10) {
            m = "0" + m;
        }
        if(ss<10) {
            s = "0" + s;
        }
        if(hh==0) {
            ddh = true;
        }
        if(!ddh) {
            return h + ":" + m + ":" + s; //Return as 00:00
        }else{
            return m + ":" + s; //Return as 00:00:00
        }
    }
    public static void addAllToQueue(ArrayList<String> cache, JTable addintable) {
        try {
            ContentPanel.player.getPlayer().tracks(true).next.clear();
        }catch (NullPointerException exc) {
            // ToDo: Find the bug (why is this raising a nullpointer exception)
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
                ContentPanel.player.getPlayer().addToQueue(s);
            }
        }catch (ArrayIndexOutOfBoundsException exception) {
            // When this is raised this is a bug
        } catch (NullPointerException exc) {
            //ContentPanel.player.getPlayer().load("spotify:track:40aG6sP7TMy3x1J1zGW8su", true, false);
            for (String s : cache) {
                if (!(counter == addintable.getSelectedRow() + 1)) {
                    counter++;
                    continue;
                }
                if(counter==addintable.getRowCount()) {
                    break; //User is on the last song
                }
                ContentPanel.player.getPlayer().addToQueue(s);
            }
        }
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
        for(ArtistSimplified artist : artists) {
            if(!(builder.length()==artists.length-1)) {
                builder.append(artist.getName()).append(", ");
            }else{
                builder.append(artist.getName());
            }
        }
        return builder.toString();
    }
    public static void removeLovedTrack(JTable table, ArrayList<String> uricache) {
        ContentPanel.api.getSpotifyApi().removeUsersSavedTracks(uricache.get(table.getSelectedRow()).split(":")[2]);
        uricache.remove(table.getSelectedRow());
        ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
    }
    public static void removeFollowedPlaylist(JTable table, ArrayList<String> uricache) {
        ContentPanel.api.getSpotifyApi().unfollowPlaylist(uricache.get(table.getSelectedRow()).split(":")[2]);
        uricache.remove(table.getSelectedRow());
        ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
    }
}
