package com.spotifyxp.deps.xyz.gianlu.zeroconf;

import org.jetbrains.annotations.NotNull;

/**
 * An interface that will be notified of a packet transmission
 *
 * @see Zeroconf#addReceiveListener
 * @see Zeroconf#addSendListener
 */
public interface PacketListener {
    void packetEvent(@NotNull Packet packet);
}
