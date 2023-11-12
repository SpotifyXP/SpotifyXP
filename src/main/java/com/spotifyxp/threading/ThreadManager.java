package com.spotifyxp.threading;

import java.util.ArrayList;

public class ThreadManager {
    private static final ArrayList<DefThread> runningThreads = new ArrayList<>();

    public void addThread(DefThread thread) {
        //Utils.checkPermission(DefThread.class); ToDo:  A suspicious class tried to call ThreadManager.addThread! Blocking access... in Intiator.java:46
        if(runningThreads.contains(thread)) return;
        runningThreads.add(thread);
    }

    public int getThreadCount() {
        return runningThreads.size();
    }
}
