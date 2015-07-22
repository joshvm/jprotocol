package common;

import com.github.joshvm.jprotocol.type.StringType;
import com.github.joshvm.jprotocol.type.Type;

import java.nio.ByteBuffer;

public class UserType extends Type<User> {

    public UserType(){
        super("user", VAR_BYTE);
    }

    public byte[] serializeObject(final User u){
        return buffer(4 + u.getName().length() + 1)
                .putInt(u.getId())
                .put(Type.<String>get(StringType.NAME).serialize(u.getName()))
                .array();
    }

    public User deserializeBuffer(final ByteBuffer buffer){
        return new User(
                buffer.getInt(),
                Type.<String>get(StringType.NAME).deserialize(buffer)
        );
    }
}
