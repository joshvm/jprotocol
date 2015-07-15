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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@ToString(exclude = "protocol")
@EqualsAndHashCode
public class PacketDefinition {

    private static class TypesParser {

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

    private static class ListenersParser{

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

    public Packet<WritableBuffer> out(final Object... args){
        return protocol.getPacketEncoder().encode(this, args);
    }

    public Packet<ReadableBuffer> in(final ReadableBuffer buffer){
        return protocol.getPacketDecoder().decode(this, buffer);
    }

    public <T> void fireOnPacket(final T from, final Packet<ReadableBuffer> pkt){
        listeners.forEach(l -> l.onPacket(from, pkt));
    }

    protected static PacketDefinition parse(final Protocol protocol, final Element e){
        final int opcode = Integer.parseInt(e.getAttributeValue("opcode"));
        final Types in = TypesParser.parse(e, "in");
        final Types out = TypesParser.parse(e, "out");
        final List<PacketListener> listeners = ListenersParser.parse(e);
        return new PacketDefinition(protocol, opcode, in, out, listeners);
    }
}
