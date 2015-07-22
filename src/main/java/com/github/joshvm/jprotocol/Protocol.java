package com.github.joshvm.jprotocol;

import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;
import com.github.joshvm.jprotocol.packet.decoder.DefaultPacketDecoder;
import com.github.joshvm.jprotocol.packet.decoder.PacketDecoder;
import com.github.joshvm.jprotocol.packet.encoder.DefaultPacketEncoder;
import com.github.joshvm.jprotocol.packet.encoder.PacketEncoder;
import com.github.joshvm.jprotocol.packet.parser.DefaultPacketParser;
import com.github.joshvm.jprotocol.packet.parser.PacketParser;
import com.github.joshvm.jprotocol.type.Type;
import lombok.Getter;
import lombok.ToString;
import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A protocol consists of several things:
 *
 * <ul>
 *     <li>
 *         A packet parser: The parser is used to parse the incoming client's read buffer into a collection of {@link com.github.joshvm.jprotocol.packet.Packet} with {@link ReadableBuffer}
 *     </li>
 *     <li>
 *         A packet encoder: The encoder is responsible for taking a variable-length amount of arguments and serializing it (as per structure) to a {@link com.github.joshvm.jprotocol.packet.Packet}s with {@link WritableBuffer}
 *     </li>
 *     <li>
 *         A packet decoder: The decoder is responsible for the deserialization of one {@link PacketDefinition}
 *     </li>
 *     <li>
 *         Packet definitions: A {@link PacketDefinition} is a representation of the underlying structure of a packet.
 *     </li>
 * </ul>
 *
 * Considering a protocol is loaded from XML; a typical structure might look like:
 *
 * <pre>
 *     {@code
 *     <protocol>
 *
 *         <packet-parser class="path.to.packet.parser"/> <!-- Optional (defaults to DefaultPacketParser)-->
 *         <packet-encoder class="path.to.packet.encoder"/> <!-- Optional (defaults to DefaultPacketEncoder)-->
 *         <packet-decoder class="path.to.packet.decoder"/> <!-- Optional (defaults to DefaultPacketDecoder)-->
 *
 *         <types> <!-- if you have custom types in your project (optional) -->
 *             <type class="path.to.my.type"/> <!-- load type from a designated class -->
 *             <type package="path.to.types"/> <!-- load types from a designated package -->
 *         </types>
 *
 *         <!-- available type names: bool, byte, char, short, int, float, long, double, string -->
 *
 *         <packets>
 *             <packet opcode="0"> <!-- ex: login packet -->
 *                <in length="var_byte"> <!-- length should be defined when you have variable-length types -->
 *                    <type name="string"/> <!-- username -->
 *                    <type name="string"/> <!-- password -->
 *                </in>
 *                <out> <!-- length attribute is optional - defaults to the sum of the length of types -->
 *                    <type name="byte"/> <!-- response -->
 *                </out>
 *                <listeners>
 *                    <listener class="path.to.login.listener"/>
 *                </listeners>
 *             </packet>
 *             <packet opcode="1"> <!-- ex: response packet -->
 *                 <out> <!-- in tag not necessary here as this is a send-only packet -->
 *                     <type name="byte"/>
 *                     <type name="byte"/>
 *                 </out>
 *             </packet>
 *         </packets>
 *     </protocol>
 *     }
 * </pre>
 *
 * @see Type
 * @see PacketParser
 * @see DefaultPacketParser
 * @see PacketEncoder
 * @see DefaultPacketEncoder
 * @see PacketDecoder
 * @see DefaultPacketDecoder
 * @see PacketDefinition
 */
@ToString
public class Protocol {

    @Getter private final Map<Integer, PacketDefinition> packets;
    @Getter private final Map<String, PacketDefinition> namedPackets;
    @Getter private final PacketParser packetParser;
    @Getter private final PacketEncoder packetEncoder;
    @Getter private final PacketDecoder packetDecoder;

    /**
     *
     * @param packetParser the {@link PacketParser}
     * @param packetEncoder the {@link PacketEncoder}
     * @param packetDecoder the {@link PacketDecoder}
     */
    protected Protocol(final PacketParser packetParser, final PacketEncoder packetEncoder, final PacketDecoder packetDecoder){
        this.packetParser = packetParser;
        this.packetEncoder = packetEncoder;
        this.packetDecoder = packetDecoder;

        packets = new LinkedHashMap<>();
        namedPackets = new LinkedHashMap<>();
    }

    /**
     *
     * This method maps the packet definition by opcode to the underlying map
     *
     * @param pkt the packet definition to add to this protocol
     */
    public void addPacket(final PacketDefinition pkt){
        packets.put(pkt.getOpcode(), pkt);
        namedPackets.put(pkt.getName(), pkt);
    }

    /**
     *
     * This method removes the packet definition by opcode to the underlying map
     *
     * @param pkt the packet definition to remove from this protocol
     */
    public void removePacket(final PacketDefinition pkt){
        packets.remove(pkt.getOpcode());
        namedPackets.remove(pkt.getName());
    }

    /**
     *
     * This method returns all of the {@link PacketDefinition}s in the same order as they were inserted
     *
     * @return a collection of {@link PacketDefinition}s
     */
    public Collection<PacketDefinition> collectPackets(){
        return packets.values();
    }

    /**
     *
     * @param opcode the opcode of the {@link PacketDefinition}
     * @return the {@link PacketDefinition}
     */
    public PacketDefinition getPacket(final int opcode){
        return packets.get(opcode);
    }

    /**
     *
     * @param name the name of the {@link PacketDefinition}
     * @return the {@link PacketDefinition}
     */
    public PacketDefinition getPacket(final String name){
        return namedPackets.get(name);
    }

    /**
     *
     * This method is equivalent to {@code getPacket(opcode).out(args)}
     *
     * @param opcode the opcode of the {@link PacketDefinition}
     * @param args the variable-length arguments - must be the same length as the number of out types
     * @return the ready to send encoded {@link Packet}
     */
    public Packet<WritableBuffer> out(final int opcode, final Object... args){
        return getPacket(opcode).out(args);
    }

    /**
     *
     * This method is equivalent to {@code getPacket(name).out(args)}
     *
     * @param name the name of the {@link PacketDefinition}
     * @param args the variable-length arguments - must be the same length as the number of out types
     * @return the ready to send encoded {@link Packet}
     */
    public Packet<WritableBuffer> out(final String name, final Object... args){
        return getPacket(name).out(args);
    }

    /**
     *
     * This method is equivalent to {@code getPacket(opcode).in(buffer)}
     *
     * @param opcode the opcode of the {@link PacketDefinition}
     * @param buffer the incoming client buffer
     * @return the {@link Packet} if there are sufficient bytes otherwise {@code null}
     */
    @Nullable
    public Packet<ReadableBuffer> in(final int opcode, final ReadableBuffer buffer){
        return getPacket(opcode).in(buffer);
    }


    /**
     *
     * This method is equivalent to {@code getPacket(name).in(buffer)}
     *
     * @param name the name of the {@link PacketDefinition}
     * @param buffer the incoming client buffer
     * @return the {@link Packet} if there are sufficient bytes otherwise {@code null}
     */
    @Nullable
    public Packet<ReadableBuffer> in(final String name, final ReadableBuffer buffer){
        return getPacket(name).in(buffer);
    }

    /**
     *
     * @param from you should include some unique identifier here to represent the client (ex: with netty, you'd use ChannelHandlerContext)
     * @param bytes the incoming client's read buffer
     * @param <T> the generic type representing the sender (client)
     */
    public <T> void read(final T from, final byte[] bytes){
        packetParser.parse(this, new ReadableBuffer(bytes)).forEach(
                p -> p.getDefinition().fireOnPacket(from, p)
        );
    }

    /**
     *
     * @param is the {@link InputStream} to be parsed
     * @return the {@link Protocol} parsed from the given {@link InputStream}
     * @throws Exception if there is an error parsing the {@link InputStream}
     */
    public static Protocol from(final InputStream is) throws Exception{
        return parse(new Builder().build(is).getRootElement());
    }

    /**
     *
     * @param file the {@link File} to be parsed
     * @return the {@link Protocol} parsed from the given {@link File}
     * @throws Exception if there is an error parsing the {@link File}
     */
    public static Protocol from(final File file) throws Exception{
        return parse(new Builder().build(file).getRootElement());
    }

    /**
     *
     * @param reader the {@link Reader} to be parsed
     * @return the {@link Protocol} parsed from the given {@link Reader}
     * @throws Exception if there an error parsing the {@link Reader}
     */
    public static Protocol from(final Reader reader) throws Exception{
        return parse(new Builder().build(reader).getRootElement());
    }

    /**
     *
     * @param e the {@code protocol} root {@link Element}
     * @return the parsed {@link Protocol}
     */
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
