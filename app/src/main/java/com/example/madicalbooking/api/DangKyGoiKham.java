package com.example.madicalbooking.api;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DangKyGoiKham {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static JSONObject dangKyGoiKham(int maHoSo, int maGoi, String ngayDangKy, 
                                         String ngayThucHien, String trangThai, 
                                         double giaTien, String gioKham, String userId) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maHoSo", maHoSo);
            jsonBody.put("maGoi", maGoi);
            jsonBody.put("ngayDangKy", ngayDangKy);
            jsonBody.put("ngayThucHien", ngayThucHien);
            jsonBody.put("trangThai", trangThai);
            jsonBody.put("giaTien", giaTien);
            jsonBody.put("gioKham", gioKham);
            jsonBody.put("user_id", userId);

            RequestBody body = RequestBody.create(JSON, jsonBody.toString());
            return HttpProvider.sendPost(ApiConfig.Base_url + "api/dang-ky-goi-kham", body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 