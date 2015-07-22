package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * {@inheritDoc}
 * Represents a {@code String} type
 */
public class StringType extends Type<String> {

    public static final String NAME = "string";
    public static final byte TERMINATOR = '\0';

    @Getter(lazy=true) private static final StringType instance = new StringType();

    public StringType(){
        super(NAME, UNDEFINED);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] serializeObject(final String s){
        final ByteBuffer buffer = buffer(s.length() + 1);
        s.chars().forEach(c -> buffer.put((byte)c));
        buffer.put(TERMINATOR);
        return buffer.array();
    }

    /**
     * {@inheritDoc}
     */
    public String deserializeBuffer(final ByteBuffer buffer){
        final StringBuilder bldr = new StringBuilder();
        byte b;
        while((b = buffer.get()) != TERMINATOR)
            bldr.append((char)b);
        return bldr.toString();
    }
}
