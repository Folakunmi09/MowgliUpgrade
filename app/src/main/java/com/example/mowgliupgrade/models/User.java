package com.example.mowgliupgrade.models;

public class User {

    public String userId, username, email;

    public User(){

    }

    public User(String username, String email){
        this.email = email;
        this.username = username;
    }
}
