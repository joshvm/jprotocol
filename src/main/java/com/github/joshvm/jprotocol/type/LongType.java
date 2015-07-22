package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * {@inheritDoc}
 * Represents a 64-bit (8 byte) {@link Long} type
 */
public class LongType extends Type<Long> {

    public static final String NAME = "long";

    @Getter(lazy=true) private static final LongType instance = new LongType();

    public LongType(){
        super(NAME, 8);
    }

    public byte[] serializeObject(final Long l){
        return buffer().putLong(l).array();
    }

    public Long deserializeBuffer(final ByteBuffer buffer){
        return buffer.getLong();
    }
}
