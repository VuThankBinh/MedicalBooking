package com.example.madicalbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.madicalbooking.adapter.GoiKhamAdapter;
import com.example.madicalbooking.adapter.BacSiAdapter;
import com.example.madicalbooking.adapter.HoSoBenhNhanAdapter;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.GoiKhamResponse;
import com.example.madicalbooking.api.models.BacSiResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    RecyclerView GoiKhamRecyclerView;
    RecyclerView BacSiRecyclerView;
    GoiKhamAdapter goiKhamAdapter;
    BacSiAdapter bacSiAdapter;
    List<GoiKhamResponse> GoiKhamList;
    List<BacSiResponse> BacSiList;
    EditText TimKiem;
    LinearLayout datlichtiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        TimKiem = findViewById(R.id.TimKiem);
        TimKiem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, tim_kiem_goi_kham.class);
            startActivity(intent);
        });
        datlichtiem=findViewById(R.id.datlichtiem);
        datlichtiem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, dat_lich_tiem_chung.class);
            startActivity(intent);
        });

        // Khởi tạo RecyclerView cho gói khám
        GoiKhamRecyclerView = findViewById(R.id.GoiKhamRecyclerView);
        GoiKhamRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo RecyclerView cho bác sĩ
        BacSiRecyclerView = findViewById(R.id.BacSiRecyclerView);
        BacSiRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo danh sách
        GoiKhamList = new ArrayList<>();
        BacSiList = new ArrayList<>();

        bottomNavigationView = findViewById(R.id.navView);
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
                } else if (itemId == R.id.nav_taikhoan) {
                    Intent intent = new Intent(MainActivity.this, thong_tin_tai_khoan.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        getGoiKham();
        getBacSi();
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
                            goiKhamAdapter = new GoiKhamAdapter(MainActivity.this, GoiKhamList, new GoiKhamAdapter.OnGoiKhamClickListener() {
                                @Override
                                public void GoiKhamClickListener(GoiKhamResponse goiKham) {
                                    Intent intent = new Intent(MainActivity.this, thong_tin_goi_kham.class);
                                    intent.putExtra("goiKhamId", goiKham.getMaGoi());
                                    startActivity(intent);
                                }
                            });
                            GoiKhamRecyclerView.setAdapter(goiKhamAdapter);
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GoiKhamResponse>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getBacSi() {
        RetrofitClient
                .getInstance()
                .getApi()
                .getBacSiAll()
                .enqueue(new Callback<List<BacSiResponse>>() {
                    @Override
                    public void onResponse(Call<List<BacSiResponse>> call, Response<List<BacSiResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            System.out.println("Danh sách bác sĩ: " + json);
                            BacSiList = response.body();
                            bacSiAdapter = new BacSiAdapter(MainActivity.this, BacSiList, new BacSiAdapter.OnBacSiClickListener() {
                                @Override
                                public void onBacSiClick(BacSiResponse bacSi) {
                                    // TODO: Xử lý sự kiện click vào bác sĩ
                                    Toast.makeText(MainActivity.this, "Bạn đã chọn bác sĩ: " + bacSi.getHoTen(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            BacSiRecyclerView.setAdapter(bacSiAdapter);
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BacSiResponse>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}