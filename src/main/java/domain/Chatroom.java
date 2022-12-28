package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chatroom<ID extends UUID> extends Entity<ID> {
    private String name;

    private String passwd;

    private int type;

    private List<ID> participants;
    public Chatroom(){

    }
    public Chatroom( String name,int type) {
        this.type = type;
        this.name = name;
        passwd="";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ID> getParticipants() {
        return participants;
    }

    public boolean isMember( ID id){
        return participants.contains(id);
    }
    public void setParticipants(List<ID> participants) {
        this.participants = participants;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getPasswd() != null ? getPasswd().hashCode() : 0);
        result = 31 * result + getType();
        result = 31 * result + (getParticipants() != null ? getParticipants().hashCode() : 0);
        return result;
    }
}
