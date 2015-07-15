package com.github.joshvm.jprotocol.packet.parser;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

public class DefaultPacketParser implements PacketParser {

    @Getter(lazy=true) private static final DefaultPacketParser instance = new DefaultPacketParser();

    public void parse(final Protocol protocol, final ReadableBuffer buffer, final List<Packet<ReadableBuffer>> out){
        while(buffer.hasRemaining()){
            final PacketDefinition definition = protocol.getPacket(buffer.readByte());
            if(definition == null)
                continue;
            Optional.ofNullable(definition.in(buffer)).ifPresent(out::add);
        }
    }

}
