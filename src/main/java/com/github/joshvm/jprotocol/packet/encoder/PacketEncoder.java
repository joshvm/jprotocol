package com.github.joshvm.jprotocol.packet.encoder;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;

/**
 * This class is responsible for encoding {@link Packet}s based on a {@link PacketDefinition} and the arguments
 * used for the serialization itself.
 */
public interface PacketEncoder {

    /**
     *
     * @param definition the {@link PacketDefinition}
     * @param args the variable-length number of parameters used to encode the packet, as per definition
     * @return the serialized ready-to-send {@link Packet}
     */
    Packet<WritableBuffer> encode(final PacketDefinition definition, final Object... args);
}
