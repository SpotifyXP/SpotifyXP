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

import com.spotifyxp.logging.ConsoleLoggingModules;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * @author devgianlu
 */
public final class PipeOutput implements SinkOutput {
    
    private final File file;
    private OutputStream stream;

    public PipeOutput(@NotNull File file) {
        this.file = file;
    }

    @Override
    public void write(byte[] buffer, int offset, int len) throws IOException {
        if (stream == null) {
            if (!file.exists()) {
                try {
                    Process p = new ProcessBuilder()
                            .command("mkfifo", file.getAbsolutePath())
                            .redirectError(ProcessBuilder.Redirect.INHERIT)
                            .start();
                    p.waitFor();
                    if (p.exitValue() != 0)
                        ConsoleLoggingModules.warning("Failed creating pipe! {exit: {}}", p.exitValue());
                    else
                        ConsoleLoggingModules.info("Created pipe: " + file);
                } catch (InterruptedException ex) {
                    throw new IllegalStateException(ex);
                }
            }

            stream = Files.newOutputStream(file.toPath());
        }

        stream.write(buffer, 0, len);
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
