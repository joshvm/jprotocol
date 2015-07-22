package server;

import com.github.joshvm.jprotocol.Protocol;
import common.Connection;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestServer {

    private static final List<Connection> clients = new ArrayList<>();
    private static Protocol protocol;

    public static void main(String[] args) throws Exception{
        int counter = 0;
        protocol = Protocol.from(new File("src/test/resources/server-protocol.xml"));
        final ServerSocket server = new ServerSocket(7495);
        while(server.isBound()){
            final Socket socket = server.accept();
            System.out.println("socket: " + socket);
            final Connection con = new Connection(protocol, socket, counter++);
            con.setCallback(c -> {
                clients.remove(c);
                sendToAll("leave", c.getUser().getId());
            });
            con.write("init", con.getUser().getId());
            clients.forEach(c -> {
                c.write("join", con.getUser(), true);
                con.write("join", c.getUser(), false);
            });
            clients.add(con);
            con.start();
        }
    }

    public static void sendToAll(final String name, final Object... args){
        clients.forEach(c -> c.write(name, args));
    }
}
