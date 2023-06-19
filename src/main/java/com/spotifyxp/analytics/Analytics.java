package com.spotifyxp.analytics;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.ConnectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class Analytics {
    //Only sends (pc name, program name, program version, date and time)
    public Analytics() {
        //Step 1: Request Token
        try {
            if (ConnectionUtils.makePingToServer()) {
                return; // If the server is on the local network don't send analytics
            }
            if (!PublicValues.config.get(ConfigValues.sendanalytics.name).equals("true")) {
                return;
            }
            String hostname;
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                ConsoleLogging.Throwable(e);
                return; //Don't continue because java couldn't determine the pc name
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH-mm");
            LocalDateTime now = LocalDateTime.now();
            String datetime = dtf.format(now) + "_" + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
            GetMethod method = new GetMethod("https://api.werwolf2303.de/api");
            HttpClient client = new HttpClient();
            method.setRequestHeader("ip", InetAddress.getLocalHost().getHostAddress());
            method.setRequestHeader("RequestNew", "yes");
            client.executeMethod(method);
            String token = method.getResponseBodyAsString();
            GetMethod apicall = new GetMethod("https://api.werwolf2303.de/api?point=index&return=list");
            apicall.setRequestHeader("ip", InetAddress.getLocalHost().getHostAddress());
            apicall.setRequestHeader("RequestNew", "no");
            apicall.setRequestHeader("Authorization", token);
            apicall.setRequestHeader("apimethod", "analytics");
            apicall.setRequestHeader("programname", "spotifyxp");
            apicall.setRequestHeader("programversion", PublicValues.version);
            apicall.setRequestHeader("serverpcname", hostname);
            apicall.setRequestHeader("progdatetime", datetime);
            HttpClient client2 = new HttpClient();
            client2.executeMethod(apicall);
        }catch (Exception e) {
        }
    }
}
