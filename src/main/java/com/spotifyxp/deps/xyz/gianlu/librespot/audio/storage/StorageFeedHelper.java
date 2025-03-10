/*
 * Copyright 2021 devgianlu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spotifyxp.deps.xyz.gianlu.librespot.audio.storage;

import com.spotifyxp.deps.com.spotify.metadata.Metadata;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.HaltListener;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.NormalizationData;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.PlayableContentFeeder;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.format.SuperAudioFormat;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.crypto.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Gianlu
 */
public final class StorageFeedHelper {

    private StorageFeedHelper() {
    }

    public static @NotNull PlayableContentFeeder.LoadedStream loadTrack(@NotNull Session session, @NotNull Metadata.Track track, @NotNull Metadata.AudioFile file, boolean preload, @Nullable HaltListener haltListener) throws IOException {
        long start = System.currentTimeMillis();
        byte[] key = session.audioKey().getAudioKey(track.getGid(), file.getFileId());
        int audioKeyTime = (int) (System.currentTimeMillis() - start);

        AudioFileStreaming stream = new AudioFileStreaming(session, file, key, haltListener);
        stream.open();

        session.send(Packet.Type.Unknown_0x4f, new byte[0]);

        InputStream in = stream.stream();
        NormalizationData normalizationData = NormalizationData.read(in);
        if (in.skip(0xa7) != 0xa7) throw new IOException("Couldn't skip 0xa7 bytes!");

        return new PlayableContentFeeder.LoadedStream(track, stream, normalizationData, new PlayableContentFeeder.Metrics(file.getFileId(), preload, preload ? -1 : audioKeyTime), SuperAudioFormat.get(file.getFormat()));
    }

    public static @NotNull PlayableContentFeeder.LoadedStream loadEpisode(@NotNull Session session, Metadata.@NotNull Episode episode, Metadata.@NotNull AudioFile file, boolean preload, @Nullable HaltListener haltListener) throws IOException {
        long start = System.currentTimeMillis();
        byte[] key = session.audioKey().getAudioKey(episode.getGid(), file.getFileId());
        int audioKeyTime = (int) (System.currentTimeMillis() - start);

        AudioFileStreaming stream = new AudioFileStreaming(session, file, key, haltListener);
        stream.open();

        InputStream in = stream.stream();
        NormalizationData normalizationData = NormalizationData.read(in);
        if (in.skip(0xa7) != 0xa7) throw new IOException("Couldn't skip 0xa7 bytes!");

        return new PlayableContentFeeder.LoadedStream(episode, stream, normalizationData, new PlayableContentFeeder.Metrics(file.getFileId(), preload, preload ? -1 : audioKeyTime), SuperAudioFormat.get(file.getFormat()));
    }
}
