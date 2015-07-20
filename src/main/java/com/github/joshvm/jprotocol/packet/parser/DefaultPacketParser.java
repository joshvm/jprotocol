package com.github.joshvm.jprotocol.packet.parser;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * {@inheritDoc}
 * The default implementation of the packet parser that will be used if one is not provided in the XML.
 */
public class DefaultPacketParser implements PacketParser {

    @Getter(lazy=true) private static final DefaultPacketParser instance = new DefaultPacketParser();

    /**
     * {@inheritDoc}
     * The default implementation for how packet parsing
     */
    public void parse(final Protocol protocol, final ReadableBuffer buffer, final List<com.github.joshvm.jprotocol.packet.Packet> out){
        while(buffer.hasRemaining()){
            final PacketDefinition definition = protocol.getPacket(buffer.readByte());
            if(definition == null)
                continue;
            try{
                Optional.ofNullable(definition.in(buffer)).ifPresent(out::add);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
