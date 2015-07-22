package com.github.joshvm.jprotocol.packet;

import com.github.joshvm.jprotocol.PacketDefinition;
import com.github.joshvm.jprotocol.packet.buffer.Buffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * This class represents a single packet associated with a {@link PacketDefinition}.
 * If the {@link Buffer} is a {@link com.github.joshvm.jprotocol.packet.buffer.WritableBuffer} then it is used to send.
 * If the {@link Buffer} is a {@link com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer} then it is used for reading.
 *
 * @param <B> the type of {@link Buffer}, either a {@link com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer} or {@link com.github.joshvm.jprotocol.packet.buffer.WritableBuffer}
 */
@AllArgsConstructor
public class Packet<B extends Buffer> {

    @NonNull @Getter private final PacketDefinition definition;
    @NonNull @Getter private final B buffer;

    public byte[] toByteArray(){
        return buffer.toByteArray();
    }

    public B buffer(){
        return getBuffer();
    }

    public <T> T buffer(final Function<B, T> func){
        return func.apply(buffer);
    }

    public void withBuffer(final Consumer<B> consumer){
        consumer.accept(buffer);
    }
}
