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

package com.spotifyxp.deps.xyz.gianlu.librespot.crypto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Gianlu
 */
public class Packet {
    public final byte cmd;
    public final byte[] payload;
    private Type type = null;

    Packet(byte cmd, byte[] payload) {
        this.cmd = cmd;
        this.payload = payload;
    }

    @Nullable
    public Type type() {
        if (type == null) type = Type.parse(cmd);
        return type;
    }

    public boolean is(@NotNull Type type) {
        return type() == type;
    }

    public enum Type {
        SecretBlock(0x02),
        Ping(0x04),
        StreamChunk(0x08),
        StreamChunkRes(0x09),
        ChannelError(0x0a),
        ChannelAbort(0x0b),
        RequestKey(0x0c),
        AesKey(0x0d),
        AesKeyError(0x0e),
        Image(0x19),
        CountryCode(0x1b),
        Pong(0x49),
        PongAck(0x4a),
        Pause(0x4b),
        ProductInfo(0x50),
        LegacyWelcome(0x69),
        LicenseVersion(0x76),
        Login(0xab),
        APWelcome(0xac),
        AuthFailure(0xad),
        MercuryReq(0xb2),
        MercurySub(0xb3),
        MercuryUnsub(0xb4),
        MercuryEvent(0xb5),
        TrackEndedTime(0x82),
        UnknownData_AllZeros(0x1f),
        PreferredLocale(0x74),
        Unknown_0x4f(0x4f),
        Unknown_0x0f(0x0f),
        Unknown_0x10(0x10);

        public final byte val;

        Type(int val) {
            this.val = (byte) val;
        }

        @Nullable
        public static Packet.Type parse(byte val) {
            for (Type cmd : values())
                if (cmd.val == val)
                    return cmd;

            return null;
        }

        public static Packet.Type forMethod(@NotNull String method) {
            switch (method) {
                case "SUB":
                    return MercurySub;
                case "UNSUB":
                    return MercuryUnsub;
                default:
                    return MercuryReq;
            }
        }
    }
}
