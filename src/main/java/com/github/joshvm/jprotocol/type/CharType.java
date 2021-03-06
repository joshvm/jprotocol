package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

/**
 * {@inheritDoc}
 * Represents a 16-bit (2 byte) {@link Character} type
 */
public class CharType extends Type<Character> {

    public static final String NAME = "char";

    @Getter(lazy=true) private static final CharType instance = new CharType();

    public CharType(){
        super(NAME, 2);
    }

    public byte[] serializeObject(final Character c){
        return buffer().putChar(c).array();
    }

    public Character deserializeBuffer(final ByteBuffer buffer){
        return buffer.getChar();
    }
}
