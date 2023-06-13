package com.spotifyxp.theming;

import java.awt.*;

public interface Theme {
    String getAuthor();
    boolean isLight();
    void styleElement(java.lang.Class component);
}
