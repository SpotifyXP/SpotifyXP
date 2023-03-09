package com.spotifyxp.utils;

import com.spotifyxp.logging.ConsoleLogging;

import java.io.*;

public class FileUtils {
    public static void appendToFile(String path, String towrite) {
        try {
            File file = new File(path);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(towrite + "\n");
            br.close();
            fr.close();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
