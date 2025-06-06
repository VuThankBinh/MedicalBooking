package com.example.madicalbooking.api.models;

import com.google.gson.annotations.SerializedName;

public class KhoaResponse {
    @SerializedName("idKhoa")
    private int idKhoa;

    @SerializedName("tenKhoa")
    private String tenKhoa;

    @SerializedName("moTa")
    private String moTa;

    @SerializedName("daKichHoat")
    private boolean daKichHoat;

    // Getters
    public int getIdKhoa() { return idKhoa; }
    public String getTenKhoa() { return tenKhoa; }
    public String getMoTa() { return moTa; }
    public boolean isDaKichHoat() { return daKichHoat; }
} 