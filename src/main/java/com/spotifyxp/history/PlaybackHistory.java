package com.spotifyxp.history;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.utils.TrackUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.hc.core5.http.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PlaybackHistory extends JFrame2 {
    //Icon location X:20  Y:86
    public static class SongEntry {
        public String songURI;
        public String songName;
        public String artistName;
        public String albumName;
        public String songLength;

        @Override
        public String toString() {
            return songURI + "," + songName + "," + artistName + "," + albumName + "," + songLength;
        }
    }


    private static String filePath;
    public PlaybackHistory() {
        filePath = new File(PublicValues.fileslocation, "playback.history").getAbsolutePath();
    }

    public ArrayList<SongEntry> get15Songs(int offset) {
        ArrayList<SongEntry> songs = new ArrayList<>();
        int counter = 0;
        try (LineIterator it = FileUtils.lineIterator(new File(filePath), "UTF-8")) {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (counter >= offset) {
                    songs.add(parseEntry(line));
                }
                counter++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return songs;
    }


    private SongEntry parseEntry(String line) {
        SongEntry entry = new SongEntry();
        int counter = 0;
        for(String s : line.split("::")) {
            switch (counter) {
                case 0:
                    entry.songURI = s;
                    break;
                case 1:
                    entry.songName = s;
                    break;
                case 2:
                    entry.artistName = s;
                    break;
                case 3:
                    entry.albumName = s;
                    break;
                case 4:
                    entry.songLength = s;
                    break;
            }
            counter++;
        }
        return entry;
    }

    private void append(String line) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    public void addSong(Track t) throws IOException, ParseException, SpotifyWebApiException {
        StringBuilder builder = new StringBuilder();
        builder.append(t.getUri());
        builder.append("::");
        builder.append(t.getName());
        builder.append("::");
        builder.append(TrackUtils.getArtists(t.getArtists()));
        builder.append("::");
        builder.append(Factory.getSpotifyApi().getTrack(t.getUri()).build().execute().getAlbum().getUri());
        builder.append("::");
        builder.append(t.getDurationMs());
        append(builder.toString());
    }

    @SuppressWarnings("all")
    public void addSong(TrackSimplified t) throws IOException, ParseException, SpotifyWebApiException {
        StringBuilder builder = new StringBuilder();
        builder.append(t.getUri());
        builder.append("::");
        builder.append(t.getName());
        builder.append("::");
        builder.append(TrackUtils.getArtists(t.getArtists()));
        builder.append("::");
        builder.append(Factory.getSpotifyApi().getTrack(t.getUri()).build().execute().getAlbum().getUri());
        builder.append("::");
        builder.append(t.getDurationMs());
        append(builder.toString());
    }

    public void removeAllSongs() throws IOException {
        new File(filePath).delete();
        new File(filePath).createNewFile();
    }
}
