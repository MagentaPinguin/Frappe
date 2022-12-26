package domain;

import java.time.LocalDateTime;
import java.util.UUID;

import static anexe.Constants.FORMATTER;
//" Linking entity
public class Friendship<ID extends UUID> extends Entity<ID>{

    ID first;
    ID second;
    LocalDateTime date;

    public Friendship() {}

    public Friendship(ID first, ID second) {
        var rez=first.compareTo(second);

        if(rez<0){
            this.first = first;
            this.second = second;
        }
        else {
            this.second = first;
            this.first = second;
        }

        date = LocalDateTime.now();
    }

    public ID getFirst() {
        return first;
    }

    public ID getSecond() {
        return second;
    }

    public void setFirst( ID id) {
        first=id;
    }

    public void  setSecond( ID id) {
         second=id;
    }

    public void setDate(LocalDateTime date) {
        this.date =date;
    }


    @Override
    public String toString() {
        return "Friendship: " + first +" " + second+" from:"+ getDate().format(FORMATTER);
    }

    public LocalDateTime getDate() {
        return date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship<?> friendship)) return false;
        return first==friendship.first && friendship.second==second;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}
