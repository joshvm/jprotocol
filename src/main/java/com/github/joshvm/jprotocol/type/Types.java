package com.github.joshvm.jprotocol.type;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.stream.Stream;

/**
 * This class represents a collection of {@link Type} as well as a length.
 * It is used in a {@link com.github.joshvm.jprotocol.PacketDefinition} to represent the incoming/outcoming data.
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Types {

    @Getter private final Type[] types;
    @Getter private final int length;

    public Types(final Type[] types){
        this(types, calculateLength(types));
    }

    public Type getType(final int idx){
        return types[idx];
    }

    /**
     *
     * @return the number of {@link Type}s
     */
    public int count(){
        return types.length;
    }

    /**
     *
     * This method is called by the protocol parser (from the XML) if there is no length attribute associated
     * with the in/out elements.
     *
     * @param types the {@link Type}s
     * @return the length of the provided types.
     */
    public static int calculateLength(final Type... types){
        return Stream.of(types)
                .mapToInt(Type::getLength)
                .sum();
    }
}
