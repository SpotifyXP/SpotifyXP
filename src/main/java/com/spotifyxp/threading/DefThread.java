package com.spotifyxp.threading;

public class DefThread {
    String name;
    Thread t;
    boolean wasStarted = false;
    Runnable toRun;
    boolean isDaemon = true;
    public String getName() {
        return name;
    }
    public boolean isDaemon() {
        return isDaemon;
    }
    public DefThread(Runnable runnable, String threadName) {
        name = threadName;
        t = new Thread(runnable);
        t.setDaemon(isDaemon);
        toRun = runnable;
    }
    public DefThread(Runnable runnable, String threadName, boolean daemon) {
        isDaemon = daemon;
        name = threadName;
        t = new Thread(runnable);
        t.setDaemon(isDaemon);
        toRun = runnable;
    }
    public DefThread(Runnable runnable) {
        t = new Thread(runnable);
        t.setDaemon(true);
    }
    public boolean isAlive() {
        return t.isAlive();
    }
    void restart() {
        t = new Thread(toRun);
        t.setDaemon(isDaemon);
        t.start();
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
