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

    protected ByteBuffer buffer(final int length){
        return Utils.buffer(length);
    }

    protected ByteBuffer buffer(){
        return Utils.buffer(length);
    }

    protected ByteBuffer buffer(final byte[] bytes){
        return Utils.buffer(bytes);
    }

    @Nullable
    protected byte[] readBytes(final ByteBuffer buffer){
        return Utils.readBytes(buffer, length);
    }

    public abstract byte[] serialize(final T obj);

    @Nullable
    public T deserialize(final ByteBuffer buffer){
        return length == UNDEFINED ? deserializeBuffer(buffer) :
                Optional.ofNullable(Utils.readBytes(buffer, length))
                .map(ByteBuffer::wrap)
                .map(this::deserializeBuffer)
                .orElse(null);
    }

    protected abstract T deserializeBuffer(final ByteBuffer buffer);

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

    public static boolean registerClass(final Class<? extends Type> clazz){
        try{
            register(clazz.newInstance());
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean registerClass(final String typeClass){
        try{
            return registerClass((Class<? extends Type>)Class.forName(typeClass));
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static void register(final Type type){
        MAP.put(type.getName(), type);
    }

    public static void unregister(final Type type){
        MAP.remove(type.getName());
    }

    public static <V> Type<V> get(final String name){
        return MAP.get(name);
    }

}
