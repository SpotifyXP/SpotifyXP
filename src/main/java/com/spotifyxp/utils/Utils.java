package com.spotifyxp.utils;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public class Utils {
    public static void replaceFileString(String old, String newstring, String filelocation) {
        try {
            FileInputStream fis = new FileInputStream(filelocation);
            String content = IOUtils.toString(fis, Charset.defaultCharset());
            content = content.replaceAll(old, newstring);
            FileOutputStream fos = new FileOutputStream(filelocation);
            IOUtils.write(content, Files.newOutputStream(Paths.get(filelocation)), Charset.defaultCharset());
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
