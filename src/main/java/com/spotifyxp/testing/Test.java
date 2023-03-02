package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.bypass.PortBypass;
import com.spotifyxp.configuration.Config;
public class Test {
    public void old() {
        System.setProperty("http.proxyHost", "34.110.251.255");
        System.setProperty("http.proxyPort", "80");
    }
    public static void main(String[] args) {
        PublicValues.config = new Config();

    }
}
