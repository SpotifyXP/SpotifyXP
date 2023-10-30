package com.spotifyxp.injector;

import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.URLUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@SuppressWarnings("JavadocDeclaration")
public class InjectorAPI {
    //The extension repo filesystem
    // Extension INFO     /repo/{EXTENSION_NAME-EXTENSION_AUTHOR-EXTENSION_VERSION}.json
    // Extension FILE     /repo/storage/{EXTENSION_NAME-EXTENSION_AUTHOR-EXTENSION_VERSION}.jar

    //No Deletion or Upload API

    final String repoRootURL = "https://raw.githubusercontent.com/SpotifyXP/SpotifyXP-Repository/main";
    final String repoURL = "https://raw.githubusercontent.com/SpotifyXP/SpotifyXP-Repository/main/repo";
    public final ArrayList<Extension> extensions = new ArrayList<>();

    public static final String compatibleMinVersion = "2.0.0";

    public static class Extension {
        public String location;
        public String name;
        public String author;
        public String version;
        public String description;
        public String identifier;
        public String minVersion;
    }

    /**
     * Parses all extensions in the Spotify extension store
     */

    int retryCounter = 0;

    public void parseExtensions() {
        String repofile = URLUtils.getURLResponseAsString(repoURL + "/REPO.INFO");
        try {
            JSONObject object = new JSONObject(repofile);
            for (Object o : new JSONArray(object.getJSONObject("REPO").getJSONArray("EXTENSIONS"))) {
                JSONObject extension = new JSONObject(o.toString());
                String content = URLUtils.getURLResponseAsString(repoRootURL + extension.getString("LOCATION"));
                JSONObject contentJ = new JSONObject(content);
                Extension e = new Extension();
                e.location = repoRootURL + contentJ.getString("FILE");
                e.name = contentJ.getString("NAME");
                e.identifier = contentJ.getString("IDENTIFIER");
                e.author = contentJ.getString("AUTHOR");
                e.version = contentJ.getString("VERSION");
                e.description = contentJ.getString("DESCRIPTION");
                e.minVersion = contentJ.getString("MINVERSION");
                extensions.add(e);
            }
        }catch (JSONException e) {
            //Connection failed
            if(retryCounter > 3) {
                GraphicalMessage.sorryError("Connection failed!");
                return;
            }
            retryCounter++;
            parseExtensions();
        }
    }

    /**
     * Gets the extension with the given identifier
     * @param identifier identifier
     * @return
     */
    public Extension getExtension(String identifier) {
        Extension url = new Extension();
        for(Extension e : extensions) {
            if(e.identifier.equals(identifier)) {
                url = e;
            }
        }
        return url;
    }

    String getFileName(String url) {
        return url.split("/")[url.split("/").length-1];
    }

    @FunctionalInterface
    public interface ProgressRunnable {
        void run(long filesizeDownloaded);
    }

    /**
     * Gets the extension size
     * @param url url of the extension
     * @return
     */
    public long getExtensionSize(String url) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (new URL(url).openConnection());
            return httpConnection.getContentLength();
        }catch (Exception e) {
            return -1;
        }
    }

    /**
     * Downloads the extension specified
     * @param url url of the extension
     * @param progressRunnable for track of progress (currently downloaded size)
     */
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
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
    }
}
