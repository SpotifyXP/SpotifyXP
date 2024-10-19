package com.spotifyxp.tray;

import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.AsyncActionListener;
import com.spotifyxp.utils.AsyncMouseListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("CanBeFinal")
public class SystemTrayDialog {
    String toolt = null;
    SystemTray systemTray = null;
    Image image = null;
    TrayIcon trayIcon = null;
    boolean calledadd = false;
    boolean calledopen = false;
    PopupMenu menu;
    boolean isExtended = false;
    JDialog extendedDialog;
    ActionListener onExtendedRightClick;
    ExtendedSystemTray extendedSystemTray;

    public void add(String trayicon, String tooltip) {
        calledadd = true;
        systemTray = SystemTray.getSystemTray();
        image = Toolkit.getDefaultToolkit().getImage(trayicon);
        menu = new PopupMenu("TrayDialog menu");
        toolt = tooltip;
    }

    public void setExtendedTrayIcon(ExtendedSystemTray extendedTrayIcon, ActionListener onRightClick, Image image, String tooltip) {
        calledadd = true;
        systemTray = SystemTray.getSystemTray();
        this.image = image;
        toolt = tooltip;
        isExtended = true;
        onExtendedRightClick = onRightClick;
        extendedSystemTray = extendedTrayIcon;
    }

    public void add(ImageIcon trayicon, String tooltip) {
        calledadd = true;
        systemTray = SystemTray.getSystemTray();
        image = trayicon.getImage();
        menu = new PopupMenu("TrayDialog menu");
        toolt = tooltip;
    }

    public void addEntry(String name, ActionListener onclick) {
        if(calledadd) {
            MenuItem action = new MenuItem(name);
            action.addActionListener(new AsyncActionListener(onclick));
            menu.add(action);
        }
    }

    public void open(ActionListener ondoubleclick) {
        if(!calledopen && calledadd) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(new AsyncActionListener(ondoubleclick));
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                ConsoleLogging.Throwable(awtException);
            }
        }
    }

    public void openExtended() {
        if(!calledopen && isExtended) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(SwingUtilities.isRightMouseButton(e)) {
                        onExtendedRightClick.actionPerformed(new ActionEvent(e.getSource(), e.getID(), ""));
                        return;
                    }

                    if(extendedDialog != null) {
                        extendedDialog.dispose();
                        extendedDialog = null;
                        return;
                    }

                    LookAndFeel originalLookAndFeel = UIManager.getLookAndFeel();
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                             IllegalAccessException ex) {
                        ConsoleLogging.Throwable(ex);
                    }

                    extendedDialog = new JDialog();
                    extendedDialog.setUndecorated(true);
                    extendedDialog.setAlwaysOnTop(true);
                    extendedDialog.setLocationRelativeTo(null);
                    extendedDialog.setLocation(e.getLocationOnScreen());
                    extendedDialog.addWindowFocusListener(new WindowAdapter() {
                        @Override
                        public void windowLostFocus(WindowEvent e) {
                            e.getWindow().dispose();
                        }
                    });
                    extendedDialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            try {
                                UIManager.setLookAndFeel(originalLookAndFeel);
                            } catch (UnsupportedLookAndFeelException ex) {
                                throw new RuntimeException(ex);
                            }
                            extendedSystemTray.onClose();
                            extendedDialog = null;
                        }
                    });
                    extendedDialog.setVisible(true);
                    double screenWidth = extendedDialog.getGraphicsConfiguration().getBounds().getWidth();
                    double screenHeight = extendedDialog.getGraphicsConfiguration().getBounds().getHeight();
                    double maxDialogWidth = (screenWidth - extendedDialog.getWidth()) / 5.0;
                    double maxDialogHeight = (screenHeight - extendedDialog.getHeight()) / 2.5;
                    extendedDialog.setMaximumSize(new Dimension((int) maxDialogWidth, (int) maxDialogHeight));
                    extendedSystemTray.onInit(extendedDialog);
                    extendedSystemTray.onOpen();
                    extendedDialog.repaint();
                    extendedDialog.pack();
                    extendedDialog.setLocation((int) (extendedDialog.getLocation().getX() - extendedDialog.getWidth()), (int) (extendedDialog.getLocation().getY() - extendedDialog.getHeight()));
                }
            });
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                ConsoleLogging.Throwable(awtException);
            }
        }
    }

    public void open(MouseAdapter adapter) {
        if(!calledopen) {
            calledopen = true;
            trayIcon = new TrayIcon(image, toolt);
            trayIcon.setPopupMenu(menu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new AsyncMouseListener(adapter));
            try {
                systemTray.add(trayIcon);
            } catch (AWTException awtException) {
                ConsoleLogging.Throwable(awtException);
            }
        }
    }
}
