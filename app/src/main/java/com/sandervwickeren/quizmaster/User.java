package com.sandervwickeren.quizmaster;

public class User {

    public String name;
    public String email;

    // Required for FB
    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
