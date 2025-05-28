package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.madicalbooking.adapter.ChonHoSoBenhNhanAdapter;
import com.example.madicalbooking.adapter.HoSoBenhNhanAdapter;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.HoSoBenhNhanResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chon_ho_so extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    private String KEY_USER_ID = "nguoi_dung_id";
    private String userId;
    private int goiKhamId;
    RecyclerView recyclerView;

    ChonHoSoBenhNhanAdapter adapter;
    LinearLayout createHoSo;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_ho_so);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(KEY_USER_ID, "9");
        back=findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });
        recyclerView = findViewById(R.id.recyclerViewHoSo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        createHoSo=findViewById(R.id.createHoSo);
        createHoSo.setOnClickListener(view -> {
            Intent intent = new Intent(chon_ho_so.this, sua_thong_tin_ca_nhan.class);
            startActivity(intent);
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
                            Gson gson = new Gson();
                            String json = gson.toJson(hoSoBenhNhanResponse);
                            System.out.println("json: " + json);
                            adapter = new ChonHoSoBenhNhanAdapter(chon_ho_so.this, hoSoBenhNhanResponse, new ChonHoSoBenhNhanAdapter.OnHoSoClickListener() {
                                @Override
                                public void onHoSoClick(HoSoBenhNhanResponse response) {
                                    selectItemHoSo(response);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(chon_ho_so.this, "Không thể tải danh sách hồ sơ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<HoSoBenhNhanResponse>> call, Throwable t) {
                        Log.e("TAG", "Lỗi kết nối: ", t);
                        Toast.makeText(chon_ho_so.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void selectItemHoSo(HoSoBenhNhanResponse hoSoBenhNhanResponse) {
        Intent intent = new Intent(chon_ho_so.this, xac_nhan_thong_tin.class);
        Intent intent1= getIntent();
        goiKhamId=intent1.getIntExtra("goiKhamId", -1);
        System.out.println("goiKhamId1: "+goiKhamId);

        intent.putExtra("dichVu", intent1.getStringExtra("dichVu"));
        if(intent1.getStringExtra("dichVu").equals("GoiKham")){
            intent.putExtra("goiKhamid", goiKhamId);
            intent.putExtra("goiKhamJson", intent1.getStringExtra("goiKhamJson"));
        }
        Gson gson = new Gson();
        String json = gson.toJson(hoSoBenhNhanResponse);
        System.out.println("json: " + json);
        intent.putExtra("hoSoBenhNhanResponse", json);
        startActivity(intent);
    }

}