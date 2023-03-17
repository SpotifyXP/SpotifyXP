package com.spotifyxp.panels;

import com.spotifyxp.logging.ConsoleLogging;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientIDPanel {
    public ClientIDPanel() {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        JTextField textField = new JTextField();
        textField.setBounds(10, 55, 508, 47);
        contentPane.add(textField);
        textField.setColumns(10);
        JLabel clientidlabel = new JLabel("Please enter the client id here");
        clientidlabel.setBounds(10, 22, 205, 22);
        contentPane.add(clientidlabel);
        JButton clientidokbutton = new JButton("Save ClientID");
        clientidokbutton.setBounds(10, 132, 508, 35);
        clientidokbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        contentPane.add(clientidokbutton);
        frame.setVisible(true);
        frame.pack();
        while(frame.isVisible()) {
            try {
                Thread.sleep(99);
            } catch (InterruptedException e) {
                ConsoleLogging.Throwable(e);
            }
        }
    }
}
