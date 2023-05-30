package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.analytics.Analytics;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.deps.com.spotify.metadata.Metadata;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.utils.PlayerUtils;
import com.spotifyxp.utils.Token;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.swing.*;
import java.awt.*;
import java.util.TimeZone;

public class Test {
    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod("http://127.0.0.1:790/search");
        method.setRequestHeader("key", "nevermind");
        client.executeMethod(method);
        System.out.println("Headers ");
        for(Header h : method.getResponseHeaders()) {
            System.out.println("   " + h);
        }
        System.out.println("Response: " + method.getResponseBodyAsString());
    }
}
