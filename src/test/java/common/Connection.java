package common;

import com.github.joshvm.jprotocol.Protocol;
import com.github.joshvm.jprotocol.packet.Packet;
import com.github.joshvm.jprotocol.packet.buffer.WritableBuffer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Connection extends Thread{

    public interface Callback{

        void disconnected(final Connection con);
    }

    private final Protocol protocol;

    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    @Getter private final User user;

    @Getter @Setter private Callback callback;

    @SneakyThrows
    public Connection(final Protocol protocol, final Socket socket, final int id){
        this.protocol = protocol;
        this.socket = socket;

        user = new User(id, socket.toString());

        out = new DataOutputStream(socket.getOutputStream());
        out.flush();

        in = new DataInputStream(socket.getInputStream());
    }

    @SneakyThrows
    public void write(final int opcode, final Object... args){
        final Packet<WritableBuffer> pkt = protocol.out(opcode, args);
        out.write(pkt.toByteArray());
        out.flush();
    }

    @SneakyThrows
    public void write(final String name, final Object... args){
        final Packet<WritableBuffer> pkt = protocol.out(name, args);
        out.write(pkt.toByteArray());
        out.flush();
    }

    public void run(){
        while(socket.isConnected()){
            try{
                final byte[] bytes = new byte[in.available()];
                if(bytes.length == 0){
                    Thread.sleep(2000);
                    continue;
                }
                in.readFully(bytes);
                System.out.println("received: " + Arrays.toString(bytes));
                protocol.read(this, bytes);
            }catch(Exception ex){
                ex.printStackTrace();
                break;
            }
        }
        if(callback != null)
            callback.disconnected(this);
    }
}
