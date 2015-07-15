package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

public class StringType extends Type<String> {

    public static final String NAME = "string";
    public static final byte TERMINATOR = '\0';

    @Getter(lazy=true) private static final StringType instance = new StringType();

    public StringType(){
        super(NAME, VAR_SHORT);
    }

    public byte[] serialize(final String s){
        final ByteBuffer buffer = buffer();
        s.chars().forEach(
                c -> buffer.put((byte)c)
        );
        buffer.put(TERMINATOR);
        return buffer.array();
    }

    public String deserialize(final byte[] bytes){
        final ByteBuffer buffer = buffer(bytes);
        final StringBuilder bldr = new StringBuilder();
        byte b;
        while((b = buffer.get()) != TERMINATOR)
            bldr.append((char)b);
        return bldr.toString();
    }
}
