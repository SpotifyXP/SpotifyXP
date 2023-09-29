package com.spotifyxp.deps.xyz.gianlu.librespot.crypto;

import com.spotifyxp.deps.se.michaelthelin.spotify.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoAES {
    public CryptoAES(String password) {
        key = password.getBytes();
    }
    private static final String CipherMode = "AES/ECB/PKCS5Padding";
    public static byte[] encrypt(byte[] content, String password) {
        try {/* ww w. ja  v a2 s  .  co  m*/
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String content, String password) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encrypt(data, password);
        String result = byte2hex(data);
        return result;
    }

    private static SecretKeySpec createKey(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(32);
        sb.append(password);
        while (sb.length() < 32) {
            sb.append("0");
        }
        if (sb.length() > 32) {
            sb.setLength(32);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    public static String byte2hex(byte[] b) { // ???????
        StringBuffer sb = new StringBuffer(b.length * 2);
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            // ?????????????
            tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase(); // ?????
    }


     byte[]  key = "!@#$!@#$%^&**&^%".getBytes();
    final String algorithm="AES";

    public String encrypt(String data){

        byte[] dataToSend = data.getBytes();
        Cipher c = null;
        try {
            c = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SecretKeySpec k =  new SecretKeySpec(key, algorithm);
        try {
            c.init(Cipher.ENCRYPT_MODE, k);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] encryptedData = "".getBytes();
        try {
            encryptedData = c.doFinal(dataToSend);
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] encryptedByteValue = Base64.encode(encryptedData).getBytes();
        return  new String(encryptedByteValue);//.toString();
    }

    public String decrypt(String data){

        byte[] encryptedData  = Base64.decode(data);
        Cipher c = null;
        try {
            c = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SecretKeySpec k =
                new SecretKeySpec(key, algorithm);
        try {
            c.init(Cipher.DECRYPT_MODE, k);
        } catch (InvalidKeyException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        byte[] decrypted = null;
        try {
            decrypted = c.doFinal(encryptedData);
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String(decrypted);
    }
}
