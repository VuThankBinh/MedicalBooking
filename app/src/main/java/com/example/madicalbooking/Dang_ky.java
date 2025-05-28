package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.OtpRequest;
import com.example.madicalbooking.api.models.OtpResponse;
import com.example.madicalbooking.api.models.RegisterRequest;
import com.example.madicalbooking.api.models.RegisterResponse;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dang_ky extends AppCompatActivity {

    static boolean daXn = false;
    EditText etEmail, etPassword, etConfimPassword, etHoTen, etSoDienThoai, etDiaChi, etNgaySinh, etCCCD;
    Spinner spinnerGioiTinh;
    Button btnLogin;

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfimPassword = findViewById(R.id.etConfimPassword);
        etHoTen = findViewById(R.id.etHoTen);
        etSoDienThoai = findViewById(R.id.etSoDienThoai);
        etDiaChi = findViewById(R.id.etDiaChi);
        etNgaySinh = findViewById(R.id.etNgaySinh);
        etCCCD = findViewById(R.id.etCCCD);
        spinnerGioiTinh = findViewById(R.id.spinnerGioiTinh);
        btnLogin = findViewById(R.id.btnLogin);
    }
    private boolean validateInputs() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfimPassword.getText().toString().trim();
        String hoTen = etHoTen.getText().toString().trim();
        String soDienThoai = etSoDienThoai.getText().toString().trim();
        String diaChi = etDiaChi.getText().toString().trim();
        String ngaySinh = etNgaySinh.getText().toString().trim();
        String cccd = etCCCD.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải từ 6 ký tự");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfimPassword.setError("Mật khẩu không khớp");
            return false;
        }

        if (hoTen.isEmpty()) {
            etHoTen.setError("Vui lòng nhập họ tên");
            return false;
        }

        if (soDienThoai.length() != 10 || !soDienThoai.matches("\\d+")) {
            etSoDienThoai.setError("Số điện thoại không hợp lệ");
            return false;
        }

        if (diaChi.isEmpty()) {
            etDiaChi.setError("Vui lòng nhập địa chỉ");
            return false;
        }

        if (!ngaySinh.matches("\\d{2}/\\d{2}/\\d{4}")) {
            etNgaySinh.setError("Ngày sinh không hợp lệ (dd/MM/yyyy)");
            return false;
        }

        if (cccd.length() != 12 || !cccd.matches("\\d+")) {
            etCCCD.setError("CCCD không hợp lệ (12 số)");
            return false;
        }

        // Có thể kiểm tra thêm giới tính nếu cần (dựa trên position Spinner)
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initViews();
        btnLogin.setOnClickListener(view -> {
            if (validateInputs()) {
                sendOtp(etEmail.getText().toString().trim());
            }
        });
    }private void sendOtp(String email) {
        OtpRequest request = new OtpRequest(email);

        RetrofitClient.getInstance()
                .getApi()
                .sendOtp(request)
                .enqueue(new Callback<OtpResponse>() {
                    @Override
                    public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            OtpResponse otpResponse = response.body();
                            if (otpResponse.isSuccess()) {
                                // Chuyển đến màn hình xác nhận OTP
                                Intent intent = new Intent(Dang_ky.this, Xac_thuc_otp.class);
                                intent.putExtra("email", email);
                                startActivityForResult(intent, 1);
                            } else {
                                Toast.makeText(Dang_ky.this, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Dang_ky.this, Xac_thuc_otp.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(Dang_ky.this, "Gửi OTP thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OtpResponse> call, Throwable t) {
                        Log.e("TAG", "Lỗi kết nối: ", t);
                        Toast.makeText(Dang_ky.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Luôn chạy lại khi quay về Activity này
        if(daXn){

            registerUser(etEmail.getText().toString(),
                    etPassword.getText().toString(),
                    etHoTen.getText().toString(),
                    etSoDienThoai.getText().toString(),
                    etDiaChi.getText().toString(),
                    etNgaySinh.getText().toString(),
                    spinnerGioiTinh.getSelectedItem().toString(),
                    etCCCD.getText().toString());

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // OTP xác thực thành công, tiến hành đăng ký
            registerUser(etEmail.getText().toString(),
                    etPassword.getText().toString(),
                    etHoTen.getText().toString(),
                    etSoDienThoai.getText().toString(),
                    etDiaChi.getText().toString(),
                    etNgaySinh.getText().toString(),
                    spinnerGioiTinh.getSelectedItem().toString(),
                    etCCCD.getText().toString());
        }
    }
    private void registerUser(String email, String matKhau, String hoTen, String soDienThoai, String diaChi, String ngaySinh, String gioiTinh, String cccd) {
        String inputDate = "16/12/2003";

        // Định dạng gốc
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Định dạng bạn muốn
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formattedDate = null;
        try {
            Date date = originalFormat.parse(inputDate);
            formattedDate = targetFormat.format(date);
            System.out.println("Kết quả: " + formattedDate); // In ra: 2003-16-12
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RegisterRequest registerRequest = new RegisterRequest(email, matKhau, hoTen, soDienThoai, diaChi, formattedDate, gioiTinh, cccd);

        RetrofitClient.getInstance()
                .getApi()
                .register(registerRequest)
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            RegisterResponse registerResponse = response.body();

                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(Dang_ky.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                finish(); // Quay lại màn hình đăng nhập
                            } else {
                                Toast.makeText(Dang_ky.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Gson gson = new Gson();
                            String json = gson.toJson(registerRequest);
                            Log.d("RegisterRequestJSON", json);
                            System.out.println("JSON gửi đi: " + gson.toJson(registerRequest));
                            Toast.makeText(Dang_ky.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Log.e("TAG", "Lỗi kết nối: ", t);
                        Toast.makeText(Dang_ky.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}