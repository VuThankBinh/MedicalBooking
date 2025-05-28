package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.HoSoBenhNhanRequest;
import com.example.madicalbooking.api.models.HoSoBenhNhanResponse;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sua_thong_tin_ca_nhan extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 100;
    EditText edtName, edtBirth, edtGender, edtID, edtPhone, edtJob, edtEmail, edtAddress, edtIssueDate;
    Button btnScanQR, btnSave;
    ImageView back;

    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_thong_tin_ca_nhan);
        // Khởi tạo EditText
        edtName = findViewById(R.id.edtName);
        edtBirth = findViewById(R.id.edtBirth);
        edtGender = findViewById(R.id.edtGender);
        edtID = findViewById(R.id.edtID);
        edtPhone = findViewById(R.id.edtPhone);
        edtJob = findViewById(R.id.edtJob);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtIssueDate = findViewById(R.id.edtIssueDate);

        btnScanQR = findViewById(R.id.btnScanQR);
        btnSave = findViewById(R.id.btnSave);
        back=findViewById(R.id.back);

        btnScanQR.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                startQRScanner();
            } else {
                requestCameraPermission();
            }
        });
        back.setOnClickListener(v -> {
            finish();
        });
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveHoSoBenhNhan();
            }
        });

    }
    private void saveHoSoBenhNhan(){
        sharedPreferences = getSharedPreferences("ZimStayPrefs", MODE_PRIVATE);
        HoSoBenhNhanRequest request = new HoSoBenhNhanRequest();
        request.setHoTen(edtName.getText().toString());
        request.setNgaySinh(edtBirth.getText().toString());
        request.setGioiTinh(edtGender.getText().toString());
        request.setMaDinhDanh(edtID.getText().toString());
        request.setSoDienThoai(edtPhone.getText().toString());
        request.setNgheNghiep(edtJob.getText().toString());
        request.setEmail(edtEmail.getText().toString());
        request.setNoiThuongTru(edtAddress.getText().toString());
        request.setNgayCap(edtIssueDate.getText().toString());
        // Lấy người dùng ID từ SharedPreferences
        try {
            int nguoiDungId = Integer.parseInt(sharedPreferences.getString("nguoi_dung_id", "1"));
            request.setNguoiDungId(nguoiDungId);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson=new Gson();
        System.out.println("request: "+gson.toJson(request));
        RetrofitClient.getInstance()
                .getApi()
                .createHoSoBenhNhan(request) // Sửa nếu cần
                .enqueue(new Callback<HoSoBenhNhanResponse>() {
                    @Override
                    public void onResponse(Call<HoSoBenhNhanResponse> call, Response<HoSoBenhNhanResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HoSoBenhNhanResponse res = response.body();

                            System.out.println("response: "+gson.toJson(res));
                            Toast.makeText(getApplicationContext(), "Tạo hồ sơ thành công", Toast.LENGTH_LONG).show();
                            // Nếu muốn reset form hoặc quay lại màn hình trước, có thể thêm ở đây
                             finish(); // nếu muốn kết thúc activity
                        } else {
                            Toast.makeText(getApplicationContext(), "Tạo hồ sơ thất bại. Vui lòng kiểm tra lại.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HoSoBenhNhanResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("API_ERROR", "onFailure: ", t);

                    }
                });
    }
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_CODE);
    }

    private void startQRScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Quét mã QR CCCD");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRScanner();
            } else {
                Toast.makeText(this, "Cần quyền truy cập camera để quét mã QR", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateInputs() {
        if (edtName.getText().toString().trim().isEmpty()) {
            edtName.setError("Vui lòng nhập họ tên");
            edtName.requestFocus();
            return false;
        }

        if (edtBirth.getText().toString().trim().isEmpty()) {
            edtBirth.setError("Vui lòng nhập ngày sinh");
            edtBirth.requestFocus();
            return false;
        }

        if (edtGender.getText().toString().trim().isEmpty()) {
            edtGender.setError("Vui lòng nhập giới tính");
            edtGender.requestFocus();
            return false;
        }

        if (edtID.getText().toString().trim().isEmpty()) {
            edtID.setError("Vui lòng nhập CCCD");
            edtID.requestFocus();
            return false;
        }

        if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.setError("Vui lòng nhập số điện thoại");
            edtPhone.requestFocus();
            return false;
        }

        if (edtJob.getText().toString().trim().isEmpty()) {
            edtJob.setError("Vui lòng nhập nghề nghiệp");
            edtJob.requestFocus();
            return false;
        }

        if (edtEmail.getText().toString().trim().isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return false;
        }

        if (edtAddress.getText().toString().trim().isEmpty()) {
            edtAddress.setError("Vui lòng nhập địa chỉ");
            edtAddress.requestFocus();
            return false;
        }

        if (edtIssueDate.getText().toString().trim().isEmpty()) {
            edtIssueDate.setError("Vui lòng nhập ngày cấp");
            edtIssueDate.requestFocus();
            return false;
        }

        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Hủy quét", Toast.LENGTH_SHORT).show();
            } else {
                String[] rawParts = result.getContents().split("\\|");
                List<String> parts = new ArrayList<>();
                for (String part : rawParts) {
                    if (!part.isEmpty()) {
                        parts.add(part);
                    }
                }
                if (parts.size() >= 6) {
                    String birthDate = formatDateString(parts.get(2));
                    String issueDate = formatDateString(parts.get(5));
                    edtID.setText(parts.get(0));
                    edtName.setText(parts.get(1));
                    edtBirth.setText(birthDate);
                    edtGender.setText(parts.get(3));
                    edtAddress.setText(parts.get(4));
                    edtIssueDate.setText(issueDate);
                } else {
                    Toast.makeText(this, "Không đúng định dạng CCCD", Toast.LENGTH_LONG).show();
                }


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public String formatDateString(String input) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = inputFormat.parse(input);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return input; // Trả lại nguyên gốc nếu lỗi
        }
    }
}