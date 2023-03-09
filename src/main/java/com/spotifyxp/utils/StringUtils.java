package com.spotifyxp.utils;


import com.spotifyxp.logging.ConsoleLogging;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class StringUtils {
    public static String generateStringFrom(int length) {
        int number = 1;
        Random r = new Random();
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        StringBuilder strbuilder = new StringBuilder();
        for(int i=0; i < length; i++) {
            if(r.nextInt(3)==number) {
                //Number (1,2,3)
                strbuilder.append(numbers.charAt(r.nextInt(numbers.length())));
            }else{
                //Letters (A,B,C)
                strbuilder.append(letters.charAt(r.nextInt(letters.length())));
            }
        }
        return strbuilder.toString();
    }
    public static String hashWith256(String textToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            return Base64.getEncoder().encodeToString(hashedByetArray);
        } catch (NoSuchAlgorithmException e) {
            ConsoleLogging.Throwable(e);
        }
        return "FAILED";
    }


}
