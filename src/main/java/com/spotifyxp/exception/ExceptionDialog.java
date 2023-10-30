package com.spotifyxp.exception;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.panels.SplashPanel;

import javax.swing.*;
import java.awt.*;

public class ExceptionDialog {
    public ExceptionDialog(Throwable ex) {
        e = ex;
        exceptiontext = new JTextPane();
        exceptiontext.setText("[" + e.toString() + "]" + " ");
    }
    static Throwable e;
    JTextPane exceptiontext;

    /**
     * Gets a preview string of the exception (used inside the exception counter dialog)
     * @return string preview for the excpetion
     */
    public String getPreview() {
        return exceptiontext.getText();
    }

    /**
     * Opens a real exception window
     */
    public void openReal() {
        if(PublicValues.config.getString(ConfigValues.hideExceptions.name).equals("true")) {
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

        exceptiontext = new JTextPane();
        exceptionscrollpane.setViewportView(exceptiontext);

        exceptiontext.setEditable(false);
        exceptiontext.setText("[" + e.toString() + "]" + " ");

        for(StackTraceElement trace : e.getStackTrace()) {
            exceptiontext.setText(exceptiontext.getText() + trace + "\n");
        }

        JButton exceptionokbutton = new JButton(PublicValues.language.translate("exception.dialog.button.text"));
        exceptionokbutton.setBounds(0, 377, 589, 23);
        contentPane.add(exceptionokbutton);

        exceptionokbutton.addActionListener(e -> frame.dispose());

        frame.getContentPane().add(contentPane);
        frame.setPreferredSize(new Dimension(605, 439));
        frame.setVisible(true);
        frame.pack();
    }

    /**
     * Adds an exception to the list (add to the exception counter)
     * @param ex instance of an Exception
     */
    public static void open(Throwable ex) {
        if(ContentPanel.errorQueue != null) {
            ContentPanel.errorQueue.add(new ExceptionDialog(ex));
        }
    }
}
