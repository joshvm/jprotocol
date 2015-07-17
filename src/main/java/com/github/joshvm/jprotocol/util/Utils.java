package com.github.joshvm.jprotocol.util;

import com.github.joshvm.jprotocol.type.Type;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

public final class Utils {

    private Utils(){}

    public static ByteBuffer buffer(final int length){
        return ByteBuffer.allocate(length);
    }

    public static ByteBuffer buffer(final Type type){
        return buffer(type.getLength());
    }

    public static ByteBuffer buffer(final byte[] bytes){
        return ByteBuffer.wrap(bytes);
    }

    @Nullable
    public static byte[] readBytes(final ByteBuffer buffer, final int length){
        if(length == 0)
            return new byte[0];
        final int pos = buffer.position();
        int newLength = length;
        switch(length){
            case Type.VAR_BYTE:
                if(buffer.remaining() >= 1)
                    newLength = buffer.get();
                break;
            case Type.VAR_SHORT:
                if(buffer.remaining() >= 2)
                    newLength = buffer.getShort();
                break;
            case Type.VAR_INT:
                if(buffer.remaining() >= 4)
                    newLength = buffer.getInt();
                break;
        }
        if(newLength < 0 || buffer.remaining() < newLength){
            buffer.position(pos);
            return null;
        }
        final byte[] result = new byte[newLength];
        buffer.get(result);
        return result;
    }

}
