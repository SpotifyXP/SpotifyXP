package com.spotifyxp.tray;

import javax.swing.*;

public interface ExtendedSystemTray {
    void onInit(JDialog dialog);

    void onOpen();

    void onClose();
}
