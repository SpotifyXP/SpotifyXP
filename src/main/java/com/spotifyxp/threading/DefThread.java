package com.spotifyxp.threading;

import com.spotifyxp.PublicValues;

public class DefThread {
    final String name;
    Thread t;
    boolean wasStarted = false;
    final Runnable toRun;
    final boolean isDaemon;
    public String getName() {
        return name;
    }
    public boolean isDaemon() {
        return isDaemon;
    }
    public DefThread(Runnable runnable, String threadName) {
        this(runnable, threadName, false);
    }
    public DefThread(Runnable runnable) {
        this(runnable, "Thread-" + PublicValues.threadManager.getThreadCount(), false);
    }

    public DefThread(Runnable runnable, String threadName, boolean daemon) {
        isDaemon = daemon;
        name = threadName;
        t = new Thread(runnable);
        t.setDaemon(isDaemon);
        toRun = runnable;
        PublicValues.threadManager.addThread(this);
    }

    public boolean isAlive() {
        return t.isAlive();
    }
    void restart() {
        t = new Thread(toRun);
        t.setDaemon(isDaemon);
        t.start();
        PublicValues.threadManager.addThread(this);
    }
    //Only for shutdownHook
    public Thread getRawThread() {
        return t;
    }
    public void start() {
        if(wasStarted) {
            restart();
            return;
        }
        t.start();
        wasStarted = true;
    }
}
