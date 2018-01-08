package com.example.bogdanbm.finalweapontool;

/**
 * Created by BogdanBM on 1/8/2018.
 */

public class User {
    String id;
    String email;
    String role;

    public User(String id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
