package com.spotifyxp.utils;

import java.io.InputStream;

public class OverwriteFactory {
    static OverwriteEnum overwriteWith;

    public static void setOverwrite(OverwriteEnum overwrite) {
        overwriteWith = overwrite;
    }

    public static void run(InputStream stream) {
        if(overwriteWith == null) {
            return;
        }
        overwriteWith.execute(stream);
    }

    public interface OverwriteEnum {
        void execute(InputStream stream);
    }
}
