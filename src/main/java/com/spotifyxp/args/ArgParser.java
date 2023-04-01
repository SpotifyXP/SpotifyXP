package com.spotifyxp.args;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLoggingModules;

public class ArgParser {
    public ArgParser(String[] args) {
        try {
            if (args[0].equals("--setup-complete")) {
                PublicValues.foundSetupArgument = true;
            }
            if(args.length>1) {
                if (args[1].equals("--debug")) {
                    PublicValues.debug = true;
                    ConsoleLoggingModules modules = new ConsoleLoggingModules("Module");
                    modules.setColored(false);
                    modules.setShowTime(false);
                }
            }
        }catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }
}
