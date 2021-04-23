package com.example.easybus;

public class User {
    String email;
    String password;
    String fullName;
    String emName;
    String emPhone;
    String imageURL; //

    public User() {
    }

    public User(String email, String password, String fullName, String emName, String emPhone,String imageURL) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.emName=emName;
        this.emPhone=emPhone;
        this.imageURL = imageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmName() {
        return emName;
    }

    public void setEmName(String emName) {
        this.emName = emName;
    }

    public String getEmPhone() {
        return emPhone;
    }

    public void setEmPhone(String emPhone) {
        this.emPhone = emPhone;
    }

    public String getImageURL() { return imageURL; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
