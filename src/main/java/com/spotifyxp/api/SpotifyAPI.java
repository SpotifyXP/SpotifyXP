package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.lib.libWebServer;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.json.JSONObject;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("CanBeFinal")
public class SpotifyAPI {
    private static final String deviceName = "XPS";
    public static int waitAmount = 4;
    static SpotifyApi spotifyApi = null;
    public SpotifyAPI() {
        if(!new File(PublicValues.fileslocation).exists()) {
            if(!new File(PublicValues.fileslocation).mkdir()) {
                ConsoleLogging.changeName("SpotifyAPI");
                ConsoleLogging.error(PublicValues.language.translate("error.configuration.failedcreate"), 39);
            }
        }
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
        xyz.gianlu.librespot.player.Player player;
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
        public xyz.gianlu.librespot.player.Player getPlayer() {
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
    @SuppressWarnings({"CanBeFinal", "Convert2Lambda"})
    public static class OAuthPKCE {
        private final String redirectURI = "http://127.0.0.1:2400/redirectSpotify";
        private String code = "";
        private String refreshToken = "";
        public static String token = "";
        private long expiresIn = 0;
        private boolean isFirst = true;
        private final String CLIENT_ID = "091d7b71d33743d389cef8449136c62e"; 
        @SuppressWarnings("FieldCanBeLocal")
        private final String scopes = "ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private";
        private final String code_verifier = StringUtils.generateStringFrom(50);
        @SuppressWarnings("FieldCanBeLocal")
        private final String challenge = PkceUtil.generateCodeChallenge(code_verifier);
        private final Timer timer = new Timer();
        public OAuthPKCE() {
            //Requests a token without client_secret with the PKCE method
            ConnectionUtils.openBrowser("https://accounts.spotify.com/authorize?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + redirectURI + "&code_challenge_method=S256&code_challenge=" + challenge + "&scope=" + scopes);
            startServer();
            getTokenFromCode();
        }
        private void getTokenFromCode() {
            startServerSecondStep();
            try {
                HttpClient client = new HttpClient();
                PostMethod get = new PostMethod("https://accounts.spotify.com/api/token");
                get.setQueryString(new NameValuePair[]{new NameValuePair("grant_type", "authorization_code"), new NameValuePair("code", code), new NameValuePair("redirect_uri", redirectURI), new NameValuePair("client_id", CLIENT_ID), new NameValuePair("code_verifier", code_verifier)});
                get.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                client.executeMethod(get);
                token = new JSONObject(get.getResponseBodyAsString()).getString("access_token");
                expiresIn = new JSONObject(get.getResponseBodyAsString()).getLong("expires_in");
                refreshToken = new JSONObject(get.getResponseBodyAsString()).getString("refresh_token");
            } catch (IOException e) {
                ConsoleLogging.Throwable(e);
            }
            //Start refresh timer
            startExpirationTimer();
        }
        private TimerTask ExpirationTask() {
            return  new TimerTask() {
                @Override
                public void run() {
                    if(!isFirst) {
                        refresh();
                    }else{
                        isFirst = false;
                    }
                }
            };
        }
        private void startExpirationTimer() {
            timer.schedule(ExpirationTask(), TimeUnit.SECONDS.toMillis(expiresIn));
        }
        public void refresh() {
            JSONObject refreshres = new JSONObject(ConnectionUtils.makePost("https://accounts.spotify.com/api/token", new NameValuePair[]{new NameValuePair("grant_type", "refresh_token"), new NameValuePair("refresh_token", refreshToken), new NameValuePair("client_id", CLIENT_ID)}, new Header("Content-Type", "application/x-www-form-urlencoded")));
            token = refreshres.getString("access_token");
            expiresIn = refreshres.getLong("expires_in");
            refreshToken = refreshres.getString("refresh_token");
            spotifyApi.setAccessToken(token);
            spotifyApi.setRefreshToken(refreshToken);
            System.out.println("Triggered refresh");
        }
        private void startServerSecondStep() {
            libWebServer server = new libWebServer(2400);
            server.addHttpContext("/redirectSpotify", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) {
                    try {
                        Thread.sleep(99);
                    } catch (InterruptedException e) {
                        ConsoleLogging.Throwable(e);
                    }
                    try {
                        WebUtils.sendCode(exchange, 200, new Resources().readToString("worker.html"));
                    } catch (Exception e) {
                        ConsoleLogging.Throwable(e);
                    }
                    if (exchange.getRequestURI().toString().contains("?")) {
                        //Do something with the token
                        System.out.println(exchange.getRequestURI().toString());
                        //Then stop the server
                        try {
                            Thread.sleep(99);
                        } catch (InterruptedException e) {
                            ConsoleLogging.Throwable(e);
                        }
                        server.stop();
                    }
                }
            });
            server.start();
        }
        private boolean stop = false;
        @SuppressWarnings("BusyWait")
        private void startServer() {
            libWebServer server = new libWebServer(2400);
            server.addHttpContext("/redirectSpotify", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) {
                    try {
                        Thread.sleep(99);
                    } catch (InterruptedException e) {
                        ConsoleLogging.Throwable(e);
                    }
                    try {
                        WebUtils.sendCode(exchange, 200, new Resources().readToString("worker.html"));
                    } catch (Exception e) {
                        ConsoleLogging.Throwable(e);
                    }
                    if (exchange.getRequestURI().toString().contains("?")) {
                        //Do something with the token
                        code = exchange.getRequestURI().toString().split("\\?")[1].replace("code=", "");
                        //Then stop the server
                        stop = true;
                    }
                }
            });
            server.start();
            while(!stop) {
                try {
                    Thread.sleep(99);
                } catch (InterruptedException e) {
                    ConsoleLogging.Throwable(e);
                }
            }
            server.stop();
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
}
