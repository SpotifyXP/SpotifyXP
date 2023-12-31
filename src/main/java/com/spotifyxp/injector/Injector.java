package com.spotifyxp.injector;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.GraphicalMessage;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings({"DataFlowIssue", "UnusedAssignment"})
public class Injector {
    /**
     * Injects all extensions found inside the Extensions folder
     */
    public void autoInject() {
        if(!new File(PublicValues.appLocation, "Extensions").exists()) {
            new File(PublicValues.appLocation, "Extensions").mkdir();
        }else{
            for(java.io.File f : new File(PublicValues.appLocation, "Extensions").listFiles()) {
                if(f.isFile()) {
                    loadJarAt(f.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Opens a file selection window. Selection of custom jar to inject
     * @param path Path to start with
     */
    public void openInjectWindow(String path) {
        String openpath = path;
        if(path.isEmpty()) {
            openpath = System.getProperty("user.dir");
        }
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Executables", "jar");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Choose jar to inject");
        int returnVal = chooser.showOpenDialog(ContentPanel.frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            loadJarAt(file.getAbsolutePath());
        }
    }

    /**
     * Load extension jar file at the path
     * @param path path of jar file
     */
    public void loadJarAt(String path) {
        InjectionEntry entry = new InjectionEntry();
        boolean fi = false;
        boolean fv = false;
        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(path).toURI().toURL()});
            String mainclasspath = IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream("injector.info")), Charset.defaultCharset());
            Class<?> jarclass = classLoader.loadClass(mainclasspath.split(":")[1]);
            Object t = jarclass.newInstance();
            for (Method m : jarclass.getDeclaredMethods()) {
                if(m.getName().equals("getIdentifier")) {
                    Object o = m.invoke(t);
                    entry.identifier = o.toString();
                    fi = true;
                }
                if(m.getName().equals("getVersion")) {
                    Object o = m.invoke(t);
                    entry.version = o.toString();
                    fv = true;
                }
            }
            entry.filename = new File(path).getName();
            if(fv & fi) {
                jarclass.getDeclaredMethod("init").invoke(t);
                entry.loaded = true;
                ConsoleLogging.info("Loaded Extension => " + entry.identifier + "-" + entry.version);
                entry.loader = classLoader;
            }else{
                classLoader.close();
            }
        }catch (Exception e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
            entry.failed = true;
        }
        if(!fv || !fi) {
            entry.failed = true;
        }else {
            injectedJars.add(entry);
        }
    }

    public final ArrayList<InjectionEntry> injectedJars = new ArrayList<>();

    public ArrayList<InjectionEntry> getInjectedJars() {
        return injectedJars;
    }

    public static class InjectionEntry {
        public String filename = "";
        public String identifier = "";
        public String version = "";
        public boolean loaded = false;
        public boolean failed = false;
        public URLClassLoader loader = null;
    }
}
