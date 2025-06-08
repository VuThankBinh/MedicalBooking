package com.example.madicalbooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madicalbooking.adapter.VaccineAdapter;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.VaccineResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dat_lich_tiem_chung extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText edtSearch;
    private ImageView btnBack;
    private VaccineAdapter adapter;
    private List<VaccineResponse> vaccineList;
    private List<VaccineResponse> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich_tiem_chung);

        // Khởi tạo views
        initViews();
        
        // Khởi tạo RecyclerView
        vaccineList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new VaccineAdapter(this, filteredList, new VaccineAdapter.OnVaccineClickListener() {
            @Override
            public void onViewDetailClick(VaccineResponse vaccine) {
                showVaccineDetail(vaccine);
            }

            @Override
            public void onBookClick(VaccineResponse vaccine) {
                // TODO: Chuyển đến màn hình đặt lịch
                Toast.makeText(dat_lich_tiem_chung.this, 
                    "Đặt lịch tiêm vaccine: " + vaccine.getName(), 
                    Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Xử lý sự kiện tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterVaccines(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý sự kiện nút back
        btnBack.setOnClickListener(v -> finish());

        // Lấy danh sách vaccine
        getVaccineList();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewVaccine);
        edtSearch = findViewById(R.id.TimKiem);
        btnBack = findViewById(R.id.btnBack);
    }

    private void getVaccineList() {
        RetrofitClient.getInstance()
            .getApi()
            .getVaccines()
            .enqueue(new Callback<List<VaccineResponse>>() {
                @Override
                public void onResponse(Call<List<VaccineResponse>> call, Response<List<VaccineResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        vaccineList.clear();
                        vaccineList.addAll(response.body());
                        filterVaccines(edtSearch.getText().toString());
                    } else {
                        Toast.makeText(dat_lich_tiem_chung.this, 
                            "Lỗi khi lấy danh sách vaccine", 
                            Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<VaccineResponse>> call, Throwable t) {
                    Toast.makeText(dat_lich_tiem_chung.this, 
                        "Lỗi kết nối: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void filterVaccines(String query) {
        if (query.isEmpty()) {
            filteredList.clear();
            filteredList.addAll(vaccineList);
        } else {
            filteredList = vaccineList.stream()
                .filter(vaccine -> 
                    vaccine.getName().toLowerCase().contains(query.toLowerCase()) ||
                    vaccine.getManufacturer().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        }
        adapter.updateData(filteredList);
    }

    private void showVaccineDetail(VaccineResponse vaccine) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_chi_tiet_vaccine, null);
        builder.setView(dialogView);

        // Ánh xạ các view trong dialog
        TextView tvName = dialogView.findViewById(R.id.tvName);
        TextView tvManufacturer = dialogView.findViewById(R.id.tvManufacturer);
        TextView tvDescription = dialogView.findViewById(R.id.tvDescription);
        TextView tvDoses = dialogView.findViewById(R.id.tvDoses);
        TextView tvContraindications = dialogView.findViewById(R.id.tvContraindications);
        TextView tvSideEffects = dialogView.findViewById(R.id.tvSideEffects);
        Button btnClose = dialogView.findViewById(R.id.btnClose);

        // Hiển thị thông tin vaccine
        tvName.setText(vaccine.getName());
        tvManufacturer.setText("Nhà sản xuất: " + vaccine.getManufacturer());
        tvDescription.setText(vaccine.getDescription());
        tvDoses.setText("Số liều: " + vaccine.getNumberOfDoses() + "\n" +
                       "Khoảng cách giữa các liều: " + vaccine.getIntervalBetweenDoses() + " ngày");
        tvContraindications.setText(vaccine.getContraindications());
        tvSideEffects.setText(vaccine.getSideEffects());

        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Xử lý sự kiện đóng dialog
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }
}