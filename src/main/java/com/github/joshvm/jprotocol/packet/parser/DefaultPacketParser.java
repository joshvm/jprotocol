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

    private byte[] cache;

    public void parse(final Protocol protocol, final ReadableBuffer in, final List<Packet<ReadableBuffer>> out){
        ReadableBuffer buffer = in;
        if(cache != null)
            buffer = new ReadableBuffer(cacheBytes(in.remainingBytes()));
        while(buffer.hasRemaining()){
            final PacketDefinition definition = protocol.getPacket(buffer.readByte());
            if(definition == null)
                continue;
            final Packet<ReadableBuffer> pkt = definition.in(buffer);
            if(pkt == null){
                cacheBytes(in.remainingBytes());
                return;
            }
            out.add(pkt);
            try{
                Optional.ofNullable(definition.in(buffer)).ifPresent(out::add);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        cache = null;
    }

    private byte[] cacheBytes(final byte[] bytes){
        if(cache == null){
            cache = bytes;
            return null;
        }
        final byte[] newBytes = new byte[cache.length + bytes.length];
        System.arraycopy(cache, 0, newBytes, 0, cache.length);
        System.arraycopy(bytes, 0, newBytes, cache.length, bytes.length);
        return cache = newBytes;
    }

}
