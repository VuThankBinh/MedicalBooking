package com.example.madicalbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madicalbooking.R;
import com.example.madicalbooking.api.models.VaccineResponse;

import java.util.List;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.ViewHolder> {
    private List<VaccineResponse> vaccineList;
    private Context context;
    private OnVaccineClickListener listener;

    public interface OnVaccineClickListener {
        void onViewDetailClick(VaccineResponse vaccine);
        void onBookClick(VaccineResponse vaccine);
    }

    public VaccineAdapter(Context context, List<VaccineResponse> vaccineList, OnVaccineClickListener listener) {
        this.context = context;
        this.vaccineList = vaccineList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dong_tiem_chung, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VaccineResponse vaccine = vaccineList.get(position);
        holder.tvName.setText(vaccine.getName());
        holder.tvManufacturer.setText(vaccine.getManufacturer());
        holder.tvPrice.setText("700.000đ"); // Giá cố định cho mẫu

        holder.btnViewDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetailClick(vaccine);
            }
        });

        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(vaccine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vaccineList.size();
    }

    public void updateData(List<VaccineResponse> newList) {
        this.vaccineList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvManufacturer, tvPrice;
        Button btnViewDetail, btnBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tensp);
            tvManufacturer = itemView.findViewById(R.id.tenChuyenKhoa);
            tvPrice = itemView.findViewById(R.id.giasp);
            btnViewDetail = itemView.findViewById(R.id.btnXemChiTiet);
            btnBook = itemView.findViewById(R.id.btnDatLich);
        }
    }
} 