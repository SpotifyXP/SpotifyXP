package com.spotifyxp.lastfm;

import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.manager.InstanceManager;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class LastFMConverter {
    public static String getArtistURIfromName(String name) {
        try {
            return InstanceManager.getSpotifyApi().searchArtists(name).build().execute().getItems()[0].getUri();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getAlbumURIfromName(String name) {
        try {
            return InstanceManager.getSpotifyApi().searchAlbums(name).build().execute().getItems()[0].getUri();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getTrackURIfromName(String name) {
        try {
            return InstanceManager.getSpotifyApi().searchTracks(name).build().execute().getItems()[0].getUri();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
