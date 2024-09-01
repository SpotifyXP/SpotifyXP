package com.spotifyxp.deps.xyz.gianlu.zeroconf;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecordTXT extends Record {
    private Map<String, String> values;

    RecordTXT() {
        super(TYPE_TXT);
    }

    RecordTXT(String name, Map<String, String> values) {
        this();
        setName(name);
        this.values = values;
    }

    RecordTXT(String name, String map) {
        this();
        setName(name);
        values = new LinkedHashMap<>();
        String[] q = map.split(", *");
        for (String s : q) {
            String[] kv = s.split("=");
            if (kv.length == 2) values.put(kv[0], kv[1]);
        }
    }

    @Override
    protected void readData(int len, ByteBuffer in) {
        int end = in.position() + len;
        values = new LinkedHashMap<>();
        while (in.position() < end) {
            int slen = in.get() & 0xFF;
            StringBuilder sb = new StringBuilder(slen);
            for (int i = 0; i < slen; i++) sb.append((char) in.get());

            String value = sb.toString();
            int ix = value.indexOf("=");
            if (ix > 0) values.put(value.substring(0, ix), value.substring(ix + 1));
        }
    }

    @Override
    protected int writeData(ByteBuffer out, Packet packet) {
        if (values != null) {
            int len = 0;
            for (Map.Entry<String, String> e : values.entrySet()) {
                String value = e.getKey() + "=" + e.getValue();
                out.put((byte) value.length());
                out.put(value.getBytes(StandardCharsets.UTF_8));
                len += value.length() + 1;
            }
            return len;
        } else {
            return -1;
        }
    }

    public Map<String, String> getValues() {
        return values;
    }

    @Override
    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{type:text, name:\"");
        sb.append(getName());
        sb.append("\"");
        if (values != null) {
            sb.append(", values:");
            sb.append(getValues());
        }
        sb.append('}');
        return sb.toString();
    }
}

