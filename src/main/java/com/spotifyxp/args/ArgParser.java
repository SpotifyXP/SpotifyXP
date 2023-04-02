package com.spotifyxp.args;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLoggingModules;

public class ArgParser {
    public ArgParser(String[] args) {
        for(String s : args) {
            if(s.equals("--setup-complete")) {
                PublicValues.foundSetupArgument = true;
            }
            if(s.equals("--debug")) {
                PublicValues.debug = true;
                ConsoleLoggingModules modules = new ConsoleLoggingModules("Module");
                modules.setColored(false);
                modules.setShowTime(false);
            }
        }
    }
}
