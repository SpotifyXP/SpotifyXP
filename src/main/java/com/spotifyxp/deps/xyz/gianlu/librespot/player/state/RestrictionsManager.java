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

package com.spotifyxp.deps.xyz.gianlu.librespot.player.state;

import com.spotifyxp.deps.com.spotify.connectstate.Player;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.contexts.AbsSpotifyContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gianlu
 */
public final class RestrictionsManager {
    public static final String REASON_ENDLESS_CONTEXT = "endless_context";
    public static final String REASON_NO_PREV_TRACK = "no_prev_track";
    public static final String REASON_NO_NEXT_TRACK = "no_next_track";
    private final Player.Restrictions.Builder restrictions;

    public RestrictionsManager(@NotNull AbsSpotifyContext context) {
        restrictions = Player.Restrictions.newBuilder();

        if (!context.isFinite()) {
            disallow(Action.SHUFFLE, REASON_ENDLESS_CONTEXT);
            disallow(Action.REPEAT_CONTEXT, REASON_ENDLESS_CONTEXT);
        }
    }

    @NotNull
    public synchronized Player.Restrictions toProto() {
        return restrictions.build();
    }

    public synchronized boolean can(@NotNull Action action) {
        switch (action) {
            case SHUFFLE:
                return restrictions.getDisallowTogglingShuffleReasonsCount() == 0;
            case REPEAT_CONTEXT:
                return restrictions.getDisallowTogglingRepeatContextReasonsCount() == 0;
            case REPEAT_TRACK:
                return restrictions.getDisallowTogglingRepeatTrackReasonsCount() == 0;
            case PAUSE:
                return restrictions.getDisallowPausingReasonsCount() == 0;
            case RESUME:
                return restrictions.getDisallowResumingReasonsCount() == 0;
            case SEEK:
                return restrictions.getDisallowSeekingReasonsCount() == 0;
            case SKIP_PREV:
                return restrictions.getDisallowSkippingPrevReasonsCount() == 0;
            case SKIP_NEXT:
                return restrictions.getDisallowSkippingNextReasonsCount() == 0;
            default:
                throw new IllegalArgumentException("Unknown restriction for " + action);
        }
    }

    public synchronized void allow(@NotNull Action action) {
        switch (action) {
            case SHUFFLE:
                restrictions.clearDisallowTogglingShuffleReasons();
                break;
            case REPEAT_CONTEXT:
                restrictions.clearDisallowTogglingRepeatContextReasons();
                break;
            case REPEAT_TRACK:
                restrictions.clearDisallowTogglingRepeatTrackReasons();
                break;
            case PAUSE:
                restrictions.clearDisallowPausingReasons();
                break;
            case RESUME:
                restrictions.clearDisallowResumingReasons();
                break;
            case SEEK:
                restrictions.clearDisallowSeekingReasons();
                break;
            case SKIP_PREV:
                restrictions.clearDisallowSkippingPrevReasons();
                break;
            case SKIP_NEXT:
                restrictions.clearDisallowSkippingNextReasons();
                break;
            default:
                throw new IllegalArgumentException("Unknown restriction for " + action);
        }
    }

    public synchronized void disallow(@NotNull Action action, @NotNull String reason) {
        allow(action);

        switch (action) {
            case SHUFFLE:
                restrictions.addDisallowTogglingShuffleReasons(reason);
                break;
            case REPEAT_CONTEXT:
                restrictions.addDisallowTogglingRepeatContextReasons(reason);
                break;
            case REPEAT_TRACK:
                restrictions.addDisallowTogglingRepeatTrackReasons(reason);
                break;
            case PAUSE:
                restrictions.addDisallowPausingReasons(reason);
                break;
            case RESUME:
                restrictions.addDisallowResumingReasons(reason);
                break;
            case SEEK:
                restrictions.addDisallowSeekingReasons(reason);
                break;
            case SKIP_PREV:
                restrictions.addDisallowSkippingPrevReasons(reason);
                break;
            case SKIP_NEXT:
                restrictions.addDisallowSkippingNextReasons(reason);
                break;
            default:
                throw new IllegalArgumentException("Unknown restriction for " + action);
        }
    }

    public enum Action {
        SHUFFLE, REPEAT_CONTEXT, REPEAT_TRACK, PAUSE, RESUME, SEEK, SKIP_PREV, SKIP_NEXT
    }
}
