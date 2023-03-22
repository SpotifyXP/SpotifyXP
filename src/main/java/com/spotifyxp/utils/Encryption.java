package com.spotifyxp.utils;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.spotifyxp.logging.ConsoleLogging;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

public class Encryption {
    String key = "";
    public Encryption() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            key = Base64.getEncoder().encodeToString(sb.toString().getBytes()) + Base64.getEncoder().encodeToString(System.getProperty("user.name").getBytes());
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
    }
    public String encrypt(String enckey) {
        try {
            return new String(AES.encrypt(enckey, key));
        }catch (Exception e) {
            //Failed
            return enckey;
        }
    }
    public String decrypt(String decKey) {
        try {
            return new String(AES.decrypt(decKey, key));
        }catch (Exception e) {
            //Failed

            return decKey;
        }
    }
}
