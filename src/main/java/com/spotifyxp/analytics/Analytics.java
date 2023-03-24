package com.spotifyxp.analytics;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.ConnectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class Analytics {
    //Only sends (pc name, program name, program version, date and time)
    public Analytics() {
        if(ConnectionUtils.makePingToServer()) {
            return;
        }
        String hostname;
        HttpClient client = new HttpClient();
        try {
             hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            ConsoleLogging.Throwable(e);
            return; //Don't continue because java couldn't determine the pc name
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH-mm");
        LocalDateTime now = LocalDateTime.now();
        String datetime = dtf.format(now) + "_" + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
        try {
            PostMethod post = new PostMethod("https://api.werwolf2303.de?point=analytics&programname=spotifyxp&programversion=" + PublicValues.version + "&serverpcname=" + hostname + "&progdatetime=" + datetime);
            post.setParameter("Auth", "public");
            client.executeMethod(post);
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
