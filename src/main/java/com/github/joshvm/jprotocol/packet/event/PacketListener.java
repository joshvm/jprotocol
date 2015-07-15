package com.github.joshvm.jprotocol.packet.event;

import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;

public interface PacketListener<T> {

    void onPacket(final T from, final Packet<ReadableBuffer> pkt);
}
