package com.spotifyxp.dummy;

import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.AbstractRequest;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.IRequest;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

public class DummySpapi extends SpotifyApi {
    @Override
    public GetCurrentUsersProfileRequest.Builder getCurrentUsersProfile() {
        return null;
    }
}
