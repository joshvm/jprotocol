package com.github.joshvm.jprotocol.packet.buffer;

import com.github.joshvm.jprotocol.type.BooleanType;
import com.github.joshvm.jprotocol.type.ByteType;
import com.github.joshvm.jprotocol.type.CharType;
import com.github.joshvm.jprotocol.type.DoubleType;
import com.github.joshvm.jprotocol.type.FloatType;
import com.github.joshvm.jprotocol.type.IntType;
import com.github.joshvm.jprotocol.type.LongType;
import com.github.joshvm.jprotocol.type.ShortType;
import com.github.joshvm.jprotocol.type.StringType;
import com.github.joshvm.jprotocol.type.Type;
import lombok.NonNull;

import java.nio.ByteBuffer;

/**
 * This class represents an underlying byte array (a buffer) used to read {@link Type}s (deserialization buffer).
 * This class contains several readXXX() methods designed to deserialize the underlying buffer into defined types.
 */
public class ReadableBuffer implements Buffer{

    @NonNull private final ByteBuffer buffer;

    public ReadableBuffer(final byte[] bytes){
        buffer = ByteBuffer.wrap(bytes);
    }

    public byte[] remainingBytes(){
        final byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    public int remaining(){
        return buffer.remaining();
    }

    public ByteBuffer buffer(){
        return buffer;
    }

    public byte[] toByteArray(){
        return buffer.array();
    }

    public boolean hasRemaining(){
        return buffer.hasRemaining();
    }

    public int position(){
        return buffer.position();
    }

    public void setPosition(final int position){
        buffer.position(position);
    }

    public int size(){
        return buffer.limit();
    }

    public <T> T read(final Type<T> type){
        return type.deserialize(buffer);
    }

    public <T> T read(final String type){
        return read(Type.get(type));
    }

    public boolean readBoolean(){
        return read(BooleanType.NAME);
    }

    public byte readByte(){
        return read(ByteType.NAME);
    }

    public char readChar(){
        return read(CharType.NAME);
    }

    public short readShort(){
        return read(ShortType.NAME);
    }

    public int readInt(){
        return read(IntType.NAME);
    }

    public float readFloat(){
        return read(FloatType.NAME);
    }

    public long readLong(){
        return read(LongType.NAME);
    }

    public double readDouble(){
        return read(DoubleType.NAME);
    }

    public String readString(){
        return read(StringType.NAME);
    }
}
