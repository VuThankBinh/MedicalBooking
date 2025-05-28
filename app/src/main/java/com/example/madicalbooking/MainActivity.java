package com.example.madicalbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.madicalbooking.adapter.GoiKhamAdapter;
import com.example.madicalbooking.adapter.HoSoBenhNhanAdapter;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.GoiKhamResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    RecyclerView GoiKhamRecyclerView;
    GoiKhamAdapter adapter;
    List<GoiKhamResponse> GoiKhamList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        GoiKhamRecyclerView = findViewById(R.id.GoiKhamRecyclerView);
        GoiKhamRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        bottomNavigationView = findViewById(R.id.navView);
        // Thiết lập item được chọn là Practice
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    return true;
                } else if (itemId == R.id.nav_hoso) {
                    Intent intent = new Intent(MainActivity.this, ho_so.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_phieukham) {
                    Intent intent = new Intent(MainActivity.this, phieu_kham.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_thongbao) {
                    Intent intent = new Intent(MainActivity.this, thong_bao.class);
                    startActivity(intent);
                    finish();
                    return true;
                }else if (itemId == R.id.nav_taikhoan) {
                    Intent intent = new Intent(MainActivity.this, thong_tin_tai_khoan.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
        getGoiKham();
    }
    private void getGoiKham() {
        RetrofitClient
                .getInstance()
                .getApi()
                .getGoiKhamAll()
                .enqueue(new Callback<List<GoiKhamResponse>>() {
                    @Override
                    public void onResponse(Call<List<GoiKhamResponse>> call, Response<List<GoiKhamResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            System.out.println("trả về: "+json);
                            GoiKhamList = response.body();
                            // Khởi tạo adapter
                            adapter = new GoiKhamAdapter(MainActivity.this, GoiKhamList, new GoiKhamAdapter.OnGoiKhamClickListener() {
                                @Override
                                public void GoiKhamClickListener(GoiKhamResponse goiKham) {
                                    // Xử lý sự kiện click, ví dụ:
//                                    Toast.makeText(MainActivity.this, "Bạn đã chọn: " + goiKham.getTenGoi(), Toast.LENGTH_SHORT).show();

                                    // Hoặc chuyển sang màn hình chi tiết:
                                    Intent intent = new Intent(MainActivity.this, thong_tin_goi_kham.class);
                                    intent.putExtra("goiKhamId", goiKham.getMaGoi());
                                    startActivity(intent);
                                }
                            });

                            GoiKhamRecyclerView.setAdapter(adapter);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GoiKhamResponse>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

}