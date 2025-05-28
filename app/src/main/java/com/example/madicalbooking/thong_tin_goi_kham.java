package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.madicalbooking.adapter.GoiKhamAdapter;
import com.example.madicalbooking.api.ApiConfig;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.GoiKhamResponse;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class thong_tin_goi_kham extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_goi_kham);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        int goiKhamId = intent.getIntExtra("goiKhamId", -1);
        anhXa();
        getGoiKhamDetail(goiKhamId);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDatLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thong_tin_goi_kham.this, chon_thong_tin_goi_kham.class);
                intent.putExtra("goiKhamId", goiKhamId);
                System.out.println("goiKhamId2: "+goiKhamId);
                startActivity(intent);
            }
        });
    }
    ImageView imgGoiKham;
    TextView tvName1;
    TextView tvName2;
    TextView tvDiaChi;
    TextView tvDiaChiChiTiet;
    TextView tvLichKham;
    TextView tvGia;
    TextView tvThoiGian;
    TextView tvMoTa;
    Button btnDatLich;
    ImageView back;
    private void anhXa() {
        back=findViewById(R.id.back);
        imgGoiKham = findViewById(R.id.imgGoiKham);
        tvName1 = findViewById(R.id.tvName1);
        tvName2 = findViewById(R.id.tvName2);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvDiaChiChiTiet = findViewById(R.id.tvDiaChiChiTiet);
        tvLichKham = findViewById(R.id.tvLichKham);
        tvGia = findViewById(R.id.tvGia);
        tvThoiGian = findViewById(R.id.tvThoiGian);
        tvMoTa = findViewById(R.id.tvMoTa);
        btnDatLich = findViewById(R.id.btnDatLich);
    }
    private void getGoiKhamDetail(int goiKhamId) {
        RetrofitClient
                .getInstance()
                .getApi()
                .getGoiKhamDetail(goiKhamId)
                .enqueue(new Callback<GoiKhamResponse>() {
                    @Override
                    public void onResponse(Call<GoiKhamResponse> call, Response<GoiKhamResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            GoiKhamResponse goiKham = response.body();
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            System.out.println("trả về: " + json);
                            tvName1.setText(goiKham.getTenGoi());
                            tvName2.setText(goiKham.getTenGoi());
                            tvMoTa.setText(goiKham.getMoTa());

                            // Ví dụ tạm thời cứng địa chỉ và lịch khám
                            tvDiaChi.setText("Phòng khám đa khoa Hưng Yên");
                            tvDiaChiChiTiet.setText("Địa chỉ: Đường Hải Thượng Lãn Ông, Phường An Tảo, Thành Phố Hưng Yên");
                            tvLichKham.setText("Lịch khám: Thứ 2,3,4,5,6,7");

                            // Hiển thị giá theo format tiền Việt Nam
                            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                            String formatted = formatter.format(goiKham.getGia());
                            tvGia.setText(formatted + " đ");

                            // Hiển thị thời gian
                            tvThoiGian.setText("Thời gian: " + goiKham.getThoiGianThucHien() + " phút");

                            // Hiển thị ảnh gói khám (ví dụ dùng Glide hoặc Picasso)
                            String urlAnhGoiKham = ApiConfig.Base_url+"uploads/images/"+goiKham.getAnh();
                            Glide.with(thong_tin_goi_kham.this)
                                    .load(urlAnhGoiKham)
                                    .placeholder(R.drawable.anhnengoi)
                                    .error(R.drawable.anhnengoi)
                                    .centerCrop()
                                    .into(imgGoiKham);
                            // Ẩn nút Đặt lịch nếu trạng thái không hoạt động
                            if (!goiKham.isTrangThai()) {
                                btnDatLich.setVisibility(View.GONE);
                            } else {
                                btnDatLich.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            Toast.makeText(thong_tin_goi_kham.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            System.out.println("Lỗi kết nối: "+ json);
                        }

                    }

                    @Override
                    public void onFailure(Call<GoiKhamResponse> call, Throwable t) {
                        System.out.println("Lỗi gọi API: " + t.getMessage());
                    }
                });
    }

}