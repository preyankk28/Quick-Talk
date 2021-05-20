package io.quicktalk.agilean.Model;

public class User {
    private String Email;
    private String FirstName;
    private String LastName;
    private String Uid;

    public User(String email, String firstName, String lastName, String uid) {
        Email = email;
        FirstName = firstName;
        LastName = lastName;
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
