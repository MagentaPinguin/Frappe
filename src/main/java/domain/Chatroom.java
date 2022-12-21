package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chatroom<ID extends UUID> extends Entity<ID> {
    private String name;

    private String passwd;

    private boolean type;

    private List<Entity<ID>> participants;

    public Chatroom(boolean type, String name, String passwd) {
        this.type = type;
        this.name = name;
        this.passwd = passwd;
        participants=new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chatroom<?> chatroom = (Chatroom<?>) o;

        if (type != chatroom.type) return false;
        return name.equals(chatroom.name);
    }

    @Override
    public int hashCode() {
        int result = (type ? 1 : 0);
        result = 31 * result + name.hashCode();
        return result;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }
}
