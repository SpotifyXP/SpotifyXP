package com.spotifyxp.testing;

import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.injector.InjectorAPI;
import com.spotifyxp.utils.ConnectionUtils;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("Connection available: " + ConnectionUtils.isConnectedToInternet());
    }
}
