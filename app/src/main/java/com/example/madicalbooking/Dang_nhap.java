package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.LoginRequest;
import com.example.madicalbooking.api.models.LoginResponse;
import com.example.madicalbooking.api.models.OtpRequest;
import com.example.madicalbooking.api.models.OtpResponse;
import com.example.madicalbooking.api.models.TokenResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dang_nhap extends AppCompatActivity {
    private static final String TAG = "loginActivity";
    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        // Ẩn ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Ánh xạ các view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        checkTokenExpiration();
        // Xử lý sự kiện click nút đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (validateInput(email, password)) {
                    loginUser(email, password);
                }
            }
        });

        // Xử lý sự kiện click quên mật khẩu
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEmail.getText().toString().trim().isEmpty()){
                    Toast.makeText(Dang_nhap.this, "Bạn phải nhập email đã", Toast.LENGTH_SHORT).show();
                }
                sendOtp(etEmail.getText().toString().trim());
            }
        });

        // Xử lý sự kiện click đăng ký
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dang_nhap.this, Dang_ky.class);
                startActivity(intent);
            }
        });
    }
    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        return true;
    }

    private void sendOtp(String email) {
        OtpRequest request = new OtpRequest(email);

        RetrofitClient.getInstance()
                .getApi()
                .sendOTPreset(request)
                .enqueue(new Callback<OtpResponse>() {
                    @Override
                    public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            OtpResponse otpResponse = response.body();
                            if (otpResponse.isSuccess()) {
                                // Chuyển đến màn hình xác nhận OTP
                                Intent intent = new Intent(Dang_nhap.this, Xac_thuc_otp.class);
                                intent.putExtra("email", email);
                            } else {
                                Toast.makeText(Dang_nhap.this, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Dang_nhap.this, Xac_thuc_otp.class);
                                intent.putExtra("email", email);
                                intent.putExtra("resetpass", true);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(Dang_nhap.this, "Gửi OTP thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OtpResponse> call, Throwable t) {
                        Log.e(TAG, "Lỗi kết nối: ", t);
                    }
                });
        if(Dang_ky.daXn){
            finish();
        }
    }
    private void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        Gson gson = new Gson();
        System.out.println("request: "+ gson.toJson(loginRequest));
        RetrofitClient.getInstance()
                .getApi()
                .login(loginRequest)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Gson gson = new Gson();
                        System.out.println("response: " + gson.toJson(response.body()));
                        System.out.println("response is null: " + response.body() ==null);
                        if (response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.getToken() != null) {
                                // Lưu thông tin người dùng
                                saveUserData(loginResponse);

//                                Toast.makeText(Dang_nhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(Dang_nhap.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
                            } else {
                                Toast.makeText(Dang_nhap.this, "Thông tin tài khoản không chính xác", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Dang_nhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e(TAG, "Lỗi kết nối: ", t);
                        Toast.makeText(Dang_nhap.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void saveUserData(LoginResponse data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (data != null) {
            editor.putString(KEY_TOKEN, data.getToken());
            editor.putString(KEY_USER_EMAIL, data.getEmail());
            editor.putString(KEY_ACCOUNT_TYPE, data.getVaiTro());
            checkTokenExpiration();
        }

        editor.apply();
    }

    private void checkTokenExpiration() {
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        System.out.println("token: " +token);
        if (token != null) {
            RetrofitClient.getInstance()
                    .getApi()
                    .checkToken("Bearer " + token) // Sửa nếu cần
                    .enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                int id=response.body().getId();
                                editor.putString("nguoi_dung_id", String.valueOf(id));
                                editor.apply();
                                TokenResponse tokenCheckResponse = response.body();
                                Toast.makeText(Dang_nhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Dang_nhap.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                                // Sửa tại đây
                                else {
                                    Toast.makeText(Dang_nhap.this, "Phiên đăng nhập đã hết hạn", Toast.LENGTH_SHORT).show();

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