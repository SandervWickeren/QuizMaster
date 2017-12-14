package com.sandervwickeren.quizmaster;

/***********************************************************************
 Class to save name and email together. Used to save information into
 Firebase. Retrieved data 'll be converted back into an instance of
 this class.

 ***********************************************************************/
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
