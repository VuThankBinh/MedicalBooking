package com.example.madicalbooking.api.models;

public class LoginRequest {
    private String email;
    private String matKhau;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.matKhau = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return matKhau;
    }

    public void setPassword(String password) {
        this.matKhau = password;
    }
} 