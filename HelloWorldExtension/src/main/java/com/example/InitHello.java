package com.example;

import com.spotifyxp.injector.Injector;
import com.spotifyxp.injector.InjectorInterface;

import java.util.ArrayList;

public class Initiator implements InjectorInterface {
    @Override
    public String getIdentifier() {
        return "HelloWorldExtension";
    }

    @Override
    public String getVersion() {
        return "v1.0";
    }

    @Override
    public String getAuthor() {
        return "Werwolf2303";
    }


    //Not required
    @Override
    public ArrayList<Injector.Dependency> getDependencies() {
        return new ArrayList<>();
    }
    //-----

    //Example for dependencies
    @Override
    public ArrayList<Injector.Dependency> getDependencies() {
        ArrayList<Injector.Dependency> dependencies = new ArrayList<>();
        Injector.Dependency anImportantDependency = new Injector.Dependency();
        anImportantDependency.identifier = "Dependency Identifier";
        anImportantDependency.version = "Dependency Version";
        anImportantDependency.author = "Dependency Author";
        dependencies.add(anImportantDependency); //Add dependency to extension dependency list
        return dependencies;
    }
    //-----

    @Override
    public void init() {
        System.out.println("Hello World extension");
    }
}

