package com.example.ridingcompanion;

public class User {
    private String username;
    private String email;
    private String phone;
    private String dob;

    public User(String username, String email, String phone, String dob) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
