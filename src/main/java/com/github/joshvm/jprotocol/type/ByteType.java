package com.github.joshvm.jprotocol.type;

import lombok.Getter;

public class ByteType extends Type<Byte> {

    public static final String NAME = "byte";

    @Getter(lazy=true) private static final ByteType instance = new ByteType();

    public ByteType(){
        super(NAME, 1);
    }

    public byte[] serialize(final Byte b){
        return buffer().put(b).array();
    }

    public Byte deserialize(final byte[] bytes){
        return buffer(bytes).get();
    }
}
