package com.spotifyxp.updater;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.utils.DoubleArrayList;

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
}
