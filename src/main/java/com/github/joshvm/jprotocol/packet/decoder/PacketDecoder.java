package com.github.joshvm.jprotocol.packet.decoder;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;

import javax.annotation.Nullable;

/**
 * This class is responsible for decoding the incoming client buffer into readable {@link Packet} objects.
 * This class is used in conjunction with the {@link com.github.joshvm.jprotocol.packet.parser.PacketParser};
 * The packet parsing is based on the decoder.
 *
 * @see com.github.joshvm.jprotocol.packet.parser.PacketParser
 */
public interface PacketDecoder {

    /**
     *
     * @param definition the {@link PacketDefinition}
     * @param in the client/incoming read buffer
     * @return the decoded {@link Packet} object if there are sufficient bytes, otherwise {@code null}
     */
    @Nullable
    Packet<ReadableBuffer> decode(final PacketDefinition definition, final ReadableBuffer in);

}
