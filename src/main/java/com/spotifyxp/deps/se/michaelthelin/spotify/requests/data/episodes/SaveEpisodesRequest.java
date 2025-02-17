package com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.episodes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.AbstractDataRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.IOException;

@JsonDeserialize(builder = SaveEpisodesRequest.Builder.class)
public class SaveEpisodesRequest extends AbstractDataRequest<String> {

    protected SaveEpisodesRequest(AbstractDataRequest.Builder<String, ?> builder) {
        super(builder);
    }

    public static final class Builder extends AbstractDataRequest.Builder<String, Builder> {

        public Builder(String accessToken) {
            super(accessToken);
        }

        public Builder ids(final String[] ids) {
            assert (ids != null);
            return setBody(RequestBody.create(new JSONObject().put("ids", ids).toString(), MediaType.get("application/json")));
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public SaveEpisodesRequest build() {
            setPath("/v1/me/episodes");
            return new SaveEpisodesRequest(this);
        }
    }

    @Override
    public String execute() throws IOException {
        return putJson();
    }
}
