package com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.detailed;

import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

/**
 * The server was acting as a gateway or proxy and received an invalid response from the upstream server.
 */
public class BadGatewayException extends SpotifyWebApiException {

  public BadGatewayException() {
    super();
  }

  public BadGatewayException(String message) {
    super(message);
  }

  public BadGatewayException(String message, Throwable cause) {
    super(message, cause);
  }

}
