package com.spotifyxp.utils;


import com.spotifyxp.logging.ConsoleLogging;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class StringUtils {
    public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }
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
