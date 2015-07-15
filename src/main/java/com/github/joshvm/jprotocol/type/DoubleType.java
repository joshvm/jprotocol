package com.github.joshvm.jprotocol.type;

import lombok.Getter;

public class DoubleType extends Type<Double> {

    public static final String NAME = "double";

    @Getter(lazy=true) private static final DoubleType instance = new DoubleType();

    public DoubleType(){
        super(NAME, 8);
    }

    public byte[] serialize(final Double d){
        return buffer().putDouble(d).array();
    }

    public Double deserialize(final byte[] bytes){
        return buffer(bytes).getDouble();
    }
}
