package client;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.event.PacketListener;
import common.Connection;
import common.User;

public class LeaveListener implements PacketListener<Connection> {

    public void onPacket(final Protocol protocol, final Connection from, final Packet<ReadableBuffer> pkt){
        final int id = pkt.buffer(ReadableBuffer::readInt);
        final User user = TestClient.remove(id);
        if(user == null)
            return;
        System.out.printf("%s has just left the chat%n", user.getName());
    }
}
