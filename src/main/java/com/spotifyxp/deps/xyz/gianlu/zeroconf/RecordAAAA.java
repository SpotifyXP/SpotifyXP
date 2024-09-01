package com.spotifyxp.deps.xyz.gianlu.zeroconf;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class RecordAAAA extends Record {
    private byte[] address;

    RecordAAAA() {
        super(TYPE_AAAA);
    }

    RecordAAAA(String name, Inet6Address value) {
        this();
        setName(name);
        setTTL(120);
        this.address = value.getAddress();
    }

    @Override
    protected void readData(int len, ByteBuffer in) {
        address = new byte[len];
        in.get(address);
    }

    @Override
    protected int writeData(ByteBuffer out, Packet packet) {
        if (address != null) {
            out.put(address);
            return address.length;
        } else {
            return -1;
        }
    }

    @Nullable
    public Inet6Address getAddress() {
        try {
            return address == null ? null : (Inet6Address) InetAddress.getByAddress(address);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    @NotNull
    public String toString() {
        return "{type:aaaa, name:\"" + getName() + "\", address:\"" + getAddress() + "\"}";
    }
}

