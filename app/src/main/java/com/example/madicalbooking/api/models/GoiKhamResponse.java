package com.example.madicalbooking.api.models;

public class GoiKhamResponse {
    private int maGoi;
    private String tenGoi;
    private String moTa;
    private double gia;
    private int thoiGianThucHien;
    private boolean trangThai;
    private String anhSp;

    public GoiKhamResponse() {
    }

    public GoiKhamResponse(int maGoi, String tenGoi, String moTa, double gia, int thoiGianThucHien, boolean trangThai, String anh) {
        this.maGoi = maGoi;
        this.tenGoi = tenGoi;
        this.moTa = moTa;
        this.gia = gia;
        this.thoiGianThucHien = thoiGianThucHien;
        this.trangThai = trangThai;
        this.anhSp = anh;

    }

    public String getAnh() {
        return anhSp;
    }

    public void setAnh(String anh) {
        this.anhSp = anh;
    }

    public int getMaGoi() {
        return maGoi;
    }

    public void setMaGoi(int maGoi) {
        this.maGoi = maGoi;
    }

    public String getTenGoi() {
        return tenGoi;
    }

    public void setTenGoi(String tenGoi) {
        this.tenGoi = tenGoi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getThoiGianThucHien() {
        return thoiGianThucHien;
    }

    public void setThoiGianThucHien(int thoiGianThucHien) {
        this.thoiGianThucHien = thoiGianThucHien;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
