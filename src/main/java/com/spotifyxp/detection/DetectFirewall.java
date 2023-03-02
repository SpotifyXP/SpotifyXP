package com.spotifyxp.detection;

import com.spotifyxp.utils.URLUtils;

public class DetectFirewall {
    public boolean hasFirewall(boolean force) {
        return true;
    }
    public boolean hasFirewall() {
        //INFO: Port 80 and 443 should be open
        //INFO: The port 19132 is mostly closed
        if(URLUtils.isURLReachable("http://portquiz.net", 19132)) {
            if(URLUtils.isURLReachable("http://portquiz.net", 9090)) {
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }
}
