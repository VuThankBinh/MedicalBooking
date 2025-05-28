package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.TokenResponse;
import com.example.madicalbooking.api.models.UpdateProfileRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class thay_doi_thong_tin_tai_khoan extends AppCompatActivity {

    Intent intent;
    String userId,token;
    private static final String TAG = "loginActivity";
    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;
    private EditText ediName, ediEmail, edtSoDienThoai, edtGioiTinh, edtNgaySinh;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thay_doi_thong_tin_tai_khoan);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ImageView back= findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        
        // Khởi tạo các view
        ediName = findViewById(R.id.ediName);
        ediEmail = findViewById(R.id.ediEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtGioiTinh = findViewById(R.id.edtGioiTinh);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Lấy thông tin người dùng
        getUserData();

        // Xử lý sự kiện cập nhật
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        String hoTen = ediName.getText().toString().trim();
        String soDienThoai = edtSoDienThoai.getText().toString().trim();
        String gioiTinh = edtGioiTinh.getText().toString().trim();
        String ngaySinh = edtNgaySinh.getText().toString().trim();
        String diaChi = ""; // Thêm trường địa chỉ nếu cần

        if (hoTen.isEmpty() || soDienThoai.isEmpty() || gioiTinh.isEmpty() || ngaySinh.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest(
            Integer.parseInt(userId),
            hoTen,
            soDienThoai,
            diaChi,
            ngaySinh,
            gioiTinh
        );

        RetrofitClient.getInstance()
            .getApi()
            .updateProfile(request)
            .enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String errorMessage = "Cập nhật thông tin thất bại";
                        if (response.code() == 400) {
                            errorMessage = "Thông tin không hợp lệ";
                        } else if (response.code() == 401) {
                            errorMessage = "Phiên đăng nhập hết hạn";
                        }
                        Toast.makeText(thay_doi_thong_tin_tai_khoan.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void getUserData() {
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        userId = sharedPreferences.getString("nguoi_dung_id", "0");

        System.out.println("token: " +token);
        if (token != null) {
            RetrofitClient.getInstance()
                    .getApi()
                    .checkToken("Bearer " + token)
                    .enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ediName.setText(response.body().getHoTen());
                                ediEmail.setText(response.body().getEmail());
                                edtSoDienThoai.setText(response.body().getSoDienThoai());
                                edtGioiTinh.setText(response.body().getGioiTinh());
                                edtNgaySinh.setText(response.body().getNgaySinh());
                            } else {
                                Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                            Log.e(TAG, "Lỗi kết nối: ", t);
                        }
                    });
        }
    }
}