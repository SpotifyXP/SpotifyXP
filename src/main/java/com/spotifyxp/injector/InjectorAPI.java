package com.spotifyxp.injector;

import com.spotifyxp.PublicValues;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.utils.ConnectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class InjectorAPI {
    public static ArrayList<InjectorRepository> injectorRepos = new ArrayList<>();

    public static class InjectorRepository {
        private final String url;
        private final boolean isAvailable;

        public InjectorRepository(String url) {
            this.url = url;
            this.isAvailable = ConnectionUtils.isWebsiteReachable(this.url);
            if(!this.isAvailable) {
                ConsoleLogging.error("Repository with url '" + this.url + "' is not reachable");
            }
        }

        public String getUrl() {
            return this.url;
        }

        public boolean isAvailable() {
            return this.isAvailable;
        }
    }
    public static class APIInjectorRepository {
        private String name;
        private String url;
        private final ArrayList<String> extensionURLs = new ArrayList<>();

        public APIInjectorRepository buildFromResponse(String response, String url) {
            try {
                this.url = url;
                JSONObject root = new JSONObject(response);
                this.name = root.getString("name");
                for(Object o : root.getJSONArray("extensions")) {
                    this.extensionURLs.add(new JSONObject(o.toString()).getString("location"));
                }
            }catch (Exception e) {
                ConsoleLogging.Throwable(e);
            }
            return this;
        }

        public String getUrl() {
            return this.url;
        }

        public String getName() {
            return this.name;
        }

        public ArrayList<String> getExtensionURLs() {
            return this.extensionURLs;
        }
    }
    private final ArrayList<InjectorAPI.Extension> extensions = new ArrayList<>();
    public static String compatibleMinVersion = "2.0.3";
    private static final ArrayList<APIInjectorRepository> apiInjectorRepositories = new ArrayList<>();
    public static class Extension {
        private final String location;
        private final String name;
        private final String author;
        private final String version;
        private final String description;
        private final String identifier;
        private final ArrayList<ExtensionSimplified> dependencies;
        private final String minVersion;

        public Extension(String location,
                         String identifier,
                         String name,
                         String author,
                         String version,
                         String description,
                         ArrayList<ExtensionSimplified> dependencies,
                         String minVersion) {
            this.location = location;
            this.name = name;
            this.author = author;
            this.version = version;
            this.description = description;
            this.identifier = identifier;
            this.dependencies = dependencies;
            this.minVersion = minVersion;
        }

        public String getLocation() {
            return this.location;
        }

        public String getAuthor() {
            return this.author;
        }

        public String getName() {
            return this.name;
        }

        public String getVersion() {
            return this.version;
        }

        public String getDescription() {
            return this.description;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public ArrayList<ExtensionSimplified> getDependencies() {
            return this.dependencies;
        }

        public String getMinVersion() {
            return this.minVersion;
        }
    }
    public static class ExtensionSimplified {
        private final String location;
        private final String name;
        private final String author;
        private final String version;
        private final String description;
        private final String identifier;
        private final String minVersion;

        public ExtensionSimplified(String location,
                                   String identifier,
                                   String name,
                                   String author,
                                   String version,
                                   String description,
                                   String minVersion) {
            this.location = location;
            this.name = name;
            this.author = author;
            this.version = version;
            this.description = description;
            this.identifier = identifier;
            this.minVersion = minVersion;
        }

        public String getLocation() {
            return this.location;
        }

        public String getAuthor() {
            return this.author;
        }

        public String getName() {
            return this.name;
        }

        public String getVersion() {
            return this.version;
        }

        public String getDescription() {
            return this.description;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public String getMinVersion() {
            return this.minVersion;
        }
    }

    @FunctionalInterface
    public interface ProgressRunnable {
        void run(long filesizeDownloaded);
    }

    public InjectorAPI() {
        //injectorRepos.add(new InjectorRepository(
        //        "https://raw.githubusercontent.com/SpotifyXP/SpotifyXP-Repository/main/repo"));
        injectorRepos.add(new InjectorRepository("http://127.0.0.1:8000/repo"));
        Events.triggerEvent(SpotifyXPEvents.injectorAPIReady.getName());
        for(InjectorRepository repository : injectorRepos) {
            apiInjectorRepositories.add(new APIInjectorRepository().buildFromResponse(ConnectionUtils.makeGet(repository.url + "/repo.json"), repository.url));
        }
    }

    public ExtensionSimplified getExtensionWithNameAndAuthor(String name, String author) {
        for(APIInjectorRepository repository : getRepositories()) {
            for (ExtensionSimplified simplified : getExtensions(repository)) {
                if (simplified.getName().equals(name) && simplified.getAuthor().equals(author)) {
                    return simplified;
                }
            }
        }
        return null;
    }

    public APIInjectorRepository getExtensionRepository(String name, String author) {
        for(APIInjectorRepository repository : getRepositories()) {
            for(ExtensionSimplified extensionSimplified : getExtensions(repository)) {
                if(extensionSimplified.getName().equals(name) && extensionSimplified.getAuthor().equals(author)) {
                    return repository;
                }
            }
        }
        return null;
    }

    public APIInjectorRepository getRepository(int index) throws ArrayIndexOutOfBoundsException {
        return apiInjectorRepositories.get(index);
    }

    public ArrayList<APIInjectorRepository> getRepositories() throws ArrayIndexOutOfBoundsException {
        return new ArrayList<>(apiInjectorRepositories);
    }

    public int getSizeOfExtension(APIInjectorRepository repository, Extension e) throws IOException {
        return new URL(repository.url + e.location).openStream().available();
    }

    public int getSizeOfExtension(APIInjectorRepository repository, ExtensionSimplified e) throws IOException {
        return new URL(repository.url + e.location).openStream().available();
    }

    public Extension getExtension(String extensionURL, APIInjectorRepository repository) {
        JSONObject root = new JSONObject(ConnectionUtils.makeGet(repository.url + "/" + extensionURL + ".json"));
        ArrayList<ExtensionSimplified> dependencies = new ArrayList<>();
        for(Object o : root.getJSONArray("dependencies")) {
            JSONObject dependency = new JSONObject(o.toString());
            Extension dependencyExtension = getExtension(dependency.getString("location"), repository);
            ExtensionSimplified simplified = new ExtensionSimplified(
                    dependency.getString("location"),
                    dependency.getString("identifier"),
                    dependency.getString("name"),
                    dependency.getString("author"),
                    dependency.getString("version"),
                    dependency.getString("description"),
                    dependency.getString("minversion"));
            dependencies.add(simplified);
            dependencies.addAll(dependencyExtension.dependencies);
        }
        return new Extension(
                root.getString("location"),
                root.getString("identifier"),
                root.getString("name"),
                root.getString("author"),
                root.getString("version"),
                root.getString("description"),
                dependencies,
                root.getString("minversion")
        );
    }

    public ArrayList<ExtensionSimplified> getExtensions(APIInjectorRepository repository) {
        ArrayList<ExtensionSimplified> extensions = new ArrayList<>();
        for(String s : repository.extensionURLs) {
            JSONObject root = new JSONObject(ConnectionUtils.makeGet(repository.url + s));
            extensions.add(new ExtensionSimplified(
                    root.getString("location"),
                    root.getString("identifier"),
                    root.getString("name"),
                    root.getString("author"),
                    root.getString("version"),
                    root.getString("description"),
                    root.getString("minversion")
            ));
        }
        return extensions;
    }

    public ArrayList<ExtensionSimplified> getExtensionWithIdentifier(String identifier, APIInjectorRepository repository) {
        ArrayList<ExtensionSimplified> extensions = new ArrayList<>();
        for(ExtensionSimplified simplified : getExtensions(repository)) {
            if(simplified.getIdentifier().toLowerCase().contains(identifier)) {
                extensions.add(simplified);
            }
        }
        return extensions;
    }

    public Extension getExtensionFromSimplified(ExtensionSimplified extensionSimplified, APIInjectorRepository repository) {
        return getExtension(extensionSimplified.getName() + "-" + extensionSimplified.getAuthor(), repository);
    }

    String getFileName(String url) {
        return url.split("/")[url.split("/").length-1];
    }

    public void downloadExtension(Extension e, APIInjectorRepository repository, InjectorAPI.ProgressRunnable progressRunnable) throws IOException {
        HttpURLConnection httpConnection = (HttpURLConnection) (new URL(repository.url + e.location).openConnection());
        long completeFileSize = httpConnection.getContentLength();
        java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
        java.io.FileOutputStream fos = new java.io.FileOutputStream(PublicValues.appLocation + "/Extensions/" + getFileName(e.location));
        java.io.BufferedOutputStream bout = new BufferedOutputStream(
                fos, 1024);
        byte[] data = new byte[1024];
        long downloadedFileSize = 0;
        int x;
        while ((x = in.read(data, 0, 1024)) >= 0) {
            downloadedFileSize += x;
            final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);
            long finalDownloadedFileSize = downloadedFileSize;
            SwingUtilities.invokeLater(() -> progressRunnable.run(finalDownloadedFileSize));
            bout.write(data, 0, x);
        }
        bout.close();
        in.close();
    }

    InjectorStoreFile injectorStoreFile = null;

    public InjectorStoreFile getInjectorFile() {
        if(injectorStoreFile == null) {
            injectorStoreFile = new InjectorStoreFile();
        }
        return injectorStoreFile;
    }

    public class InjectorStoreFile {
        final String path;
        JSONObject cache;


        InjectorStoreFile() {
            path = new File(PublicValues.appLocation, "InjectorStore.json").getAbsolutePath();
            if(!new File(path).exists()) {
                JSONObject root = new JSONObject();
                root.put("installed", new JSONArray());
                com.spotifyxp.utils.FileUtils.appendToFile(path, root.toString());
            }
            read();
        }

        public void read() {
            try {
                cache = new JSONObject(FileUtils.readFileToString(new File(path), Charset.defaultCharset()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void write(ExtensionSimplified simplified) {
            int counter = 0;
            for(Object o : cache.getJSONArray("installed")) {
                JSONObject extension = new JSONObject(o.toString());
                if(extension.getString("name").equals(simplified.getName()) && extension.getString("author").equals(simplified.getAuthor())) {
                    extension.put("location", simplified.getLocation());
                    extension.put("identifier", simplified.getIdentifier());
                    extension.put("name", simplified.getName());
                    extension.put("author", simplified.getAuthor());
                    extension.put("version",simplified.getVersion());
                    extension.put("description", simplified.getDescription());
                    extension.put("minversion", simplified.getMinVersion());
                    cache.getJSONArray("installed").remove(counter);
                    cache.getJSONArray("installed").put(counter, extension);
                    try {
                        FileWriter writer = new FileWriter(path, false);
                        writer.write(cache.toString());
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                counter++;
            }
            JSONObject extension = new JSONObject();
            extension.put("location", simplified.getLocation());
            extension.put("identifier", simplified.getIdentifier());
            extension.put("name", simplified.getName());
            extension.put("author", simplified.getAuthor());
            extension.put("version",simplified.getVersion());
            extension.put("description", simplified.getDescription());
            extension.put("minversion", simplified.getMinVersion());
            cache.getJSONArray("installed").put(extension);
            try {
                FileWriter writer = new FileWriter(path, false);
                writer.write(cache.toString());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public ArrayList<ExtensionSimplified> getExtensions() {
            ArrayList<ExtensionSimplified> extensions = new ArrayList<>();
            for(Object o : cache.getJSONArray("installed")) {
                JSONObject root = new JSONObject(o.toString());
                extensions.add(new ExtensionSimplified(
                   root.getString("location"),
                   root.getString("identifier"),
                   root.getString("name"),
                   root.getString("author"),
                   root.getString("version"),
                   root.getString("description"),
                   root.getString("minversion")
                ));
            }
            return extensions;
        }

        public void remove(ExtensionSimplified ex) {
            int counter = 0;
            for(Object o : cache.getJSONArray("installed")) {
                JSONObject extension = new JSONObject(o.toString());
                if(ex.getName().equals(extension.getString("name")) && ex.getAuthor().equals(extension.getString("author"))) {
                    break;
                }
                counter++;
            }
            cache.getJSONArray("installed").remove(counter);
            try {
                FileWriter writer = new FileWriter(path, false);
                writer.write(cache.toString());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
