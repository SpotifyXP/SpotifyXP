package com.spotifyxp.deps.xyz.gianlu.zeroconf;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author devgianlu
 */
public final class DiscoveredService {
    public final String target;
    public final int port;
    public final String name;
    public final String service;
    public final String protocol;
    public final String domain;
    public final String serviceName;
    private final long expiration;
    private final List<Record> relatedRecords = new ArrayList<>(5);

    DiscoveredService(@NotNull RecordSRV record) {
        expiration = System.currentTimeMillis() + record.ttl * 1000L;

        target = record.getTarget();
        port = record.getPort();
        serviceName = record.getName();

        String[] split = serviceName.split("\\.");
        if (split.length != 4) throw new IllegalArgumentException("Invalid service name: " + record.getName());

        name = split[0];
        service = split[1];
        protocol = split[2];
        domain = "." + split[3];
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiration;
    }

    void addRelatedRecord(@NotNull Record record) {
        relatedRecords.removeIf(Record::isExpired);
        relatedRecords.add(record);
    }

    @NotNull
    public List<Record> getRelatedRecords() {
        return Collections.unmodifiableList(relatedRecords);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscoveredService that = (DiscoveredService) o;
        if (port != that.port) return false;
        if (!target.equals(that.target)) return false;
        if (!name.equals(that.name)) return false;
        if (!service.equals(that.service)) return false;
        if (!protocol.equals(that.protocol)) return false;
        return domain.equals(that.domain);
    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        result = 31 * result + port;
        result = 31 * result + name.hashCode();
        result = 31 * result + service.hashCode();
        result = 31 * result + protocol.hashCode();
        result = 31 * result + domain.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DiscoveredService{" +
                "target='" + target + '\'' +
                ", port=" + port +
                ", name='" + name + '\'' +
                ", service='" + service + '\'' +
                ", protocol='" + protocol + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
