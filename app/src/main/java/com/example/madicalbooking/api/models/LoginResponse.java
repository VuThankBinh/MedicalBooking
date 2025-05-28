package com.example.madicalbooking.api.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;
    @SerializedName("email")
    private String email;
    @SerializedName("hoTen")
    private String hoTen;
    @SerializedName("vaiTro")
    private String vaiTro;

    public LoginResponse() {
    }

    public LoginResponse(String token, String email, String hoTen, String vaiTro) {
        this.token = token;
        this.email = email;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
}