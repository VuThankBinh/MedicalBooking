package com.example.madicalbooking.api.models;

public class HoSoBenhNhanRequest {

        private int maHoSo;
        private int nguoiDungId;
        private String hoTen;
        private String ngaySinh;
        private String gioiTinh;
        private String maDinhDanh;
        private String soDienThoai;
        private String ngheNghiep;
        private String email;
        private String noiThuongTru;
        private String ngayCap;


        // Getters và Setters

    public HoSoBenhNhanRequest() {
    }

    public HoSoBenhNhanRequest(int maHoSo, int nguoiDungId, String hoTen, String ngaySinh, String gioiTinh, String maDinhDanh, String soDienThoai, String ngheNghiep, String email, String noiThuongTru, String ngayCap) {
        this.maHoSo = maHoSo;
        this.nguoiDungId = nguoiDungId;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.maDinhDanh = maDinhDanh;
        this.soDienThoai = soDienThoai;
        this.ngheNghiep = ngheNghiep;
        this.email = email;
        this.noiThuongTru = noiThuongTru;
        this.ngayCap = ngayCap;
    }

    public int getMaHoSo() {
        return maHoSo;
    }

    public void setMaHoSo(int maHoSo) {
        this.maHoSo = maHoSo;
    }

    public int getNguoiDungId() {
        return nguoiDungId;
    }

    public void setNguoiDungId(int nguoiDungId) {
        this.nguoiDungId = nguoiDungId;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getMaDinhDanh() {
        return maDinhDanh;
    }

    public void setMaDinhDanh(String maDinhDanh) {
        this.maDinhDanh = maDinhDanh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNgheNghiep() {
        return ngheNghiep;
    }

    public void setNgheNghiep(String ngheNghiep) {
        this.ngheNghiep = ngheNghiep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoiThuongTru() {
        return noiThuongTru;
    }

    public void setNoiThuongTru(String noiThuongTru) {
        this.noiThuongTru = noiThuongTru;
    }

    public String getNgayCap() {
        return ngayCap;
    }

    public void setNgayCap(String ngayCap) {
        this.ngayCap = ngayCap;
    }
}
