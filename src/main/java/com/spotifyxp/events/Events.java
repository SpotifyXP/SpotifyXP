package com.spotifyxp.events;

import java.util.ArrayList;
import java.util.Collections;

public class Events {
    static ArrayList<Runnable> queueUpdateEvents = new ArrayList<>();
    static ArrayList<Runnable> queueAdvanceEvents = new ArrayList<>();
    static ArrayList<Runnable> queueRegressEvents = new ArrayList<>();
    static ArrayList<Runnable> playerLockReleaseEvents = new ArrayList<>();

    private static void createThreadWith(ArrayList<Runnable> toWorkOn) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for(Runnable run : toWorkOn) {
                    run.run();
                }
            }
        });
        t.start();
    }

    private static ArrayList<Runnable> getCopy(ArrayList<Runnable> runnables) {
        //Prevents the concurrentModificationException issue when runnables are trying to remove themselfes
        return new ArrayList<>(runnables);
    }

    public static void registerToQueueUpdateEvent(Runnable runnable) {
        queueUpdateEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueUpdateEvents() {
        createThreadWith(getCopy(queueUpdateEvents));
    }

    public static void registerToQueueAdvanceEvent(Runnable runnable) {
        queueAdvanceEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueAdvanceEvents() {
        createThreadWith(getCopy(queueAdvanceEvents));
    }

    public static void registerToQueueRegressEvent(Runnable runnable) {
        queueRegressEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueRegressEvents() {
        createThreadWith(getCopy(queueRegressEvents));
    }

    public static void registerPlayerLockReleaseEvent(Runnable runnable) {
        playerLockReleaseEvents.add(runnable);
    }

    public static void INTERNALtriggerPlayerLockReleaseEvents() {
        createThreadWith(getCopy(playerLockReleaseEvents));
    }

    public static void remove(Runnable runnable) {
        queueRegressEvents.remove(runnable);
        queueUpdateEvents.remove(runnable);
        queueAdvanceEvents.remove(runnable);
        playerLockReleaseEvents.remove(runnable);
    }
}
