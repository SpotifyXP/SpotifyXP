package com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed;

import java.io.IOException;

/**
 * The server is currently unable to handle the request due to a temporary condition which will be alleviated after some
 * delay. You can choose to resend the request again.
 */
public class ServiceUnavailableException extends IOException {

    public ServiceUnavailableException() {
        super();
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
