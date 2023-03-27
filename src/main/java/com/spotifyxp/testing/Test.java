package com.spotifyxp.testing;



import com.spotifyxp.swingextension.JImageButton;
import com.spotifyxp.utils.Resources;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("EmptyMethod")
public class Test {
    static class Panel extends JPanel {
        public Panel() {
            setLayout(null);
            setPreferredSize(new Dimension(800, 851));
            JButton btnNewButton = new JButton("New button");
            btnNewButton.setBounds(341, 11, 89, 23);
            add(btnNewButton);

            JButton btnNewButton_1 = new JButton("New button");
            btnNewButton_1.setBounds(341, 136, 89, 23);
            add(btnNewButton_1);

            JButton btnNewButton_2 = new JButton("New button");
            btnNewButton_2.setBounds(341, 290, 89, 23);
            add(btnNewButton_2);

            JButton btnNewButton_3 = new JButton("New button");
            btnNewButton_3.setBounds(341, 446, 89, 23);
            add(btnNewButton_3);

            JButton btnNewButton_4 = new JButton("New button");
            btnNewButton_4.setBounds(341, 580, 89, 23);
            add(btnNewButton_4);

            JButton btnNewButton_5 = new JButton("New button");
            btnNewButton_5.setBounds(341, 744, 89, 23);
            add(btnNewButton_5);

            JButton btnNewButton_6 = new JButton("New button");
            btnNewButton_6.setBounds(341, 851, 89, 23);
            add(btnNewButton_6);
        }
    }
    public static void main(String[] args ) {
        JFrame frame = new JFrame("Test");
        JScrollPane pane = new JScrollPane();
        pane.getVerticalScrollBar().setUnitIncrement(16);
        pane.setViewportView(new Panel());
        frame.getContentPane().add(pane);
        frame.setPreferredSize(new Dimension(800, 300));
        frame.setVisible(true);
        frame.pack();
    }

}
