package com.github.joshvm.jprotocol.packet.decoder;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;
import lombok.Getter;

import javax.annotation.Nullable;

/**
 * {@inheritDoc}
 * The default implementation of packet decoding if there is not one provided in the XML.
 */
public class DefaultPacketDecoder implements PacketDecoder {

    @Getter(lazy=true) private static final DefaultPacketDecoder instance = new DefaultPacketDecoder();

    /**
     * {@inheritDoc}
     */
    @Nullable
    public Packet<ReadableBuffer> decode(final PacketDefinition definition, final ReadableBuffer in){
        final int pos = in.position();
        final Object[] objs = new Object[definition.getIn().count()];
        for(int i = 0; i < objs.length; i++){
            objs[i] = in.read(definition.getIn().getType(i));
            if(objs[i] == null){
                in.setPosition(pos);
                return null;
            }
        }
        final WritableBuffer out = new WritableBuffer();
        for(int i = 0; i < objs.length; i++)
            out.write(definition.getIn().getType(i), objs[i]);
        return new Packet<>(definition, new ReadableBuffer(out.toByteArray()));
    }

}
