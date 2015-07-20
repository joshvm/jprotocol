package com.github.joshvm.jprotocol.packet.buffer;

/**
 * A buffer represents some sort of process that has the ability to provide byte arrays.
 * By default, there are 2 implementations of this:
 * <ul>
 *     <li>{@link ReadableBuffer}: responsible for <b>reading</b> bytes (deserialization)</li>
 *     <li>{@link WritableBuffer}: responsible for <b>writing</b> bytes (serialization)</li>
 * </ul>
 */
public interface Buffer {

    byte[] toByteArray();
}
