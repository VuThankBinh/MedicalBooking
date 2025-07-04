package com.example.madicalbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madicalbooking.adapter.ThongBaoAdapter;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.ThongBaoResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class thong_bao extends AppCompatActivity {
    private RecyclerView rvThongBao;
    private ImageView btnBack;
    private ThongBaoAdapter adapter;
    private List<ThongBaoResponse> thongBaoList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_bao);

        // Khởi tạo views
        initViews();
        
        // Khởi tạo RecyclerView
        thongBaoList = new ArrayList<>();
        adapter = new ThongBaoAdapter(this, thongBaoList);
        rvThongBao.setLayoutManager(new LinearLayoutManager(this));
        rvThongBao.setAdapter(adapter);

        // Lấy danh sách thông báo
        getThongBaoList();

        // Xử lý sự kiện click
        btnBack.setOnClickListener(v -> finish());

        // Thiết lập bottom navigation
        bottomNavigationView = findViewById(R.id.navView);
        bottomNavigationView.setSelectedItemId(R.id.nav_thongbao);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(thong_bao.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_hoso) {
                    Intent intent = new Intent(thong_bao.this, ho_so.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_phieukham) {
                    Intent intent = new Intent(thong_bao.this, phieu_kham.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_thongbao) {
                    return true;
                } else if (itemId == R.id.nav_taikhoan) {
                    Intent intent = new Intent(thong_bao.this, thong_tin_tai_khoan.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void initViews() {
        rvThongBao = findViewById(R.id.rvThongBao);
        btnBack = findViewById(R.id.btnBack);
    }

    private void getThongBaoList() {
        // TODO: Lấy userId từ SharedPreferences hoặc từ đăng nhập
        int userId = 10; // Tạm thời hardcode

        RetrofitClient.getInstance()
            .getApi()
            .getThongBaoByUserId(userId)
            .enqueue(new Callback<List<ThongBaoResponse>>() {
                @Override
                public void onResponse(Call<List<ThongBaoResponse>> call, Response<List<ThongBaoResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        thongBaoList.clear();
                        thongBaoList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(thong_bao.this, "Lỗi khi lấy danh sách thông báo", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ThongBaoResponse>> call, Throwable t) {
                    Toast.makeText(thong_bao.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}