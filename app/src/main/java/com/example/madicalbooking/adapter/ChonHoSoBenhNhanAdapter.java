package com.example.madicalbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ChonHoSoBenhNhanAdapter extends RecyclerView.Adapter<ChonHoSoBenhNhanAdapter.HoSoViewHolder> {

    private List<HoSoBenhNhanResponse> hoSoList;
    private Context context;
    private OnHoSoClickListener onHoSoClickListener;

    // Interface để callback về activity khi xóa xong

    public interface OnHoSoClickListener {
        void onHoSoClick(HoSoBenhNhanResponse response);
    }

    public ChonHoSoBenhNhanAdapter(Context context, List<HoSoBenhNhanResponse> hoSoList, OnHoSoClickListener listener) {
        this.context = context;
        this.hoSoList = hoSoList;
        this.onHoSoClickListener = listener;
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
        holder.del.setVisibility(View.GONE);
        holder.hoso.setOnClickListener(view -> {
            onHoSoClickListener.onHoSoClick(hoSo);

        });

    }


    @Override
    public int getItemCount() {
        return hoSoList.size();
    }

    public static class HoSoViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtSDT, txtNgaySinh, txtDiaChi;
        ImageView del;
        LinearLayout hoso;

        public HoSoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtSDT = itemView.findViewById(R.id.txtSDT);
            txtNgaySinh = itemView.findViewById(R.id.txtNgaySinh);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            del = itemView.findViewById(R.id.del);
            hoso=itemView.findViewById(R.id.hoso);
        }
    }
}
