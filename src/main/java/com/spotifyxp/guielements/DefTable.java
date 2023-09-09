package com.spotifyxp.guielements;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

public class DefTable extends JTable {
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
}
