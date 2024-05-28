package com.spotifyxp.exception;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.utils.ClipboardUtil;
import com.spotifyxp.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class ExceptionDialog {
    private final Throwable e;
    private final JTextPane exceptiontext;

    public ExceptionDialog(Throwable ex) {
        this.e = ex;
        this.exceptiontext = new JTextPane();
        this.exceptiontext.setText("[" + this.e.toString() + "]" + " ");
    }

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
        if(PublicValues.config.getBoolean(ConfigValues.hideExceptions.name)) {
            return;
        }
        if(SplashPanel.getElementByNameAutoThrow("frame", JFrame.class).isVisible()) {
            SplashPanel.getElementByNameAutoThrow("frame", JFrame.class).setAlwaysOnTop(false);
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

        exceptionscrollpane.setViewportView(exceptiontext);

        exceptiontext.setEditable(false);

        for(StackTraceElement trace : e.getStackTrace()) {
            exceptiontext.setText(exceptiontext.getText() + trace + "\n");
        }

        ContextMenu menu = new ContextMenu(contentPane, exceptiontext);
        menu.addItem(PublicValues.language.translate("ui.general.copy"), () -> ClipboardUtil.set(exceptiontext.getText()));

        JButton exceptionokbutton = new JButton(PublicValues.language.translate("exception.dialog.button.text"));
        exceptionokbutton.setBounds(0, 377, 589, 23);
        contentPane.add(exceptionokbutton);

        exceptionokbutton.addActionListener(e -> frame.dispose());

        frame.getContentPane().add(contentPane);
        frame.setPreferredSize(new Dimension(605, 439));
        frame.setVisible(true);
        frame.pack();
    }

    public String getExcptionMessage() {
        StringBuilder builder = new StringBuilder();
        for(StackTraceElement element : e.getStackTrace()) {
            builder.append(element.toString()).append("\n");
        }
        return builder.toString();
    }

    public String getAsFormattedText() {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(e.toString()).append("] ");
        for(StackTraceElement element : e.getStackTrace()) {
            builder.append(element.toString()).append("\n");
        }
        return builder.toString();
    }

    public String getExceptionName() {
        return Utils.getClassName(e.getClass());
    }
}
