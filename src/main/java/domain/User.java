package domain;

import java.util.UUID;

public class User extends Entity<UUID>{
    //# User private data
    private String username;
    private String passwd;

    //# Pseudo public data
    private String firstname;
    private String lastname;


    public User(){};

    public User(String username, String passwd, String firstname, String lastname) {
        this.username = username;
        this.passwd = passwd;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public User( User another){
        this.id=another.id;
        this.username = another.username;
        this.passwd = another.passwd;
        this.firstname = another.firstname;
        this.lastname = another.lastname;
    }

    public User(String[] args) {
        this.username = args[1];
        this.passwd = args[2];
        this.firstname = args[3];
        this.lastname = args[4];
        setId(UUID.fromString(args[0]));
    }

    @Override
    public String toString() {
        return "Username: " + username +
                ", lastName: " + lastname + '\'' +
                ", firstName: " + firstname +'.';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && passwd.equals(user.passwd) && firstname.equals(user.firstname) && lastname.equals(user.lastname);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + passwd.hashCode();
        result = 31 * result + firstname.hashCode();
        result = 31 * result + lastname.hashCode();
        return result;
    }
}
