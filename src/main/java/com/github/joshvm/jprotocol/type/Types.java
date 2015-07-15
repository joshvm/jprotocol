package com.github.joshvm.jprotocol.type;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.stream.Stream;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Types {

    @Getter private final Type[] types;
    @Getter private final int length;

    public Types(final Type[] types){
        this(types, calculateLength(types));
    }

    public Type get(final int idx){
        return types[idx];
    }

    public int count(){
        return types.length;
    }

    public static int calculateLength(final Type... types){
        return Stream.of(types)
                .mapToInt(Type::getLength)
                .sum();
    }
}
