package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

public class ShortType extends Type<Short> {

    public static final String NAME = "short";

    @Getter(lazy=true) private static final ShortType instance = new ShortType();

    public ShortType(){
        super(NAME, 2);
    }

    public byte[] serialize(final Short s){
        return buffer().putShort(s).array();
    }

    public Short deserializeBuffer(final ByteBuffer buffer){
        return buffer.getShort();
    }
}
