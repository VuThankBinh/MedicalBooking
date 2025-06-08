package com.example.madicalbooking.api.models;

public class GoiKhamTrucTiepResponse {
    private int idGoiKham;
    private String tenGoiKham;
    private String moTa;
    private int giaTien;
    private int idChuyenKhoa;
    private String tenChuyenKhoa;
    private boolean trangThai;
    private int thoiGianKham;

    public int getIdGoiKham() {
        return idGoiKham;
    }

    public void setIdGoiKham(int idGoiKham) {
        this.idGoiKham = idGoiKham;
    }

    public String getTenGoiKham() {
        return tenGoiKham;
    }

    public void setTenGoiKham(String tenGoiKham) {
        this.tenGoiKham = tenGoiKham;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }

    public int getIdChuyenKhoa() {
        return idChuyenKhoa;
    }

    public void setIdChuyenKhoa(int idChuyenKhoa) {
        this.idChuyenKhoa = idChuyenKhoa;
    }

    public String getTenChuyenKhoa() {
        return tenChuyenKhoa;
    }

    public void setTenChuyenKhoa(String tenChuyenKhoa) {
        this.tenChuyenKhoa = tenChuyenKhoa;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public int getThoiGianKham() {
        return thoiGianKham;
    }

    public void setThoiGianKham(int thoiGianKham) {
        this.thoiGianKham = thoiGianKham;
    }
} 