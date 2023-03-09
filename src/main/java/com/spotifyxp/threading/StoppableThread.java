package com.spotifyxp.threading;


import com.spotifyxp.custom.StoppableThreadRunnable;

@SuppressWarnings("CanBeFinal")
public class StoppableThread {
    private final StoppableThreadRunnable tr;
    private volatile Thread t;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean r;
    private int c = 0;
    public StoppableThread(StoppableThreadRunnable runnable, boolean repeat) {
        tr = runnable;
        t = new Thread(this::run);
        r = repeat;
    }

    public void stop() {
        t = null;
    }

    public void start() {
        t.start();
    }

    public int getCounter() {
        return c;
    }

    void run() {
        Thread tt = Thread.currentThread();
        while (t==tt) {
            if(r) {
                tr.run(c);
            }else{
                tr.run(c);
                stop();
            }
            c++;
        }
        c = 0;
    }
}
