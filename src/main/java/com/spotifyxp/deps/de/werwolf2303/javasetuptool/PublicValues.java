package com.spotifyxp.deps.de.werwolf2303.javasetuptool;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.components.FeatureSelectionComponent;

import javax.swing.*;
import java.util.ArrayList;

public class PublicValues {
    public static enum SetupType {
        TYPICAL,
        COMPLETE,
        CUSTOM
    }
    public static int setup_width = 600;
    public static int setup_height = 400;
    public static int setup_bar_height = 85;
    public static int button_width = 117;
    public static int button_height = 29;
    public static SetupType INTERNALType = SetupType.COMPLETE;
    public static boolean INTERNALBlockNextPrev = false;
    public static Runnable install;
    public static Runnable resetNext;
    public static Runnable changeinstall;
    public static ArrayList<FeatureSelectionComponent.Feature> features; //You can use this after the FeatureSelectionComponent
    public static JPanel INTERNALContentManager;
}
