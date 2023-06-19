package com.spotifyxp.args;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLoggingModules;

import java.util.ArrayList;

public class ArgParser {
    public ArrayList<Argument> arguments = new ArrayList<>();
    public ArgParser() {
        arguments.add(new CustomSaveDir());
        arguments.add(new Debug());
        arguments.add(new Language());
        arguments.add(new SetupComplete());
        arguments.add(new SteamDeckMode());
    }
    public void printHelp() {
        System.out.println("SpotifyXP - " + PublicValues.version + "\n");
        System.out.println("Usage java -jar SpotifyXP.jar <argument>..." + "\n\n");
        for(Argument a : arguments) {
            if(a.hasParameter()) {
                System.out.println("--" + a.getName() + "=<parameter>" + "   =>   " + a.getDescription());
            }else {
                System.out.println("--" + a.getName() + "   =>   " + a.getDescription());
            }
        }
    }
    public void parseArguments(String[] args) {
        boolean isvalid = false;
        for(String s : args) {
            String argument = s.replace("--", "");
            try {
                argument = s.replace("--", "").split("=")[0];
            }catch (Exception ignored) {
            }
            String parameter = "";
            try {
                parameter = s.split("=")[1];
            }catch (Exception ignored) {
            }
            for(Argument a : arguments) {
                if(a.getName().equals(argument)) {
                    isvalid = true;
                    a.runArgument(parameter).run();
                    break;
                }
            }
            if(!isvalid) {
                System.out.println("Invalid argument: " + argument + "\n");
                printHelp();
            }
        }
    }
}
