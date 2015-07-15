package com.github.joshvm.jprotocol.packet.decoder;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;
import com.github.joshvm.jprotocol.type.Type;
import lombok.Getter;

public class DefaultPacketDecoder implements PacketDecoder {

    @Getter(lazy=true) private static final DefaultPacketDecoder instance = new DefaultPacketDecoder();

    public Packet<ReadableBuffer> decode(final PacketDefinition definition, final ReadableBuffer in){
        final WritableBuffer out = new WritableBuffer();
        for(final Type type : definition.getIn().getTypes())
            out.write(type, in.read(type));
        return new Packet<>(definition, new ReadableBuffer(out.toByteArray()));
    }

}
