package com.spotifyxp.theming;

public interface Theme {
    String getAuthor();
    boolean isLight();
    void initTheme();
    boolean hasLegacyUI();
}
