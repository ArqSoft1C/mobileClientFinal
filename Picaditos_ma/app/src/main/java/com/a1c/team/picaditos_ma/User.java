package com.a1c.team.picaditos_ma;

public class User {

    private String email;
    private String name;
    private String lastname;
    private String username;
    private String picture;
    private String phone;

    public User(String email, String name, String lastname, String username, String picture, String phone) {
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.picture = picture;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

