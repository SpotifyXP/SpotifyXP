package com.spotifyxp.utils;

import com.spotifyxp.logging.ConsoleLogging;
import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureDSA;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static final String ALGO = "AES";

    public static String encrypt(String data, String secretkey) throws Exception {
        Key key = new SecretKeySpec(secretkey.getBytes(), ALGO);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return new BASE64Encoder().encode(encVal);
    }
    public static String decrypt(String encryptedData, String secretkey) throws Exception {
        Key key = new SecretKeySpec(secretkey.getBytes(), ALGO);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }
}