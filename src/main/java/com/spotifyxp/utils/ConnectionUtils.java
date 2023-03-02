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
import java.io.File;
import java.io.IOException;

public class ConnectionUtils {
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
        String browserpath = "";
        if(new File("pom.xml").exists()) {
            browserpath="C:\\Program Files\\Mozilla Firefox\\firefox.exe";
        }else {
            browserpath = PublicValues.config.get(ConfigValues.mypalpath.name);
        }
        ProcessBuilder builder = new ProcessBuilder("\"" + browserpath + "\"", url);
        try {
            builder.start();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
