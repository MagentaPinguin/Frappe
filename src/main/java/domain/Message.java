package domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message<ID extends UUID> extends Entity<ID>{

    private ID sender;

    private String context;

    private LocalDateTime data;
    public Message(ID sender, String context) {
        this.sender = sender;
        this.context = context;
        this.data = LocalDateTime.now();
    }

    public Message(ID sender, String context, LocalDateTime data) {
        this.sender = sender;
        this.context = context;
        this.data = data;
    }


    public ID getSender() {
        return sender;
    }

    public void setSender(ID sender) {
        this.sender = sender;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message<?> message = (Message<?>) o;

        if (getSender() != null ? !getSender().equals(message.getSender()) : message.getSender() != null) return false;
        if (getContext() != null ? !getContext().equals(message.getContext()) : message.getContext() != null)
            return false;
        return getData() != null ? getData().equals(message.getData()) : message.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = getSender() != null ? getSender().hashCode() : 0;
        result = 31 * result + (getContext() != null ? getContext().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }

}
