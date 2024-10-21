package com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.artists;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Artist;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.AbstractDataRequest;

import java.io.IOException;

/**
 * Get Spotify catalog information about artists similar to a given artist. Similarity is based on analysis of the
 * Spotify community’s listening history.
 */
@JsonDeserialize(builder = GetArtistsRelatedArtistsRequest.Builder.class)
public class GetArtistsRelatedArtistsRequest extends AbstractDataRequest<Artist[]> {

    /**
     * The private {@link GetArtistsRelatedArtistsRequest} constructor.
     *
     * @param builder A {@link GetArtistsRelatedArtistsRequest.Builder}.
     */
    private GetArtistsRelatedArtistsRequest(final Builder builder) {
        super(builder);
    }

    /**
     * Get the related {@link Artist} objects.
     *
     * @return An array of {@link Artist} objects.
     * @throws IOException In case of networking issues.
     */
    public Artist[] execute() throws
            IOException {
        return new Artist.JsonUtil().createModelObjectArray(getJson(), "artists");
    }

    /**
     * Builder class for building a {@link GetArtistsRelatedArtistsRequest}.
     */
    public static final class Builder extends AbstractDataRequest.Builder<Artist[], Builder> {

        /**
         * Create a new {@link GetArtistsRelatedArtistsRequest.Builder} instance.
         *
         * @param accessToken Required. A valid access token from the Spotify Accounts service.
         */
        public Builder(final String accessToken) {
            super(accessToken);
        }

        /**
         * The artist ID setter.
         *
         * @param id The Spotify ID for the artist.
         * @return A {@link GetArtistsRelatedArtistsRequest.Builder}.
         * @see <a href="https://developer.spotify.com/web-api/user-guide/#spotify-uris-and-ids">Spotify URIs &amp; IDs</a>
         */
        public Builder id(final String id) {
            assert (id != null);
            assert (!id.isEmpty());
            return setPathParameter("id", id);
        }

        /**
         * The request build method.
         *
         * @return A custom {@link GetArtistsRelatedArtistsRequest}.
         */
        @Override
        public GetArtistsRelatedArtistsRequest build() {
            setPath("/v1/artists/{id}/related-artists");
            return new GetArtistsRelatedArtistsRequest(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
