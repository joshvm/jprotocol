package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * {@inheritDoc}
 * Represents a 32-bit (4 byte) {@link Integer} type
 */
public class IntType extends Type<Integer> {

    public static final String NAME = "int";

    @Getter(lazy=true) private static final IntType instance = new IntType();

    public IntType(){
        super(NAME, 4);
    }

    public byte[] serializeObject(final Integer i){
        return buffer().putInt(i).array();
    }

    public Integer deserializeBuffer(final ByteBuffer buffer){
        return buffer.getInt();
    }
}
