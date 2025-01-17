package com.spotifyxp.utils;


import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


public class Resources {
    public String readToString(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        InputStream stream = getClass().getResourceAsStream(path);
        try {
            assert stream != null;
            return IOUtils.toString(stream, Charset.defaultCharset());
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        ConsoleLogging.Throwable(new Exception());
        return path;
    }

    public InputStream readToInputStream(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return getClass().getResourceAsStream(path);
    }
}
