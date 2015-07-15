package com.github.joshvm.jprotocol.packet.parser;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;

import java.util.ArrayList;
import java.util.List;

public interface PacketParser {

    void parse(final Protocol protocol, final ReadableBuffer in, final List<Packet<ReadableBuffer>> out);

    default List<Packet<ReadableBuffer>> parse(final Protocol protocol, final ReadableBuffer in){
        final List<Packet<ReadableBuffer>> list = new ArrayList<>();
        parse(protocol, in, list);
        return list;
    }
}
