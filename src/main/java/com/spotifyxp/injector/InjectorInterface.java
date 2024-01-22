package com.spotifyxp.injector;

import java.util.ArrayList;

public interface InjectorInterface {
    String getIdentifier();
    String getVersion();
    String getAuthor();
    default ArrayList<Injector.Dependency> getDependencies() {
        return new ArrayList<>();
    }
    void init();
}
