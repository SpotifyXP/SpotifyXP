package com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.browse.miscellaneous;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.AbstractDataRequest;

import java.io.IOException;
import java.util.List;

/**
 * Retrieve a list of available genres seed parameter values for recommendations.
 */
@JsonDeserialize(builder = GetAvailableGenreSeedsRequest.Builder.class)
public class GetAvailableGenreSeedsRequest extends AbstractDataRequest<String[]> {

    /**
     * The private {@link GetAvailableGenreSeedsRequest} constructor.
     *
     * @param builder A {@link GetAvailableGenreSeedsRequest.Builder}.
     */
    private GetAvailableGenreSeedsRequest(final Builder builder) {
        super(builder);
    }

    /**
     * Get all available genre seeds.
     *
     * @return All available genre seeds.
     * @throws IOException In case of networking issues.
     */
    public String[] execute() throws
            IOException {
        List<String> genres = new Gson().fromJson(
                JsonParser
                        .parseString(getJson())
                        .getAsJsonObject()
                        .get("genres")
                        .getAsJsonArray(),
                new TypeToken<List<String>>() {
                }.getType()
        );

        return genres.toArray(new String[0]);
    }

    /**
     * Builder class for building a {@link GetAvailableGenreSeedsRequest.Builder}.
     */
    public static final class Builder extends AbstractDataRequest.Builder<String[], Builder> {

        /**
         * Create a new {@link GetAvailableGenreSeedsRequest.Builder} instance.
         *
         * @param accessToken Required. A valid access token from the Spotify Accounts service.
         */
        public Builder(final String accessToken) {
            super(accessToken);
        }

        /**
         * The request build method.
         *
         * @return A custom {@link GetAvailableGenreSeedsRequest}.
         */
        @Override
        public GetAvailableGenreSeedsRequest build() {
            setPath("/v1/recommendations/available-genre-seeds");
            return new GetAvailableGenreSeedsRequest(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
