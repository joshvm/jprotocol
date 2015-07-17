package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

public class CharType extends Type<Character> {

    public static final String NAME = "char";

    @Getter(lazy=true) private static final CharType instance = new CharType();

    public CharType(){
        super(NAME, 2);
    }

    public byte[] serialize(final Character c){
        return buffer().putChar(c).array();
    }

    public Character deserializeBuffer(final ByteBuffer buffer){
        return buffer.getChar();
    }
}
