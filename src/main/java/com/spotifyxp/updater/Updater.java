package com.spotifyxp.updater;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.utils.DoubleArrayList;
import org.json.JSONObject;

public class Updater {
    public DoubleArrayList updateAvailable() {
        GitHubAPI.Releases releases = new GitHubAPI.Releases();
        GitHubAPI.Release release = releases.getLatest();
        DoubleArrayList arrayList = new DoubleArrayList();
        if(Integer.parseInt(release.version.replace("v", "").replace(".", ""))>Integer.parseInt(PublicValues.version.replace(".", ""))) {
            arrayList.add(true, release);
        }else{
            arrayList.add(false, release);
        }
        return arrayList;
    }

    public String getChangelogForNewest() {
        JSONObject root = new JSONObject(GitHubAPI.makeRequestGet("https://api.github.com/repos/Werwolf2303/SpotifyXP/releases/latest"));
        GitHubAPI.Release release = new GitHubAPI.Release();
        return root.getString("body");
    }
}
