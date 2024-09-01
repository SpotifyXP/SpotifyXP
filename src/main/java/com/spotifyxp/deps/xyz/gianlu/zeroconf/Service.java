package com.spotifyxp.deps.xyz.gianlu.zeroconf;

import org.jetbrains.annotations.NotNull;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.*;

/**
 * Service represents a Service to be announced by the Zeroconf class.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Service {
    private final String alias;
    private final String service;
    private final int port;
    private final Map<String, String> text;
    private final List<InetAddress> addresses = new ArrayList<>();
    private String domain;
    private String protocol;
    private String host;

    /**
     * Create a new {@link Service} to be announced by this object.
     *
     * A JmDNS `type` field of "_foobar._tcp.local." would be specified here as a `service` param of "foobar".
     *
     * @param alias   the service alias, eg "My Web Server"
     * @param service the service type, eg "http"
     * @param port    the service port
     */
    public Service(@NotNull String alias, @NotNull String service, int port) {
        this.alias = alias;
        for (int i = 0; i < alias.length(); i++) {
            char c = alias.charAt(i);
            if (c < 0x20 || c == 0x7F)
                throw new IllegalArgumentException(alias);
        }

        this.service = service;
        this.port = port;
        this.protocol = "tcp";
        this.text = new LinkedHashMap<>();
    }

    private static void esc(String in, StringBuilder out) {
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (c == '.' || c == '\\') out.append('\\');
            out.append(c);
        }
    }

    @Override
    public String toString() {
        return "Service{" +
                "alias='" + alias + '\'' +
                ", service='" + service + '\'' +
                ", port=" + port +
                ", text=" + text +
                ", addresses=" + addresses +
                ", domain='" + domain + '\'' +
                ", protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                '}';
    }

    /**
     * Set the protocol, which can be one of "tcp" (the default) or "udp"
     *
     * @param protocol the protocol
     * @return this
     */
    @NotNull
    public Service setProtocol(String protocol) {
        if ("tcp".equals(protocol) || "udp".equals(protocol)) this.protocol = protocol;
        else throw new IllegalArgumentException(protocol);
        return this;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Set the domain, which defaults to {@link Zeroconf#getDomain} and must begin with "."
     *
     * @param domain the domain
     * @return this
     */
    @NotNull
    public Service setDomain(String domain) {
        if (domain == null || domain.length() < 2 || domain.charAt(0) != '.')
            throw new IllegalArgumentException(domain);

        this.domain = domain;
        return this;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the host which is hosting this service, which defaults to {@link Zeroconf#getLocalHostName}.
     * It is possible to announce a service on a non-local host
     *
     * @param host the host
     * @return this
     */
    @NotNull
    public Service setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Set the Text record to go with this Service, which is of the form "key1=value1, key2=value2"
     * Any existing Text records are replaced
     *
     * @param text the text
     * @return this
     */
    @NotNull
    public Service setText(String text) {
        this.text.clear();
        String[] q = text.split(", *");
        for (String s : q) {
            String[] r = s.split("=");
            if (r.length == 2) this.text.put(r[0], r[1]);
            else throw new IllegalArgumentException(text);
        }

        return this;
    }

    /**
     * Set the Text record to go with this Service, which is specified as a Map of keys and values
     * Any existing Text records are replaced
     *
     * @param text the text
     * @return this
     */
    @NotNull
    public Service setText(Map<String, String> text) {
        this.text.clear();
        this.text.putAll(text);
        return this;
    }

    /**
     * Add a Text record entry to go with this Service to the existing list of Text record entries.
     *
     * @param key   the text key
     * @param value the corresponding value.
     * @return this
     */
    @NotNull
    public Service putText(String key, String value) {
        this.text.put(key, value);
        return this;
    }

    /**
     * Add an InetAddress to the list of addresses for this service. By default they are taken
     * from {@link Zeroconf#getLocalAddresses}, as the hostname is taken from {@link Zeroconf#getLocalHostName}.
     * If advertising a Service on a non-local host, the addresses must be set manually using this
     * method.
     *
     * @param address the InetAddress this Service resides on
     * @return this
     */
    @NotNull
    public Service addAddress(@NotNull InetAddress address) {
        addresses.add(address);
        return this;
    }

    @NotNull
    public Service addAddresses(Collection<InetAddress> addresses) {
        this.addresses.addAll(addresses);
        return this;
    }

    /**
     * @return whether the service has addresses to announce
     */
    public boolean hasAddresses() {
        return !addresses.isEmpty();
    }

    /**
     * @return the alias
     */
    @NotNull
    public String getAlias() {
        return alias;
    }

    /**
     * Return the instance-name for this service. This is the "fully qualified domain name" of
     * the service and looks something like "My Service._http._tcp.local"
     *
     * @return the instance name
     */
    @NotNull
    public String getInstanceName() {
        StringBuilder sb = new StringBuilder();
        esc(alias, sb);
        sb.append("._");
        esc(service, sb);
        sb.append("._");
        sb.append(protocol);
        sb.append(domain);
        return sb.toString();
    }

    /**
     * Return the service-name for this service. This is the "domain name" of
     * the service and looks something like "._http._tcp.local" - i.e. the InstanceName
     * without the alias. Note the rather ambiguous term "service name" comes from the spec.
     *
     * @return the service name
     */
    @NotNull
    public String getServiceName() {
        StringBuilder sb = new StringBuilder();
        sb.append('_');
        esc(service, sb);
        sb.append("._");
        sb.append(protocol);
        sb.append(domain);
        return sb.toString();
    }

    @NotNull
    Packet getPacket() {
        Packet packet = new Packet();
        packet.setAuthoritative(true);

        String fqdn = getInstanceName();
        String ptrname = getServiceName();

        packet.addAnswer(new RecordPTR(ptrname, fqdn).setTTL(28800));
        packet.addAnswer(new RecordSRV(fqdn, host, port).setTTL(120));
        if (!text.isEmpty()) packet.addAnswer(new RecordTXT(fqdn, text).setTTL(120));

        for (InetAddress address : addresses) {
            if (address instanceof Inet4Address)
                packet.addAnswer(new RecordA(host, (Inet4Address) address));
            else if (address instanceof Inet6Address)
                packet.addAnswer(new RecordAAAA(host, (Inet6Address) address));
        }

        return packet;
    }
}
