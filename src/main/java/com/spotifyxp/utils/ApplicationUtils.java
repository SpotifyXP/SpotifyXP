package com.spotifyxp.utils;

import org.json.JSONObject;

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
}
