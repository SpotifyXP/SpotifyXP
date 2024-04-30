package com.spotifyxp.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AsyncActionListener implements ActionListener {
    private final ActionListener listener;

    public AsyncActionListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Utils.executeAsync(() -> listener.actionPerformed(e));
    }
}
