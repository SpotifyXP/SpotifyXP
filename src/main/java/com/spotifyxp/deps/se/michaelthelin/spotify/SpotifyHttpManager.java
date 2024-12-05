package com.spotifyxp.deps.se.michaelthelin.spotify;

import com.google.gson.*;
import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed.*;
import com.spotifyxp.enums.HttpStatusCodes;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.manager.InstanceManager;
import kotlin.Pair;
import okhttp3.Authenticator;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SpotifyHttpManager implements IHttpManager {

    private static final Gson GSON = new Gson();
    private final OkHttpClient httpClient;
    private final InetSocketAddress proxyAddress;
    private final Proxy.Type proxyType;
    private final String proxyUsername;
    private final String proxyPassword;
    private final Integer connectionRequestTimeout;
    private final Integer connectTimeout;
    private final Integer socketTimeout;

    /**
     * Construct a new SpotifyHttpManager instance.
     *
     * @param builder The builder.
     */
    public SpotifyHttpManager(Builder builder) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        this.proxyAddress = builder.proxyAddress;
        this.proxyType = builder.proxyType;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
        this.connectionRequestTimeout = builder.connectionRequestTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.socketTimeout = builder.socketTimeout;

        if (connectTimeout != null) clientBuilder.setConnectTimeout$okhttp(connectTimeout);
        if (connectionRequestTimeout != null) clientBuilder.setCallTimeout$okhttp(connectionRequestTimeout);
        if (proxyAddress != null) {
            if (proxyUsername != null && proxyPassword != null) {
                clientBuilder.setProxyAuthenticator$okhttp(new Authenticator() {
                    @Nullable
                    @Override
                    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
                        String credential = Credentials.basic(proxyUsername, proxyPassword);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                });
            }
            clientBuilder.setProxy$okhttp(new Proxy(proxyType, proxyAddress));
        }

        this.httpClient = new OkHttpClient(clientBuilder);
    }

    public static URI makeUri(String uriString) {
        try {
            return new URI(uriString);
        } catch (URISyntaxException e) {
            ConsoleLoggingModules.error(
                    "URI Syntax Exception for \"" + uriString + "\"");
            return null;
        }
    }

    public InetSocketAddress getProxyAddress() {
        return proxyAddress;
    }

    public Proxy.Type getProxyType() {
        return proxyType;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    @Override
    public String get(URI uri, Map<String, String> headers) throws
            IOException {
        assert (uri != null);
        assert (!uri.toString().isEmpty());

        Request request = new Request.Builder()
                .url(uri.toURL())
                .headers(Headers.of(headers))
                .build();
        ConsoleLoggingModules.info(
                "GET request uses these headers: " + GSON.toJson(headers));

        ConsoleLoggingModules.info("GET request with URI: " + uri);

        String responseBody;
        try {
            responseBody = getResponseBody(execute(httpClient, request), String.valueOf(uri), "get", request, null);
        } catch (UnauthorizedException exc) {
            throw new UnauthorizedException();
        }

        return responseBody;
    }

    public String get(URI uri, Headers headers) throws IOException {
        assert (uri != null);
        assert (headers != null);
        Map<String, String> headersMap = new HashMap<>();
        for (Pair<? extends String, ? extends String> header : headers) {
            headersMap.put(header.getFirst(), header.getSecond());
        }
        return get(uri, headersMap);
    }

    @Override
    public String post(URI uri, Map<String, String> headers, RequestBody body) throws
            IOException {
        assert (uri != null);
        assert (!uri.toString().isEmpty());

        Request request = new Request.Builder()
                .url(uri.toURL())
                .headers(Headers.of(headers))
                .post(body)
                .build();

        ConsoleLoggingModules.info(
                "POST request uses these headers: " + GSON.toJson(headers));

        String responseBody;
        try {
            responseBody = getResponseBody(execute(httpClient, request), String.valueOf(uri), "post", request, body);
        } catch (UnauthorizedException exc) {
            throw new UnauthorizedException();
        }

        return responseBody;
    }

    public String post(URI uri, Headers headers, RequestBody body) throws IOException {
        assert (uri != null);
        assert (headers != null);
        Map<String, String> headersMap = new HashMap<>();
        for (Pair<? extends String, ? extends String> header : headers) {
            headersMap.put(header.getFirst(), header.getSecond());
        }
        return post(uri, headersMap, body);
    }

    @Override
    public String put(URI uri, Map<String, String> headers, RequestBody body) throws
            IOException {
        assert (uri != null);
        assert (!uri.toString().isEmpty());

        Request request = new Request.Builder()
                .url(uri.toURL())
                .headers(Headers.of(headers))
                .put(body)
                .build();

        ConsoleLoggingModules.info(
                "PUT request uses these headers: " + GSON.toJson(headers));
        String responseBody;
        try {
            responseBody = getResponseBody(execute(httpClient, request), String.valueOf(uri), "put", request, body);
        } catch (UnauthorizedException exc) {
            throw new UnauthorizedException();
        }

        return responseBody;
    }

    public String put(URI uri, Headers headers, RequestBody body) throws IOException {
        assert (uri != null);
        assert (headers != null);
        Map<String, String> headersMap = new HashMap<>();
        for (Pair<? extends String, ? extends String> header : headers) {
            headersMap.put(header.getFirst(), header.getSecond());
        }
        return put(uri, headersMap, body);
    }

    @Override
    public String delete(URI uri, Map<String, String> headers, RequestBody body) throws
            IOException {
        assert (uri != null);
        assert (!uri.toString().isEmpty());

        Request request = new Request.Builder()
                .url(uri.toURL())
                .headers(Headers.of(headers))
                .delete()
                .build();

        ConsoleLoggingModules.info(
                "DELETE request uses these headers: " + GSON.toJson(headers));

        String responseBody;
        try {
            responseBody = getResponseBody(execute(httpClient, request), String.valueOf(uri), "delete", request, body);
        } catch (UnauthorizedException exc) {
            throw new UnauthorizedException();
        }

        return responseBody;
    }

    public String delete(URI uri, Headers headers, RequestBody body) throws IOException {
        assert (uri != null);
        assert (headers != null);
        Map<String, String> headersMap = new HashMap<>();
        for (Pair<? extends String, ? extends String> header : headers) {
            headersMap.put(header.getFirst(), header.getSecond());
        }
        return delete(uri, headersMap, body);
    }

    //ToDo: Remove this dirty hack
    private int retries = 0;
    private Response execute(OkHttpClient httpClient, Request method) throws
            IOException {
        Response response;
        try {
            response = httpClient.newCall(method).execute();
        } catch (UnknownHostException e) {
            // No internet?
            if(retries > 30) {
                throw new IOException(e.getMessage());
            }
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            retries++;
            return execute(httpClient, method);
        }
        retries = 0;
        return response;
    }

    public static boolean triggerTokenExpire = false;

    private String getResponseBody(Response httpResponse, String uri, String method, Request base, RequestBody body) throws
            IOException {

        final String responseBody = httpResponse.body() != null
                ? httpResponse.body().string()
                : null;
        String errorMessage = httpResponse.message();

        ConsoleLoggingModules.debug(
                "The http response has body " + responseBody);

        if (responseBody != null && !responseBody.isEmpty()) {
            try {
                final JsonElement jsonElement = JsonParser.parseString(responseBody);

                if (jsonElement.isJsonObject()) {
                    final JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                    if (jsonObject.has("error")) {
                        if (jsonObject.has("error_description")) {
                            errorMessage = jsonObject.get("error_description").getAsString();
                        } else if (jsonObject.get("error").isJsonObject() && jsonObject.getAsJsonObject("error").has("message")) {
                            errorMessage = jsonObject.getAsJsonObject("error").get("message").getAsString();
                        }
                    }
                }
            } catch (JsonSyntaxException e) {
                // Not necessary
            }
        }

        if (triggerTokenExpire) {
            httpResponse = new Response.Builder()
                    .priorResponse(httpResponse)
                    .code(HttpStatusCodes.UNAUTHORIZED.getValue())
                    .build();
        }

        ConsoleLoggingModules.debug(
                "The http response has status code " + httpResponse.code());

        switch (httpResponse.code()) {
            case 400:
                throw new BadRequestException(errorMessage);
            case 401:
                //Refresh token
                Events.triggerEvent(SpotifyXPEvents.apikeyrefresh.getName());
                base = base.newBuilder().removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer " + InstanceManager.getPkce().getToken())
                        .build();
                switch (method) {
                    case "get":
                        triggerTokenExpire = false;
                        return get(URI.create(uri), base.headers());
                    case "post":
                        triggerTokenExpire = false;
                        return post(URI.create(uri), base.headers(), body);
                    case "put":
                        triggerTokenExpire = false;
                        return put(URI.create(uri), base.headers(), body);
                    case "delete":
                        triggerTokenExpire = false;
                        return delete(URI.create(uri), base.headers(), body);
                }
            case 403:
                throw new ForbiddenException(errorMessage);
            case 404:
                throw new NotFoundException(errorMessage);
            case 429: // TOO_MANY_REQUESTS (additional status code, RFC 6585)
                // Sets "Retry-After" header as described at https://beta.developer.spotify.com/documentation/web-api/#rate-limiting
                String header = httpResponse.header("Retry-After");

                if (header != null) {
                    throw new TooManyRequestsException(errorMessage, Integer.parseInt(header));
                } else {
                    throw new TooManyRequestsException(errorMessage);
                }
            case 500:
                throw new InternalServerErrorException(errorMessage);
            case 502:
                throw new BadGatewayException(errorMessage);
            case 503:
                throw new ServiceUnavailableException(errorMessage);
            default:
                return responseBody;
        }
    }

    public static class Builder {
        private InetSocketAddress proxyAddress;
        private Proxy.Type proxyType;
        private String proxyUsername;
        private String proxyPassword;
        private Integer cacheMaxEntries;
        private Integer cacheMaxObjectSize;
        private Integer connectionRequestTimeout;
        private Integer connectTimeout;
        private Integer socketTimeout;

        public Builder setProxyAddress(InetSocketAddress proxyAddress) {
            this.proxyAddress = proxyAddress;
            return this;
        }

        public Builder setProxyType(Proxy.Type proxyType) {
            this.proxyType = proxyType;
            return this;
        }

        public Builder setProxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public Builder setProxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public Builder setCacheMaxEntries(Integer cacheMaxEntries) {
            this.cacheMaxEntries = cacheMaxEntries;
            return this;
        }

        public Builder setCacheMaxObjectSize(Integer cacheMaxObjectSize) {
            this.cacheMaxObjectSize = cacheMaxObjectSize;
            return this;
        }

        public Builder setConnectionRequestTimeout(Integer connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }

        public Builder setConnectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setSocketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public SpotifyHttpManager build() {
            return new SpotifyHttpManager(this);
        }
    }
}
