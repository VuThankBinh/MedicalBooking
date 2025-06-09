package com.example.madicalbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.madicalbooking.api.ApiConfig;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.TokenResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class thong_tin_tai_khoan extends AppCompatActivity {

    Intent intent;
    String userId,token;
    private static final String TAG = "loginActivity";
    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tai_khoan);
        if(getActionBar() != null){
            getActionBar().hide();
        }
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        intent=getIntent();
        userId=sharedPreferences.getString("nguoi_dung_id","0");
        token=sharedPreferences.getString(KEY_TOKEN,"");
        System.out.println("userId: "+userId);
        System.out.println("token: "+token);
        bottomNavigationView = findViewById(R.id.navView);
        // Thiết lập item được chọn là Practice
        bottomNavigationView.setSelectedItemId(R.id.nav_taikhoan);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    return true;
                } else if (itemId == R.id.nav_hoso) {
                    Intent intent = new Intent(thong_tin_tai_khoan.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_phieukham) {
                    Intent intent = new Intent(thong_tin_tai_khoan.this, phieu_kham.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_thongbao) {
                    Intent intent = new Intent(thong_tin_tai_khoan.this, thong_bao.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if (itemId == R.id.nav_taikhoan) {

                    return true;
                }
                return false;
            }
        });
        getUserData();
        LinearLayout thongtin=findViewById(R.id.thongtin);
        thongtin.setOnClickListener(v -> {
            Intent intent = new Intent(thong_tin_tai_khoan.this, thay_doi_thong_tin_tai_khoan.class);
            startActivity(intent);
        });
        LinearLayout thaydoimatkhau=findViewById(R.id.thaydoimatkhau);
        thaydoimatkhau.setOnClickListener(v -> {
            Intent intent = new Intent(thong_tin_tai_khoan.this, thay_doi_mat_khau.class);
            startActivity(intent);
        });
        LinearLayout logout=findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(thong_tin_tai_khoan.this, Dang_nhap.class);
            startActivity(intent);
            finish();
        });
    }
    private void getUserData() {
        String token = sharedPreferences.getString(KEY_TOKEN, null);

        System.out.println("token: " +token);
        if (token != null) {
            RetrofitClient.getInstance()
                    .getApi()
                    .checkToken("Bearer " + token)
                    .enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                TokenResponse userData = response.body();
                                Gson gson= new Gson();

                                System.out.println("userData: " +gson.toJson(userData));
                                TextView tvName = findViewById(R.id.tvName);
                                ImageView avatarImage = findViewById(R.id.avatarImage);
                                
                                // Cập nhật tên
                                tvName.setText(userData.getHoTen());
                                System.out.println("images: "+ApiConfig.Base_url + "uploads/images/" + userData.getAvatar());
                                
                                // Cập nhật avatar nếu có
                                if (userData.getAvatar() != null && !userData.getAvatar().isEmpty()) {
                                    // Sử dụng Glide hoặc Picasso để load ảnh từ URL
                                    Glide.with(thong_tin_tai_khoan.this)
                                        .load( ApiConfig.Base_url + "uploads/images/" + userData.getAvatar())
                                        .placeholder(R.drawable.ic_tai_khoan)
                                        .error(R.drawable.ic_tai_khoan)
                                        .into(avatarImage);
                                }
                            } else {
                                Toast.makeText(thong_tin_tai_khoan.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                            Log.e(TAG, "Lỗi kết nối: ", t);
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }
}