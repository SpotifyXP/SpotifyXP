package com.spotifyxp.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AsyncMouseListener implements MouseListener {
    private final MouseListener listener;

    public AsyncMouseListener(MouseListener listener) {
        this.listener = listener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Thread thread = new Thread(() -> listener.mouseClicked(e), "AsyncMouseListener clicked");
        thread.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Thread thread = new Thread(() -> listener.mousePressed(e), "AsyncMouseListener pressed");
        thread.start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Thread thread = new Thread(() -> listener.mouseReleased(e), "AsyncMouseListener released");
        thread.start();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Thread thread = new Thread(() -> listener.mouseEntered(e));
        thread.start();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Thread thread = new Thread(() -> listener.mouseEntered(e));
        thread.start();
    }
}
