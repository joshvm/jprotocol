package server;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.event.PacketListener;
import common.Connection;

public class ChangeNameListener implements PacketListener<Connection> {

    public void onPacket(final Protocol protocol, final Connection from, final Packet<ReadableBuffer> pkt){
        final String newName = pkt.buffer(ReadableBuffer::readString).trim();
        if(newName.isEmpty())
            return;
        from.getUser().setName(newName);
        TestServer.sendToAll("change name", from.getUser());
    }
}
