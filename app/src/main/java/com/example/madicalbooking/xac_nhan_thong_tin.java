package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madicalbooking.api.models.HoSoBenhNhanResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class xac_nhan_thong_tin extends AppCompatActivity {

    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    private String KEY_USER_ID = "nguoi_dung_id";
    private String userId;
    SharedPreferences sharedPreferences;
    ImageView back;
    int goiKhamId=0;
    Intent intent;
    Button btnXacNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_thong_tin);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(KEY_USER_ID, "9");
        back=findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });
        intent=getIntent();
        String dichvu=intent.getStringExtra("dichVu");
        if(dichvu.equals("GoiKham")){
            getThongTin();
        }
        btnXacNhan=findViewById(R.id.btnTiepTuc);
        btnXacNhan.setOnClickListener(view -> {
            putInfomation();
        });
    }
    private void getThongTin(){
        goiKhamId=intent.getIntExtra("goiKhamid", -1);
        String goiKhamJson=intent.getStringExtra("goiKhamJson");
        String hoSoBenhNhanResponse=intent.getStringExtra("hoSoBenhNhanResponse");
//            System.out.println("goiKhamId: "+goiKhamId);
        System.out.println("goiKhamJson: "+goiKhamJson);
//            System.out.println("hoSoBenhNhanResponse: "+hoSoBenhNhanResponse);
        try {
            JSONObject goiKhamObj = new JSONObject(goiKhamJson);
            JSONObject hoSoObj = new JSONObject(hoSoBenhNhanResponse);

            // Lấy thông tin từ gói khám
            String tenGoi = goiKhamObj.getString("tenGoi");
            String gioChon = goiKhamObj.getString("gioChon");
            String ngayChon = goiKhamObj.getString("ngayChon");
            double giaGoi = goiKhamObj.getDouble("giaGoi");
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(giaGoi); // kết quả ví dụ: 1,200,000
            // Lấy thông tin bệnh nhân
            String hoTen = hoSoObj.getString("hoTen");
            String soDienThoai = hoSoObj.getString("soDienThoai");
            String ngaySinh = hoSoObj.getString("ngaySinh");
            String diaChi = hoSoObj.getString("noiThuongTru");

            // Gán dữ liệu lên UI
            ((TextView) findViewById(R.id.tv_ho_ten)).setText(hoTen);
            ((TextView) findViewById(R.id.tv_sdt)).setText(soDienThoai);
            ((TextView) findViewById(R.id.tv_ngay_sinh)).setText(ngaySinh);
            ((TextView) findViewById(R.id.tv_dia_chi)).setText(diaChi);



            System.out.println("tenGoi: "+tenGoi);
            System.out.println("gioChon: "+gioChon);
            System.out.println("ngayChon: "+ngayChon);

            ((TextView) findViewById(R.id.tv_ten_goi)).setText(tenGoi);
            ((TextView) findViewById(R.id.tv_gio)).setText(gioChon);
            ((TextView) findViewById(R.id.tv_ngay)).setText(ngayChon);
            ((TextView) findViewById(R.id.tv_gia_goi)).setText(formatted +"đ");
            ((TextView) findViewById(R.id.tv_gia_goi2)).setText(formatted +"đ");
            ((TextView) findViewById(R.id.tv_gia_goi3)).setText(formatted +"đ");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void putInfomation() {
        Intent intent = new Intent(xac_nhan_thong_tin.this, thanh_toan.class);
        Intent intent1= getIntent();
        goiKhamId=intent1.getIntExtra("goiKhamId", -1);

        intent.putExtra("dichVu", intent1.getStringExtra("dichVu"));
        if(intent1.getStringExtra("dichVu").equals("GoiKham")){
            intent.putExtra("goiKhamid", goiKhamId);
            intent.putExtra("goiKhamJson", intent1.getStringExtra("goiKhamJson"));
        }
        Gson gson = new Gson();
        String json = gson.toJson(intent1.getStringExtra("hoSoBenhNhanResponse"));
        System.out.println("json: " + json);
        intent.putExtra("hoSoBenhNhanResponse", json);
        startActivity(intent);
    }
}