package com.spotifyxp.testing;

import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.history.PlaybackHistory;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        new CustomSaveDir().runArgument(new File("data").getAbsolutePath()).run();

    }
}
