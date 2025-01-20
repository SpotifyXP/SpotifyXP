package com.spotifyxp.exception;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.utils.ClipboardUtil;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionDialog {
    private final Throwable e;
    private final JTextPane exceptionText;

    public ExceptionDialog(Throwable ex) {
        this.e = ex;
        this.exceptionText = new JTextPane();
        this.exceptionText.setText("[" + this.e.toString() + "]" + " ");
        this.exceptionText.setEditable(false);
    }

    /**
     * Gets a preview string of the exception (used inside the exception counter dialog)
     *
     * @return string preview for the excpetion
     */
    public String getPreview() {
        return exceptionText.getText();
    }

    public static String extractStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    /**
     * Opens a real exception window
     */
    public void openReal() {
        if (PublicValues.config.getBoolean(ConfigValues.hideExceptions.name)) {
            return;
        }
        if (SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }

        JFrame frame = new JFrame(PublicValues.language.translate("exception.dialog.title"));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        
        if (!exceptionText.getText().contains(extractStackTrace(e))) {
            exceptionText.setText(extractStackTrace(e));
        }
        
        JLabel exceptionLabel = new JLabel(PublicValues.language.translate("exception.dialog.label"));
        exceptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        exceptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(exceptionLabel, BorderLayout.NORTH);

        JScrollPane exceptionScrollPane = new JScrollPane();
        exceptionScrollPane.setViewportView(exceptionText);
        contentPane.add(exceptionScrollPane, BorderLayout.CENTER);

        ContextMenu menu = new ContextMenu(contentPane, exceptionText);
        menu.addItem(PublicValues.language.translate("ui.general.copy"), () -> ClipboardUtil.set(exceptionText.getText()));
        for(ContextMenu.GlobalContextMenuItem item : PublicValues.globalContextMenuItems) {
            menu.addItem(item.name, item.torun);
        }

        JButton exceptionOkButton = new JButton(PublicValues.language.translate("exception.dialog.button.text"));
        contentPane.add(exceptionOkButton, BorderLayout.SOUTH);
        exceptionOkButton.addActionListener(e -> frame.dispose());

        frame.getContentPane().add(contentPane);
        frame.setPreferredSize(new Dimension(600, 439));
        frame.setVisible(true);
        frame.pack();
    }

    public String getAsFormattedText() {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(e.toString()).append("] ");
        for (StackTraceElement element : e.getStackTrace()) {
            builder.append(element.toString()).append("\n");
        }
        return builder.toString();
    }
}
