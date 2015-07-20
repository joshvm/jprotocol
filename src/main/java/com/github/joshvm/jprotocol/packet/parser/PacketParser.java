package com.github.joshvm.jprotocol.packet.parser;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for parsing the incoming read buffer into a collection of {@link Packet}.
 */
public interface PacketParser {

    /**
     *
     * @param protocol the {@link Protocol}
     * @param in the {@link ReadableBuffer} representing the client/incoming read buffer
     * @param out the {@link List} of {@link Packet}s parsed by the {@code in} buffer
     */
    void parse(final Protocol protocol, final ReadableBuffer in, final List<Packet> out);

    /**
     *
     * This method is a helper method for the {@link PacketParser#parse(Protocol, ReadableBuffer, List)} method
     *
     * @param protocol the {@link Protocol}
     * @param in the {@link ReadableBuffer} representing the client/incoming buffer
     * @return the {@link List} of {@link Packet}s parsed by the {@code in} buffer
     */
    default List<Packet> parse(final Protocol protocol, final ReadableBuffer in){
        final List<Packet> list = new ArrayList<>();
        parse(protocol, in, list);
        return list;
    }
}
