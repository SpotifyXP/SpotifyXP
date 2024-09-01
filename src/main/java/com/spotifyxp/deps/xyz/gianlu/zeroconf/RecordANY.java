package com.spotifyxp.deps.xyz.gianlu.zeroconf;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class RecordANY extends Record {

    RecordANY() {
        super(TYPE_ANY);
    }

    RecordANY(String name) {
        this();
        setName(name);
    }

    @Override
    protected void readData(int len, ByteBuffer in) {
        throw new IllegalStateException();
    }

    @Override
    protected int writeData(ByteBuffer out, Packet packet) {
        return -1;
    }

    @Override
    @NotNull
    public String toString() {
        return "{type:any, name:\"" + getName() + "\"}";
    }
}

