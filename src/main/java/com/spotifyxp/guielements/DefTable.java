package com.spotifyxp.guielements;

import com.spotifyxp.utils.RunnableQueue;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefTable extends JTable {
    final ArrayList<Runnable> modifyactions = new ArrayList<>();
    final boolean execute = true;
    final RunnableQueue queue = new RunnableQueue(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()));

    Thread modifyactionsworker = new Thread(() -> {
        for(Runnable run : modifyactions) {
            if(!execute) {
                break;
            }
            run.run();
            modifyactions.remove(0);
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
        queue.enqueue(runnable);
    }
}
