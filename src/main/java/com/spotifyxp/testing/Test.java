package com.spotifyxp.testing;



import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLoggingModules;

@SuppressWarnings("EmptyMethod")
public class Test {
    public static void main(String[] args ) {
        ConsoleLoggingModules modules = new ConsoleLoggingModules("CssBox");
        modules.setColored(true);
        modules.setShowTime(false);
        args = new String[] {"http://www.google.de"};

    }

}
