package com.spotifyxp.api;

import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.ConnectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Base64;

public class GitHubAPI {
    static final String token = new String(Base64.getDecoder().decode("Z2hwX3hBQ3Y3U1pVWnB2OWE0aWF6YXI3amlMNzFZRE5tVjB0aFlnSA=="));
    @SuppressWarnings("SameParameterValue")
    static String makeRequestGet(String url) {
        String ret = "FAILED";
        try {
            HttpClient client = new HttpClient();
            GetMethod post = new GetMethod(url);
            post.addRequestHeader("Authorization", token);
            post.addRequestHeader("Accept", "application/vnd.github+json");
            post.addRequestHeader("X-GitHub-Api-Version", "2022-11-28");
            client.executeMethod(post);
            ret = post.getResponseBodyAsString();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }
    public static class Release {
        public String downloadURL = "";
        public String version = "";
    }
    public static class Releases {
        public Release getLatest() {
            JSONObject root = new JSONObject(makeRequestGet("https://api.github.com/repos/Werwolf2303/SpotifyXP/releases/latest"));
            JSONObject ret = new JSONObject(ConnectionUtils.makeGet(new JSONObject(root.getJSONArray("assets").get(0).toString()).getString("url")));
            Release release = new Release();
            release.downloadURL = ret.getString("browser_download_url");
            release.version = root.getString("tag_name");
            return release;
        }
    }
}
