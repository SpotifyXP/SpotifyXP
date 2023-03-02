package com.spotifyxp.bypass;



import com.spotifyxp.ExitCodes;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.URLUtils;
import org.json.JSONObject;


public class PortBypass {
    public PortBypass(String ip, String port) {
        if(URLUtils.isURLReachable(ip, Integer.parseInt(port))) {
            System.setProperty("http.proxyHost", ip);
            System.setProperty("http.proxyPort", port);
        }else{
            System.exit(ExitCodes.PROXY_NOT_RECHEABLE.getCode());
        }
    }
}
