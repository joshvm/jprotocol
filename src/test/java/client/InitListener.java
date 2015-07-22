package client;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.event.PacketListener;
import common.Connection;

public class InitListener implements PacketListener<Connection> {

    public void onPacket(final Protocol protocol, final Connection from, final Packet<ReadableBuffer> pkt){
        final int id = pkt.buffer(ReadableBuffer::readInt);
        from.getUser().setId(id);
        TestClient.add(from.getUser());
        System.out.printf("Welcome, your id is now: %d%n", id);
    }
}
