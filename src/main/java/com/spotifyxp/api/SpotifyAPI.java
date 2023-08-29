package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Artist;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.ApResolver;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.exception.ExceptionDialog;
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
    public SpotifyAPI() {
        getSpotifyApi();
    }
    public SpotifyApi getSpotifyApi() {
        if(spotifyApi==null) {
            spotifyApi = SpotifyApi.builder().setAccessToken(OAuthPKCE.token).build();
        }
        return spotifyApi;
    }
    @SuppressWarnings("CanBeFinal")
    public static class Player {
        int wait;
        com.spotifyxp.deps.xyz.gianlu.librespot.player.Player player;
        SpotifyAPI api;
        int times = 0;

        public void retry() {
            try {
                player = PlayerUtils.buildPlayer();
            }catch (EOFException e) {
                handleEOFBug();
                return;
            }
            try {
                player.waitReady();
            } catch (InterruptedException e) {
                ConsoleLogging.Throwable(e);
                ExceptionDialog.open(e);
            } catch (NullPointerException e) {
                handleEOFBug();
                return;
            }
            ConsoleLogging.info(PublicValues.language.translate("debug.connection.ready"));
            player.addEventsListener(new PlayerListener(this, api));
            PublicValues.spotifyplayer = player;
        }

        void handleEOFBug() {
            ApResolver.eof = true;
            retry();
        }

        public Player(SpotifyAPI a) {
            api = a;
            try {
                player = PlayerUtils.buildPlayer();
            }catch (EOFException e) {
                handleEOFBug();
                return;
            }
            try {
                player.waitReady();
            } catch (InterruptedException e) {
                ConsoleLogging.Throwable(e);
                ExceptionDialog.open(e);
            } catch (NullPointerException e) {
                handleEOFBug();
                return;
            }
            ConsoleLogging.info(PublicValues.language.translate("debug.connection.ready"));
            player.addEventsListener(new PlayerListener(this, api));
            PublicValues.spotifyplayer = player;
        }

        public com.spotifyxp.deps.xyz.gianlu.librespot.player.Player getPlayer() {
            return player;
        }
    }
    @SuppressWarnings({"CanBeFinal"})
    public static class OAuthPKCE {
        public static String token = "";
        private final String scopes = "ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private";
        public OAuthPKCE() {
            try {
                TokenProvider.StoredToken provider = PublicValues.session.tokens().getToken(scopes.split(" "));
                token = provider.accessToken;
                spotifyApi.setAccessToken(token);
                PublicValues.apikey = provider.accessToken;
            } catch (IOException | MercuryClient.MercuryException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }
        public void refresh() {
            try {
                TokenProvider.StoredToken provider = PublicValues.session.tokens().getToken(scopes.split(" "));
                token = provider.accessToken;
                spotifyApi.setAccessToken(token);
                PublicValues.apikey = provider.accessToken;
            } catch (IOException | MercuryClient.MercuryException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }
        public String makePost(String url) {
            if(!url.contains("https")) {
                url = "https://api.spotify.com" + url;
            }
            String ret = "FAILED";
            try {
                HttpClient client = new HttpClient();
                PostMethod post = new PostMethod(url);
                post.addRequestHeader("Authorization", "Bearer " + token);
                client.executeMethod(post);
                ret = post.getResponseBodyAsString();
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
            return ret;
        }

        public String makePost(String url, String body) {
            if(!url.contains("https")) {
                url = "https://api.spotify.com" + url;
            }
            String ret = "FAILED";
            try {
                HttpClient client = new HttpClient();
                PostMethod post = new PostMethod(url);
                post.addRequestHeader("Authorization", "Bearer " + token);
                post.setRequestBody(body);
                client.executeMethod(post);
                ret = post.getResponseBodyAsString();
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
            return ret;
        }

        public String makeGet(String url) {
            if(!url.contains("https")) {
                url = "https://api.spotify.com" + url;
            }
            String ret = "FAILED";
            try {
                HttpClient client = new HttpClient();
                GetMethod post = new GetMethod(url);
                post.addRequestHeader("Authorization", "Bearer " + token);
                client.executeMethod(post);
                ret = post.getResponseBodyAsString();
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
            return ret;
        }
        public String makeGet(String url, NameValuePair... pairs) {
            if(!url.contains("https")) {
                url = "https://api.spotify.com" + url;
            }
            String ret = "FAILED";
            try {
                StringBuilder builder = new StringBuilder();
                HttpClient client = new HttpClient();
                GetMethod post = new GetMethod(url);
                post.addRequestHeader("Authorization", "Bearer " + token);
                int i = 0;
                for(NameValuePair pair : pairs) {
                    if(i == 0) {
                        builder.append("?").append(pair.getName()).append("=").append(pair.getValue());
                    }else{
                        builder.append("&").append(pair.getName()).append("=").append(pair.getValue());
                    }
                    i++;
                }
                post.setURI(new URI(url + builder.toString()));
                client.executeMethod(post);
                ret = post.getResponseBodyAsString();
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
            return ret;
        }
        @SuppressWarnings("UnusedReturnValue")
        public String makePut(String url) {
            if(!url.contains("https")) {
                url = "https://api.spotify.com" + url;
            }
            String ret = "FAILED";
            try {
                HttpClient client = new HttpClient();
                PutMethod post = new PutMethod(url);
                post.addRequestHeader("Authorization", "Bearer " + token);
                client.executeMethod(post);
                ret = post.getResponseBodyAsString();
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
            return ret;
        }
        @SuppressWarnings("UnusedReturnValue")
        public String makeDelete(String url) {
            if(!url.contains("https")) {
                url = "https://api.spotify.com" + url;
            }
            String ret = "FAILED";
            try {
                HttpClient client = new HttpClient();
                DeleteMethod post = new DeleteMethod(url);
                post.addRequestHeader("Authorization", "Bearer " + token);
                client.executeMethod(post);
                ret = post.getResponseBodyAsString();
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
            return ret;
        }
    }
    public void addAllAlbumsToList(ArrayList<String> uricache, String fromuri, JTable totable) {
        DefThread thread = new DefThread(new Runnable() {
            @Override
            public void run() {
                int offset = 0;
                int limit = 50;
                while(uricache.size()!=new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/artists/" + fromuri.split(":")[2] + "/albums?offset=" + offset + "&limit=" + limit + "&include_groups=album,single,compilation,appears_on&market=" + ContentPanel.countryCode.toString())).getInt("total")) {
                    for(Object o : new JSONObject(PublicValues.elevated.makeGet("https://api.spotify.com/v1/artists/" + fromuri.split(":")[2] + "/albums?offset=" + offset + "&limit=" + limit + "&include_groups=album,single,compilation,appears_on&market=" + ContentPanel.countryCode.toString())).getJSONArray("items")) {
                        JSONObject object = new JSONObject(o.toString());
                        uricache.add(object.getString("uri"));
                        ((DefaultTableModel) totable.getModel()).addRow(new Object[] {object.getString("name")});
                    }
                    offset=offset+50;
                }
            }
        });
        thread.start();
    }
    public void addAlbumToList(AlbumSimplified simplified, JTable table) {
        ((DefaultTableModel) table.getModel()).addRow(new Object[] {simplified.getName()});
    }
    public void addArtistToList(Artist artist, JTable table) {
        ((DefaultTableModel) table.getModel()).addRow(new Object[]{artist.getName()});
    }
    public void addSongToList(String artists, Track track, JTable table) {
        ((DefaultTableModel) table.getModel()).addRow(new Object[]{track.getName() + " - " + track.getAlbum().getName() + " - " + artists, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
    }
    public void addPlaylistToList(PlaylistSimplified simplified, JTable table) {
        ((DefaultTableModel) table.getModel()).addRow(new Object[]{simplified.getName() + " - " + simplified.getOwner().getDisplayName()});
    }
}
