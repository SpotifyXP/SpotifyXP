package com.spotifyxp.injector;

import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.URLUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InjectorAPI {
    //The extension repo filesystem
    // Extension INFO     /repo/{EXTENSION_NAME-EXTENSION_AUTHOR-EXTENSION_VERSION}.json
    // Extension FILE     /repo/storage/{EXTENSION_NAME-EXTENSION_AUTHOR-EXTENSION_VERSION}.jar

    //No Deletion or Upload API

    String repoRootURL = "https://raw.githubusercontent.com/werwolf2303/SpotifyXP-Repo/main";
    String repoURL = "https://raw.githubusercontent.com/werwolf2303/SpotifyXP-Repo/main/repo";
    public ArrayList<Extension> extensions = new ArrayList<>();

    public static class Extension {
        public String location;
        public String name;
        public String author;
        public String version;
    }

    public void parseExtensions() {
        String repofile = URLUtils.getURLResponseAsString(repoURL + "/REPO.INFO");
        JSONObject object = new JSONObject(repofile);
        for(Object o : new JSONArray(object.getJSONObject("REPO").getJSONArray("EXTENSIONS"))) {
            JSONObject extension = new JSONObject(o.toString());
            String content = URLUtils.getURLResponseAsString(repoRootURL + extension.getString("LOCATION"));
            Extension e = new Extension();
            e.location = repoRootURL + new JSONObject(content).getString("FILE");
            e.name = new JSONObject(content).getString("NAME");
            e.author = new JSONObject(content).getString("AUTHOR");
            e.version = new JSONObject(content).getString("VERSION");
            extensions.add(e);
        }
    }

    public Extension getExtension(String name) {
        Extension url = new Extension();
        for(Extension e : extensions) {
            if(e.name.equals(name)) {
                url = e;
            }
        }
        return url;
    }

    String getFileName(String url) {
        return url.split("/")[url.split("/").length-1];
    }

    @FunctionalInterface
    public static interface ProgressRunnable {
        void run(long filesizeDownloaded);
    }

    public long getExtensionSize(String url) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(url).openConnection());
            long completeFileSize = httpConnection.getContentLength();
            return completeFileSize;
        }catch (Exception e) {
            return -1;
        }
    }

    public void downloadExtension(String url, ProgressRunnable progressRunnable) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(url).openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(PublicValues.appLocation + "/Extensions/" + getFileName(url));
            java.io.BufferedOutputStream bout = new BufferedOutputStream(
                    fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);
                long finalDownloadedFileSize = downloadedFileSize;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressRunnable.run(finalDownloadedFileSize);
                    }
                });
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
    }
}
