package com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spotifyxp.deps.se.michaelthelin.spotify.enums.ModelObjectType;
import com.spotifyxp.deps.se.michaelthelin.spotify.enums.ReleaseDatePrecision;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.AbstractModelObject;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.IModelObject;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.search.interfaces.ISearchModelObject;

import java.util.Arrays;
import java.util.Objects;

@JsonDeserialize(builder = EpisodeWrapped.Builder.class)
public class EpisodeWrapped extends AbstractModelObject {
    private final String added_at;
    private final Episode episode;

    private EpisodeWrapped(final Builder builder) {
        super(builder);
        this.episode = builder.episode;
        this.added_at = builder.added_at;
    }

    public Episode getEpisode() { return episode; }

    public static final class Builder extends AbstractModelObject.Builder {
        private Episode episode;
        private String added_at;

        public Builder setEpisode(Episode episode) {
            this.episode = episode;
            return this;
        }

        public Builder setAddedAt(String added_at) {
            this.added_at = added_at;
            return this;
        }

        @Override
        public EpisodeWrapped build() {
            return new EpisodeWrapped(this);
        }
    }

    public static final class JsonUtil extends AbstractModelObject.JsonUtil<EpisodeWrapped> {
        @Override
        public EpisodeWrapped createModelObject(JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }

            JsonObject originalJSON = jsonObject;
            jsonObject = jsonObject.getAsJsonObject("episode");

            return new EpisodeWrapped.Builder()
                    .setAddedAt(originalJSON.get("added_at").getAsString())
                    .setEpisode(new Episode.Builder().setAudioPreviewUrl(
                            hasAndNotNull(jsonObject, "audio_preview_url")
                                    ? jsonObject.get("audio_preview_url").getAsString()
                                    : null)
                    .setDescription(
                            hasAndNotNull(jsonObject, "description")
                                    ? jsonObject.get("description").getAsString()
                                    : null)
                    .setDurationMs(
                            hasAndNotNull(jsonObject, "duration_ms")
                                    ? jsonObject.get("duration_ms").getAsInt()
                                    : null)
                    .setExplicit(
                            hasAndNotNull(jsonObject, "explicit")
                                    ? jsonObject.get("explicit").getAsBoolean()
                                    : null)
                    .setExternalUrls(
                            hasAndNotNull(jsonObject, "external_urls")
                                    ? new ExternalUrl.JsonUtil().createModelObject(
                                    jsonObject.getAsJsonObject("external_urls"))
                                    : null)
                    .setHref(
                            hasAndNotNull(jsonObject, "href")
                                    ? jsonObject.get("href").getAsString()
                                    : null)
                    .setId(
                            hasAndNotNull(jsonObject, "id")
                                    ? jsonObject.get("id").getAsString()
                                    : null)
                    .setImages(
                            hasAndNotNull(jsonObject, "images")
                                    ? new Image.JsonUtil().createModelObjectArray(
                                    jsonObject.getAsJsonArray("images"))
                                    : null)
                    .setExternallyHosted(
                            hasAndNotNull(jsonObject, "is_externally_hosted")
                                    ? jsonObject.get("is_externally_hosted").getAsBoolean()
                                    : null)
                    .setPlayable(
                            hasAndNotNull(jsonObject, "is_playable")
                                    ? jsonObject.get("is_playable").getAsBoolean()
                                    : null)
                    .setLanguages(
                            hasAndNotNull(jsonObject, "languages")
                                    ? new Gson().fromJson(
                                    jsonObject.getAsJsonArray("languages"), String[].class)
                                    : null)
                    .setName(
                            hasAndNotNull(jsonObject, "name")
                                    ? jsonObject.get("name").getAsString()
                                    : null)
                    .setReleaseDate(
                            hasAndNotNull(jsonObject, "release_date")
                                    ? jsonObject.get("release_date").getAsString()
                                    : null)
                    .setReleaseDatePrecision(
                            hasAndNotNull(jsonObject, "release_date_precision")
                                    ? ReleaseDatePrecision.keyOf(
                                    jsonObject.get("release_date_precision").getAsString().toLowerCase())
                                    : null)
                    .setResumePoint(
                            hasAndNotNull(jsonObject, "resume_point")
                                    ? new ResumePoint.JsonUtil().createModelObject(
                                    jsonObject.getAsJsonObject("resume_point"))
                                    : null)
                    .setShow(
                            hasAndNotNull(jsonObject, "show")
                                    ? new ShowSimplified.JsonUtil().createModelObject(
                                    jsonObject.getAsJsonObject("show"))
                                    : null)
                    .setType(
                            hasAndNotNull(jsonObject, "type")
                                    ? ModelObjectType.keyOf(
                                    jsonObject.get("type").getAsString().toLowerCase())
                                    : null)
                    .setUri(
                            hasAndNotNull(jsonObject, "uri")
                                    ? jsonObject.get("uri").getAsString()
                                    : null)
                    .build()).build();
        }
    }

    @Override
    public String toString() {
        return "EpisodeWrapped(added_at=" + added_at + ", Episode(name=" + episode.getName() + ", description=" + episode.getDescription() + ", show=" + episode.getShow() + ", audioPreviewUrl="
                + episode.getAudioPreviewUrl() + ", durationMs=" + episode.getDurationMs() + ", explicit=" + episode.getExplicit() + ", externalUrls=" + episode.getExternalUrls()
                + ", href=" + episode.getHref() + ", id=" + episode.getId() + ", images=" + Arrays.toString(episode.getImages()) + ", isExternallyHosted="
                + episode.getExternallyHosted() + ", isPlayable=" + episode.getPlayable() + ", languages=" + Arrays.toString(episode.getLanguages())
                + ", releaseDate=" + episode.getReleaseDate() + ", releaseDatePrecision=" + episode.getReleaseDatePrecision() + ", resumePoint="
                + episode.getResumePoint() + ", type=" + episode.getType() + ", uri=" + episode.getUri() + ")";
    }

    @Override
    public Builder builder() {
        return new Builder();
    }
}
