package common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class User {

    @Getter @Setter private int id;
    @Getter @Setter private String name;
}
