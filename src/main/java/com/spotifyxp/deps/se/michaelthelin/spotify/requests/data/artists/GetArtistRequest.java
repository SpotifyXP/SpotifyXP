package com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.artists;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Artist;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.AbstractDataRequest;

import java.io.IOException;

/**
 * Get Spotify catalog information for a single artist identified by their unique Spotify ID.
 */
@JsonDeserialize(builder = GetArtistRequest.Builder.class)
public class GetArtistRequest extends AbstractDataRequest<Artist> {

    /**
     * The private {@link GetArtistRequest} constructor.
     *
     * @param builder A {@link GetArtistRequest.Builder}.
     */
    private GetArtistRequest(final Builder builder) {
        super(builder);
    }

    /**
     * Get an {@link Artist}.
     *
     * @return An {@link Artist}.
     * @throws IOException In case of networking issues.
     */
    public Artist execute() throws
            IOException {
        return new Artist.JsonUtil().createModelObject(getJson());
    }

    /**
     * Builder class for building a {@link GetArtistRequest}.
     */
    public static final class Builder extends AbstractDataRequest.Builder<Artist, Builder> {

        /**
         * Create a new {@link GetArtistRequest.Builder} instance.
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
         * @return A {@link GetArtistRequest.Builder}.
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
         * @return A custom {@link GetArtistRequest}.
         */
        @Override
        public GetArtistRequest build() {
            setPath("/v1/artists/{id}");
            return new GetArtistRequest(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
