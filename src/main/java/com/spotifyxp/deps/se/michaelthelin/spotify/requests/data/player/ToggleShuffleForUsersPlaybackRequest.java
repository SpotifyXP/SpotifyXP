package com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.AbstractDataRequest;

import java.io.IOException;

/**
 * Toggle shuffle on or off for user’s playback.
 */
@JsonDeserialize(builder = ToggleShuffleForUsersPlaybackRequest.Builder.class)
public class ToggleShuffleForUsersPlaybackRequest extends AbstractDataRequest<String> {

    /**
     * The private {@link ToggleShuffleForUsersPlaybackRequest} constructor.
     *
     * @param builder A {@link ToggleShuffleForUsersPlaybackRequest.Builder}.
     */
    private ToggleShuffleForUsersPlaybackRequest(final Builder builder) {
        super(builder);
    }

    /**
     * Toggle the shuffle state of an user's playback.
     *
     * @return A string. <b>Note:</b> This endpoint doesn't return something in its response body.
     * @throws IOException In case of networking issues.
     */
    public String execute() throws
            IOException {
        return putJson();
    }

    /**
     * Builder class for building a {@link ToggleShuffleForUsersPlaybackRequest}.
     */
    public static final class Builder extends AbstractDataRequest.Builder<String, Builder> {

        /**
         * Create a new {@link ToggleShuffleForUsersPlaybackRequest.Builder}.
         * <p>
         * Your access token must have the {@code user-modify-playback-state} scope authorized in order to control playback.
         *
         * @param accessToken Required. A valid access token from the Spotify Accounts service.
         * @see <a href="https://developer.spotify.com/web-api/using-scopes/">Spotify: Using Scopes</a>
         */
        public Builder(final String accessToken) {
            super(accessToken);
        }

        /**
         * The toggle state setter.
         *
         * @param state Required. {@code true}: Shuffle user's playback. {@code false}: Do not shuffle user's playback.
         * @return A {@link ToggleShuffleForUsersPlaybackRequest.Builder}.
         */
        public Builder state(final Boolean state) {
            return setQueryParameter("state", state);
        }

        /**
         * The device ID setter.
         *
         * @param device_id Optional. The ID of the device this command is targeting. If not supplied, the
         *                  user's currently active device is the target.
         * @return A {@link ToggleShuffleForUsersPlaybackRequest.Builder}.
         * @see <a href="https://developer.spotify.com/web-api/user-guide/#spotify-uris-and-ids">Spotify: URIs &amp; IDs</a>
         */
        public Builder device_id(final String device_id) {
            assert (device_id != null);
            assert (!device_id.isEmpty());
            return setQueryParameter("device_id", device_id);
        }

        /**
         * The request build method.
         *
         * @return A custom {@link ToggleShuffleForUsersPlaybackRequest}.
         */
        @Override
        public ToggleShuffleForUsersPlaybackRequest build() {
            setContentType("application/json");
            setPath("/v1/me/player/shuffle");
            return new ToggleShuffleForUsersPlaybackRequest(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
