package client;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.event.PacketListener;
import common.Connection;
import common.User;

public class JoinListener implements PacketListener<Connection> {

    public void onPacket(final Protocol protocol, final Connection from, final Packet<ReadableBuffer> pkt){
        final User user = pkt.buffer(b -> b.read("user"));
        final boolean includeMsg = pkt.buffer(ReadableBuffer::readBoolean);
        TestClient.add(user);
        if(includeMsg)
            System.out.printf("%s has just joined the chat%n", user.getName());
    }
}
