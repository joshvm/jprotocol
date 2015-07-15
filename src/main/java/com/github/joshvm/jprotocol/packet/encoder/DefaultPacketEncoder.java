package com.github.joshvm.jprotocol.packet.encoder;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;
import com.github.joshvm.jprotocol.type.Type;
import lombok.Getter;

public class DefaultPacketEncoder implements PacketEncoder{

    @Getter(lazy=true) private static final DefaultPacketEncoder instance = new DefaultPacketEncoder();

    public Packet<WritableBuffer> encode(final PacketDefinition definition, final Object... args){
        if(args.length != definition.getOut().count())
            throw new IllegalArgumentException(String.format("Expecting %d args. Received %d", definition.getOut().count(), args.length));
        final WritableBuffer payload = new WritableBuffer();
        for(int i = 0; i < args.length; i++)
            payload.write(definition.getOut().get(i).serialize(args[i]));
        final byte[] payloadBytes = payload.toByteArray();
        final WritableBuffer buffer = new WritableBuffer();
        buffer.writeByte(definition.getOpcode());
        switch(definition.getOut().getLength()){
            case Type.VAR_BYTE:
                buffer.writeByte(payloadBytes.length);
                break;
            case Type.VAR_SHORT:
                buffer.writeShort(payloadBytes.length);
                break;
            case Type.VAR_INT:
                buffer.writeInt(payloadBytes.length);
                break;
        }
        buffer.write(payloadBytes);
        return new Packet<>(definition, buffer);
    }
}
