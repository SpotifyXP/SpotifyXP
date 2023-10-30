package com.spotifyxp.utils;


import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.logging.ConsoleLogging;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class ConnectionUtils {
    public static String makeGet(String url) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            return EntityUtils.toString(client.execute(get).getEntity());
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }

    public static String makeGet(String url, NameValuePair[] topost, Header... headers) {
        try {
            HttpClient client = HttpClients.createDefault();
            StringBuilder queries = new StringBuilder();
            queries.append("?");
            for(NameValuePair pair : topost) {
                queries.append("&").append(pair.getName()).append("=").append(pair.getValue());
            }
            HttpGet get = new HttpGet(url + queries.toString().replace("?&", "?"));
            for(Header header : headers) {
                get.addHeader(header);
            }
            return EntityUtils.toString(client.execute(get).getEntity());
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }

    public static String makePost(String url, NameValuePair[] topost, Header... headers) {
        try {
            HttpClient client = HttpClients.createDefault();
            StringBuilder queries = new StringBuilder();
            queries.append("?");
            for(NameValuePair pair : topost) {
                queries.append("&").append(pair.getName()).append("=").append(pair.getValue());
            }
            HttpPost post = new HttpPost(url + queries.toString().replace("?&", "?"));
            for(Header header : headers) {
                post.addHeader(header);
            }
            return EntityUtils.toString(client.execute(post).getEntity());
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }

    public static String makeDelete(String url, NameValuePair[] topost, Header... headers) {
        try {
            HttpClient client = HttpClients.createDefault();
            StringBuilder queries = new StringBuilder();
            queries.append("?");
            for(NameValuePair pair : topost) {
                queries.append("&").append(pair.getName()).append("=").append(pair.getValue());
            }
            HttpDelete delete = new HttpDelete(url + queries.toString().replace("?&", "?"));
            for(Header header : headers) {
                delete.addHeader(header);
            }
            return EntityUtils.toString(client.execute(delete).getEntity());
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }

    public static void openBrowser(String url) {
        String browserpath = PublicValues.config.getString(ConfigValues.mypalpath.name);
        if(browserpath.isEmpty())  {
            if(Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                }catch (Exception ignored) {
                }
            }
            JOptionPane.showConfirmDialog(null, "Please set the mypal path in settings", "Info", JOptionPane.OK_CANCEL_OPTION);
            return;
        }
        ProcessBuilder builder = new ProcessBuilder("\"" + browserpath + "\"", url);
        try {
            builder.start();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
