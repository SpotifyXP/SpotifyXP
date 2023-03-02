package com.spotifyxp.utils;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class Utils {
    public static void replaceFileString(String old, String newstring, String filelocation) {
        try {
            String fileName = filelocation;
            FileInputStream fis = new FileInputStream(fileName);
            String content = IOUtils.toString(fis, Charset.defaultCharset());
            content = content.replaceAll(old, newstring);
            FileOutputStream fos = new FileOutputStream(fileName);
            IOUtils.write(content, new FileOutputStream(fileName), Charset.defaultCharset());
            fis.close();
            fos.close();
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public static int calculateRest(int from, int by) {
        return from - Math.round(from/by);
    }
}
