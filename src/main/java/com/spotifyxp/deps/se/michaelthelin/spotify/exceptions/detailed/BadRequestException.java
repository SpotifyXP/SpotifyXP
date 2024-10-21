package com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed;

import java.io.IOException;

/**
 * The request could not be understood by the server due to malformed syntax.
 */
public class BadRequestException extends IOException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
