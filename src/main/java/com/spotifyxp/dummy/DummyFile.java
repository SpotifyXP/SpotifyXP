package com.spotifyxp.dummy;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class DummyFile {
    File dummy = null;
    public DummyFile(String path, String name) {
        try {
            dummy = new File(path, name);
            dummy.createNewFile();
        } catch (IOException e) {
        }
    }
    public DummyFile(String path) {
        try {
            dummy = new File(path, new Random().nextInt(1000)+".txt");
            dummy.createNewFile();
        } catch (IOException e) {
        }
    }
    public DummyFile() {
        try {
            dummy = new File(new Random().nextInt(1000)+".txt");
            dummy.createNewFile();
        } catch (IOException e) {
        }
    }
    public File getDummy() {
        return dummy;
    }
    public void delete() {
        dummy.delete();
    }
}
