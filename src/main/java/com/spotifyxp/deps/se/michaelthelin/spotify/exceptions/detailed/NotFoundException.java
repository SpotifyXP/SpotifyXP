package com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed;

import java.io.IOException;

/**
 * The requested resource could not be found. This error can be due to a temporary or permanent condition.
 */
public class NotFoundException extends IOException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
