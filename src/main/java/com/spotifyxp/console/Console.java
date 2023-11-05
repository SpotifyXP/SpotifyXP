package com.spotifyxp.console;

import com.spotifyxp.console.commands.Command;
import com.spotifyxp.console.commands.HelpCommand;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;

import java.util.ArrayList;
import java.util.Scanner;

public class Console {
    private static final ArrayList<Command> commands = new ArrayList<>();
    private static final commandLinePrefix prefix = new Console.commandLinePrefix("> ");

    public static class commandLinePrefix {
        private String prefix;
        private final ArrayList<Object> objectsToRemember = new ArrayList<>();


        public commandLinePrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }

        public void addObjectToRemember(Object o) {
            objectsToRemember.add(o);
        }

        public void removeObjectToRemember(Object o) {
            objectsToRemember.remove(o);
        }

        public Object getObjectToRemember(int at) {
            return objectsToRemember.get(at);
        }

        public int getNumberOfObjectsToRemember() {
            return objectsToRemember.size();
        }
    }

    //SpotifyXP ConsoleMode

    public Console() {
        commands.add(new HelpCommand());
    }

    public static void printHelp() {
        System.out.println("SpotifyXP - ConsoleMode");
        for(Command c : commands) {
            if(c.hasParameter()) {
                System.out.println("\n" + c.getName() + "=<parameter>" + "   =>   " + c.getDescription());
            }else {
                System.out.println("\n" + c.getName() + "   =>   " + c.getDescription());
            }
        }
    }

    @SuppressWarnings("all")
    void commandLoop() {
        //In here handle the commands
        System.out.print(prefix.getPrefix());
        Scanner command = new Scanner(System.in);

        if(command.hasNextLine()) {
            String com = command.nextLine();
            boolean found = false;
            for(Command c : commands) {
                if (com.equals(c.getName())) {
                    c.runArgument(com.replace(c.getName() + " ", "")).run();
                    commandLoop();
                    break;
                }
            }
            if(!com.replace(" ", "").isEmpty()) {
                System.out.println("Invalid command: '" + com.split(" ")[0] + "'!");
            }
            commandLoop();
        }
    }



    public void start() {
        commandLoop();
    }
}
