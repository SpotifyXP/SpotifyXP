package com.spotifyxp.utils;

import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;

import java.util.ArrayList;

public class ArrayListUtils {
    public static String ArrayListtoString(ArrayList<String> list) {
        StringBuilder builder = new StringBuilder();
        for(String s : list) {
            if(builder.toString().equals("")) {
                builder.append(s);
            }else{
                builder.append(", ").append(s);
            }
        }
        return builder.toString();
    }
    public static String ArtistSimplifiedtoString(ArtistSimplified[] list) {
        StringBuilder builder = new StringBuilder();
        for(ArtistSimplified s : list) {
            if(builder.toString().equals("")) {
                builder.append(s.getName());
            }else{
                builder.append(", ").append(s.getName());
            }
        }
        return builder.toString();
    }
}
