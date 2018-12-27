package com.example.teamyuml.decentworkmobile.model;

public class UserList {

    private int id;
    private String email;

    public UserList(String email, int id) {
        this.email = email;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        return email;
    }
}
