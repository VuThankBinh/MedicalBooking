package com.example.madicalbooking;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.madicalbooking.api.ApiConfig;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.TokenResponse;
import com.example.madicalbooking.api.models.UpdateProfileRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class thay_doi_thong_tin_tai_khoan extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Uri selectedImageUri;
    private ImageView hinhDaiDien;
    private TextView doianh;
    private String currentPhotoPath;
    private String uploadedFileName;
    private File photoFile;

    Intent intent;
    String userId,token;
    private static final String TAG = "loginActivity";
    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;
    private EditText ediName, ediEmail, edtSoDienThoai, edtGioiTinh, edtNgaySinh, edtDiaChi;
    private Button btnUpdate;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private Runnable pendingAction;

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
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnUpdate = findViewById(R.id.btnUpdate);

        hinhDaiDien = findViewById(R.id.hinhdaidien);
        doianh = findViewById(R.id.doianh);

        setupImagePickers();
        setupPermissionLauncher();

        doianh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

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

    private void setupImagePickers() {
        // Xử lý chọn ảnh từ thư viện
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        // Hiển thị ảnh ngay lập tức
                        loadImage(selectedImageUri);
                        // Upload ảnh
                        uploadImage(selectedImageUri);
                    }
                }
            }
        );

        // Xử lý chụp ảnh từ camera
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.containsKey("data")) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        try {
                            // Hiển thị ảnh ngay lập tức
                            if (!isFinishing() && !isDestroyed() && hinhDaiDien != null) {
                                Glide.with(this)
                                    .load(imageBitmap)
                                    .placeholder(R.drawable.ic_tai_khoan)
                                    .error(R.drawable.ic_tai_khoan)
                                    .circleCrop()
                                    .into(hinhDaiDien);
                            }
                            // Lưu bitmap vào file tạm
                            File tempFile = createTempFileFromBitmap(imageBitmap);
                            selectedImageUri = Uri.fromFile(tempFile);
                            // Upload ảnh
                            uploadImage(selectedImageUri);
                        } catch (IOException e) {
                            if (!isFinishing() && !isDestroyed()) {
                                Toast.makeText(this, "Lỗi khi xử lý ảnh", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        );
    }

    private void setupPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                boolean allGranted = true;
                for (Boolean isGranted : permissions.values()) {
                    if (!isGranted) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    if (pendingAction != null) {
                        pendingAction.run();
                        pendingAction = null;
                    }
                } else {
                    new AlertDialog.Builder(this)
                        .setTitle("Cần cấp quyền")
                        .setMessage("Để sử dụng tính năng này, vui lòng cấp quyền trong Cài đặt")
                        .setPositiveButton("Vào cài đặt", (dialog, which) -> {
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                }
            }
        );
    }

    private void checkAndRequestPermissions(Runnable action) {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            };
        } else {
            permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) 
                != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (allGranted) {
            action.run();
        } else {
            boolean shouldShowRationale = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    shouldShowRationale = true;
                    break;
                }
            }

            if (shouldShowRationale) {
                new AlertDialog.Builder(this)
                    .setTitle("Cần cấp quyền")
                    .setMessage("Ứng dụng cần quyền truy cập camera và thư viện ảnh để thay đổi ảnh đại diện")
                    .setPositiveButton("OK", (dialog, which) -> {
                        pendingAction = action;
                        requestPermissionLauncher.launch(permissions);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            } else {
                pendingAction = action;
                requestPermissionLauncher.launch(permissions);
            }
        }
    }

    private void showImagePickerDialog() {
        String[] options = {"Chọn từ thư viện", "Chụp ảnh mới"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh đại diện");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Chọn từ thư viện
                checkAndRequestPermissions(() -> {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    imagePickerLauncher.launch(intent);
                });
            } else {
                // Chụp ảnh mới
                checkAndRequestPermissions(() -> {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        cameraLauncher.launch(intent);
                    } else {
                        if (!isFinishing() && !isDestroyed()) {
                            Toast.makeText(this, "Không tìm thấy ứng dụng camera", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.show();
    }

    private File createTempFileFromBitmap(Bitmap bitmap) throws IOException {
        File tempFile = File.createTempFile("avatar", ".jpg", getCacheDir());
        FileOutputStream fos = new FileOutputStream(tempFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        fos.close();
        return tempFile;
    }

    private void uploadImage(Uri imageUri) {
        try {
            File imageFile = createTempFileFromUri(imageUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "avatar_" + System.currentTimeMillis() + ".jpg", requestFile);

            RetrofitClient.getInstance()
                .getApi()
                .uploadImage(filePart)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }

                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String responseString = response.body().string();
                                System.out.println("Response upload: " + responseString);
                                
                                // Tách đường dẫn từ chuỗi trả về
                                if (responseString.contains("File đã được lưu tại:")) {
                                    String[] parts = responseString.split("\\\\");
                                    if (parts.length > 0) {
                                        String fileName = parts[parts.length - 1].trim();
                                        System.out.println("Extracted filename: " + fileName);
                                        
                                        // Tạo URL đầy đủ cho ảnh
                                        String imageUrl = ApiConfig.Base_url + "uploads/images/" + fileName;

                                        // Load ảnh từ URL
                                        loadImageFromUrl(imageUrl);

                                        // Lưu tên file để sử dụng khi cập nhật profile
                                        uploadedFileName = fileName;
                                        Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Đã tải ảnh lên thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (!isFinishing() && !isDestroyed()) {
                                            Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Không thể trích xuất tên file", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    if (!isFinishing() && !isDestroyed()) {
                                        Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Response không hợp lệ: " + responseString, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (IOException e) {
                                if (!isFinishing() && !isDestroyed()) {
                                    Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Lỗi khi đọc phản hồi từ server", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            if (!isFinishing() && !isDestroyed()) {
                                String errorBody = "";
                                try {
                                    errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                                } catch (Exception e) {
                                    errorBody = e.getMessage();
                                }
                                Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Upload ảnh thất bại: " + errorBody, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (!isFinishing() && !isDestroyed()) {
                            Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Lỗi kết nối khi upload ảnh", Toast.LENGTH_SHORT).show();
                            System.out.println("Lỗi kết nối khi upload ảnh: " + t.getMessage());
                        }
                    }
                });
        } catch (IOException e) {
            if (!isFinishing() && !isDestroyed()) {
                System.out.println("File error: " + e.getMessage());
                Toast.makeText(this, "Lỗi khi xử lý ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear any pending Glide requests
        if (hinhDaiDien != null && !isFinishing() && !isDestroyed()) {
            try {
                Glide.with(this).clear(hinhDaiDien);
            } catch (Exception e) {
                // Ignore any Glide exceptions during destroy
            }
        }
    }

    private void loadImage(Uri imageUri) {
        if (!isFinishing() && !isDestroyed() && hinhDaiDien != null) {
            try {
                Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.ic_tai_khoan)
                    .error(R.drawable.ic_tai_khoan)
                    .circleCrop()
                    .into(hinhDaiDien);
            } catch (Exception e) {
                // Ignore any Glide exceptions
            }
        }
    }

    private void loadImageFromUrl(String imageUrl) {
        if (!isFinishing() && !isDestroyed() && hinhDaiDien != null) {
            try {
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_tai_khoan)
                    .error(R.drawable.ic_tai_khoan)
                    .circleCrop()
                    .into(hinhDaiDien);
            } catch (Exception e) {
                // Ignore any Glide exceptions
            }
        }
    }

    private void updateProfile() {
        String hoTen = ediName.getText().toString().trim();
        String soDienThoai = edtSoDienThoai.getText().toString().trim();
        String gioiTinh = edtGioiTinh.getText().toString().trim();
        String ngaySinh = edtNgaySinh.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();

        if (hoTen.isEmpty() || soDienThoai.isEmpty() || gioiTinh.isEmpty() || ngaySinh.isEmpty() || diaChi.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateProfileRequest request = new UpdateProfileRequest(
            Integer.parseInt(userId),
            hoTen,
            soDienThoai,
            diaChi,
            ngaySinh,
            gioiTinh,
            uploadedFileName // Sử dụng tên file đã upload
        );

        // In ra thông tin request để debug
        Gson gson = new Gson();
        String requestJson = gson.toJson(request);
        System.out.println("Request update profile: " + requestJson);

        RetrofitClient.getInstance()
            .getApi()
            .updateProfile(request)
            .enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
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
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Toast.makeText(thay_doi_thong_tin_tai_khoan.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private File createTempFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("avatar", ".jpg", getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        
        inputStream.close();
        outputStream.close();
        
        return tempFile;
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
                                edtDiaChi.setText(response.body().getDiaChi());
                                Glide.with(thay_doi_thong_tin_tai_khoan.this)
                                        .load(ApiConfig.Base_url + "uploads/images/" + response.body().getAvatar())
                                        .placeholder(R.drawable.ic_tai_khoan)
                                        .error(R.drawable.ic_tai_khoan)
                                        .circleCrop()
                                        .into(hinhDaiDien);
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