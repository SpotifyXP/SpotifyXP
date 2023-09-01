package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.TokenProvider;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import java.io.IOException;

public class OAuthPKCE {
    private String token = "";
    private final String scopes = "ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private";
    public OAuthPKCE() {

    }

    public OAuthPKCE(boolean init) {
        try {
            TokenProvider.StoredToken provider = PublicValues.session.tokens().getToken(scopes.split(" "));
            token = provider.accessToken;
        } catch (IOException | MercuryClient.MercuryException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        }
    }

    public String getToken() {
        return token;
    }

    public void refresh() {
        try {
            TokenProvider.StoredToken provider = PublicValues.session.tokens().getToken(scopes.split(" "));
            token = provider.accessToken;
            Factory.getSpotifyApi().setAccessToken(token);
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
