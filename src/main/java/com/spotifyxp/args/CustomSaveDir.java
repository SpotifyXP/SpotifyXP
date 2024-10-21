package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class CustomSaveDir implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return () -> {
            PublicValues.fileslocation = parameter1;
            PublicValues.configfilepath = PublicValues.fileslocation + "/config.json";
            PublicValues.customSaveDir = true;
            PublicValues.appLocation = PublicValues.fileslocation;
        };
    }

    @Override
    public String getName() {
        return "custom-savedir";
    }

    @Override
    public String getDescription() {
        return "Sets save directory (normally AppData) (Paramter z.b 'C://bla' or '/etc/bla')";
    }

    @Override
    public boolean hasParameter() {
        return true;
    }
}