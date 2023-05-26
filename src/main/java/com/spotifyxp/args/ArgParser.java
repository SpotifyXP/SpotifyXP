package com.spotifyxp.args;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLoggingModules;

public class ArgParser {
    public ArgParser(String[] args) {
        int counter = 0;
        for(String s : args) {
            if(s.contains("--custom-savedir")) {
                PublicValues.fileslocation = s.split("=")[1];
                PublicValues.configfilepath = PublicValues.fileslocation + "/config.properties";
            }
            if(s.equals("--setup-complete")) {
                PublicValues.foundSetupArgument = true;
            }
            if(s.contains("--language")) {
                PublicValues.language.setNoAutoFindLanguage(s.split("=")[1]);
            }
            if(s.equals("--debug")) {
                PublicValues.debug = true;
                ConsoleLoggingModules modules = new ConsoleLoggingModules("Module");
                modules.setColored(false);
                modules.setShowTime(false);
            }
            counter++;
        }
    }
}
