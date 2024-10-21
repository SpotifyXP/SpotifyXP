package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class Language implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return () -> PublicValues.language.setNoAutoFindLanguage(parameter1);
    }

    @Override
    public String getName() {
        return "language";
    }

    @Override
    public String getDescription() {
        return "Sets the language to use (Parameter z.b 'de' or 'en')";
    }

    @Override
    public boolean hasParameter() {
        return true;
    }
}