package com.example.madicalbooking.api.models;

import com.google.gson.annotations.SerializedName;

public class BacSiResponse {
    @SerializedName("idBacSi")
    private int idBacSi;

    @SerializedName("idNguoiDung")
    private int idNguoiDung;

    @SerializedName("email")
    private String email;

    @SerializedName("hoTen")
    private String hoTen;

    @SerializedName("soDienThoai")
    private String soDienThoai;

    @SerializedName("diaChi")
    private String diaChi;

    @SerializedName("ngaySinh")
    private String ngaySinh;

    @SerializedName("gioiTinh")
    private String gioiTinh;

    @SerializedName("cccd")
    private String cccd;

    @SerializedName("idChuyenKhoa")
    private int idChuyenKhoa;

    @SerializedName("tenChuyenKhoa")
    private String tenChuyenKhoa;

    @SerializedName("idKhoa")
    private int idKhoa;

    @SerializedName("tenKhoa")
    private String tenKhoa;

    @SerializedName("chuyenMon")
    private String chuyenMon;

    @SerializedName("soGiayPhep")
    private String soGiayPhep;

    @SerializedName("soNamKinhNghiem")
    private int soNamKinhNghiem;

    @SerializedName("dangLamViec")
    private boolean dangLamViec;

    @SerializedName("daKichHoat")
    private boolean daKichHoat;

    @SerializedName("anhDaiDien")
    private String anhDaiDien;

    // Getters
    public int getIdBacSi() { return idBacSi; }
    public int getIdNguoiDung() { return idNguoiDung; }
    public String getEmail() { return email; }
    public String getHoTen() { return hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getDiaChi() { return diaChi; }
    public String getNgaySinh() { return ngaySinh; }
    public String getGioiTinh() { return gioiTinh; }
    public String getCccd() { return cccd; }
    public int getIdChuyenKhoa() { return idChuyenKhoa; }
    public String getTenChuyenKhoa() { return tenChuyenKhoa; }
    public int getIdKhoa() { return idKhoa; }
    public String getTenKhoa() { return tenKhoa; }
    public String getChuyenMon() { return chuyenMon; }
    public String getSoGiayPhep() { return soGiayPhep; }
    public int getSoNamKinhNghiem() { return soNamKinhNghiem; }
    public boolean isDangLamViec() { return dangLamViec; }
    public boolean isDaKichHoat() { return daKichHoat; }
    public String getAnhDaiDien() { return anhDaiDien; }
} 