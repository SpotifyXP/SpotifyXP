package com.spotifyxp.analytics;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.Werwolf2303API;
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
import java.util.HashMap;
import java.util.TimeZone;

public class Analytics {
    //Only sends (pc name, program name, program version, date and time)
    public Analytics() {
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
            Werwolf2303API api = new Werwolf2303API();
            HashMap<String, String> params = new HashMap<>();
            params.put("apimethod", "analytics");
            params.put("programname", "spotifyxp");
            params.put("programversion", PublicValues.version);
            params.put("serverpcname", hostname);
            params.put("progdatetime", datetime);
            api.makeRequest("point=index&return=list", params);
        }catch (Exception e) {
        }
    }
}
