package com.spotifyxp.utils;

import java.util.Date;

public class StartupTime {
    final long whenstarted;
    public StartupTime() {
        whenstarted = new Date().getTime();
    }
    public String getMMSSCoded() {
        long timemillis = new Date().getTime() - whenstarted;
        boolean ddh = false;
        float ttr = timemillis/(float)1000;
        int seconds = Math.round(ttr);
        int hh = seconds/60/60;
        seconds = seconds - hh * 3600;
        int mm = seconds/60;
        seconds = seconds - mm * 60;
        int ss = seconds;
        String h = String.valueOf(hh);
        String m = String.valueOf(mm);
        String s = String.valueOf(ss);
        if(mm<10) {
            m = "0" + m;
        }
        if(ss<10) {
            s = "0" + s;
        }
        return m + ":" + s;
    }
    public String getMMSS() {
        long timemillis = new Date().getTime() - whenstarted;
        boolean ddh = false;
        float ttr = timemillis/(float)1000;
        int seconds = Math.round(ttr);
        int hh = seconds/60/60;
        seconds = seconds - hh * 3600;
        int mm = seconds/60;
        seconds = seconds - mm * 60;
        int ss = seconds;
        String h = String.valueOf(hh);
        String m = String.valueOf(mm);
        String s = String.valueOf(ss);
        if(mm<10) {
            m = "0" + m;
        }
        if(ss<10) {
            s = "0" + s;
        }
        if(m.startsWith("0")) {
            m = m.replace("0", "");
        }
        if(s.startsWith("0")) {
            s = s.replace("0", "");
        }
        if(s.isEmpty()) {
            return m + " Minutes";
        }
        if(m.isEmpty()) {
            return s + " Seconds";
        }
        return m + " Minutes " + s + " Seconds";
    }
}
