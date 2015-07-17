package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

public class LongType extends Type<Long> {

    public static final String NAME = "long";

    @Getter(lazy=true) private static final LongType instance = new LongType();

    public LongType(){
        super(NAME, 8);
    }

    public byte[] serialize(final Long l){
        return buffer().putLong(l).array();
    }

    public Long deserializeBuffer(final ByteBuffer buffer){
        return buffer.getLong();
    }
}
