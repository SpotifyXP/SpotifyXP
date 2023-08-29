package com.spotifyxp.deps.de.werwolf2303.javasetuptool.components;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.Setup;

import javax.swing.*;

public interface Component {
    String getName();
    JPanel drawable();
    void init();
    void nowVisible();
    void onLeave();
    void giveComponents(JButton next, JButton previous, JButton cancel, JButton custom1, JButton custom2, Runnable fin, Setup.SetupBuilder builder);
}
