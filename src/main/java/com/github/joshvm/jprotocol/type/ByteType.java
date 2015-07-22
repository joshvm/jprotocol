package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * {@inheritDoc}
 * Represents a 8-bit (1 byte) {@link Byte} type
 */
public class ByteType extends Type<Byte> {

    public static final String NAME = "byte";

    @Getter(lazy=true) private static final ByteType instance = new ByteType();

    public ByteType(){
        super(NAME, 1);
    }

    public byte[] serializeObject(final Byte b){
        return buffer().put(b).array();
    }

    public Byte deserializeBuffer(final ByteBuffer buffer){
        return buffer.get();
    }
}
