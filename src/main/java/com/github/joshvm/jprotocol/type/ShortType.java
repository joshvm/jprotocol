package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * {@inheritDoc}
 * Represents a 16-bit (2 byte) {@link Short} type
 */
public class ShortType extends Type<Short> {

    public static final String NAME = "short";

    @Getter(lazy=true) private static final ShortType instance = new ShortType();

    public ShortType(){
        super(NAME, 2);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] serializeObject(final Short s){
        return buffer().putShort(s).array();
    }

    /**
     * {@inheritDoc}
     */
    public Short deserializeBuffer(final ByteBuffer buffer){
        return buffer.getShort();
    }
}
