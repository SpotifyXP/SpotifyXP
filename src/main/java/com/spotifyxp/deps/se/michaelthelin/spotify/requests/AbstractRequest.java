package com.spotifyxp.deps.se.michaelthelin.spotify.requests;

import com.google.gson.*;
import com.spotifyxp.deps.se.michaelthelin.spotify.IHttpManager;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApiThreading;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.utils.NameValuePair;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.http2.Header;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class AbstractRequest<T> implements IRequest<T> {

    private final IHttpManager httpManager;
    private final Map<String, String> headers;
    private final String contentType;
    private final List<NameValuePair> bodyParameters;
    private URI uri;
    private RequestBody body;

    protected AbstractRequest(Builder<T, ?> builder) {
        assert (builder != null);
        assert (builder.path != null);
        assert (!builder.path.isEmpty());

        this.httpManager = builder.httpManager;

        HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder();
        httpUrlBuilder.scheme(builder.scheme)
                .host(builder.host)
                .port(builder.port)
                .encodedPath(builder.path); //ToDo: Find the right solution
        if (!builder.queryParameters.isEmpty()) {
            for (NameValuePair parameter : builder.queryParameters) {
                httpUrlBuilder.addQueryParameter(parameter.getName(), parameter.getValue());
            }
        }

        this.uri = httpUrlBuilder.build().uri();

        this.headers = builder.headers;
        this.contentType = builder.contentType;
        this.body = builder.body;
        this.bodyParameters = builder.bodyParameters;
    }

    /**
     * Get something asynchronously.
     *
     * @return A {@link CompletableFuture} for a generic.
     */
    public CompletableFuture<T> executeAsync() {
        return SpotifyApiThreading.executeAsync(
                this::execute);
    }

    public void initializeBody() {
        if (body == null && contentType != null) {
            if (contentType.equals("application/json")) {
                body = RequestBody.create(
                        bodyParametersToJson(bodyParameters),
                        MediaType.parse("application/json"));
            }
            if (contentType.equals("application/x-www-form-urlencoded")) {
                FormBody.Builder builder = new FormBody.Builder();
                for (NameValuePair parameter : bodyParameters) {
                    builder.add(parameter.getName(), parameter.getValue());
                }
                body = builder.build();
            }
        }
    }

    public String bodyParametersToJson(List<NameValuePair> bodyParameters) {
        JsonObject jsonObject = new JsonObject();
        JsonElement jsonElement;

        for (NameValuePair nameValuePair : bodyParameters) {
            try {
                jsonElement = JsonParser.parseString(nameValuePair.getValue());
            } catch (JsonSyntaxException e) {
                jsonElement = new JsonPrimitive(nameValuePair.getValue());
            }

            jsonObject.add(nameValuePair.getName(), jsonElement);
        }

        return jsonObject.toString();
    }

    public String getJson() throws
            IOException {

        String json = httpManager.get(uri, headers);

        return returnJson(json);
    }

    public String postJson() throws
            IOException {
        initializeBody();

        String json = httpManager.post(uri, headers, body);

        return returnJson(json);
    }

    public String putJson() throws
            IOException {
        initializeBody();

        String json = httpManager.put(uri, headers, body);

        return returnJson(json);
    }

    public String deleteJson() throws
            IOException {
        initializeBody();

        String json = httpManager.delete(uri, headers, body);

        return returnJson(json);
    }

    private String returnJson(String json) {
        if (json == null) {
            ConsoleLoggingModules.debug(
                    "The httpManager returned json == null.");
            return null;
        } else if (json.isEmpty()) {
            ConsoleLoggingModules.debug(
                    "The httpManager returned json == \"\".");
            return null;
        } else {
            ConsoleLoggingModules.debug(
                    "The httpManager returned json == " + json + ".");
            return json;
        }
    }

    public IHttpManager getHttpManager() {
        return httpManager;
    }

    public URI getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestBody getBody() {
        return body;
    }

    public List<NameValuePair> getBodyParameters() {
        return bodyParameters;
    }

    public static abstract class Builder<T, BT extends Builder<T, ?>> implements IRequest.Builder<T, BT> {
        private String contentType;
        private final List<NameValuePair> pathParameters = new ArrayList<>();
        private final List<NameValuePair> queryParameters = new ArrayList<>();
        private final Map<String, String> headers = new HashMap<>();
        private final List<NameValuePair> bodyParameters = new ArrayList<>();
        private IHttpManager httpManager = SpotifyApi.DEFAULT_HTTP_MANAGER;
        private String scheme = SpotifyApi.DEFAULT_SCHEME;
        private String host = SpotifyApi.DEFAULT_HOST;
        private Integer port = SpotifyApi.DEFAULT_PORT;
        private String path = null;
        private RequestBody body = null;

        protected Builder() {
        }

        public BT setHttpManager(final IHttpManager httpManager) {
            this.httpManager = httpManager;
            return self();
        }

        public BT setScheme(final String scheme) {
            this.scheme = scheme;
            return self();
        }

        public BT setHost(final String host) {
            assert (!scheme.isEmpty());
            this.host = host;
            return self();
        }

        public BT setPort(final Integer port) {
            this.port = port;
            return self();
        }

        public BT setPath(final String path) {
            String builtPath = path;

            for (NameValuePair nameValuePair : pathParameters) {
                // Don't remove the "\\" before the "}" to prevent a regex issue on Android.
                String key = "\\{" + nameValuePair.getName() + "\\}";
                builtPath = builtPath.replaceAll(key, nameValuePair.getValue());
            }

            this.path = builtPath;
            return self();
        }

        public BT setPathParameter(final String name, final String value) {
            listAddOnce(this.pathParameters, new NameValuePair(name, value));
            return self();
        }

        public BT setDefaults(final IHttpManager httpManager,
                              final String scheme,
                              final String host,
                              final Integer port) {
            setHttpManager(httpManager);
            setScheme(scheme);
            setHost(host);
            setPort(port);

            return self();
        }

        public <X> BT setQueryParameter(final String name, final X value) {
            listAddOnce(this.queryParameters, new NameValuePair(name, String.valueOf(value)));
            return self();
        }

        public <X> BT setHeader(final String name, final X value) {
            listAddOnce(this.headers, new Header(name, String.valueOf(value)));
            return self();
        }

        public BT setContentType(String contentType) {
            this.contentType = contentType;
            setHeader("Content-Type", contentType);
            return self();
        }

        public BT setBody(final RequestBody httpEntity) {
            this.body = httpEntity;
            return self();
        }

        public <X> BT setBodyParameter(final String name, final X value) {
            listAddOnce(this.bodyParameters, new NameValuePair(name, String.valueOf(value)));
            return self();
        }

        private void listAddOnce(List<NameValuePair> nameValuePairs, NameValuePair newNameValuePair) {
            nameValuePairs.removeAll(nameValuePairs.stream()
                    .filter(nameValuePair -> nameValuePair.getName().equals(newNameValuePair.getName()))
                    .collect(Collectors.toList()));
            nameValuePairs.add(newNameValuePair);
        }

        private void listAddOnce(Map<String, String> headers, Header newHeader) {
            headers.remove(newHeader.name.string(Charset.defaultCharset()));
            headers.put(newHeader.name.string(Charset.defaultCharset()), newHeader.value.string(Charset.defaultCharset()));
        }

        /**
         * Return this instance to simulate a self-type.
         *
         * @return This instance.
         */
        protected abstract BT self();
    }
}
