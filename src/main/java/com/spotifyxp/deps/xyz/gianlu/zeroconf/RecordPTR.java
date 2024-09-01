package com.spotifyxp.deps.xyz.gianlu.zeroconf;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class RecordPTR extends Record {
    private String value;

    RecordPTR() {
        super(TYPE_PTR);
    }

    RecordPTR(String name, String value) {
        this();
        setName(name);
        this.value = value;
    }

    /**
     * For queries
     */
    RecordPTR(String name) {
        this();
        setName(name);
    }

    @Override
    protected void readData(int len, ByteBuffer in) {
        value = readName(in);
    }

    @Override
    protected int writeData(ByteBuffer out, Packet packet) {
        return value != null ? writeName(value, out, packet) : -1;
    }

    public String getValue() {
        return value;
    }

    @Override
    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{type:ptr, name:\"");
        sb.append(getName());
        sb.append('\"');
        if (value != null) {
            sb.append(", value:\"");
            sb.append(getValue());
            sb.append('\"');
        }
        sb.append('}');
        return sb.toString();
    }
}

