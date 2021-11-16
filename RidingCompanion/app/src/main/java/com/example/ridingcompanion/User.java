package com.example.ridingcompanion;

public class User {
    private String username;
    private String email;
    private String phone;
    private String dob;
    private String weight;
    private String height;
    private String gender;

    public User(String username, String email, String phone, String dob, String weight, String height, String gender) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
