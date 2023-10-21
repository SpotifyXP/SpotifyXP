package com.spotifyxp.api;

import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * This api is my private one from <a href="https://api.werwolf2303.de">https://api.werwolf2303.de</a>
 */
public class Werwolf2303API {
    String apikey;
    boolean suc = false;
    public Werwolf2303API() {
        String key = requestAPIKey();
        if(!key.isEmpty()) {
            apikey = key;
            suc = true;
        }
    }

    String requestAPIKey() {
        try {
            GetMethod method = new GetMethod("https://api.werwolf2303.de/api");
            HttpClient client = new HttpClient();
            method.setRequestHeader("ip", InetAddress.getLocalHost().getHostAddress());
            method.setRequestHeader("RequestNew", "yes");
            client.executeMethod(method);
            return method.getResponseBodyAsString();
        }catch (Exception e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
            return "";
        }
    }

    public String makeRequest(String endpoint, HashMap<String, String> parameters) {
        try {
            HttpClient client = new HttpClient();
            GetMethod apicall = new GetMethod("https://api.werwolf2303.de/api?" + endpoint);
            apicall.setRequestHeader("ip", InetAddress.getLocalHost().getHostAddress());
            apicall.setRequestHeader("RequestNew", "no");
            apicall.setRequestHeader("Authorization", apikey);
            for (Map.Entry<String, String> i : parameters.entrySet()) {
                apicall.setRequestHeader(i.getKey(), i.getValue());
            }
            client.executeMethod(apicall);
            return apicall.getResponseBodyAsString();
        }catch (Exception e) {
            return "";
        }
    }
}
