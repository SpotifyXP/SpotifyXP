package com.spotifyxp.deps.xyz.gianlu.librespot.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SuppressWarnings("UnusedAssignment")
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
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return new String(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String content, String password) {
        byte[] data = null;
        try {
            data = content.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        data = encrypt(data, password);
        return byte2hex(data);
    }

    private static SecretKeySpec createKey(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuilder sb = new StringBuilder(32);
        sb.append(password);
        while (sb.length() < 32) {
            sb.append("0");
        }
        if (sb.length() > 32) {
            sb.setLength(32);
        }

        data = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(data, "AES");
    }

    public static String byte2hex(byte[] b) { // ???????
        StringBuilder sb = new StringBuilder(b.length * 2);
        String tmp;
        for (byte value : b) {
            // ?????????????
            tmp = (Integer.toHexString(value & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase(); // ?????
    }


    final byte[] key;
    final String algorithm = "AES";

    public String encrypt(String data) {
        byte[] dataToSend = data.getBytes();
        Cipher c = null;
        try {
            c = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        SecretKeySpec k = new SecretKeySpec(key, algorithm);
        try {
            c.init(Cipher.ENCRYPT_MODE, k);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] encryptedData = "".getBytes();
        try {
            encryptedData = c.doFinal(dataToSend);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        byte[] encryptedByteValue = Base64.getEncoder().encode(encryptedData);
        return new String(encryptedByteValue);
    }

    public String decrypt(String data) {
        byte[] encryptedByteValue = java.util.Base64.getDecoder().decode(data.getBytes());
        Cipher c = null;
        try {
            c = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        SecretKeySpec k = new SecretKeySpec(key, algorithm);
        try {
            c.init(Cipher.DECRYPT_MODE, k);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] decryptedData = "".getBytes();
        try {
            decryptedData = c.doFinal(encryptedByteValue);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return new String(decryptedData);
    }
}
