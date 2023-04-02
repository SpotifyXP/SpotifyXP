package com.spotifyxp.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.spotifyxp.utils.EncryptorAesGcmPassword;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.Resources;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@SuppressWarnings("EmptyMethod")
public class Test {
    public static void main(String[] args ) {
        System.out.println("Unrestricting Security");
        System.out.println("Done!");
        try {
            Security.setProperty("crypto.policy", "unlimited");
            String encrypted = EncryptorAesGcmPassword.encrypt("Hello World Java Encryption".getBytes(), "97531gb+++");
            System.out.println(encrypted);
            System.out.println("Decrypted: " + EncryptorAesGcmPassword.decrypt(encrypted, "97531gb+++"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
