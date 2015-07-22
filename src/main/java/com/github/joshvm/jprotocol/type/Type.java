package com.github.joshvm.jprotocol.type;

import com.github.joshvm.jprotocol.util.Utils;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * This class represents a single data type that a {@link com.github.joshvm.jprotocol.PacketDefinition} can consist of.
 * It is also a representation as a type that can be serialized and deserialized.
 * Serialization refers to the process of converting an object of some generic type to a byte array.
 * Deserialization refers to the process of converting a byte array to an object of some generic type.
 *
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class Type<T> {

    public static final int UNDEFINED = -10;
    public static final int VAR_BYTE = -1;
    public static final int VAR_SHORT = -2;
    public static final int VAR_INT = -4;

    private static final Map<String, Type> MAP = new HashMap<>();

    static{
        registerPackage(Type.class.getPackage().getName());
    }

    @Getter @NonNull private final String name;
    @Getter private final int length;

    /**
     *
     * This method is exactly the same as {@code Utils.buffer(length)}
     *
     * @param length the length (or capacity) of the {@link ByteBuffer}
     * @return the {@link ByteBuffer} that has a capacity of {@code length}
     */
    protected ByteBuffer buffer(final int length){
        return Utils.buffer(length);
    }

    /**
     *
     * This method is exactly the same as {@code buffer(getLength())} or {@code Utils.buffer(length)}
     *
     * @return the {@link ByteBuffer} that has the capacity of the length
     */
    protected ByteBuffer buffer(){
        return Utils.buffer(length);
    }

    /**
     *
     * This method is exactly the same as {@code Utils.buffer(bytes)}
     *
     * @param bytes the byte array
     * @return the wrapped {@link ByteBuffer} that is wrapping the provided bytes
     */
    protected ByteBuffer buffer(final byte[] bytes){
        return Utils.buffer(bytes);
    }

    /**
     *
     * This method is shorthand for {@code Utils.readBytes(buffer, getLength())}.
     * This method can return {@code null} if there are insufficient bytes in the buffer
     *
     * @param buffer the {@link ByteBuffer} to be read from
     * @return the byte array if there are enough bytes, {@code null} if there are not
     */
    @Nullable
    protected byte[] readBytes(final ByteBuffer buffer){
        return Utils.readBytes(buffer, length);
    }

    /**
     *
     * This method can return {@code null} if there are insufficient bytes in the buffer.
     *
     * @param buffer the {@link ByteBuffer}
     * @return the deserialized object if there is enough bytes in the buffer, otherwise {@code null}
     */
    @Nullable
    public T deserialize(final ByteBuffer buffer){
        return length == UNDEFINED ? deserializeBuffer(buffer) :
                Optional.ofNullable(Utils.readBytes(buffer, length))
                .map(ByteBuffer::wrap)
                .map(this::deserializeBuffer)
                .orElse(null);
    }

    /**
     *
     * This method differs from {@link Type#serializeObject(Object)} because this method includes the variable lengths (if one is available).
     *
     * @param obj the object to be serialized
     * @return the serialized bytes
     */
    public byte[] serialize(final T obj){
        final byte[] bytes = serializeObject(obj);
        if(length >= 0 || length == UNDEFINED)
            return bytes;
        final ByteBuffer buffer = Utils.buffer(Math.abs(length) + bytes.length);
        switch(length){
            case Type.VAR_BYTE:
                buffer.put((byte)bytes.length);
                break;
            case Type.VAR_SHORT:
                buffer.putShort((short)bytes.length);
                break;
            case Type.VAR_INT:
                buffer.putInt(bytes.length);
                break;
        }
        buffer.put(bytes);
        return buffer.array();
    }

    /**
     *
     * This method shouldn't return null considering that this method is called with a {@link ByteBuffer} that
     * has enough bytes to deserialize the object
     *
     * @param buffer the {@link ByteBuffer}
     * @return the object serialized from the buffer
     */
    public abstract T deserializeBuffer(final ByteBuffer buffer);

    /**
     *
     * This method is called by {@link Type#serialize(Object)} - this method represents only the payload.
     * The {@link Type#serialize(Object)} method includes the length as well.
     *
     * @param obj the object to be serialized
     * @return the serialized byte array
     */
    public abstract byte[] serializeObject(final T obj);

    /**
     *
     * @param packageName the name of the package containing the {@link Type}s
     * @return the number of types registered from the given package
     */
    public static int registerPackage(final String packageName){
        return new Reflections(ClasspathHelper.forPackage(packageName)).getSubTypesOf(Type.class).stream()
                .mapToInt(c -> {
                    try{
                        return registerClass(c) ? 1 : 0;
                    }catch(Exception ex){
                        return 0;
                    }
                })
                .sum();
    }

    /**
     *
     * @param clazz the {@link Type} class
     * @return {@code true} if the class was successfully registered, {@code false} otherwise
     */
    public static boolean registerClass(final Class<? extends Type> clazz){
        try{
            register(clazz.newInstance());
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * This method is equivalent to {@code registerClass(Class.forName(typeClass))}
     *
     * @param typeClass the fully qualified name of the {@link Type} class
     * @return {@code true} if the class was successfully registered, {@code false} otherwise
     */
    public static boolean registerClass(final String typeClass){
        try{
            return registerClass((Class<? extends Type>)Class.forName(typeClass));
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * This method maps the {@link Type} to an underlying {@link Map} by the name.
     * This means that if 2 types have the same name, the type that was registered last will be the one used.
     *
     * @param type the {@link Type} to be added
     */
    public static void register(final Type type){
        MAP.put(type.getName(), type);
    }

    /**
     *
     * @param type the {@link Type} to be removed
     */
    public static void unregister(final Type type){
        MAP.remove(type.getName());
    }

    /**
     *
     * @param name the name of the {@link Type}
     * @param <V> generic type
     * @return the {@link Type} that matches the provided name, {@code null} otherwise
     */
    @Nullable
    public static <V> Type<V> get(final String name){
        return MAP.get(name);
    }

}
