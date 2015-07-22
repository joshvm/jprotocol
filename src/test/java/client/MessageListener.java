package client;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.event.PacketListener;
import common.Connection;
import common.User;

public class MessageListener implements PacketListener<Connection> {

    public void onPacket(final Protocol protocol, final Connection from, final Packet<ReadableBuffer> pkt){
        final int fromId = pkt.buffer(ReadableBuffer::readInt);
        System.out.println("fromId: " + fromId);
        final String msg = pkt.buffer(ReadableBuffer::readString);
        final User fromUser = TestClient.user(fromId);
        System.out.println(fromUser);
        if(fromUser == null)
            return;
        System.out.printf("[%s] %s%n", fromUser.getName(), msg);
    }
}
