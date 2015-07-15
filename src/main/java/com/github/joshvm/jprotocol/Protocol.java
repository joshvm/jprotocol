package com.github.joshvm.jprotocol;

import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;
import com.github.joshvm.jprotocol.packet.encoder.DefaultPacketEncoder;
import com.github.joshvm.jprotocol.packet.encoder.PacketEncoder;
import com.github.joshvm.jprotocol.packet.parser.DefaultPacketParser;
import com.github.joshvm.jprotocol.packet.parser.PacketParser;
import com.github.joshvm.jprotocol.packet.decoder.DefaultPacketDecoder;
import com.github.joshvm.jprotocol.packet.decoder.PacketDecoder;
import com.github.joshvm.jprotocol.type.Type;
import lombok.Getter;
import lombok.ToString;
import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@ToString
public class Protocol {

    @Getter private final Map<Integer, PacketDefinition> packets;
    @Getter private final PacketParser packetParser;
    @Getter private final PacketEncoder packetEncoder;
    @Getter private final PacketDecoder packetDecoder;

    protected Protocol(final PacketParser packetParser, final PacketEncoder packetEncoder, final PacketDecoder packetDecoder){
        this.packetParser = packetParser;
        this.packetEncoder = packetEncoder;
        this.packetDecoder = packetDecoder;

        packets = new LinkedHashMap<>();
    }

    public void addPacket(final PacketDefinition pkt){
        packets.put(pkt.getOpcode(), pkt);
    }

    public void removePacket(final PacketDefinition pkt){
        packets.remove(pkt.getOpcode());
    }

    public Collection<PacketDefinition> listPackets(){
        return packets.values();
    }

    public PacketDefinition getPacket(final int opcode, final PacketDefinition defPkt){
        return packets.getOrDefault(opcode, defPkt);
    }

    public PacketDefinition getPacket(final int opcode){
        return packets.get(opcode);
    }

    public Packet<WritableBuffer> out(final int opcode, final Object... args){
        return getPacket(opcode).out(args);
    }

    public Packet<ReadableBuffer> in(final int opcode, final ReadableBuffer buffer){
        return getPacket(opcode).in(buffer);
    }

    public <T> void read(final T from, final byte[] bytes){
        packetParser.parse(this, new ReadableBuffer(bytes)).forEach(
                p -> p.getDefinition().fireOnPacket(from, p)
        );
    }

    public static Protocol from(final InputStream is) throws Exception{
        return parse(new Builder().build(is).getRootElement());
    }

    public static Protocol from(final File file) throws Exception{
        return parse(new Builder().build(file).getRootElement());
    }

    public static Protocol from(final Reader reader) throws Exception{
        return parse(new Builder().build(reader).getRootElement());
    }

    private static Protocol parse(final Element e){
        Optional.ofNullable(e.getFirstChildElement("types"))
                .map(t -> t.getChildElements("type"))
                .ifPresent(types -> {
                    for(int i = 0; i < types.size(); i++){
                        final Element type = types.get(i);
                        Optional.ofNullable(type.getAttributeValue("class"))
                                .ifPresent(Type::registerClass);
                        Optional.ofNullable(type.getAttributeValue("package"))
                                .ifPresent(Type::registerPackage);
                    }
                });
        final PacketParser parser = Optional.ofNullable(e.getFirstChildElement("packet-parser"))
                .map(pse -> {
                    final String className = pse.getAttributeValue("class");
                    try{
                        final Class<PacketParser> clazz = (Class<PacketParser>)Class.forName(className);
                        return clazz.newInstance();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        return null;
                    }
                })
                .orElse(DefaultPacketParser.getInstance());
        final PacketEncoder encoder = Optional.ofNullable(e.getFirstChildElement("packet-encoder"))
                .map(pse -> {
                    final String className = pse.getAttributeValue("class");
                    try{
                        final Class<PacketEncoder> clazz = (Class<PacketEncoder>)Class.forName(className);
                        return clazz.newInstance();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        return null;
                    }
                })
                .orElse(DefaultPacketEncoder.getInstance());
        final PacketDecoder decoder = Optional.ofNullable(e.getFirstChildElement("packet-decoder"))
                .map(pse -> {
                    final String className = pse.getAttributeValue("class");
                    try{
                        final Class<PacketDecoder> clazz = (Class<PacketDecoder>)Class.forName(className);
                        return clazz.newInstance();
                    }catch(Exception ex){
                        ex.printStackTrace();
                        return null;
                    }
                })
                .orElse(DefaultPacketDecoder.getInstance());
        final Protocol protocol = new Protocol(parser, encoder, decoder);
        final Elements packets = e.getFirstChildElement("packets").getChildElements("packet");
        for(int i = 0; i < packets.size(); i++){
            final PacketDefinition pkt = PacketDefinition.parse(protocol, packets.get(i));
            protocol.addPacket(pkt);
        }
        return protocol;
    }
}
