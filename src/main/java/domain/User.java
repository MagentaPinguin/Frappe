package domain;

import java.util.UUID;

public class User extends Entity<UUID>{
    //# User private data
    private String userName;
    private String passwd;

    //# Pseudo public data
    private String firstName;
    private String lastName;


    public User(){};

    public User(String userName, String passwd, String firstName, String lastName) {
        this.userName = userName;
        this.passwd = passwd;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User( User another){
        this.id=another.id;
        this.userName = another.userName;
        this.passwd = another.passwd;
        this.firstName = another.firstName;
        this.lastName = another.lastName;
    }

    public User(String[] args) {
        this.userName = args[1];
        this.passwd = args[2];
        this.firstName = args[3];
        this.lastName = args[4];
        setId(UUID.fromString(args[0]));
    }

    @Override
    public String toString() {
        return "Username: " + userName +
                ", lastName: " + lastName + '\'' +
                ", firstName: " + firstName+'.';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName) && passwd.equals(user.passwd) && firstName.equals(user.firstName) && lastName.equals(user.lastName);
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + passwd.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}
