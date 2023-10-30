package com.spotifyxp.api;

import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.ConnectionUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;

public class GitHubAPI {
    /**
     * This method makes get Requests with the GitHub api token to the specified URL
     * @param  url  the page to go to
     * @return     The response body of the requested page
     */
    @SuppressWarnings("SameParameterValue")
    public static String makeRequestGet(String url) {
        String ret = "FAILED";
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            get.addHeader("Accept", "application/vnd.github+json");
            get.addHeader("X-GitHub-Api-Version", "2022-11-28");
            ret = EntityUtils.toString(client.execute(get).getEntity());
        } catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        }
        return ret;
    }

    /**
     * When an instance of this class is returned it has the following values
     * <br>downloadURL -> The .jar file url of the Release
     * <br>version -> The version of the Release
     */
    public static class Release {
        public String downloadURL = "";
        public String version = "";
    }
    public static class Releases {
        /**
         * Gets the latest SpotifyXP release
         * @return      instance of the Release class
         * @see         Release
         */
        public Release getLatest() {
            JSONObject root = new JSONObject(makeRequestGet("https://api.github.com/repos/SpotifyXP/SpotifyXP/releases/latest"));
            JSONObject ret = new JSONObject(ConnectionUtils.makeGet(new JSONObject(root.getJSONArray("assets").get(0).toString()).getString("url")));
            Release release = new Release();
            release.downloadURL = ret.getString("browser_download_url");
            release.version = root.getString("tag_name");
            return release;
        }
    }
}
