package com.spotifyxp.testing;


import com.spotifyxp.api.GitHubAPI;

public class Test {
    public static void main(String[] args) {
        System.err.println(new GitHubAPI.Releases().getLatest().version);
    }
}
