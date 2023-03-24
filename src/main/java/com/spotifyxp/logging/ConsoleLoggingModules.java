package com.spotifyxp.logging;

import com.spotifyxp.lib.libDetect;
import com.spotifyxp.utils.FileUtils;
import com.spotifyxp.utils.UniversalPath;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConsoleLoggingModules {
    static class ColorMap {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset
        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = RESET;   // WHITE
        // Bold
        public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
        public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
        public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
        public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
        public static final String WHITE_BOLD = "\033[1;37m";  // WHITE
        // Underline
        public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
        public static final String RED_UNDERLINED = "\033[4;31m";    // RED
        public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
        public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
        public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
        public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
        public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
        public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
        // Background
        public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
        public static final String RED_BACKGROUND = "\033[41m";    // RED
        public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
        public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
        public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
        public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
        public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
        public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE
        // High Intensity
        public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
        public static final String RED_BRIGHT = "\033[0;91m";    // RED
        public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
        public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
        public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
        public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
        public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
        public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE
        // Bold High Intensity
        public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
        public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
        public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
        public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
        public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
        public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
        public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
        // High Intensity backgrounds
        public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
        public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
        public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
        public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
        public static void reset() {
            System.out.print(RESET);
        }
    }
    static String n = "";
    static boolean st = true;
    static boolean c = false;
    static boolean i = false;
    static boolean lfe = false;
    static String lfp = "";
    static String ca = "";
    public ConsoleLoggingModules(String name) {
        n = name;
        i = true;
    }
    public static void changeName(String name) {
        n = name;
    }
    public void setColored(boolean colored) {
        c = colored;
        if(colored) {
            n = ColorMap.CYAN + n + ColorMap.RESET;
        }
    }
    public void setLogFileEnabled() {
        if(lfe) {
            return; //Logfile already created
        }
        String path = UniversalPath.getTempDirectory();
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_hh#mm#ss");
        String time = dateFormat.format(date);
        if(libDetect.isWindows()) {
            if (!new File(UniversalPath.getTempDirectory() + "\\libwerwolf").exists()) {
                //noinspection ResultOfMethodCallIgnored
                new File(UniversalPath.getTempDirectory() + "\\libwerwolf").mkdir();
            }
            if(!new File(UniversalPath.getTempDirectory() + "\\libwerwolf\\logs").exists()) {
                //noinspection ResultOfMethodCallIgnored
                new File(UniversalPath.getTempDirectory() + "\\libwerwolf\\logs").mkdir();
            }
        }else{
            if (!new File(UniversalPath.getTempDirectory() + "/libwerwolf").exists()) {
                //noinspection ResultOfMethodCallIgnored
                new File(UniversalPath.getTempDirectory() + "/libwerwolf").mkdir();
            }
            if(!new File(UniversalPath.getTempDirectory() + "/libwerwolf/logs").exists()) {
                //noinspection ResultOfMethodCallIgnored
                new File(UniversalPath.getTempDirectory() + "/libwerwolf/logs").mkdir();
            }
        }
        String additional = "/libwerwolf/logs/log" + time + ".lwlogfile";
        if(libDetect.isWindows()) {
            //noinspection ResultOfMethodCallIgnored
            additional.replace("/", "\\");
        }
        File f = new File(path + additional);
        try {
            //noinspection ResultOfMethodCallIgnored
            f.createNewFile();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        lfe = true;
        lfp = path + "/libwerwolf/logs/log" + time + ".lwlogfile";
    }
    public void setLogFileEnabled(String path) {
        if(lfe) {
            return; //Logfile already created
        }
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_hh#mm#ss");
        String time = dateFormat.format(date);
        File f = new File(path + "/log" + time + ".lwlogfile");
        try {
            //noinspection ResultOfMethodCallIgnored
            f.createNewFile();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        lfe = true;
        lfp = path + "/libwerwolf/log" + time + ".lwlogfile";
    }
    public void setShowTime(boolean showTime) {
        st = showTime;
    }
    static void setLine(int number) {
        ca = n;
        n = n + ":" + number;
    }
    public static void info(String message, Object... object) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        for(String m : message.split("\\{\\}")) {
            try {
                builder.append(m).append(object[counter]);
            }catch (ArrayIndexOutOfBoundsException exc) {
                builder.append(m);
            }
            counter++;
        }
        info(builder.toString());
    }
    public static void info(String message, int linenumber) {
        setLine(linenumber);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        if(i) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [INFO ] " + message);
                }else{
                    System.out.println(n + " [INFO ] " + message);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.BLUE_BOLD + "INFO " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }else{
                    System.out.println(n + " [" + ColorMap.BLUE_BOLD + "INFO " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }
            }
        }
        n = ca;
        log(time + " " + n + " [INFO ] " + message);
    }
    static void log(String tolog) {
        if(lfe) {
            FileUtils.appendToFile(lfp, tolog);
        }
    }
    public static void error(String message, int linenumber) {
        setLine(linenumber);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        if(i) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [ERROR ] " + message);
                }else{
                    System.out.println(n + " [ERROR ] " + message);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.RED + "ERROR " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }else{
                    System.out.println(n + " [" + ColorMap.RED + "ERROR " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }
            }
        }
        n = ca;
        log(time + " " + n + " [ERROR ] " + message);
    }
    public static void error(String message, Object... object) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        for(String m : message.split("\\{\\}")) {
            try {
                builder.append(m).append(object[counter]);
            }catch (ArrayIndexOutOfBoundsException exc) {
                builder.append(m);
            }
            counter++;
        }
        error(builder.toString());
    }
    @SuppressWarnings("SameReturnValue")
    public static boolean isTraceEnabled() {
        return true; //For logger compatibility (Because I replaced Log4J with my logger)
    }
    public static void Throwable(Throwable throwable, int linenumber) {
        setLine(linenumber);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        for(StackTraceElement s : throwable.getStackTrace()) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [THROWABLE ] " + s);
                }else{
                    System.out.println(n + " [THROWABLE ] " + s);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.RED_BOLD + "THROWABLE " + ColorMap.WHITE + "] " + ColorMap.RESET + s);
                }else{
                    System.out.println(n + " [" + ColorMap.RED_BOLD + "THROWABLE " + ColorMap.WHITE + "] " + ColorMap.RESET + s);
                }
            }
            log(time + " " + n + " [THROWABLE ] " + s);
        }
        n = ca;
    }
    public static void warning(String message, int linenumber) {
        setLine(linenumber);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        if(i) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [WARNING ] " + message);
                }else{
                    System.out.println(n + " [WARNING ] " + message);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.YELLOW + "WARNING " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }else{
                    System.out.println(n + " [" + ColorMap.YELLOW + "WARNING " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }
            }
        }
        n = ca;
        log(time + " " + n + " [WARNING ] " + message);
    }
    public static void warning(String message, Object... object) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        for(String m : message.split("\\{\\}")) {
            try {
                builder.append(m).append(object[counter]);
            }catch (ArrayIndexOutOfBoundsException exc) {
                builder.append(m);
            }
            counter++;
        }
        warning(builder.toString());
    }
    public static void debug(String message, int linenumber) {
        setLine(linenumber);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        if(i) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [DEBUG ] " + message);
                }else{
                    System.out.println(n + " [DEBUG ] " + message);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.YELLOW + "DEBUG " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }else{
                    System.out.println(n + " [" + ColorMap.YELLOW + "DEBUG " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }
            }
        }
        n = ca;
        log(time + " " + n + " [DEBUG ] " + message);
    }
    public static void debug(String message, Object... object) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        for(String m : message.split("\\{\\}")) {
            try {
                builder.append(m).append(object[counter]);
            }catch (ArrayIndexOutOfBoundsException exc) {
                builder.append(m);
            }
            counter++;
        }
        debug(builder.toString());
    }
    public static void info(String message) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        if(i) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [INFO ] " + message);
                }else{
                    System.out.println(n + " [INFO ] " + message);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.BLUE_BOLD + "INFO " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }else{
                    System.out.println(n + " [" + ColorMap.BLUE_BOLD + "INFO " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }
            }
        }
        log(time + " " + n + " [INFO ] " + message);
    }
    public static void debug(String message) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        if(i) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [DEBUG ] " + message);
                }else{
                    System.out.println(n + " [DEBUG ] " + message);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.BLUE_BOLD + "DEBUG " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }else{
                    System.out.println(n + " [" + ColorMap.BLUE_BOLD + "DEBUG " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }
            }
        }
        log(time + " " + n + " [DEBUG ] " + message);
    }
    public static void error(String message) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        if(i) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [ERROR ] " + message);
                }else{
                    System.out.println(n + " [ERROR ] " + message);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.RED + "ERROR " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }else{
                    System.out.println(n + " [" + ColorMap.RED + "ERROR " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }
            }
        }
        log(time + " " + n + " [ERROR ] " + message);
    }
    public static void Throwable(Throwable throwable) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        for(StackTraceElement s : throwable.getStackTrace()) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [THROWABLE ] " + s);
                }else{
                    System.out.println(n + " [THROWABLE ] " + s);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.RED_BOLD + "THROWABLE " + ColorMap.WHITE + "] " + ColorMap.RESET + s);
                }else{
                    System.out.println(n + " [" + ColorMap.RED_BOLD + "THROWABLE " + ColorMap.WHITE + "] " + ColorMap.RESET + s);
                }
            }
            log(time + " " + n + " [THROWABLE ] " + s);
        }
    }
    public static void warning(String message) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String time = dateFormat.format(date);
        if(i) {
            if(!c) {
                if(st) {
                    System.out.println(time + " " + n + " [WARNING ] " + message);
                }else{
                    System.out.println(n + " [WARNING ] " + message);
                }
            }else{
                if(st) {
                    System.out.println(ColorMap.BLUE + time + ColorMap.WHITE + " " + n + " [" + ColorMap.YELLOW + "WARNING " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }else{
                    System.out.println(n + " [" + ColorMap.YELLOW + "WARNING " + ColorMap.WHITE + "] " + ColorMap.RESET + message);
                }
            }
        }
        log(time + " " + n + " [WARNING ] " + message);
    }
}
