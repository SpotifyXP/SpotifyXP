package com.spotifyxp.api;

import com.google.gson.Gson;
import com.spotifyxp.utils.ConnectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GitHubAPI {
    public static class Artifacts {
        public String total_count;
        public List<Artifact> artifacts;
    }

    public static class Artifact {
        public String id;
        public String name;
        public String digest;
        public int size_in_bytes;
        public String archive_download_url;
        public ArtifactWorkflowRun workflow_run;
    }

    public static class ArtifactWorkflowRun {
        public String head_branch;
        public String head_sha;
    }

    public static class Commits {
        public Commit commit;
    }

    public static class Commit {
        public String message;
    }

    public static Artifacts getArtifacts() throws IOException {
        return new Gson().fromJson(ConnectionUtils.makeGet("https://api.github.com/repos/SpotifyXP/SpotifyXP/actions/artifacts", new HashMap<>()), Artifacts.class);
    }

    public static String getCommitMessage(String commmit_id) throws IOException {
        return new Gson().fromJson(ConnectionUtils.makeGet("https://api.github.com/repos/SpotifyXP/SpotifyXP/commits/" + commmit_id, new HashMap<>()), Commits.class).commit.message;
    }
}
