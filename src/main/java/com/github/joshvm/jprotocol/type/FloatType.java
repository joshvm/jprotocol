package com.github.joshvm.jprotocol.type;

import lombok.Getter;

import java.nio.ByteBuffer;

public class FloatType extends Type<Float> {

    public static final String NAME = "float";

    @Getter(lazy=true) private static final FloatType instance = new FloatType();

    public FloatType(){
        super(NAME, 4);
    }

    public byte[] serialize(final Float f){
        return buffer().putFloat(f).array();
    }

    public Float deserializeBuffer(final ByteBuffer buffer){
        return buffer.getFloat();
    }
}
