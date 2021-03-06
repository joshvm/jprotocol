package server;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.event.PacketListener;
import common.Connection;

public class MessageListener implements PacketListener<Connection> {

    public void onPacket(final Protocol protocol, final Connection from, final Packet<ReadableBuffer> pkt){
        final String msg = pkt.buffer(ReadableBuffer::readString).trim();
        if(msg.isEmpty())
            return;
        TestServer.sendToAll("message", from.getUser().getId(), msg);
    }
}
