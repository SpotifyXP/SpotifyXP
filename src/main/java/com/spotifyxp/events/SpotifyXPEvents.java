package com.spotifyxp.events;

public enum SpotifyXPEvents {
    queueUpdate("queueUpdate", "Fires when the player queue updates"),
    queueAdvance("queueAdvance", "Fires when the player queue advances"),
    queueRegress("queueRegress", "Fires when the player queue regresses"),
    playerLockRelease("playerLockRelease", "Fires when the player finished processing the metadata"),
    onFrameReady("frameReady", "Fires when the main JFrame finished building itself (before opening)"),
    trackNext("trackNext", "Fires when next track plays"),
    trackLoad("trackLoad", "Fires when the next track loads"),
    trackLoadFinished("trackLoadFinished", "Fires when the track loading is finished");

    public String getName() {
        return name;
    }

    private String name;
    private String description;
    SpotifyXPEvents(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
