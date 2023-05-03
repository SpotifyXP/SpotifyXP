package com.spotifyxp.utils;

import java.io.InputStream;

public class OverwriteFactory {
    static OverwriteEnum overwriteWith = new OverwriteEnum() {
        @Override
        public void execute(InputStream stream) {
            //Dummy
        }
    };

    public static void setOverwrite(OverwriteEnum overwrite) {
        overwriteWith = overwrite;
    }

    public static void run(InputStream stream) {
        overwriteWith.execute(stream);
    }

    public static interface OverwriteEnum {
        void execute(InputStream stream);
    }
}
