package com.spotifyxp.threading;

import com.spotifyxp.utils.Utils;

import java.util.ArrayList;

public class ThreadManager {
    private static final ArrayList<DefThread> runningThreads = new ArrayList<>();

    public void addThread(DefThread thread) {
        if (runningThreads.contains(thread)) return;
        runningThreads.add(thread);
    }

    public DefThread getThread(int number) throws ArrayIndexOutOfBoundsException {
        if(number > runningThreads.size() || number < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return runningThreads.get(number);
    }


    /**
     * This method takes an {@code Integer} and removes the thread in the array at the given index<br><br>
     * <a style="color:yellow;font:bold">Warning</a><br>
     * <a style="font:bold">Make sure the Thread is stopped or in a controllable state</a>
     * @param number
     */
    public void removeThread(int number) {
        if(number > runningThreads.size() || number < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        DefThread thread = runningThreads.get(number);
        if(!thread.getPermissionToRemoveThread().isEmpty()) {
            Utils.checkPermission("ThreadManager.removeThread", thread.getPermissionToRemoveThread());
        }
        runningThreads.remove(number);
    }

    public int getThreadCount() {
        return runningThreads.size();
    }
}
