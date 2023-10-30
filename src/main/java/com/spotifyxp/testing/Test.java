package com.spotifyxp.testing;

import com.spotifyxp.logging.ConsoleLoggingModules;

public class Test {
    public static void main(String[] args) {
        ConsoleLoggingModules v2 = new ConsoleLoggingModules();
        v2.setColored(true);

        ConsoleLoggingModules.warning("This is a warning");
    }
}
