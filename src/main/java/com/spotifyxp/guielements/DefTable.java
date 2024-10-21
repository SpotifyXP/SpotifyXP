package com.spotifyxp.guielements;

import com.spotifyxp.utils.RunnableQueue;

import javax.swing.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefTable extends JTable {
    final RunnableQueue queue = new RunnableQueue(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()));

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public int getSelectedRow() {
        if (getSelectedRowCount() > 1) {
            return super.getSelectedRows()[0];
        }
        return super.getSelectedRow();
    }

    public void addModifyAction(Runnable runnable) {
        queue.enqueue(runnable);
    }
}
