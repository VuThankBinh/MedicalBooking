package com.example.madicalbooking.model;

import com.google.gson.annotations.SerializedName;

public class PhieuKham {
    @SerializedName("maDangKy")
    private int maDangKy;

    @SerializedName("maHoSo")
    private int maHoSo;

    @SerializedName("maGoi")
    private int maGoi;

    @SerializedName("ngayDangKy")
    private String ngayDangKy;

    @SerializedName("ngayThucHien")
    private String ngayThucHien;

    @SerializedName("trangThai")
    private String trangThai;

    @SerializedName("giaTien")
    private Double giaTien;

    @SerializedName("gioKham")
    private String gioKham;

    // Getters and Setters
    public int getMaDangKy() { return maDangKy; }
    public void setMaDangKy(int maDangKy) { this.maDangKy = maDangKy; }
    
    public int getMaHoSo() { return maHoSo; }
    public void setMaHoSo(int maHoSo) { this.maHoSo = maHoSo; }
    
    public int getMaGoi() { return maGoi; }
    public void setMaGoi(int maGoi) { this.maGoi = maGoi; }
    
    public String getNgayDangKy() { return ngayDangKy; }
    public void setNgayDangKy(String ngayDangKy) { this.ngayDangKy = ngayDangKy; }
    
    public String getNgayThucHien() { return ngayThucHien; }
    public void setNgayThucHien(String ngayThucHien) { this.ngayThucHien = ngayThucHien; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public Double getGiaTien() { return giaTien; }
    public void setGiaTien(Double giaTien) { this.giaTien = giaTien; }
    
    public String getGioKham() { return gioKham; }
    public void setGioKham(String gioKham) { this.gioKham = gioKham; }
} 