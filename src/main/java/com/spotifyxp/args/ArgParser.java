package com.spotifyxp.args;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.utils.ApplicationUtils;

import java.util.ArrayList;

public class ArgParser {
    public ArrayList<Argument> arguments = new ArrayList<>();
    public ArgParser() {
        arguments.add(new CustomSaveDir());
        arguments.add(new Debug());
        arguments.add(new Language());
        arguments.add(new SetupComplete());
        arguments.add(new SteamDeckMode());
        arguments.add(new InvokeUpdater());
        arguments.add(new NoConnection());
        arguments.add(new NoGUI());
        arguments.add(new RESTApi());
        arguments.add(new Help());
    }

    /**
     * Prints an argument help containing a list of all commands available
     */
    public void printHelp() {
        System.out.println("SpotifyXP - " + ApplicationUtils.getVersion() + "\n");
        System.out.println("Usage java -jar SpotifyXP.jar <argument>..." + "\n\n");
        for(Argument a : arguments) {
            if(a.hasParameter()) {
                System.out.println("--" + a.getName() + "=<parameter>" + "   =>   " + a.getDescription());
            }else {
                System.out.println("--" + a.getName() + "   =>   " + a.getDescription());
            }
        }
    }

    /**
     * Parses all the given arguments
     * @param args
     */
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
