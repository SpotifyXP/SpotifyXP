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

package com.spotifyxp.deps.xyz.gianlu.librespot.player.mixing.output;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author devgianlu
 */
public interface SinkOutput extends Closeable {
    default boolean start(@NotNull OutputAudioFormat format) throws SinkException {
        return false;
    }

    void write(byte[] buffer, int offset, int len) throws IOException;

    default boolean setVolume(@Range(from = 0, to = 1) float volume) {
        return false;
    }

    default void release() {
    }

    default void drain() {
    }

    default void flush() {
    }

    default void stop() {
    }
}
