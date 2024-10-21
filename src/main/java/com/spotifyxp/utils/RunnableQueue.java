package com.spotifyxp.utils;

/*
Created from Nicolas Repiquet

Date: 3.11.2010

https://stackoverflow.com/questions/4086108/java-runnable-queue
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public final class RunnableQueue {

    private final ExecutorService m_executorService;
    private final Queue<Runnable> m_runnables;
    private final Runnable m_loop;

    public RunnableQueue(ExecutorService executorService) {
        m_executorService = executorService;
        m_runnables = new LinkedList<>();

        m_loop = () -> {

            Runnable l_runnable = current();

            while (l_runnable != null) {
                l_runnable.run();
                l_runnable = next();
            }
        };
    }

    private Runnable current() {
        synchronized (m_runnables) {
            return m_runnables.peek();
        }
    }

    private Runnable next() {
        synchronized (m_runnables) {
            m_runnables.remove();
            return m_runnables.peek();
        }
    }

    public void enqueue(Runnable runnable) {
        if (runnable != null) {
            synchronized (m_runnables) {
                m_runnables.add(runnable);
                if (m_runnables.size() == 1) {
                    m_executorService.execute(m_loop);
                }
            }
        }
    }
}
