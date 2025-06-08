package com.example.madicalbooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.model.ApiResponse;
import com.example.madicalbooking.api.models.HoSoBenhNhanResponse;
import com.example.madicalbooking.api.models.GoiKhamResponse;
import com.example.madicalbooking.model.PhieuKham;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class phieu_kham extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private Button btnChuaThanhToan, btnDaThanhToan, btnDaHuy;
    private PhieuKhamAdapter adapter;
    private List<PhieuKham> phieuKhamList;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ZimStayPrefs";
    private String currentStatus = "CHUA_THANH_TOAN";
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phieu_kham);

        // Khởi tạo views
        recyclerView = findViewById(R.id.recyclerViewPhieuKham);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnChuaThanhToan = findViewById(R.id.btnChuaThanhToan);
        btnDaThanhToan = findViewById(R.id.btnDaThanhToan);
        btnDaHuy = findViewById(R.id.btnDaHuy);
        
        phieuKhamList = new ArrayList<>();
        adapter = new PhieuKhamAdapter(phieu_kham.this, phieuKhamList);
        
        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Lấy thông tin người dùng
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String userId = sharedPreferences.getString("nguoi_dung_id", "");

        if (userId != null && !userId.isEmpty()) {
            setupButtonListeners(userId);
            loadPhieuKham(userId, currentStatus);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
        bottomNavigationView = findViewById(R.id.navView);
        bottomNavigationView.setSelectedItemId(R.id.nav_phieukham);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(phieu_kham.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_hoso) {
                    Intent intent = new Intent(phieu_kham.this, ho_so.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_phieukham) {

                    return true;
                } else if (itemId == R.id.nav_thongbao) {
                    Intent intent = new Intent(phieu_kham.this, thong_bao.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_taikhoan) {
                    Intent intent = new Intent(phieu_kham.this, thong_tin_tai_khoan.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupButtonListeners(String userId) {
        btnChuaThanhToan.setOnClickListener(v -> {
            currentStatus = "CHUA_THANH_TOAN";
            updateButtonStates();
            loadPhieuKham(userId, currentStatus);
        });

        btnDaThanhToan.setOnClickListener(v -> {
            currentStatus = "DA_THANH_TOAN";
            updateButtonStates();
            loadPhieuKham(userId, currentStatus);
        });

        btnDaHuy.setOnClickListener(v -> {
            currentStatus = "DA_HUY";
            updateButtonStates();
            loadPhieuKham(userId, currentStatus);
        });

        // Set initial button state
        updateButtonStates();
    }

    private void updateButtonStates() {
        btnChuaThanhToan.setBackgroundTintList(getColorStateList(
            currentStatus.equals("CHUA_THANH_TOAN") ? R.color.menu_item_color : R.color.nav_item_color));
        btnDaThanhToan.setBackgroundTintList(getColorStateList(
            currentStatus.equals("DA_THANH_TOAN") ? R.color.menu_item_color : R.color.nav_item_color));
        btnDaHuy.setBackgroundTintList(getColorStateList(
            currentStatus.equals("DA_HUY") ? R.color.menu_item_color : R.color.nav_item_color));
    }

    private void loadPhieuKham(String userId, String trangThai) {
        System.out.println("userId: " + userId);
        System.out.println("url: " + RetrofitClient
                .getInstance()
                .getApi()
                .getPhieuKham(userId, trangThai).request().url());

        phieuKhamList.clear();
        adapter.notifyDataSetChanged();

        RetrofitClient
                .getInstance()
                .getApi()
                .getPhieuKham(userId, trangThai)
                .enqueue(new Callback<List<PhieuKham>>() {
                    @Override
                    public void onResponse(Call<List<PhieuKham>> call, Response<List<PhieuKham>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Gson gson = new Gson();
                            Log.d("PhieuKham", "Data: " + gson.toJson(response.body()));
                            phieuKhamList.addAll(response.body());
                            updateUI();
                        } else {
                            updateUI();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PhieuKham>> call, Throwable t) {
                        Log.e("PhieuKham", "Lỗi: " + t.getMessage());
                        runOnUiThread(() -> {
                            Toast.makeText(phieu_kham.this, "Lỗi khi tải phiếu khám", Toast.LENGTH_SHORT).show();
                            updateUI();
                        });
                    }
                });
    }

    private void updateUI() {
        runOnUiThread(() -> {
            if (phieuKhamList.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }
}

class PhieuKhamAdapter extends RecyclerView.Adapter<PhieuKhamAdapter.ViewHolder> {
    private List<PhieuKham> phieuKhamList;
    private Context context;

    public PhieuKhamAdapter(Context context, List<PhieuKham> phieuKhamList) {
        this.context = context;
        this.phieuKhamList = phieuKhamList;
    }

    @Override
    public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phieu_kham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhieuKham phieuKham = phieuKhamList.get(position);
        
        holder.tvMaDangKy.setText("Mã đăng ký: " + phieuKham.getMaDangKy());
        holder.tvNgayDangKy.setText("Ngày đăng ký: " + formatDate(phieuKham.getNgayDangKy()));
        holder.tvNgayThucHien.setText("Ngày thực hiện: " + phieuKham.getNgayThucHien());
        holder.tvGioKham.setText("Giờ khám: " + (phieuKham.getGioKham() != null ? phieuKham.getGioKham().trim() : "Chưa có"));
        holder.tvGiaTien.setText("Giá tiền: " + (phieuKham.getGiaTien() != null ? 
                String.format("%,.0f VND", phieuKham.getGiaTien()) : "Chưa có"));
        holder.tvTrangThai.setText("Trạng thái: " + phieuKham.getTrangThai());

        // Thêm sự kiện click để hiển thị thông tin chi tiết
        holder.itemView.setOnClickListener(v -> {
            showChiTietPhieuKham(phieuKham);
        });
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(dateStr));
        } catch (Exception e) {
            try {
                // Thử parse với định dạng khác nếu định dạng đầu tiên thất bại
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                return outputFormat.format(inputFormat.parse(dateStr));
            } catch (Exception ex) {
                return dateStr;
            }
        }
    }

    private void showChiTietPhieuKham(PhieuKham phieuKham) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_chi_tiet_phieu_kham, null);
        builder.setView(dialogView);

        // Ánh xạ các view trong dialog
        TextView tvHoTen = dialogView.findViewById(R.id.tvHoTen);
        TextView tvNgaySinh = dialogView.findViewById(R.id.tvNgaySinh);
        TextView tvGioiTinh = dialogView.findViewById(R.id.tvGioiTinh);
        TextView tvSoDienThoai = dialogView.findViewById(R.id.tvSoDienThoai);
        TextView tvTenGoi = dialogView.findViewById(R.id.tvTenGoi);
        TextView tvMoTa = dialogView.findViewById(R.id.tvMoTa);
        TextView tvGia = dialogView.findViewById(R.id.tvGia);
        TextView tvThoiGianThucHien = dialogView.findViewById(R.id.tvThoiGianThucHien);
        TextView tvNgayThucHien = dialogView.findViewById(R.id.tvNgayThucHien);
        TextView tvGioKham = dialogView.findViewById(R.id.tvGioKham);
        TextView tvTrangThai = dialogView.findViewById(R.id.tvTrangThai);
        Button btnDong = dialogView.findViewById(R.id.btnDong);

        // Tạo dialog
        AlertDialog dialog = builder.create();

        // Gọi API lấy thông tin hồ sơ bệnh nhân
        RetrofitClient.getInstance().getApi()
                .getHoSoBenhNhanDetail(phieuKham.getMaHoSo())
                .enqueue(new Callback<HoSoBenhNhanResponse>() {
                    @Override
                    public void onResponse(Call<HoSoBenhNhanResponse> call, Response<HoSoBenhNhanResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HoSoBenhNhanResponse hoSo = response.body();
                            tvHoTen.setText("Họ tên: " + hoSo.getHoTen());
                            tvNgaySinh.setText("Ngày sinh: " + hoSo.getNgaySinh());
                            tvGioiTinh.setText("Giới tính: " + hoSo.getGioiTinh());
                            tvSoDienThoai.setText("Số điện thoại: " + hoSo.getSoDienThoai());
                        }
                    }

                    @Override
                    public void onFailure(Call<HoSoBenhNhanResponse> call, Throwable t) {
                        Toast.makeText(context, "Lỗi khi lấy thông tin hồ sơ", Toast.LENGTH_SHORT).show();
                    }
                });

        // Gọi API lấy thông tin gói khám
        RetrofitClient.getInstance().getApi()
                .getGoiKhamDetail(phieuKham.getMaGoi())
                .enqueue(new Callback<GoiKhamResponse>() {
                    @Override
                    public void onResponse(Call<GoiKhamResponse> call, Response<GoiKhamResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            GoiKhamResponse goiKham = response.body();
                            tvTenGoi.setText("Tên gói: " + goiKham.getTenGoi());
                            tvMoTa.setText("Mô tả: " + goiKham.getMoTa());
                            tvGia.setText("Giá: " + String.format("%,.0f VND", goiKham.getGia()));
                            tvThoiGianThucHien.setText("Thời gian thực hiện: " + goiKham.getThoiGianThucHien() + " phút");
                        }
                    }

                    @Override
                    public void onFailure(Call<GoiKhamResponse> call, Throwable t) {
                        Toast.makeText(context, "Lỗi khi lấy thông tin gói khám", Toast.LENGTH_SHORT).show();
                    }
                });

        // Hiển thị thông tin lịch hẹn
        tvNgayThucHien.setText("Ngày thực hiện: " + phieuKham.getNgayThucHien());
        tvGioKham.setText("Giờ khám: " + (phieuKham.getGioKham() != null ? phieuKham.getGioKham().trim() : "Chưa có"));
        tvTrangThai.setText("Trạng thái: " + phieuKham.getTrangThai());

        // Xử lý sự kiện đóng dialog
        btnDong.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return phieuKhamList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaDangKy, tvNgayDangKy, tvNgayThucHien, tvGioKham, tvGiaTien, tvTrangThai;

        public ViewHolder(android.view.View view) {
            super(view);
            tvMaDangKy = view.findViewById(R.id.tvMaDangKy);
            tvNgayDangKy = view.findViewById(R.id.tvNgayDangKy);
            tvNgayThucHien = view.findViewById(R.id.tvNgayThucHien);
            tvGioKham = view.findViewById(R.id.tvGioKham);
            tvGiaTien = view.findViewById(R.id.tvGiaTien);
            tvTrangThai = view.findViewById(R.id.tvTrangThai);
        }
    }
}