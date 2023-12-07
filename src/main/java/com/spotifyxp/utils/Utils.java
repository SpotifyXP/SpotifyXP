package com.spotifyxp.utils;

import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public static void checkPermission(Class... expectedCallerClasses) {
        StackTraceElement callerTrace = Thread.currentThread().getStackTrace()[3];
        for (Class expectedClass : expectedCallerClasses) {
            if (callerTrace.getClassName().equals(expectedClass.getName())) {
                return;
            }
        }
        throw new RuntimeException("A suspicious class tried to call ThreadManager.addThread! Blocking access...");
    }
}
