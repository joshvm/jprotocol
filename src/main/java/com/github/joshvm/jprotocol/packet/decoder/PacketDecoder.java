package com.github.joshvm.jprotocol.packet.decoder;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;

public interface PacketDecoder {

    Packet<ReadableBuffer> decode(final PacketDefinition definition, final ReadableBuffer in);
}
