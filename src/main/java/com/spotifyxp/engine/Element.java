package com.spotifyxp.engine;

import java.awt.*;

public interface Element {
    int getX();
    int getY();
    String getName();
    void drawElement(Graphics2D graphics2D);
    void setEnginePanelInstance(EnginePanel enginePanel);
    void setDebug(boolean debug);
}
