package com.spotifyxp.dialogs;

import com.spotifyxp.PublicValues;
import com.spotifyxp.panels.SplashPanel;

import javax.swing.*;
import java.awt.*;

public class LoginDialog {
    private JDialog dialog;


    public void open() {
        JOptionPane pane = new JOptionPane(
                PublicValues.language.translate("ui.login.message"),
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null
        );
        pane.setInitialValue(null);
        pane.setComponentOrientation(SplashPanel.frame.getComponentOrientation());
        dialog = pane.createDialog(SplashPanel.frame, PublicValues.language.translate("ui.login.title"));
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setVisible(true);
    }

    public void close() {
        dialog.dispose();
    }
}
