package com.spotifyxp.utils;

import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.*;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;

import java.util.ArrayList;

public class SpotifyUtils {
    public static Image getImageForSystem(Image[] images) {
        if(SystemUtils.getUsableRAMmb() < 512) {
            for(Image i : images) {
                if(i.getWidth() == 64) {
                    return i;
                }
            }
            ConsoleLogging.warning("Can't get the right image for the system ram! Using the default one");
        }
        return images[0];
    }

    public static ArrayList<TrackSimplified> getAllTracksAlbum(String uri) {
        ArrayList<TrackSimplified> tracks = new ArrayList<>();
        try {
            int offset = 0;
            int limit = 50;
            int parsed = 0;
            int total = Factory.getSpotifyApi().getAlbumsTracks(uri.split(":")[2]).build().execute().getTotal();
            int counter = 0;
            int last = 0;
            while(parsed != total) {
                for(TrackSimplified track : Factory.getSpotifyApi().getAlbumsTracks(uri.split(":")[2]).offset(offset).limit(limit).build().execute().getItems()) {
                    tracks.add(track);
                    parsed++;
                }
                if(last == parsed) {
                    if(counter > 1) {
                        break;
                    }
                    counter++;
                }else{
                    counter = 0;
                }
                last = parsed;
                offset += limit;
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tracks;
    }
    public static ArrayList<PlaylistTrack> getAllTracksPlaylist(String uri) {
        ArrayList<PlaylistTrack> tracks = new ArrayList<>();
        try {
            int offset = 0;
            int limit = 50;
            int parsed = 0;
            int total = Factory.getSpotifyApi().getPlaylistsItems(uri.split(":")[2]).build().execute().getTotal();
            int counter = 0;
            int last = 0;
            while(parsed != total) {
                for(PlaylistTrack track : Factory.getSpotifyApi().getPlaylistsItems(uri.split(":")[2]).offset(offset).limit(limit).build().execute().getItems()) {
                    tracks.add(track);
                    parsed++;
                }
                if(last == parsed) {
                    if(counter > 1) {
                        break;
                    }
                    counter++;
                }else{
                    counter = 0;
                }
                last = parsed;
                offset += limit;
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tracks;
    }
    public static ArrayList<EpisodeSimplified> getAllEpisodesShow(String uri) {
        ArrayList<EpisodeSimplified> episodes = new ArrayList<>();
        try {
            int offset = 0;
            int limit = 50;
            int parsed = 0;
            int total = Factory.getSpotifyApi().getShowEpisodes(uri.split(":")[2]).build().execute().getTotal();
            int counter = 0;
            int last = 0;
            while(parsed != total) {
                for(EpisodeSimplified episode : Factory.getSpotifyApi().getShowEpisodes(uri.split(":")[2]).offset(offset).limit(limit).build().execute().getItems()) {
                    episodes.add(episode);
                    parsed++;
                }
                if(last == parsed) {
                    if(counter > 1) {
                        break;
                    }
                    counter++;
                }else{
                    counter = 0;
                }
                last = parsed;
                offset += limit;
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return episodes;
    }
    public static ArrayList<AlbumSimplified> getAllAlbumsArtist(String uri) {
        ArrayList<AlbumSimplified> albums = new ArrayList<>();
        try {
            int offset = 0;
            int limit = 50;
            int parsed = 0;
            int total = Factory.getSpotifyApi().getArtistsAlbums(uri.split(":")[2]).build().execute().getTotal();
            int counter = 0;
            int last = 0;
            while(parsed != total) {
                for(AlbumSimplified album : Factory.getSpotifyApi().getArtistsAlbums(uri.split(":")[2]).offset(offset).limit(limit).build().execute().getItems()) {
                    albums.add(album);
                    parsed++;
                }
                if(last == parsed) {
                    if(counter > 1) {
                        break;
                    }
                    counter++;
                }else{
                    counter = 0;
                }
                last = parsed;
                offset += limit;
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return albums;
    }
    public static ArrayList<Track> getAllTopTracksArtist(String uri) {
        ArrayList<Track> tracks = new ArrayList<>();
        try {
            int parsed = 0;
            int total = Factory.getSpotifyApi().getArtistsTopTracks(uri.split(":")[2], ContentPanel.countryCode).build().execute().length;
            int counter = 0;
            int last = 0;
            while(parsed != total) {
                for(Track track : Factory.getSpotifyApi().getArtistsTopTracks(uri.split(":")[2], ContentPanel.countryCode).build().execute()) {
                    tracks.add(track);
                    parsed++;
                }
                if(last == parsed) {
                    if(counter > 1) {
                        break;
                    }
                    counter++;
                }else{
                    counter = 0;
                }
                last = parsed;
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tracks;
    }
}
