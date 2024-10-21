package com.spotifyxp.deps.se.michaelthelin.spotify.requests;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.spotifyxp.deps.se.michaelthelin.spotify.IHttpManager;
import com.spotifyxp.utils.NameValuePair;
import okhttp3.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IRequest<T> {

    IHttpManager getHttpManager();

    URI getUri();

    Map<String, String> getHeaders();

    RequestBody getBody();

    List<NameValuePair> getBodyParameters();

    T execute() throws
            IOException;

    CompletableFuture<T> executeAsync();

    String getJson() throws
            IOException;

    String postJson() throws
            IOException;

    String putJson() throws
            IOException;

    String deleteJson() throws
            IOException;

    @JsonPOJOBuilder(withPrefix = "set")
    interface Builder<T, BT extends Builder<T, ?>> {

        BT setHttpManager(final IHttpManager httpManager);

        BT setScheme(final String scheme);

        BT setHost(final String host);

        BT setPort(final Integer port);

        BT setPath(final String path);

        BT setPathParameter(final String name, final String value);

        BT setDefaults(final IHttpManager httpManager,
                       final String scheme,
                       final String host,
                       final Integer port);

        <ST> BT setQueryParameter(final String name, final ST value);

        <ST> BT setHeader(final String name, final ST value);

        BT setContentType(final String contentType);

        BT setBody(final RequestBody httpEntity);

        <ST> BT setBodyParameter(final String name, final ST value);

        IRequest<T> build();
    }
}
