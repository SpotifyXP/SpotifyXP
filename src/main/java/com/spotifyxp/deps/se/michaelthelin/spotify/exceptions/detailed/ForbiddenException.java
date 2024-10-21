package com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed;

import java.io.IOException;

/**
 * The server understood the request, but is refusing to fulfill it.
 */
public class ForbiddenException extends IOException {

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

}
