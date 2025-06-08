package com.example.madicalbooking.api;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DangKyGoiKham {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static String chuyenDoiGioKham(String gioKham) {
        if (gioKham == null || gioKham.isEmpty()) {
            return gioKham;
        }
        // Nếu có dạng "8.00 - 9.00" thì lấy phần đầu
        if (gioKham.contains("-")) {
            return gioKham.split("-")[0].trim();
        }
        return gioKham;
    }

    public static JSONObject dangKyGoiKham(int maHoSo, int maGoi, String ngayDangKy, 
                                         String ngayThucHien, String trangThai, 
                                         double giaTien, String gioKham, String userId) {
        try {
            // Chuyển đổi giờ khám
            String gioKhamFormatted = chuyenDoiGioKham(gioKham);
            
            // Chuyển đổi userId từ string sang int
            int userIdInt = Integer.parseInt(userId);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("maDangKy", 0); // Mặc định là 0 vì là đăng ký mới
            jsonBody.put("maHoSo", maHoSo);
            jsonBody.put("maGoi", maGoi);
            jsonBody.put("ngayDangKy", ngayDangKy);
            jsonBody.put("ngayThucHien", ngayThucHien);
            jsonBody.put("trangThai", trangThai);
            jsonBody.put("giaTien", giaTien);
            jsonBody.put("gioKham", gioKhamFormatted);
            jsonBody.put("userId", userIdInt); // Sử dụng userId dạng int

            // In ra thông tin để test
            System.out.println("URL: " + ApiConfig.Base_url + "/api/dang-ky-goi-kham");
            System.out.println("Request Body: " + jsonBody.toString(2));
            
            RequestBody body = RequestBody.create(JSON, jsonBody.toString());
            JSONObject response = HttpProvider.sendPost(ApiConfig.Base_url + "/api/dang-ky-goi-kham", body);
            
            if (response != null) {
                if (response.has("error")) {
                    System.out.println("Lỗi: " + response.getString("error"));
                    // Trả về null để báo lỗi
                    return null;
                }
                System.out.println("Response Status: 200 OK");
                System.out.println("Response Body: " + response.toString(2));
            } else {
                System.out.println("Response is null");
            }
            return response;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
} 