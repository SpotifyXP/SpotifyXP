package com.spotifyxp.deps.xyz.gianlu.librespot.player;

import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.metadata.PlayableId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface PlayerDefine {
    void volumeUp();
    void volumeUp(int steps);
    int getVolume();
    void volumeDown();
    void volumeDown(int steps);
    void setVolume(int val);
    void setShuffle(boolean val);
    void setRepeat(boolean track, boolean context);
    void play();
    void playPause();
    boolean isPaused();
    void next();
    void previous();
    void seek(int pos);
    void load(@NotNull String uri, boolean play, boolean shuffle, boolean playingFromLibrary);
    void addToQueue(@NotNull String uri);
    void removeFromQueue(@NotNull String uri);
    void waitReady() throws InterruptedException;
    @Nullable PlayableId currentPlayable();;
    boolean isActive();
    boolean isReady();
    Player.Tracks tracks(boolean withQueue);
    void clearQueue();
    MetadataWrapper currentMetadata();
    byte[] currentCoverImage() throws IOException;
    int time();
    void close();
    void removeAllFromQueue();
    void pause();
    void addEventsListener(@NotNull Player.EventsListener listener);
}
