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
    private final List<LoggerEvent> loggerlist = new ArrayList<>();
    public void registerLoggerListener(LoggerEvent toAdd) {
        loggerlist.add(toAdd);
    }
    public HTMLDialog(LoggerEvent event) {
        registerLoggerListener(event);
    }
    private final List<HtmlDialogEvents> listeners = new ArrayList<>();
    public void registerEventListener(HtmlDialogEvents toAdd) {
        listeners.add(toAdd);
    }
    JDialog dialog = new JDialog();
    JTextPane html = new JTextPane();
    boolean sendEvents = false;

    public void open(JFrame frame, String title, String htmlcode) {
        try {
            if (htmlcode.toLowerCase().contains("<script")) {
                for (HtmlDialogEvents event1 : listeners) {
                    event1.unsupportedHTMLTag("<script>");
                }
                dialog.dispose();
            }
            if (htmlcode.toLowerCase().contains("<link")) {
                for (HtmlDialogEvents event1 : listeners) {
                    event1.unsupportedHTMLTag("<link>");
                }
                dialog.dispose();
            }
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
            dialog.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(java.awt.event.WindowEvent e) {
                    for (HtmlDialogEvents event1 : listeners) {
                        event1.open(dialog);
                    }
                    sendEvents = true;
                }

                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    for (HtmlDialogEvents event1 : listeners) {
                        event1.close(dialog);
                    }
                    sendEvents = false;
                }

                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {

                }

                @Override
                public void windowIconified(java.awt.event.WindowEvent e) {

                }

                @Override
                public void windowDeiconified(java.awt.event.WindowEvent e) {

                }

                @Override
                public void windowActivated(java.awt.event.WindowEvent e) {

                }

                @Override
                public void windowDeactivated(java.awt.event.WindowEvent e) {

                }
            });
            dialog.addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    html.setMinimumSize(new Dimension(dialog.getHeight(), dialog.getWidth()));
                    for (HtmlDialogEvents event1 : listeners) {
                        event1.resize(dialog);
                    }
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
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                ConsoleLogging.error("Unknown IllegalAccess Error");
                e.printStackTrace();
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
