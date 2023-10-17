package com.spotifyxp.events;

import java.util.ArrayList;

public class Events {
    static ArrayList<Runnable> queueUpdateEvents = new ArrayList<>();
    static ArrayList<Runnable> queueAdvanceEvents = new ArrayList<>();
    static ArrayList<Runnable> queueRegressEvents = new ArrayList<>();

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

    public static void registerToQueueUpdateEvent(Runnable runnable) {
        queueUpdateEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueUpdateEvents() {
        createThreadWith(queueUpdateEvents);
    }

    public static void registerToQueueAdvanceEvent(Runnable runnable) {
        queueAdvanceEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueAdvanceEvents() {
        createThreadWith(queueAdvanceEvents);
    }

    public static void registerToQueueRegressEvent(Runnable runnable) {
        queueRegressEvents.add(runnable);
    }

    public static void INTERNALtriggerQueueRegressEvents() {
        createThreadWith(queueRegressEvents);
    }
}
