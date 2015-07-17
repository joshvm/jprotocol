package com.github.joshvm.jprotocol.packet.decoder;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;

import javax.annotation.Nullable;

public interface PacketDecoder {

    @Nullable
    Packet<ReadableBuffer> decode(final PacketDefinition definition, final ReadableBuffer in);

}
