package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;

public class ArchitectureDetection {
    public ArchitectureDetection() {
        String arch = System.getProperty("os.arch");
        if (arch.equals("x86_64")) {
            PublicValues.architecture = Architecture.amd64;
            return;
        }
        PublicValues.architecture = Architecture.valueOf(arch);
    }

    public enum Architecture {
        x86,
        amd64,
        arm,
        arm64
    }
}
