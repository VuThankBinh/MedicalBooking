package com.example.madicalbooking.api;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.RequestBody;

public class XemPhieuKham {
    public static JSONArray getPhieuKham(int maHoSo, String trangThai) {
        try {
            String url = ApiConfig.Base_url + "/dang-ky-goi-kham/ho-so/" + maHoSo + "/trang-thai/" + trangThai;
//            JSONObject response = HttpProvider.get(url);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 