package com.spotifyxp.deps.de.werwolf2303.javasetuptool.components;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.Setup;

import javax.swing.*;

public class CustomComponent extends JPanel implements Component {

    public JPanel drawable() {
        return this;
    }

    @Override
    public String getName() {
        return "CustomComponent";
    }

    public void addComponent(java.awt.Component component, Object layout) {
        add(component, layout);
    }

    public void addComponent(java.awt.Component component) {
        add(component);
    }

    public void init() {

    }

    public void nowVisible() {

    }

    public void onLeave() {

    }

    public void giveComponents(JButton next, JButton previous, JButton cancel, JButton custom1, JButton custom2, Runnable fin, Setup.SetupBuilder builder) {

    }
}
