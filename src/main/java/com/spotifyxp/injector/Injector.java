package com.spotifyxp.injector;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.GraphicalMessage;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings({"DataFlowIssue", "UnusedAssignment"})
public class Injector {
    private int availableExtensions = 0;
    private int loadedExtensions = 0;
    private ArrayList<InjectionEntry> missingJars = new ArrayList<>();
    private InjectionEntry currentEntry = null;
    /**
     * Injects all extensions found inside the Extensions folder
     */
    public void autoInject() {
        if(!new File(PublicValues.appLocation, "Extensions").exists()) {
            new File(PublicValues.appLocation, "Extensions").mkdir();
        }else{
            availableExtensions = new File(PublicValues.appLocation, "Extensions").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.split("\\.")[name.split("\\.").length - 1].equalsIgnoreCase("jar");
                }
            }).length;
            boolean firstGoThrough = true;
            while(loadedExtensions != availableExtensions) {
                System.out.println(availableExtensions);
                if(availableExtensions < 0) {
                    break;
                }
                if(availableExtensions == 0) {
                    break;
                }
                if(firstGoThrough) {
                    for (File file : new File(PublicValues.appLocation, "Extensions").listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.split("\\.")[name.split("\\.").length - 1].equalsIgnoreCase("jar");
                        }
                    })) {
                        loadJarAt(file.getAbsolutePath());
                    }
                }
                for (InjectionEntry e : new ArrayList<>(missingJars)) {
                    currentEntry = e;
                    loadJarAt(e.path);
                }
                firstGoThrough = false;
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
    @SuppressWarnings("unchecked")
    public void loadJarAt(String path) {
        if(!path.split("\\.")[path.split("\\.").length - 1].equalsIgnoreCase("jar")) return; //Invalid file
        InjectionEntry entry = new InjectionEntry();
        if(currentEntry != null) entry = currentEntry;
        entry.path = path;
        boolean foundIdentifier = false;
        boolean foundVersion = false;
        boolean foundAuthor = false;
        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(path).toURI().toURL()});
            JSONObject config = new JSONObject(IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream("plugin.json")), Charset.defaultCharset()));
            Class<?> jarclass = classLoader.loadClass(config.getString("main"));
            Object t = jarclass.newInstance();
            for (Method m : jarclass.getDeclaredMethods()) {
                if (m.getName().equals("getIdentifier")) {
                    Object o = m.invoke(t);
                    entry.identifier = o.toString();
                    foundIdentifier = true;
                }
                if (m.getName().equals("getVersion")) {
                    Object o = m.invoke(t);
                    entry.version = o.toString();
                    foundVersion = true;
                }
                if (m.getName().equals("getAuthor")) {
                    Object o = m.invoke(t);
                    entry.author = o.toString();
                    foundAuthor = true;
                }
                if (m.getName().equals("getDependencies")) {
                    Object o = m.invoke(t);
                    entry.dependencies = (ArrayList<Dependency>) o;
                }
            }
            entry.filename = new File(path).getName();
            if (foundVersion & foundIdentifier) {
                boolean metDependencies = true;
                ArrayList<Dependency> missing = new ArrayList<>();
                for (Dependency dependency : entry.dependencies) {
                    if (isJarInjected(dependency.identifier, dependency.author, dependency.version)) {
                        metDependencies = true;
                        missing.remove(dependency);
                    } else {
                        metDependencies = false;
                        missing.add(dependency);
                    }
                }
                if (!metDependencies) {
                    if (entry.gotOverTimes >= availableExtensions) {
                        //Dependency is missing
                        ConsoleLogging.error("Couldn't load extension with name => "
                                + entry.identifier + " version => "
                                + entry.version + " from => "
                                + entry.author + " because of these missing dependencies => "
                                + Arrays.toString(missing.toArray()));
                        missingJars.remove(entry);
                        availableExtensions--;
                        return;
                    }
                    entry.gotOverTimes++;
                    if (!missingJars.contains(path)) {
                        missingJars.add(entry);
                    }
                    classLoader.close();
                    return;
                } else {
                    missingJars.remove(path);
                }
                jarclass.getDeclaredMethod("init").invoke(t);
                entry.loaded = true;
                ConsoleLogging.info("Loaded Extension => " + entry.identifier + "-" + entry.version + "-" + entry.author);
                entry.loader = classLoader;
                loadedExtensions++;
            } else {
                classLoader.close();
            }
        }catch (JSONException jsonException) {
            ConsoleLogging.error("Failed to load extension: '" + path + "'! Invalid plugin.json");
            availableExtensions--;
            entry.failed = true;
        }catch (NullPointerException nullPointerException) {
            ConsoleLogging.error("Failed to load extension: '" + path + "'! plugin.json not found");
            availableExtensions--;
            entry.failed = true;
        }catch (Exception e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
            entry.failed = true;
            availableExtensions--;
        }
        if(!foundVersion || !foundIdentifier || !foundAuthor) {
            entry.failed = true;
            availableExtensions--;
            ConsoleLogging.error("Failed to load extension: '" + path + "'");
        }else {
            injectedJars.add(entry);
        }
    }

    public boolean isJarInjected(String name, String author) {
        for(InjectionEntry entry : injectedJars) {
            if(entry.identifier.equals(name) && entry.author.equals(author)) {
                return true;
            }
        }
        return false;
    }

    public boolean isJarInjected(String name, String author, String version) {
        for(InjectionEntry entry : injectedJars) {
            if(entry.identifier.equals(name) && entry.author.equals(author) && entry.version.equals(version)) {
                return true;
            }
        }
        return false;
    }

    public final ArrayList<InjectionEntry> injectedJars = new ArrayList<>();

    public ArrayList<InjectionEntry> getInjectedJars() {
        return injectedJars;
    }

    public static class InjectionEntry {
        public String filename = "";
        public String identifier = "";
        public String version = "";
        public String author = "";
        public ArrayList<Dependency> dependencies = new ArrayList<>();
        public boolean loaded = false;
        public boolean failed = false;
        public URLClassLoader loader = null;
        private int gotOverTimes = 0;
        private String path = "";
    }

    public static class Dependency {
        public String identifier = "";
        public String version = "";
        public String author = "";

        @Override
        public String toString() {
            return identifier+"-"+version+"-"+author;
        }
    }
}
