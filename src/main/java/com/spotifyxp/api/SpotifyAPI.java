package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import java.io.*;
import java.util.Timer;

import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.TokenProvider;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("CanBeFinal")
public class SpotifyAPI {
    private static final String deviceName = "XPS";
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

        @SuppressWarnings("BusyWait")
        public void retry() {
            player = PlayerUtils.buildPlayer();
            wait = 0;
            while(true) {
                assert player != null;
                if (!player.isReady()) break;
                ConsoleLogging.info(PublicValues.language.translate("debug.connection.waiting"));
                try {
                    Thread.sleep(999);
                } catch (InterruptedException e) {
                    ConsoleLogging.Throwable(e);
                }
                if(wait==waitAmount) {
                    retry();
                }
                wait++;
            }
            ConsoleLogging.info(PublicValues.language.translate("debug.connection.ready"));
            player.addEventsListener(new PlayerListener(this, api));
            PublicValues.spotifyplayer = player;
        }
        @SuppressWarnings("BusyWait")
        public Player(SpotifyAPI a) {
            api = a;
            //Make player session
            player = PlayerUtils.buildPlayer();
            wait = 0;
            while(true) {
                assert player != null;
                if (!player.isReady()) break;
                ConsoleLogging.info(PublicValues.language.translate("debug.connection.waiting"));
                try {
                    Thread.sleep(999);
                } catch (InterruptedException e) {
                    ConsoleLogging.Throwable(e);
                }
                if(wait==waitAmount) {
                    retry();
                }
                wait++;
            }
            ConsoleLogging.info(PublicValues.language.translate("debug.connection.ready"));
            player.addEventsListener(new PlayerListener(this, api));
            PublicValues.spotifyplayer = player;
        }
        public com.spotifyxp.deps.xyz.gianlu.librespot.player.Player getPlayer() {
            return player;
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
            post.addRequestHeader("Authorization", "Bearer " + OAuthPKCE.token);
            client.executeMethod(post);
            ret = post.getResponseBodyAsString();
        } catch (IOException e) {
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
            post.addRequestHeader("Authorization", "Bearer " + OAuthPKCE.token);
            client.executeMethod(post);
            ret = post.getResponseBodyAsString();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }
    public String makePut(String url) {
        if(!url.contains("https")) {
            url = "https://api.spotify.com" + url;
        }
        String ret = "FAILED";
        try {
            HttpClient client = new HttpClient();
            PutMethod post = new PutMethod(url);
            post.addRequestHeader("Authorization", "Bearer " + OAuthPKCE.token);
            client.executeMethod(post);
            ret = post.getResponseBodyAsString();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }
    @SuppressWarnings({"CanBeFinal"})
    public static class OAuthPKCE {
        public static String token = "";
        private final String scopes = "ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private";
        private final Timer timer = new Timer();
        public OAuthPKCE() {
            try {
                TokenProvider.StoredToken provider = PublicValues.session.tokens().getToken(scopes.split(" "));
                token = provider.accessToken;
                spotifyApi.setAccessToken(token);
            } catch (IOException | MercuryClient.MercuryException e) {
                ConsoleLogging.Throwable(e);
            }
        }
        public void refresh() {
            try {
                TokenProvider.StoredToken provider = PublicValues.session.tokens().getToken(scopes.split(" "));
                token = provider.accessToken;
                spotifyApi.setAccessToken(token);
            } catch (IOException | MercuryClient.MercuryException e) {
                ConsoleLogging.Throwable(e);
            }
            System.out.println("Triggered refresh");
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
                ConsoleLogging.Throwable(e);
            }
            return ret;
        }
        public String makeGet(String url, NameValuePair[] pairs) {
            if(!url.contains("https")) {
                url = "https://api.spotify.com" + url;
            }
            String ret = "FAILED";
            try {
                HttpClient client = new HttpClient();
                GetMethod post = new GetMethod(url);
                post.addRequestHeader("Authorization", "Bearer " + token);
                post.setQueryString(pairs);
                client.executeMethod(post);
                ret = post.getResponseBodyAsString();
            } catch (IOException e) {
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
                ConsoleLogging.Throwable(e);
            }
            return ret;
        }
    }
    public void addSongToList(String artists, Track track, JTable table) {
        ((DefaultTableModel) table.getModel()).addRow(new Object[]{track.getName() + " - " + track.getAlbum().getName() + " - " + artists, TrackUtils.calculateFileSizeKb(track), TrackUtils.getBitrate(),TrackUtils.getHHMMSSOfTrack(track.getDurationMs())});
    }
    public void addPlaylistToList(PlaylistSimplified simplified, JTable table) {
        ((DefaultTableModel) table.getModel()).addRow(new Object[]{simplified.getName() + " - " + simplified.getOwner().getDisplayName()});
    }
}
