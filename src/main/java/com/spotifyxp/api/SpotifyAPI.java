package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Artist;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.ApResolver;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.guielements.DefTable;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.utils.*;
import kotlin.reflect.KParameter;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import java.io.*;
import java.util.ArrayList;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.TokenProvider;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("CanBeFinal")
public class SpotifyAPI {
    public static int waitAmount = 4;
    static SpotifyApi spotifyApi = null;

    /**
     * Injects a SpotifyApi instande (For debugging)
     * @param api an instance of the class SpotifyApi
     * @see SpotifyApi
     */
    public void setSpotifyApi(SpotifyApi api) {
        spotifyApi = api;
    }

    /**
     * Adds all albums to the table specified
     * @param uricache cache that holds all uris
     * @param fromuri artist uri
     * @param totable the table to store all albums found
     */
    public void addAllAlbumsToList(ArrayList<String> uricache, String fromuri, DefTable totable) {
        DefThread thread = new DefThread(new Runnable() {
            @Override
            public void run() {
                int offset = 0;
                int limit = 50;
                while(uricache.size()!=new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/artists/" + fromuri.split(":")[2] + "/albums?offset=" + offset + "&limit=" + limit + "&include_groups=album,single,compilation,appears_on&market=" + ContentPanel.countryCode.toString())).getInt("total")) {
                    if(!totable.isVisible()) {
                        break;
                    }
                    for(Object o : new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/artists/" + fromuri.split(":")[2] + "/albums?offset=" + offset + "&limit=" + limit + "&include_groups=album,single,compilation,appears_on&market=" + ContentPanel.countryCode.toString())).getJSONArray("items")) {
                        JSONObject object = new JSONObject(o.toString());
                        uricache.add(object.getString("uri"));
                        totable.addModifyAction(new Runnable() {
                            @Override
                            public void run() {
                                ((DefaultTableModel) totable.getModel()).addRow(new Object[] {object.getString("name")});
                            }
                        });
                    }
                    offset=offset+50;
                }
            }
        });
        thread.start();
    }

    /**
     * Adds an album to a table
     * @param simplified instance of AlbumSimplified
     * @param table table to store the album
     * @see AlbumSimplified
     */
    public void addAlbumToList(AlbumSimplified simplified, DefTable table) {
        table.addModifyAction(new Runnable() {
            @Override
            public void run() {
                ((DefaultTableModel) table.getModel()).addRow(new Object[] {simplified.getName()});
            }
        });
    }

    /**
     * Adds an album to a table
     * @param artist instance of Artist
     * @param table table to store the album
     * @see Artist
     */
    public void addArtistToList(Artist artist, DefTable table) {
        table.addModifyAction(new Runnable() {
            @Override
            public void run() {
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{artist.getName()});
            }
        });
    }

    /**
     * Adds a song to a table
     * @param artists artists to insert (for display)
     * @param track instance of Track
     * @param table table to store the song
     * @see Track
     */
    public void addSongToList(String artists, Track track, DefTable table) {
        table.addModifyAction(new Runnable() {
            @Override
            public void run() {
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{track.getName() + " - " + track.getAlbum().getName() + " - " + artists, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
            }
        });
    }

    /**
     * Adds a playlist to a table
     * @param simplified instance of PlaylistSimplified
     * @param table table to store the playlist
     * @see PlaylistSimplified
     */
    public void addPlaylistToList(PlaylistSimplified simplified, DefTable table) {
        table.addModifyAction(new Runnable() {
            @Override
            public void run() {
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{simplified.getName() + " - " + simplified.getOwner().getDisplayName()});
            }
        });
    }
}
