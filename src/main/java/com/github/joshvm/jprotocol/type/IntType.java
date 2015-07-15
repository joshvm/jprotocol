package com.github.joshvm.jprotocol.type;

import lombok.Getter;

public class IntType extends Type<Integer> {

    public static final String NAME = "int";

    @Getter(lazy=true) private static final IntType instance = new IntType();

    public IntType(){
        super(NAME, 4);
    }

    public byte[] serialize(final Integer i){
        return buffer().putInt(i).array();
    }

    public Integer deserialize(final byte[] bytes){
        return buffer(bytes).getInt();
    }
}
