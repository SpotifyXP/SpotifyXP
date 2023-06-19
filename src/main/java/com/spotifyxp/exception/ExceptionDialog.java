package com.spotifyxp.exception;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.panels.SplashPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExceptionDialog {
    public static void open(Throwable e) {
        if(PublicValues.config.get(ConfigValues.hideExceptions.name).equals("true")) {
            return;
        }
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JFrame frame = new JFrame(PublicValues.language.translate("exception.dialog.title"));
        JPanel contentPane = new JPanel();
        JLabel exceptionlabel = new JLabel(PublicValues.language.translate("exception.dialog.label"));
        exceptionlabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        exceptionlabel.setHorizontalAlignment(SwingConstants.CENTER);
        exceptionlabel.setBounds(0, 0, 589, 35);
        contentPane.add(exceptionlabel);

        contentPane.setLayout(null);

        JScrollPane exceptionscrollpane = new JScrollPane();
        exceptionscrollpane.setBounds(0, 37, 589, 339);
        contentPane.add(exceptionscrollpane);

        JTextPane exceptiontext = new JTextPane();
        exceptionscrollpane.setViewportView(exceptiontext);

        exceptiontext.setEditable(false);
        exceptiontext.setText("[" + e.toString() + "]" + " ");

        for(StackTraceElement trace : e.getStackTrace()) {
            exceptiontext.setText(exceptiontext.getText() + trace + "\n");
        }

        JButton exceptionokbutton = new JButton(PublicValues.language.translate("exception.dialog.button.text"));
        exceptionokbutton.setBounds(0, 377, 589, 23);
        contentPane.add(exceptionokbutton);

        exceptionokbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.getContentPane().add(contentPane);
        frame.setPreferredSize(new Dimension(605, 439));
        frame.setVisible(true);
        frame.pack();
    }
}
