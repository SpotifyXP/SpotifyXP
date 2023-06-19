package com.spotifyxp.updater;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.deps.com.spotify.extendedmetadata.ExtendedMetadata;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.DoubleArrayList;
import org.json.JSONObject;

public class Updater {
    public static class UpdateInfo {
        public String version = "";
        public boolean updateAvailable = false;
        public String url = "";
    }
    public UpdateInfo updateAvailable() {
        try {
            GitHubAPI.Releases releases = new GitHubAPI.Releases();
            GitHubAPI.Release release = releases.getLatest();
            UpdateInfo info = new UpdateInfo();
            int Releasemain = Integer.parseInt(release.version.replace("v", "").split("\\.")[0]);
            int Releasesub = Integer.parseInt(release.version.replace("v", "").split("\\.")[1]);
            int Releaselast = 0;
            try {
                Releaselast = Integer.parseInt(release.version.replace("v", "").split("\\.")[2]);
            }catch (Exception ignored) {
            }
            int Thismain = Integer.parseInt(PublicValues.version.split("\\.")[0]);
            int Thissub = Integer.parseInt(PublicValues.version.split("\\.")[1]);
            int Thislast = 0;
            try {
                Thislast = Integer.parseInt(PublicValues.version.split("\\.")[2]);
            }catch (Exception ignored) {
            }
            if(!isNightly()) {
                if (Releasemain > Thismain) {
                    info.updateAvailable = true;
                } else {
                    if (Releasesub > Thissub) {
                        info.updateAvailable = true;
                    } else {
                        if (Releaselast > Thislast) {
                            info.updateAvailable = true;
                        } else {
                            info.updateAvailable = false;
                        }
                    }
                }
            }else{
                info.updateAvailable = false;
            }
            info.url = release.downloadURL;
            info.version = release.version;
            return info;
        }catch (Exception e) {
            return new UpdateInfo();
        }
    }

    public boolean isNightly() {
        try {
            GitHubAPI.Releases releases = new GitHubAPI.Releases();
            GitHubAPI.Release release = releases.getLatest();
            int Releasemain = Integer.parseInt(release.version.replace("v", "").split("\\.")[0]);
            int Releasesub = Integer.parseInt(release.version.replace("v", "").split("\\.")[1]);
            int Releaselast = 0;
            try {
                Releaselast = Integer.parseInt(release.version.replace("v", "").split("\\.")[2]);
            }catch (Exception ignored) {
            }
            int Thismain = Integer.parseInt(PublicValues.version.split("\\.")[0]);
            int Thissub = Integer.parseInt(PublicValues.version.split("\\.")[1]);
            int Thislast = 0;
            try {
                Thislast = Integer.parseInt(PublicValues.version.split("\\.")[2]);
            }catch (Exception ignored) {
            }
            if (Releasemain < Thismain) {
                return true;
            } else {
                if (Releasesub < Thissub) {
                    return true;
                } else {
                    if (Releaselast < Thislast) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }catch (Exception e) {
            return true;
        }
    }

    public String getChangelogForNewest() {
        JSONObject root = new JSONObject(GitHubAPI.makeRequestGet("https://api.github.com/repos/Werwolf2303/SpotifyXP/releases/latest"));
        GitHubAPI.Release release = new GitHubAPI.Release();
        return root.getString("body");
    }
}
