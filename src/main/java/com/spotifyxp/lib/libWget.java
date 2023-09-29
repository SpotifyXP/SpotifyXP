package com.spotifyxp.lib;

import com.spotifyxp.exception.ExceptionDialog;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class libWget {
    /**
     Usage:
     @apiNote When it doesnt know which filename it is it downloads the file as 'DownloadedFile.ukf' (ukf) == Unknown File Format
     @param url url from file to download
     @param replaceExisting Replace file if its already exists
     @param path path to store file
     */
    public void download(String url, String path, boolean replaceExisting) {
        url = url.replace("\\", "/");
        try {
            InputStream in = new URL(url).openStream();
            if(replaceExisting) {
                Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
            }else{
                if(!new File(path).exists()) {
                    Files.copy(in, Paths.get(path));
                }
            }
        }catch (IOException ioe) {
            ExceptionDialog.open(ioe);
        }
    }

    /**
     * Downloads the file at the given url
     * @param url url of the file
     * @apiNote downloades the file in the ukf format when it doesnt know which name it has
     */
    public void download(String url) {
        url = url.replace("\\", "/");
        try {
            InputStream in = new URL(url).openStream();
            if(url.split("/")[url.split("/").length-1]!=url.split("/")[0]) {
                Files.copy(in, Paths.get(url.split("/")[url.split("/").length-1]));
            }else {
                if (new File("DownloadedFile.ukf").exists()) {
                    Files.copy(in, Paths.get("DownloadedFile" + new Random().nextInt(1000) + ".ukf"));
                } else {
                    Files.copy(in, Paths.get("DownloadedFile.ukf"));
                }
            }
        }catch (IOException ioe) {
            ExceptionDialog.open(ioe);
        }
    }
}
