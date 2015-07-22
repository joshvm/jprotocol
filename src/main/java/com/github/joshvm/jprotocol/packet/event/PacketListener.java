package com.github.joshvm.jprotocol.packet.event;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;

/**
 *
 * This is the listener class used to handle incoming {@link Packet}s
 *
 * @param <T> the type representing the client (or who the packet is from)
 */
public interface PacketListener<T> {

    void onPacket(final Protocol protocol, final T from, final Packet<ReadableBuffer> pkt);
}
