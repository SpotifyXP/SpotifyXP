package com.spotifyxp.utils;

import com.spotifyxp.logging.ConsoleLogging;

import java.io.*;
import java.util.Scanner;

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
    public static void appendToFile(File file, String towrite) {
        try {
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(towrite + "\n");
            br.close();
            fr.close();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
    }
    public static String readToString(File file) {
        String cache = "";
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(cache=="") {
                    cache = data + "\n";
                }else{
                    cache = cache + data + "\n";
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return cache;
    }
    public static String readToString(String path) {
        String cache = "";
        try {
            File file = new File(path);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(cache=="") {
                    cache = data + "\n";
                }else{
                    cache = cache + data + "\n";
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return cache;
    }
}
