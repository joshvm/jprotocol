package com.github.joshvm.jprotocol.packet.buffer;

import com.github.joshvm.jprotocol.type.BooleanType;
import com.github.joshvm.jprotocol.type.ByteType;
import com.github.joshvm.jprotocol.type.CharType;
import com.github.joshvm.jprotocol.type.FloatType;
import com.github.joshvm.jprotocol.type.IntType;
import com.github.joshvm.jprotocol.type.LongType;
import com.github.joshvm.jprotocol.type.ShortType;
import com.github.joshvm.jprotocol.type.StringType;
import com.github.joshvm.jprotocol.type.Type;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class WritableBuffer implements Buffer{

    private final DataOutputStream dos;
    private final ByteArrayOutputStream baos;

    public WritableBuffer(){
        baos = new ByteArrayOutputStream();

        dos = new DataOutputStream(baos);
    }

    public byte[] toByteArray(){
        return baos.toByteArray();
    }

    @SneakyThrows
    public WritableBuffer write(final byte[] bytes, final int offset, final int length){
        dos.write(bytes, offset, length);
        return this;
    }

    @SneakyThrows
    public WritableBuffer write(final byte[] bytes){
        dos.write(bytes);
        return this;
    }

    public <T> WritableBuffer write(final Type<T> type, final T obj){
        return write(type.serialize(obj));
    }

    public WritableBuffer write(final String type, final Object obj){
        return write(Type.get(type), obj);
    }

    public WritableBuffer writeBoolean(final boolean b){
        return write(BooleanType.NAME, b);
    }

    public WritableBuffer writeByte(final byte b){
        return write(ByteType.NAME, b);
    }

    public WritableBuffer writeByte(final int b){
        return writeByte((byte) b);
    }

    public WritableBuffer writeChar(final char c){
        return write(CharType.NAME, c);
    }

    public WritableBuffer writeShort(final short s){
        return write(ShortType.NAME, s);
    }

    public WritableBuffer writeShort(final int s){
        return writeShort((short) s);
    }

    public WritableBuffer writeInt(final int i){
        return write(IntType.NAME, i);
    }

    public WritableBuffer writeFloat(final float f){
        return write(FloatType.NAME, f);
    }

    public WritableBuffer writeLong(final long l){
        return write(LongType.NAME, l);
    }

    public WritableBuffer writeString(final String s){
        return write(StringType.NAME, s);
    }
}
