package com.spotifyxp.utils;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.XMLPropertyListWriter;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MacOSAppUtil {
    String appName;
    String copyname;
    int copyyear;
    String executableLocation;
    String applicationsfolderpath = System.getProperty("user.home") + "/Applications";
    String iconpath;
    //
    // Hello.app
    //  -> Contents
    //     -> Info.plist (Executable Path, Name, Version etc...)
    //     -> MacOS
    //        -> Executable Location (same as app name)
    //     -> Resources
    //        -> AppIcon.icns
    //
    public MacOSAppUtil(String name) {
        appName = name;
    }

    public void setExecutableLocation(String location) {
        executableLocation = location;
    }

    public void setIcon(String location) {
        iconpath = location;
    }

    void internalCreate(String dp) {
        File contents = new File(dp, "Contents");
        if(contents.mkdir()) {
            //Create plist
            NSDictionary root = new NSDictionary();
            root.put("CFBundleExecutable", appName);
            root.put("CFBundleIconFile", "AppIcon.icns");
            root.put("CFBundleInfoDictionaryVersion", "1.0");
            root.put("CFBundlePackageType", "APPL");
            root.put("CDBundleSignature", "???");
            root.put("CFBundleVersion", "1.0");
            try {
                XMLPropertyListWriter.write(root, new File(dp + "/Contents", "Info.plist"));
            } catch (IOException e) {
                ConsoleLogging.Throwable(e);
                ExceptionDialog.open(e);
            }
            File macos = new File(dp + "/Contents", "MacOS");
            if(macos.mkdir()) {
                try {
                    File execfile = new File(macos, appName);
                    execfile.setExecutable(true, false);
                    execfile.setReadable(true, false);
                    execfile.setWritable(true, false);
                    Files.copy(IOUtils.toInputStream("#!/bin/zsh\njava -jar " + executableLocation + " --setup-complete", Charset.defaultCharset()), execfile.toPath());
                    new ProcessBuilder().command("/bin/sh", "-c", "chmod +x \"" + applicationsfolderpath + "/" + appName + ".app/Contents/MacOS/" + appName + "\"").inheritIO().start();
                }catch (IOException e) {
                    ConsoleLogging.Throwable(e);
                    ExceptionDialog.open(e);
                }
            }
            File resources = new File(dp + "/Contents", "Resources");
            if(resources.mkdir()) {
                try {
                    Files.copy(new Resources().readToInputStream(iconpath), new File(dp + "/Contents/Resources", "AppIcon.icns").toPath());
                } catch (IOException e) {
                    ExceptionDialog.open(e);
                    ConsoleLogging.Throwable(e);
                }
            }
        }
    }

    public void setCopyright(int year, String name) {
        copyname = name;
        copyyear = year;
    }

    public void create() {
        File app = new File(applicationsfolderpath, appName + ".app");
        if(!app.exists()) {
            if(app.mkdir()) {
                internalCreate(applicationsfolderpath + "/" + appName + ".app");
            }
        }
    }
}
