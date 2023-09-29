package org.tensorflow.blindhelp.examples.classification.models;

public class Volunteer {

    private String username;
    private String email;
    private String password;

    public Volunteer() {
        // Default constructor required for Firebase
    }

    public Volunteer(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
