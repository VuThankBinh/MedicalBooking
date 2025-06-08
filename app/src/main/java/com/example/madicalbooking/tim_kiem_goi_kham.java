package com.example.madicalbooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madicalbooking.adapter.GoiKhamTrucTiepAdapter;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.GoiKhamTrucTiepResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tim_kiem_goi_kham extends AppCompatActivity {
    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private TextView textViewSearchInfo;
    private ImageView imageViewBack;
    private GoiKhamTrucTiepAdapter goiKhamAdapter;
    private List<GoiKhamTrucTiepResponse> goiKhamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_goi_kham);
        
        // Ánh xạ view
        editTextSearch = findViewById(R.id.TimKiem);
        recyclerView = findViewById(R.id.recyclerViewSearch);
        textViewSearchInfo = findViewById(R.id.textViewSearchInfo);
        imageViewBack = findViewById(R.id.imageViewBack);
        
        // Khởi tạo RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        goiKhamList = new ArrayList<>();
        goiKhamAdapter = new GoiKhamTrucTiepAdapter(this, goiKhamList, new GoiKhamTrucTiepAdapter.OnGoiKhamTrucTiepClickListener() {
            @Override
            public void onGoiKhamTrucTiepClick(GoiKhamTrucTiepResponse goiKham) {
                showGoiKhamDetail(goiKham);
            }
        });
        recyclerView.setAdapter(goiKhamAdapter);

        // Xử lý sự kiện back
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý sự kiện tìm kiếm
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchGoiKham(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Load tất cả gói khám khi mới vào
        getAllGoiKham();
    }

    private void searchGoiKham(String query) {
        if (query.isEmpty()) {
            getAllGoiKham();
            return;
        }

        List<GoiKhamTrucTiepResponse> filteredList = new ArrayList<>();
        for (GoiKhamTrucTiepResponse goiKham : goiKhamList) {
            if (goiKham.getTenGoiKham().toLowerCase().contains(query.toLowerCase()) ||
                goiKham.getTenChuyenKhoa().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(goiKham);
            }
        }
        goiKhamAdapter.updateData(filteredList);
        textViewSearchInfo.setText("Kết quả tìm kiếm cho '" + query + "'");
    }

    private void getAllGoiKham() {
        System.out.println("url: "+ RetrofitClient
                .getInstance()
                .getApi()
                .getGoiKhamTrucTiep().request().url().toString());
        RetrofitClient
                .getInstance()
                .getApi()
                .getGoiKhamTrucTiep()
                .enqueue(new Callback<List<GoiKhamTrucTiepResponse>>() {
                    @Override
                    public void onResponse(Call<List<GoiKhamTrucTiepResponse>> call, Response<List<GoiKhamTrucTiepResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            goiKhamList = response.body();
                            goiKhamAdapter.updateData(goiKhamList);
                            textViewSearchInfo.setText("Tất cả gói khám");
                        } else {
                            Toast.makeText(tim_kiem_goi_kham.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GoiKhamTrucTiepResponse>> call, Throwable t) {
                        Toast.makeText(tim_kiem_goi_kham.this, "Lỗi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showGoiKhamDetail(GoiKhamTrucTiepResponse goiKham) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_chi_tiet_goi_kham, null);
        builder.setView(dialogView);

        // Ánh xạ các view trong dialog
        TextView tvTenGoiKham = dialogView.findViewById(R.id.tvTenGoiKham);
        TextView tvChuyenKhoa = dialogView.findViewById(R.id.tvChuyenKhoa);
        TextView tvMoTa = dialogView.findViewById(R.id.tvMoTa);
        TextView tvDanhSachDichVu = dialogView.findViewById(R.id.tvDanhSachDichVu);
        TextView tvGia = dialogView.findViewById(R.id.tvGia);
        Button btnDatLich = dialogView.findViewById(R.id.btnDatLich);
        Button btnClose = dialogView.findViewById(R.id.btnClose);

        // Hiển thị thông tin gói khám
        tvTenGoiKham.setText(goiKham.getTenGoiKham());
        tvChuyenKhoa.setText("Chuyên khoa: " + goiKham.getTenChuyenKhoa());
        tvMoTa.setText(goiKham.getMoTa());
//        tvDanhSachDichVu.setText(goiKham.getThoiGianKham());
        tvGia.setText(String.format("%,dđ", goiKham.getGiaTien()));

        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Xử lý sự kiện đóng dialog
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Xử lý sự kiện đặt lịch
        btnDatLich.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(tim_kiem_goi_kham.this, thong_tin_goi_kham.class);
            intent.putExtra("goiKhamId", goiKham.getIdGoiKham());
            startActivity(intent);
        });
    }
}