package com.example.madicalbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.madicalbooking.adapter.HoSoBenhNhanAdapter;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.HoSoBenhNhanResponse;
import com.example.madicalbooking.api.models.OtpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ho_so extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    private String KEY_USER_ID = "nguoi_dung_id";
    private String token;
    private String email;
    private String accountType;
    private String userId;
    RecyclerView recyclerView;

    HoSoBenhNhanAdapter adapter;
    LinearLayout createHoSo;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        token= sharedPreferences.getString(KEY_TOKEN, null);
        email = sharedPreferences.getString(KEY_USER_EMAIL, null);
        accountType = sharedPreferences.getString(KEY_ACCOUNT_TYPE, null);
        userId = sharedPreferences.getString(KEY_USER_ID, null);
        recyclerView = findViewById(R.id.recyclerViewHoSo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createHoSo=findViewById(R.id.createHoSo);
        createHoSo.setOnClickListener(view -> {
            Intent intent = new Intent(ho_so.this, sua_thong_tin_ca_nhan.class);
            startActivity(intent);
        });

        back=findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(ho_so.this, MainActivity.class);
            startActivity(intent);
        });
        bottomNavigationView = findViewById(R.id.navView);
        bottomNavigationView.setSelectedItemId(R.id.nav_hoso);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ho_so.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_hoso) {

                    return true;
                } else if (itemId == R.id.nav_phieukham) {

                    return true;
                } else if (itemId == R.id.nav_thongbao) {

                    return true;
                }else if (itemId == R.id.nav_taikhoan) {

                    return true;
                }
                return false;
            }
        });
        getListHoSoBenhNhan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListHoSoBenhNhan();

    }

    private void getListHoSoBenhNhan(){
        RetrofitClient
                .getInstance()
                .getApi()
                .getHoSoBenhNhanByNguoiDungId(userId)
                .enqueue(new Callback<List<HoSoBenhNhanResponse>>() {
                    @Override
                    public void onResponse(Call<List<HoSoBenhNhanResponse>> call, Response<List<HoSoBenhNhanResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<HoSoBenhNhanResponse> hoSoBenhNhanResponse = response.body();
                            adapter = new HoSoBenhNhanAdapter(ho_so.this, hoSoBenhNhanResponse, new HoSoBenhNhanAdapter.OnHoSoDeletedListener() {
                                @Override
                                public void onHoSoDeleted() {
                                    // Reload danh sách khi xóa xong
                                    getListHoSoBenhNhan();
                                }
                            });
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(ho_so.this, "Không thể tải danh sách hồ sơ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<HoSoBenhNhanResponse>> call, Throwable t) {
                        Log.e("TAG", "Lỗi kết nối: ", t);
                        Toast.makeText(ho_so.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}