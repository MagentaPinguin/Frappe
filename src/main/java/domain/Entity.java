package domain;

import java.util.UUID;

public abstract class Entity<ID> {
    ID id;
    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }




}
