package com.spotifyxp.utils;


import com.spotifyxp.Initiator;
import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


public class Resources {
    boolean sm = false;
    Class<?> classForResources = Initiator.class;
    public Resources(boolean suppresserrors) {
        sm = true;
    }
    public Resources(boolean suppresserrors, Class<?> classForResources) {
        this.classForResources = classForResources;
    }
    public Resources() {

    }
    public String readToString(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            if(!sm) { ConsoleLogging.Throwable(new FileNotFoundException()); }
        }
        try {
            assert stream != null;
            return IOUtils.toString(stream, Charset.defaultCharset());
        } catch (IOException e) {
            if(!sm) { ConsoleLogging.Throwable(e); }
        }
        if(!sm) { ConsoleLogging.Throwable(new Exception()); }
        return path;
    }
    public InputStream readToInputStream(String path) {
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        InputStream stream = getClass().getResourceAsStream(path);
        if(stream==null) {
            if(!sm) { ConsoleLogging.Throwable(new FileNotFoundException()); }
        }
        return stream;
    }
}
