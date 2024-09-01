package com.spotifyxp.deps.xyz.gianlu.zeroconf;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class RecordSRV extends Record {
    private int priority;
    private int weight;
    private int port;
    private String target;

    RecordSRV() {
        super(TYPE_SRV);
    }

    RecordSRV(String name, String target, int port) {
        this();
        setName(name);
        this.target = target;
        this.port = port;
    }

    @Override
    protected void readData(int len, ByteBuffer in) {
        priority = in.getShort() & 0xFFFF;
        weight = in.getShort() & 0xFFFF;
        port = in.getShort() & 0xFFFF;
        target = readName(in);
    }

    @Override
    protected int writeData(ByteBuffer out, Packet packet) {
        if (target != null) {
            out.putShort((short) priority);
            out.putShort((short) weight);
            out.putShort((short) port);
            return 6 + writeName(target, out, packet);
        } else {
            return -1;
        }
    }

    public int getPriority() {
        return priority;
    }

    public int getWeight() {
        return weight;
    }

    public int getPort() {
        return port;
    }

    public String getTarget() {
        return target;
    }

    @Override
    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{type:srv, name:\"");
        sb.append(getName());
        sb.append('\"');
        if (target != null) {
            sb.append(", priority:");
            sb.append(getPriority());
            sb.append(", weight:");
            sb.append(getWeight());
            sb.append(", port:");
            sb.append(getPort());
            sb.append(", target:\"");
            sb.append(getTarget());
            sb.append('\"');
        }
        sb.append('}');
        return sb.toString();
    }
}

