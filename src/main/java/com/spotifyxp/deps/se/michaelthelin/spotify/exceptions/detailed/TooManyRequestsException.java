package com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed;

import java.io.IOException;

/**
 * Rate limiting has been applied.
 */
public class TooManyRequestsException extends IOException {

    private int retryAfter;

    public TooManyRequestsException() {
        super();
    }

    public TooManyRequestsException(String message, int retryAfter) {
        super(message);
        this.setRetryAfter(retryAfter);
    }

    public TooManyRequestsException(String message) {
        super(message);
    }

    public TooManyRequestsException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getRetryAfter() {
        return retryAfter;
    }

    public void setRetryAfter(int retryAfter) {
        this.retryAfter = retryAfter;
    }

}
