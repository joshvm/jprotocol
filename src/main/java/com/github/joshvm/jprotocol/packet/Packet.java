package com.github.joshvm.jprotocol.packet;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.buffer.Buffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public class Packet<B extends Buffer> {

    @NonNull @Getter private final PacketDefinition definition;
    @NonNull @Getter private final B buffer;

    public B buffer(){
        return getBuffer();
    }
}
