package com.github.joshvm.jprotocol.packet.encoder;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;

public interface PacketEncoder {

    Packet<WritableBuffer> encode(final PacketDefinition definition, final Object... args);
}
