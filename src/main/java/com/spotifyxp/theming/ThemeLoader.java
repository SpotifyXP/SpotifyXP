package com.spotifyxp.theming;

import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.Utils;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;

public class ThemeLoader {
    public static String extraSearchPath = ""; //Injector entry point
    public static Theme loadedTheme = null;
    static String loadedThemePath = "";

    String getClassName(Class c) {
        return c.getName().split("\\.")[c.getName().split("\\.").length-1];
    }

    public void loadTheme(String name) {
        for (Class s : Utils.findAllClassesUsingClassLoader("com.spotifyxp.theming.themes")) {
            if (getClassName(s).equals(name)) {
                loadThemeFromClass(s);
            }
        }
        if (!extraSearchPath.equals("")) {
            for (Class s : Utils.findAllClassesUsingClassLoader(extraSearchPath)) {
                if (getClassName(s).equals(name)) {
                    loadThemeFromClass(s);
                }
            }
        }
    }

    void loadThemeFromClass(Class c) {
        if (Theme.class.isAssignableFrom(c)) {
            try {
                Object o = c.newInstance();
                loadedThemePath = c.getName();
                loadedTheme = new Theme() {
                    @Override
                    public String getAuthor() {
                        try {
                            return c.getMethod("getAuthor").invoke(o).toString();
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                            ConsoleLogging.Throwable(e);
                            ExceptionDialog.open(e);
                            return "<Not Provided>";
                        }
                    }

                    @Override
                    public boolean isLight() {
                        try {
                            return Boolean.parseBoolean(c.getMethod("isLight").invoke(o).toString());
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                            ConsoleLogging.Throwable(e);
                            ExceptionDialog.open(e);
                            return false;
                        }
                    }

                    @Override
                    public void styleElement(Class component) {
                        try {
                            //Class c = ThemeLoader.class.getClassLoader().loadClass(loadedThemePath);
                            //Object t2 = c.newInstance();
                            for(Method m : c.getMethods()) {
                                for(Parameter p : m.getParameters()) {
                                    System.out.println(m.getName() + " => " + p.getType());
                                }
                            }
                            c.getMethod("styleComponent", new Class[]{ component.getClass() }).invoke(o);
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                            ConsoleLogging.Throwable(e);
                            ExceptionDialog.open(e);
                        }
                    }
                };
            } catch (IllegalAccessException | InstantiationException e) {
                ConsoleLogging.Throwable(e);
                ExceptionDialog.open(e);
            }
        }else{
            ConsoleLogging.error("Failed to load theme class '" + c.getName() + "'. Class doesn't implement Theme");
        }
    }
}
