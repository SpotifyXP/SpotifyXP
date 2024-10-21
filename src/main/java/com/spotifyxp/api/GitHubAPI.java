package com.spotifyxp.api;

import com.spotifyxp.utils.ConnectionUtils;
import com.spotifyxp.utils.MapUtils;
import org.json.JSONObject;

import java.io.IOException;

public class GitHubAPI {

    /**
     * When an instance of this class is returned it has the following values
     * <br>downloadURL -> The .jar file url of the Release
     * <br>version -> The version of the Release
     */
    public class Release {
        public String downloadURL = "";
        public String version = "";

        public Release() {
        }

        public Release(String downloadURL, String version) {
            this.downloadURL = downloadURL;
            this.version = version;
        }
    }

    public class Releases {
        /**
         * Gets the latest SpotifyXP release
         *
         * @return instance of the Release class
         * @see Release
         */
        public Release getLatest() throws IOException {
            JSONObject root = new JSONObject(ConnectionUtils.makeGet("https://api.github.com/repos/SpotifyXP/SpotifyXP/releases/latest",
                    MapUtils.of("Accept", "application/vnd.github+json", "X-Github-Api-Version", "2022-11-28")));
            JSONObject ret = new JSONObject(ConnectionUtils.makeGet(new JSONObject(root.getJSONArray("assets").get(0).toString()).getString("url"), null));
            Release release = new Release();
            release.downloadURL = ret.getString("browser_download_url");
            release.version = root.getString("tag_name");
            return release;
        }
    }

    public Releases getReleases() {
        return new Releases();
    }
}
