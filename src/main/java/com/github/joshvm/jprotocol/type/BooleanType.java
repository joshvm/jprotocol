package com.github.joshvm.jprotocol.type;

import lombok.Getter;

public class BooleanType extends Type<Boolean> {

    public static final String NAME = "bool";

    @Getter(lazy=true) private static final BooleanType instance = new BooleanType();

    public BooleanType(){
        super(NAME, 1);
    }

    public byte[] serialize(final Boolean b){
        return buffer().put((byte) (b ? 1 : 0)).array();
    }

    public Boolean deserialize(final byte[] bytes){
        return buffer(bytes).get() == 1;
    }
}
