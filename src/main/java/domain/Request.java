package domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static anexe.Constants.FORMATTER;

public class Request<ID> extends Entity<ID>{

    private  ID sender;
    private  ID receiver;
    private  String status;
    private  LocalDateTime date;

    public Request() {

    }
    public Request(ID sender, ID receiver,String stats) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = stats;
        this.date =LocalDateTime.now();

    }
    public Request(ID sender, ID receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = "PENDING";
        this.date =LocalDateTime.now();

    }

    @Override
    public String toString() {
        return "Request{" +
                "Id=" + id +
                ", sender=" + sender +
                ", reciever=" + receiver +
                ", status='" + status + '\'' +
                ", date=" + date.format(FORMATTER) +
                '}';
    }

    public void setStatus(String status) {
        this.status = status.toUpperCase();
    }

    public ID getSender() {
        return sender;
    }

    public ID getReceiver() {
        return receiver;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request<?> request = (Request<?>) o;
        return Objects.equals(sender, request.sender)&& Objects.equals(receiver, request.receiver);
    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setSender(ID sender) {
        this.sender = sender;
    }

    public void setReceiver(ID receiver) {
        this.receiver = receiver;
    }
}
