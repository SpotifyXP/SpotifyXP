package com.spotifyxp.enums;

public enum LookAndFeel {
    Motif("com.sun.java.swing.plaf.motif.MotifLookAndFeel"),
    Windows("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"),
    Acryl("com.jtattoo.plaf.acryl.AcrylLookAndFeel"),
    Aero("com.jtattoo.plaf.aero.AeroLookAndFeel"),
    Aluminium("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"),
    Bernstein("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel"),
    Fast("com.jtattoo.plaf.fast.FastLookAndFeel"),
    Graphite("com.jtattoo.plaf.graphite.GraphiteLookAndFeel"),
    HiFi("com.jtattoo.plaf.hifi.HiFiLookAndFeel"),
    Luna("com.jtattoo.plaf.luna.LunaLookAndFeel"),
    McWin("com.jtattoo.plaf.mcwin.McWinLookAndFeel"),
    Mint("com.jtattoo.plaf.mint.MintLookAndFeel"),
    Noire("com.jtattoo.plaf.noire.NoireLookAndFeel"),
    Smart("com.jtattoo.plaf.smart.SmartLookAndFeel"),
    Texture("com.jtattoo.plaf.texture.TextureLookAndFeel"),
    Metal("javax.swing.plaf.metal.MetalLookAndFeel"),
    Nimbus("javax.swing.plaf.nimbus.NimbusLookAndFeel"),
    WindowsClassic("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"),
    FlatDarculaLaf("com.formdev.flatlaf.Darcula"),
    FlatDarkLaf("com.formdev.flatlaf.Dark"),
    FlatLightLaf("com.formdev.flatlaf.Light"),
    FlatIntelliJLaf("com.formdev.flatlaf.IntelliJ"),
    FlatMacDarkLaf("com.formdev.flatlaf.MacDark"),
    FlatMacLightLaf("com.formdev.flatlaf.MacLight");
    @SuppressWarnings({"NonFinalFieldInEnum", "CanBeFinal"})
    String selected;

    LookAndFeel(String select) {
        selected = select;
    }

    public String getClassName() {
        return selected;
    }
}
