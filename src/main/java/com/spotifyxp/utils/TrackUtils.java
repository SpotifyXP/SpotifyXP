package com.spotifyxp.utils;

import com.spotifyxp.api.SpotifyAPI;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TrackUtils {
    public static int calculateFileSizeKb(Track t) {
        double minutes = Math.round(TimeUnit.MILLISECONDS.convert(t.getDurationMs(), TimeUnit.MINUTES));
        //720kb per minute if normal 96kbps
        //2400kb per minute if extremely high 320kbps
        return Integer.parseInt(String.valueOf(Math.round(minutes*0.72)));
    }
    public static int calculateFileSizeKb(TrackSimplified t) {
        double minutes = Math.round(TimeUnit.MILLISECONDS.convert(t.getDurationMs(), TimeUnit.MINUTES));
        //720kb per minute if normal 96kbps
        //2400kb per minute if extremely high 320kbps
        return Integer.parseInt(String.valueOf(Math.round(minutes*0.72)));
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
    public static int getSecondsFromMS(long milliseconds) {
        return Math.round(milliseconds/1000);
    }
    public static String getBitrate() {
        return "96kbps";
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
}
