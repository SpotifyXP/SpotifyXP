package com.spotifyxp.threading;

import com.spotifyxp.PublicValues;

import java.util.ArrayList;
import java.util.Arrays;

public class DefThread {
    final String name;
    Thread t;
    boolean wasStarted = false;
    final Runnable toRun;
    final boolean isDaemon;
    ArrayList<Class<?>> permissionToRemoveThread;

    public String getName() {
        return name;
    }
    public boolean isDaemon() {
        return isDaemon;
    }
    public DefThread(Runnable runnable, String threadName, Class<?>... permissionToRemoveThread) {
        this(runnable, threadName, false, permissionToRemoveThread);
    }
    public DefThread(Runnable runnable, Class<?>... permissionToRemoveThread) {
        this(runnable, "Thread-" + PublicValues.threadManager.getThreadCount(), false, permissionToRemoveThread);
    }

    public DefThread(Runnable runnable, String threadName, boolean daemon, Class<?>... permissionToRemoveThread) {
        isDaemon = daemon;
        name = threadName;
        t = new Thread(runnable);
        t.setDaemon(isDaemon);
        toRun = runnable;
        PublicValues.threadManager.addThread(this);
        this.permissionToRemoveThread = new ArrayList<>(Arrays.asList(permissionToRemoveThread));
    }

    public ArrayList<Class<?>> getPermissionToRemoveThread() {
        return permissionToRemoveThread;
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
