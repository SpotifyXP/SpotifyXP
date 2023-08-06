package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.support.MacOSSupportModule;
import com.spotifyxp.utils.MacOSAppUtil;
import com.spotifyxp.video.CanvasPlayer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws Exception {
        new MacOSSupportModule();
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", "\"" + System.getProperty("java.home") + "/bin/java\"" + " -jar " + PublicValues.tempPath + "/SpotifyXP-Uninstaller.jar");
        try {
            builder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
