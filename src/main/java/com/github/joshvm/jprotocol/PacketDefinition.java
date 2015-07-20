package com.github.joshvm.jprotocol;

import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.ReadableBuffer;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;
import com.github.joshvm.jprotocol.packet.event.PacketListener;
import com.github.joshvm.jprotocol.type.Type;
import com.github.joshvm.jprotocol.type.Types;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nu.xom.Element;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * This class represents a single packet definition, or structure. Consists of:
 *
 * <ul>
 *     <li>opcode: this is the opcode used to distinguish this packet from the others</li>
 *     <li>in types: the incoming types used to construct a {@link Packet}, (can be empty if this packet definition is a send-only packet)</li>
 *     <li>out types: the outgoing types used to construct a {@link Packet}, (can be empty if this packet definition is a read-only packet)</li>
 * </ul>
 */
@AllArgsConstructor
@ToString(exclude = "protocol")
@EqualsAndHashCode
public class PacketDefinition {

    /**
     * The class used to parse the types from the XML {@link Element}
     */
    private static class TypesParser {

        /**
         *
         * @param element the type XML {@link Element}
         * @param tag the tag (either in or out)
         * @return the collection of {@link Type}s as well as the length
         */
        private static Types parse(final Element element, final String tag){
            final Optional<Element> opt = Optional.ofNullable(element.getFirstChildElement(tag));
            if(!opt.isPresent())
                return new Types(new Type[0]);
            final Type[] types = opt.map(i -> i.getChildElements("type"))
                    .map(t -> {
                        final Type[] array = new Type[t.size()];
                        for(int i = 0; i < t.size(); i++){
                            final Type type = Type.get(t.get(i).getAttributeValue("name"));
                            array[i] = type;
                        }
                        return array;
                    }).orElse(new Type[0]);
            final int length = Optional.ofNullable(opt.get().getAttributeValue("length"))
                    .map(l -> l.replaceAll("var(_|\\s)?byte", Integer.toString(Type.VAR_BYTE))
                            .replaceAll("var(_|\\s)?short", Integer.toString(Type.VAR_SHORT))
                            .replaceAll("var(_|\\s)?int", Integer.toString(Type.VAR_INT)))
                    .map(Integer::parseInt)
                    .orElse(Types.calculateLength(types));
            return new Types(types, length);
        }
    }

    /**
     * This class is used to parse the {@link PacketListener}s from the XML {@link Element}
     */
    private static class ListenersParser{

        /**
         *
         * @param element the XML listeners {@link Element}
         * @return a {@link List} of {@link PacketListener}s parsed from the element
         */
        private static List<PacketListener> parse(final Element element){
            return Optional.ofNullable(element.getFirstChildElement("listeners"))
                    .map(i -> i.getChildElements("listener"))
                    .map(listeners -> {
                        final List<PacketListener> list = new ArrayList<>(listeners.size());
                        for(int i = 0; i < listeners.size(); i++){
                            final String className = listeners.get(i).getAttributeValue("class");
                            try{
                                final Class<PacketListener> clazz = (Class<PacketListener>)Class.forName(className);
                                list.add(clazz.newInstance());
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        return list;
                    }).orElseGet(Collections::emptyList);
        }
    }

    @Getter private final Protocol protocol;
    @Getter private final int opcode;
    @Getter private final Types in;
    @Getter private final Types out;
    @Getter private final List<PacketListener> listeners;

    /**
     *
     * This method is used to construct a ready-to-send {@link Packet} given a variable-length number of {@link Object}s.
     * <b>Important: The order at which you supply arguments into this method must match the order as they are defined in XML.</b>
     * This method is equivalent to {@code protocol.getPacketEncoder().encode(this, args)}
     *
     * @param args the array of {@link Object}s used to create a ready-to-send {@link Packet}
     * @return the ready-to-send {@link Packet} serialized from the provided {@link Object}s
     */
    public Packet<WritableBuffer> out(final Object... args){
        return protocol.getPacketEncoder().encode(this, args);
    }

    /**
     *
     * This method is called by the {@link com.github.joshvm.jprotocol.packet.parser.DefaultPacketParser} as it is trying to deserialize the client input.
     * Equivalent to {@code getProtocol().getPacketDecoder().decode(this, buffer)}
     *
     * @param buffer the {@link ReadableBuffer}
     * @return the deserialized {@link Packet} if there are sufficient bytes in the buffer, otherwise {@code null}
     */
    @Nullable
    public Packet<ReadableBuffer> in(final ReadableBuffer buffer){
        return protocol.getPacketDecoder().decode(this, buffer);
    }

    /**
     *
     * This method is called by the {@link com.github.joshvm.jprotocol.packet.parser.DefaultPacketParser} when it is finished parsing all of the {@link Packet}s from the client
     *
     * @param from the client; something used to distinguish one client from another (ex: with Netty, you would use ChannelHandlerContext)
     * @param pkt the {@link Packet} that has been read from the client
     * @param <T> the generic type representing the sender (client)
     */
    public <T> void fireOnPacket(final T from, final Packet<ReadableBuffer> pkt){
        listeners.forEach(l -> l.onPacket(from, pkt));
    }

    /**
     *
     * @param protocol The {@link Protocol}
     * @param e the XML {@link Element} representing a single {@link PacketDefinition}
     * @return the {@link PacketDefinition}
     */
    protected static PacketDefinition parse(final Protocol protocol, final Element e){
        final int opcode = Integer.parseInt(e.getAttributeValue("opcode"));
        final Types in = TypesParser.parse(e, "in");
        final Types out = TypesParser.parse(e, "out");
        final List<PacketListener> listeners = ListenersParser.parse(e);
        return new PacketDefinition(protocol, opcode, in, out, listeners);
    }
}
