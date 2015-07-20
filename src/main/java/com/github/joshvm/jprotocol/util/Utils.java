package com.github.joshvm.jprotocol.util;

import com.github.joshvm.jprotocol.type.Type;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

/**
 * This class contains several utility-type methods for handling operations revolving around buffers.
 */
public final class Utils {

    private Utils(){}

    /**
     *
     * @param length the number of bytes to allocate; the capacity
     * @return the <code>length</code>-sized {@link ByteBuffer}
     */
    public static ByteBuffer buffer(final int length){
        return ByteBuffer.allocate(length);
    }

    /**
     *
     * This method is shorthand for <code>buffer(type.getLength())</code>
     *
     * @param type the {@link Type}
     * @return the wrapped {@link ByteBuffer}
     */
    public static ByteBuffer buffer(final Type type){
        return buffer(type.getLength());
    }

    /**
     * @param bytes the byte array
     * @return the wrapped {@link ByteBuffer}
     */
    public static ByteBuffer buffer(final byte[] bytes){
        return ByteBuffer.wrap(bytes);
    }

    /**
     *
     * This method attempts to read <code>length</code> from the provided <code>buffer</code>.
     * This method can return <code>null</code> if there are not enough bytes in the <code>buffer</code>.
     *
     * @param buffer the {@link ByteBuffer} to be read <code>length</code> bytes from
     * @param length the number of bytes to read
     * @return a byte array that is <code>length</code>-sized, or <code>null</code> if there are insufficient bytes
     */
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
