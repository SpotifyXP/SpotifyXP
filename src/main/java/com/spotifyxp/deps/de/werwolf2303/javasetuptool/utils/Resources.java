package com.spotifyxp.deps.de.werwolf2303.javasetuptool.utils;



import java.io.InputStream;


public class Resources {
    boolean sm = false;
    public Resources(boolean suppresserrors) {
        sm = true;
    }
    public Resources() {

    }
    public InputStream readToInputStream(String path) {
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        InputStream stream = getClass().getResourceAsStream(path);
        if(stream==null) {
            if(!sm) {
                System.err.println("File not found: " + path);
            }
        }
        return stream;
    }
}
