package com.spotifyxp.deps.se.michaelthelin.spotify;


import okhttp3.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * A simple HTTP connection interface.
 */
public interface IHttpManager {

    /**
     * Perform an HTTP GET request to the specified URL.
     *
     * @param uri     The GET request's {@link URI}.
     * @param headers The GET request's {@link Map}s.
     * @return A string containing the GET request's response body.
     * @throws IOException In case of networking issues.
     */
    String get(URI uri, Map<String, String> headers) throws
            IOException;

    /**
     * Perform an HTTP POST request to the specified URL.
     *
     * @param uri     The POST request's {@link URI}.
     * @param headers The POST request's {@link Map}s.
     * @param body    The PUT request's body as a {@link RequestBody}.
     * @return A string containing the POST request's response body.
     * @throws IOException In case of networking issues.
     */
    String post(URI uri, Map<String, String> headers, RequestBody body) throws
            IOException;

    /**
     * Perform an HTTP PUT request to the specified URL.
     *
     * @param uri     The PUT request's {@link URI}.
     * @param headers The PUT request's {@link Map}s.
     * @param body    The PUT request's body as a {@link RequestBody}.
     * @return A string containing the PUT request's response body.
     * @throws IOException In case of networking issues.
     */
    String put(URI uri, Map<String, String> headers, RequestBody body) throws
            IOException;

    /**
     * Perform an HTTP DELETE request to the specified URL.
     *
     * @param uri     The DELETE request's {@link URI}.
     * @param headers The DELETE request's {@link Map}s.
     * @param body    The DELETE request's body as a {@link RequestBody}.
     * @return A string containing the DELETE request's response body.
     * @throws IOException In case of networking issues.
     */
    String delete(URI uri, Map<String, String> headers, RequestBody body) throws
            IOException;

}
