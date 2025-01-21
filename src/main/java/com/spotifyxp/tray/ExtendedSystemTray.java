package com.spotifyxp.tray;

import javax.swing.*;

/**
 * Will be removed in the near future
 */
@Deprecated
public interface ExtendedSystemTray {
    void onInit(JDialog dialog);

    void onOpen();

    void onClose();
}
