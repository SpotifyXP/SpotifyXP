package com.spotifyxp.api;

import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.NameValuePair;
import com.spotifyxp.utils.Token;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OAuthPKCE {
    private String token = "";
    public OAuthPKCE() {

    }

    public OAuthPKCE(boolean init) {
        token = Token.getDefaultToken();
    }

    /**
     * Returns the Spotify api token that was generated from the spotify server
     * @return     API Token
     */
    public String getToken() {
        return token;
    }


    /**
     * Refreshes the Spotify api token and forwards that to all programm parts that needs it
     */
    public void refresh() {
        token = Token.getDefaultToken();
        InstanceManager.getSpotifyApi().setAccessToken(token);
    }

    /**
     * Makes a post request with the Spotify api token to the api endpoint specified with the url
     * @param  url the api endpoint
     * @return     The response of the request
     */
    public String makePost(String url) {
        if(!url.contains("https")) {
            url = "https://api.spotify.com" + url;
        }
        String ret = "FAILED";
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.addHeader("Authorization", "Bearer " + token);
            ret = EntityUtils.toString(client.execute(post).getEntity());
        } catch (IOException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }

    /**
     * Makes a post request with the Spotify api token to the api endpoint specified with the url
     * @param  url the api endpoint
     * @param body The body to send to the requested endpoint
     * @return     The response of the request
     */
    public String makePost(String url, String body) {
        if(!url.contains("https")) {
            url = "https://api.spotify.com" + url;
        }
        String ret = "FAILED";
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(body);
            post.setEntity(requestEntity);
            post.addHeader("Authorization", "Bearer " + token);
            ret = EntityUtils.toString(client.execute(post).getEntity());
        } catch (IOException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }

    /**
     * Makes a get request with the Spotify api token to the api endpoint specified with the url
     * @param  url the api endpoint
     * @return     The response of the request
     */
    public String makeGet(String url) {
        if(!url.contains("https")) {
            url = "https://api.spotify.com" + url;
        }
        String ret = "FAILED";
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet post = new HttpGet(url);
            post.addHeader("Authorization", "Bearer " + token);
            ret = EntityUtils.toString(client.execute(post).getEntity());
        } catch (IOException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }

    /**
     * Makes a post request with the Spotify api token to the api endpoint specified with the url
     * @param  url the api endpoint
     * @param pairs The headers to send
     * @return     The response of the request
     */
    public String makeGet(String url, NameValuePair... pairs) {
        if(!url.contains("https")) {
            url = "https://api.spotify.com" + url;
        }
        String ret = "FAILED";
        try {
            StringBuilder builder = new StringBuilder();
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            get.setHeader("Authorization", "Bearer " + token);
            int i = 0;
            for(NameValuePair pair : pairs) {
                if(i == 0) {
                    builder.append("?").append(pair.getName()).append("=").append(pair.getValue());
                }else{
                    builder.append("&").append(pair.getName()).append("=").append(pair.getValue());
                }
                i++;
            }
            get.setURI(new URI(url + builder));
            ret = EntityUtils.toString(client.execute(get).getEntity());
        } catch (IOException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * Makes a put request with the Spotify api token to the api endpoint specified with the url
     * @param  url the api endpoint
     * @return     The response of the request
     */
    @SuppressWarnings("UnusedReturnValue")
    public String makePut(String url) {
        if(!url.contains("https")) {
            url = "https://api.spotify.com" + url;
        }
        String ret = "FAILED";
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPut put = new HttpPut(url);
            put.addHeader("Authorization", "Bearer " + token);
            ret = EntityUtils.toString(client.execute(put).getEntity());
        } catch (IOException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }

    /**
     * Makes a delete request with the Spotify api token to the api endpoint specified with the url
     * @param  url the api endpoint
     * @return     The response of the request
     */
    @SuppressWarnings("UnusedReturnValue")
    public String makeDelete(String url) {
        if(!url.contains("https")) {
            url = "https://api.spotify.com" + url;
        }
        String ret = "FAILED";
        try {
            HttpClient client = HttpClients.createDefault();
            HttpDelete delete = new HttpDelete(url);
            delete.setHeader("Authorization", "Bearer " + token);
            ret = EntityUtils.toString(client.execute(delete).getEntity());
        } catch (IOException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }
}
