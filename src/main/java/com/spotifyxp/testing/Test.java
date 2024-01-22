package com.spotifyxp.testing;

import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.injector.InjectorAPI;

public class Test {
    public static void main(String[] args) throws Exception {
        for(SpotifyXPEvents s : SpotifyXPEvents.values()) {
            Events.register(s.getName(), true);
        }
        InjectorAPI api = new InjectorAPI();
        InjectorAPI.injectorRepos.clear();
        InjectorAPI.injectorRepos.add(new InjectorAPI.InjectorRepository("http://127.0.0.1:8000"));
    }
}
