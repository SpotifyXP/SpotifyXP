package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class ApplicationUtils {
    private ApplicationUtils() {
    }
    private static JSONObject object = null;
    private static final String ErrorMessage = "Check Application.json";

    private static void fetch() {
        object = new JSONObject(new Resources(true).readToString("Application.json"));
    }
    public static String getName() {
        if(object == null) {
            fetch();
        }
        if(object.has("Name")) {
            return object.getString("Name");
        }else{
            return ErrorMessage;
        }
    }
    public static String getVersion() {
        if(object == null) {
            fetch();
        }
        if(object.has("Version")) {
            return object.getString("Version");
        }else{
            return ErrorMessage;
        }
    }
    public static String getReleaseCandidate() {
        if(object == null) {
            fetch();
        }
        if(object.has("ReleaseCandidate")) {
            return object.getString("ReleaseCandidate");
        }else{
            return ErrorMessage;
        }
    }

    public static String getUserAgent() {
        String osSpecifier = System.getProperty("os.name").contains("mac") ? "Macintosh" :
                System.getProperty("os.name").contains("win") ? "Windows" : "Linux";; //Macintosh
        String osName = System.getProperty("os.name"); //Mac OS X
        String osVersion = System.getProperty("os.version"); //10.15
        String browserSpecifier = "Java"; //Java
        String browserDate = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")); //21012024
        String applicationName = getName(); //SpotifyXP
        String applicationVersion = getVersion(); //2.0.2
        return "Mozilla/5.0 (" + osSpecifier + "; " + osName + " " + osVersion + ") " + browserSpecifier + "/" + browserDate + " " + applicationName + "/" + applicationVersion;
    }
}
