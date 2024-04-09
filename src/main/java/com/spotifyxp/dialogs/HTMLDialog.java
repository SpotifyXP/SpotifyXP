package com.spotifyxp.dialogs;




import com.spotifyxp.events.HtmlDialogEvents;
import com.spotifyxp.events.LoggerEvent;
import com.spotifyxp.logging.ConsoleLogging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class HTMLDialog {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    JDialog dialog = new JDialog();
    JTextPane html = new JTextPane();

    public void open(JFrame frame, String title, String htmlcode) {
        try {
            dialog.setTitle(title);
            JPanel content = new JPanel();
            content.setLayout(new BorderLayout());
            frame.setFocusable(false);
            dialog.getContentPane().add(content);
            content.add(new JScrollPane(html), BorderLayout.CENTER);
            html.setEditable(false);
            html.setMinimumSize(new Dimension(dialog.getHeight(), dialog.getWidth()));
            html.setContentType("text/html");
            if (htmlcode.contains("<html>")) {
                html.setText(htmlcode);
            } else {
                html.setText("<html>" + htmlcode + "</html>");
            }
            dialog.addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    html.setMinimumSize(new Dimension(dialog.getHeight(), dialog.getWidth()));
                }

                @Override
                public void componentMoved(ComponentEvent e) {

                }

                @Override
                public void componentShown(ComponentEvent e) {

                }

                @Override
                public void componentHidden(ComponentEvent e) {

                }
            });
            dialog.setVisible(true);
            dialog.pack();
        }catch (NullPointerException ignored) {

        }
    }
    public void setLookAndFeel(String classname) {
        if(dialog == null) {
            try {
                UIManager.setLookAndFeel(classname);
            } catch (UnsupportedLookAndFeelException e) {
                ConsoleLogging.error("Unsupported LookAndFeel");
            } catch (ClassNotFoundException e) {
                ConsoleLogging.error("LookAndFeel not found: " + classname);
            } catch (InstantiationException e) {
                ConsoleLogging.error("Unknown Instantation Error");
                ConsoleLogging.Throwable(e);
            } catch (IllegalAccessException e) {
                ConsoleLogging.error("Unknown IllegalAccess Error");
                ConsoleLogging.Throwable(e);
            }
        }else{
            ConsoleLogging.error("Cant set LookAndFeel! Call open after setLookAndFeel");
        }
    }
    public JTextPane getHtmlComponent() {
        return html;
    }
    public JDialog getDialog() {
        return dialog;
    }
}