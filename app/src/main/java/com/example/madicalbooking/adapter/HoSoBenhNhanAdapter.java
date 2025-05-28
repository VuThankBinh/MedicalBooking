package com.example.madicalbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madicalbooking.R;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.HoSoBenhNhanResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoSoBenhNhanAdapter extends RecyclerView.Adapter<HoSoBenhNhanAdapter.HoSoViewHolder> {

    private List<HoSoBenhNhanResponse> hoSoList;
    private Context context;
    private OnHoSoDeletedListener listener;

    // Interface để callback về activity khi xóa xong
    public interface OnHoSoDeletedListener {
        void onHoSoDeleted();
    }

    public HoSoBenhNhanAdapter(Context context, List<HoSoBenhNhanResponse> hoSoList, OnHoSoDeletedListener listener) {
        this.context = context;
        this.hoSoList = hoSoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HoSoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_ho_so, parent, false);
        return new HoSoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoSoViewHolder holder, int position) {
        HoSoBenhNhanResponse hoSo = hoSoList.get(position);
        holder.txtTen.setText(hoSo.getHoTen());
        holder.txtSDT.setText(hoSo.getSoDienThoai());
        holder.txtNgaySinh.setText(hoSo.getNgaySinh());
        holder.txtDiaChi.setText(hoSo.getNoiThuongTru());

        holder.del.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa hồ sơ này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        // Gọi API xóa khi người dùng xác nhận
                        deleteHoSo(hoSo.getMaHoSo());
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

    }
    private void deleteHoSo(int hoSoId) {
        RetrofitClient.getInstance()
                .getApi()
                .deleteHoSoBenhNhan(hoSoId) // đảm bảo HoSoBenhNhanResponse có phương thức getId()
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        System.out.println("response.isSuccessful() = " + response.isSuccessful() + "");
                        System.out.println("response.code() = " + response.code() + "");
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Đã xóa hồ sơ", Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.onHoSoDeleted(); // callback về activity để reload danh sách
                            }
                        } else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return hoSoList.size();
    }

    public static class HoSoViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtSDT, txtNgaySinh, txtDiaChi;
        ImageView del;

        public HoSoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtSDT = itemView.findViewById(R.id.txtSDT);
            txtNgaySinh = itemView.findViewById(R.id.txtNgaySinh);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            del = itemView.findViewById(R.id.del);
        }
    }
}
