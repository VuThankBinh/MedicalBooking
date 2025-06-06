package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.madicalbooking.api.ApiConfig;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.BacSiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class thong_tin_bac_si extends AppCompatActivity {
    private ImageView imgBacSi, btnBack;
    private TextView tvTenBacSi, tvChuyenMon, tvKhoa, tvEmail, tvSoDienThoai, tvDiaChi, tvSoNamKinhNghiem, tvSoGiayPhep;
    private Button btnDatLich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_bac_si);

        // Khởi tạo views
        initViews();
        
        // Lấy ID bác sĩ từ intent
        int bacSiId = getIntent().getIntExtra("bacSiId", -1);
        if (bacSiId != -1) {
            getBacSiDetail(bacSiId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin bác sĩ", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Xử lý sự kiện click
        btnBack.setOnClickListener(v -> finish());
        btnDatLich.setOnClickListener(v -> {
            // TODO: Xử lý đặt lịch khám
            Toast.makeText(this, "Chức năng đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        imgBacSi = findViewById(R.id.imgBacSi);
        btnBack = findViewById(R.id.btnBack);
        tvTenBacSi = findViewById(R.id.tvTenBacSi);
        tvChuyenMon = findViewById(R.id.tvChuyenMon);
        tvKhoa = findViewById(R.id.tvKhoa);
        tvEmail = findViewById(R.id.tvEmail);
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvSoNamKinhNghiem = findViewById(R.id.tvSoNamKinhNghiem);
        tvSoGiayPhep = findViewById(R.id.tvSoGiayPhep);
        btnDatLich = findViewById(R.id.btnDatLich);
    }

    private void getBacSiDetail(int bacSiId) {
        RetrofitClient.getInstance()
            .getApi()
            .getBacSiById(bacSiId)
            .enqueue(new Callback<BacSiResponse>() {
                @Override
                public void onResponse(Call<BacSiResponse> call, Response<BacSiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        BacSiResponse bacSi = response.body();
                        displayBacSiInfo(bacSi);
                    } else {
                        Toast.makeText(thong_tin_bac_si.this, "Lỗi khi lấy thông tin bác sĩ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BacSiResponse> call, Throwable t) {
                    Toast.makeText(thong_tin_bac_si.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void displayBacSiInfo(BacSiResponse bacSi) {
        // Load ảnh bác sĩ
        if (bacSi.getAnhDaiDien() != null && !bacSi.getAnhDaiDien().isEmpty()) {
            Glide.with(this)
                .load(ApiConfig.Base_url + "uploads/images/" + bacSi.getAnhDaiDien())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgBacSi);
        }

        // Hiển thị thông tin
        tvTenBacSi.setText(bacSi.getHoTen());
        tvChuyenMon.setText(bacSi.getChuyenMon());
        tvKhoa.setText(bacSi.getTenKhoa());
        tvEmail.setText(bacSi.getEmail());
        tvSoDienThoai.setText(bacSi.getSoDienThoai());
        tvDiaChi.setText(bacSi.getDiaChi());
        tvSoNamKinhNghiem.setText(String.valueOf(bacSi.getSoNamKinhNghiem()) + " năm");
        tvSoGiayPhep.setText(bacSi.getSoGiayPhep());
    }
}