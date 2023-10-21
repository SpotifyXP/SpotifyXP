package com.spotifyxp.utils;


import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ConnectionUtils {
    public static String makeGet(String url) {
        try {
            HttpClient client = new HttpClient();
            GetMethod get = new GetMethod(url);
            client.executeMethod(get);
            return get.getResponseBodyAsString();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }
    public static String makeGet(String url, NameValuePair[] topost, Header... headers) {
        try {
            HttpClient client = new HttpClient();
            GetMethod get = new GetMethod(url);
            get.setQueryString(topost);
            for (Header header : headers) {
                get.addRequestHeader(header.getName(), header.getValue());
            }
            client.executeMethod(get);
            return get.getResponseBodyAsString();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }
    public static String makePost(String url, NameValuePair[] topost, Header... headers) {
        try {
            HttpClient client = new HttpClient();
            PostMethod get = new PostMethod(url);
            get.setQueryString(topost);
            if(!(headers.length==0)) {
                for (Header header : headers) {
                    get.addRequestHeader(header.getName(), header.getValue());
                }
            }
            client.executeMethod(get);
            return get.getResponseBodyAsString();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }
    public static String makeDelete(String url, NameValuePair[] topost, Header... headers) {
        try {
            HttpClient client = new HttpClient();
            DeleteMethod get = new DeleteMethod(url);
            get.setQueryString(topost);
            for (Header header : headers) {
                get.addRequestHeader(header.getName(), header.getValue());
            }
            client.executeMethod(get);
            return get.getResponseBodyAsString();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            return "FAILED";
        }
    }
    public static void openBrowser(String url) {
        String browserpath;
        if(new File("pom.xml").exists()) {
            browserpath="C:\\Program Files\\Mozilla Firefox\\firefox.exe";
        }else {
            browserpath = PublicValues.config.get(ConfigValues.mypalpath.name);
        }
        if(browserpath.isEmpty())  {
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
    public static boolean makePingToServer() {
        try {
            HttpClient client = new HttpClient();
            GetMethod get = new GetMethod("http://192.168.2.30/ping.html");
            get.setRequestHeader("Content-Type", "text/html");
            client.executeMethod(get);
            return get.getResponseBodyAsString().replace("\n", "").equals("Pong from Werwolf2303.de");
        } catch (Exception e) {
            return false;
        }
    }
}
