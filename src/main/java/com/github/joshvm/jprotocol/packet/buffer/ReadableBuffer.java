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

public class ReadableBuffer implements Buffer{

    @NonNull private final ByteBuffer buffer;

    public ReadableBuffer(final byte[] bytes){
        buffer = ByteBuffer.wrap(bytes);
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

    public byte[] remaining(){
        final byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes, buffer.position(), bytes.length);
        return bytes;
    }

    public <T> T read(final Type<T> type){
        return type.deserialize(remaining());
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
