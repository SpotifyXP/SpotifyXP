package com.example;

import com.spotifyxp.injector.InjectorInterface;

public class InitHello implements InjectorInterface {
    @Override
    public String getIdentifier() {
        //Return name of extension
        return "HelloWorldExtension";
    }

    @Override
    public String getVersion() {
        //Return version of extension
        return "v1.0";
    }

    @Override
    public void init() {
        //Java Code to execute
        System.out.println("Hello World from Extension");
    }
}
