package com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.episodes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neovisionaries.i18n.CountryCode;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Episode;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.EpisodeSimplified;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.EpisodeWrapped;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Paging;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.AbstractDataPagingRequest;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.AbstractDataRequest;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;

import java.io.IOException;

@JsonDeserialize(builder = GetUsersSavedEpisodesRequest.Builder.class)
public class GetUsersSavedEpisodesRequest extends AbstractDataRequest<Paging<EpisodeWrapped>> {

    private GetUsersSavedEpisodesRequest(Builder builder) {
        super(builder);
    }

    public static final class Builder extends AbstractDataPagingRequest.Builder<EpisodeWrapped, Builder>{
        public Builder(String accessToken) {
            super(accessToken);
        }

        @Override
        public GetUsersSavedEpisodesRequest build() {
            setPath("/v1/me/episodes");
            return new GetUsersSavedEpisodesRequest(this);
        }

        @Override
        public Builder limit(final Integer limit) {
            assert (limit != null);
            assert (1 <= limit && limit <= 50);
            return setQueryParameter("limit", limit);
        }

        public Builder market(final CountryCode market) {
            assert (market != null);
            return setQueryParameter("market", market);
        }

        @Override
        public Builder offset(final Integer offset) {
            assert (0 <= offset && offset <= 100000);
            return setQueryParameter("offset", offset);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    @Override
    public Paging<EpisodeWrapped> execute() throws IOException {
        return new EpisodeWrapped.JsonUtil().createModelObjectPaging(getJson());
    }
}
