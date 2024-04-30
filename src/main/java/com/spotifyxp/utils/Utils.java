package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.threading.DefThread;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"IntegerDivisionInFloatingPointContext", "MathRoundingWithIntArgument", "DataFlowIssue"})
public class Utils {
    public static void replaceFileString(String old, String newstring, String filelocation) {
        try {
            FileInputStream fis = new FileInputStream(filelocation);
            String content = IOUtils.toString(fis, Charset.defaultCharset());
            content = content.replaceAll(old, newstring);
            FileOutputStream fos = new FileOutputStream(filelocation);
            IOUtils.write(content, Files.newOutputStream(Paths.get(filelocation)), Charset.defaultCharset());
            fis.close();
            fos.close();
        }catch (IOException ioe) {
            ConsoleLogging.Throwable(ioe);
        }
    }
    public static int calculateRest(int from, int by) {
        return from - Math.round(from/by);
    }

    @SuppressWarnings("rawtypes")
    public static Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("rawtypes")
    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.openException(e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static String getClassName(Class c) {
        return c.getName().split("\\.")[c.getName().split("\\.").length-1];
    }

    @SuppressWarnings("all")
    public static void checkPermission(String functionOrClassName, ArrayList<Class<?>> expectedCallerClasses) {
        StackTraceElement callerTrace = Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length - 2];
        for (Class expectedClass : expectedCallerClasses) {
            if (callerTrace.getClassName().equals(expectedClass.getName())) {
                return;
            }
        }
        throw new RuntimeException("Class " + Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length - 1].getClassName() + " has no permission to access " + functionOrClassName + "! Blocking...");
    }

    public static int getDisplayNumber(JFrame frame) {
        // Get the graphics configuration of the JFrame
        GraphicsConfiguration config = frame.getGraphicsConfiguration();

        // Get the default screen device
        GraphicsDevice defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Get an array of all screen devices
        GraphicsDevice[] allScreenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        // Iterate through all screen devices to find the display number
        for (int i = 0; i < allScreenDevices.length; i++) {
            if (config.getDevice() == allScreenDevices[i]) {
                // Return the display number if the device matches
                if(PublicValues.debug) ConsoleLogging.debug("Returning screen number: " + (i + 1));
                return i + 1; // Display numbers are usually 1-based
            }
        }

        return -1;
    }

    public static void moveToScreen(JFrame frame, int targetDisplayNumber) {
        // Get the graphics configuration of the JFrame
        GraphicsConfiguration config = frame.getGraphicsConfiguration();

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

            if(PublicValues.debug) ConsoleLogging.debug("Moving JFrame to screen number: " + targetDisplayNumber);
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

    public static void executeAsync(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
