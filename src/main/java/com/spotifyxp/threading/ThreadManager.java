package com.spotifyxp.threading;


import com.spotifyxp.utils.Utils;
import java.util.ArrayList;

public class ThreadManager {
    private static final ArrayList<DefThread> runningThreads = new ArrayList<>();

    public void addThread(DefThread thread) {
        Utils.checkPermission(DefThread.class);
        if(runningThreads.contains(thread)) return;
        runningThreads.add(thread);
    }

    public int getThreadCount() {
        return runningThreads.size();
    }
}
