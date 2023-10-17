package com.spotifyxp.guielements;

import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.RunnableQueue;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefTable extends JTable {
    ArrayList<Runnable> modifyactions = new ArrayList<>();
    volatile boolean execute = true;
    RunnableQueue queue = new RunnableQueue(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));

    Thread modifyactionsworker = new Thread(new Runnable() {
        @Override
        public void run() {
            for(Runnable run : modifyactions) {
                if(!execute) {
                    break;
                }
                run.run();
                modifyactions.remove(0);
            }
        }
    });

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public int getSelectedRow() {
        if(getSelectedRowCount() > 1) {
            return super.getSelectedRows()[0];
        }
        return super.getSelectedRow();
    }

    public void addModifyAction(Runnable runnable) {
        //execute = false;
        //modifyactions.add(runnable);
        //execute = true;
        //modifyactionsworker.start();
        queue.enqueue(runnable);
    }
}
