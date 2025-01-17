package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.JFrame;

import java.awt.*;

public class Utils {
    @SuppressWarnings("rawtypes")
    public static String getClassName(Class c) {
        return c.getName().split("\\.")[c.getName().split("\\.").length - 1];
    }

    public static int getDisplayNumber(JFrame frame) {
        // Get the graphics configuration of the JFrame
        GraphicsConfiguration config = frame.getGraphicsConfiguration();

        // Get an array of all screen devices
        GraphicsDevice[] allScreenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        // Iterate through all screen devices to find the display number
        for (int i = 0; i < allScreenDevices.length; i++) {
            if (config.getDevice() == allScreenDevices[i]) {
                // Return the display number if the device matches
                if (PublicValues.debug) ConsoleLogging.debug("Returning screen number: " + (i + 1));
                return i + 1; // Display numbers are usually 1-based
            }
        }

        return -1;
    }

    public static void moveToScreen(JFrame frame, int targetDisplayNumber) {
        // Get an array of all screen devices
        GraphicsDevice[] allScreenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        if (targetDisplayNumber >= 1 && targetDisplayNumber <= allScreenDevices.length) {
            // Get the GraphicsConfiguration of the target screen
            GraphicsConfiguration targetConfig = allScreenDevices[targetDisplayNumber - 1].getDefaultConfiguration();

            // Calculate the center position on the target screen
            int x = (ContentPanel.frame.getSize().width - frame.getWidth()) / 2;
            int y = (ContentPanel.frame.getSize().height - frame.getHeight()) / 2;


            // Set the JFrame location to the center of the target screen
            frame.setLocation(targetConfig.getBounds().x + x, targetConfig.getBounds().y + y);

            if (PublicValues.debug) ConsoleLogging.debug("Moving JFrame to screen number: " + targetDisplayNumber);
        } else {
            ConsoleLogging.warning("Can't move window to the right screen");
        }
    }

    public static int getDefaultScreenNumber() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        // Get the default screen device
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();

        // Get all screen devices
        GraphicsDevice[] screenDevices = ge.getScreenDevices();

        // Find the screen number (index) of the default screen
        int defaultScreenNumber;

        for (int i = 0; i < screenDevices.length; i++) {
            if (screenDevices[i].equals(defaultScreen)) {
                defaultScreenNumber = i;
                return defaultScreenNumber;
            }
        }
        return -1;
    }
}
