package com.spotifyxp.utils;

import com.spotifyxp.logging.ConsoleLogging;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Crypto {
    String password = "";
    public Crypto() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            password = sb.toString();
        } catch (UnknownHostException | SocketException e) {
            ConsoleLogging.Throwable(e);
        }
    }
    public String decrypt(String todecrypt) throws Exception {
        try {
            return EncryptorAesGcmPassword.decrypt(todecrypt, password);
        } catch (Exception e) {
            throw new Exception();
        }
    }
    public String encrypt(String toencrypt) {
        try {
            return EncryptorAesGcmPassword.encrypt(toencrypt.getBytes(), password);
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
            return "ERROR";
        }
    }
}
