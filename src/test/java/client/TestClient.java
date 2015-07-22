package client;

import com.github.joshvm.jprotocol.Protocol;
import common.Connection;
import common.User;
import lombok.Getter;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestClient {

    @Getter private static Connection connection;
    private static Map<Integer, User> users = new HashMap<>();

    public static void main(String[] args) throws Exception{
        final Protocol protocol = Protocol.from(new File("src/test/resources/client-protocol.xml"));
        final Socket sock = new Socket("localhost", 7495);
        connection = new Connection(protocol, sock, 0);
        connection.setCallback(c -> System.exit(0));
        connection.start();
        while(sock.isConnected()){
            try(final Scanner input = new Scanner(System.in)){
                while(input.hasNextLine()){
                    final String msg = input.nextLine();
                    connection.write("message", msg);
                }
            }
        }
    }

    public static User user(final int id){
        return users.get(id);
    }

    public static void add(final User user){
        users.put(user.getId(), user);
    }

    public static User remove(final int id){
        return users.remove(id);
    }
}
