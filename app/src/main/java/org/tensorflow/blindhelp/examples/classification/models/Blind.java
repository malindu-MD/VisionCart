package org.tensorflow.blindhelp.examples.classification.models;

public class Blind {

    private String username;
    private String email;
    private String password;


    public Blind() {
        // Default constructor required for Firebase
    }


    public Blind(String username, String email, String password) {
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
