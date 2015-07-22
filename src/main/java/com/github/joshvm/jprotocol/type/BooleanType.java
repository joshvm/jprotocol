package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * {@inheritDoc}
 * Represents a single bit {@link Boolean} type. This is equivalent to the {@link ByteType} in a sense that
 * if the value is {@code true} it uses a 1 otherwise a 0.
 */
public class BooleanType extends Type<Boolean> {

    public static final String NAME = "bool";

    @Getter(lazy=true) private static final BooleanType instance = new BooleanType();

    public BooleanType(){
        super(NAME, 1);
    }

    public byte[] serializeObject(final Boolean b){
        return buffer().put((byte) (b ? 1 : 0)).array();
    }

    public Boolean deserializeBuffer(final ByteBuffer buffer){
        return buffer.get() == 1;
    }
}
