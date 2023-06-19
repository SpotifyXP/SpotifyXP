package com.spotifyxp.report;

import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.JFrame2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BugReport extends JFrame2 {
    //This class will open a BugReport with the option to send the current crash log to GitHub
    JFrame2 frame = this;
    FrameContent frameContent;
    ArrayList<CrashLogDefinition> crashLogs = new ArrayList<>();
    public static class CrashLogDefinition {
        public String date = "";
        public String time = "";
        public String content = "";
        public boolean isNightly = false;
    }
    public BugReport addCrashLog(CrashLogDefinition definition) {
        crashLogs.add(definition);
        return this;
    }
    static class FrameContent extends JPanel {
        public FrameContent() {
            //Init components

        }
    }
    public void openBugReport() {
        frameContent = new FrameContent();
        this.getContentPane().add(frameContent);
        this.setPreferredSize(new Dimension(ContentPanel.frame.getHeight()/2, ContentPanel.frame.getWidth()/2));
        this.open();
        if(crashLogs.size() == 0) {
            this.close();
        }
    }
}
