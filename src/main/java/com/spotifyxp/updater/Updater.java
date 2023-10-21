package com.spotifyxp.updater;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.ApplicationUtils;
import org.json.JSONObject;

public class Updater {
    public static boolean disable = false;

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
            int Thismain = Integer.parseInt(ApplicationUtils.getVersion().split("\\.")[0]);
            int Thissub = Integer.parseInt(ApplicationUtils.getVersion().split("\\.")[1]);
            int Thislast = 0;
            try {
                Thislast = Integer.parseInt(ApplicationUtils.getVersion().split("\\.")[2]);
            }catch (Exception ignored) {
            }
            if(!isNightly()) {
                if (Releasemain > Thismain) {
                    info.updateAvailable = true;
                } else {
                    if (Releasesub > Thissub) {
                        info.updateAvailable = true;
                    } else {
                        info.updateAvailable = Releaselast > Thislast;
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
            int Thismain = Integer.parseInt(ApplicationUtils.getVersion().split("\\.")[0]);
            int Thissub = Integer.parseInt(ApplicationUtils.getVersion().split("\\.")[1]);
            int Thislast = 0;
            try {
                Thislast = Integer.parseInt(ApplicationUtils.getVersion().split("\\.")[2]);
            }catch (Exception ignored) {
            }
            if (Releasemain < Thismain) {
                return true;
            } else {
                if (Releasesub < Thissub) {
                    return true;
                } else {
                    return Releaselast < Thislast;
                }
            }
        }catch (Exception e) {
            return true;
        }
    }

    public void invoke() {
        if(disable) {
            return;
        }
        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                ProcessBuilder builder = new ProcessBuilder("bash", "-c", "java", "-jar", PublicValues.appLocation + "/SpotifyXP-Updater.jar.bak", ApplicationUtils.getVersion(), "\"" + PublicValues.appLocation + "\"");
                builder.start();
            }catch (Exception e) {
                ConsoleLogging.Throwable(e);
                ExceptionDialog.open(e);
            }
            return;
        }
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "java", "-jar", PublicValues.appLocation + "/SpotifyXP-Updater.jar.bak", ApplicationUtils.getVersion(), "\"" + PublicValues.appLocation + "\"");
            builder.start();
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
    }

    public String getChangelogForNewest() {
        JSONObject root = new JSONObject(GitHubAPI.makeRequestGet("https://api.github.com/repos/Werwolf2303/SpotifyXP/releases/latest"));
        GitHubAPI.Release release = new GitHubAPI.Release();
        return root.getString("body");
    }
}
