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
import com.example.madicalbooking.api.models.GoiKhamTrucTiepResponse;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class GoiKhamTrucTiepAdapter extends RecyclerView.Adapter<GoiKhamTrucTiepAdapter.GoiKhamTrucTiepViewHolder> {
    private List<GoiKhamTrucTiepResponse> list;
    private Context context;
    private OnGoiKhamTrucTiepClickListener listener;

    public interface OnGoiKhamTrucTiepClickListener {
        void onGoiKhamTrucTiepClick(GoiKhamTrucTiepResponse goiKham);
    }

    public GoiKhamTrucTiepAdapter(Context context, List<GoiKhamTrucTiepResponse> list, OnGoiKhamTrucTiepClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GoiKhamTrucTiepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goi_kham_truc_tiep, parent, false);
        return new GoiKhamTrucTiepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoiKhamTrucTiepViewHolder holder, int position) {
        GoiKhamTrucTiepResponse goiKham = list.get(position);
        holder.tenGoiKham.setText(goiKham.getTenGoiKham());
        holder.tenChuyenKhoa.setText(goiKham.getTenChuyenKhoa());
        
        // Định dạng giá tiền
        DecimalFormat formatter = new DecimalFormat("#,###đ");
        String formattedPrice = formatter.format(goiKham.getGiaTien());
        holder.giasp.setText(formattedPrice);

        holder.btnXemChiTiet.setOnClickListener(v -> {
            if (listener != null) {
                listener.onGoiKhamTrucTiepClick(goiKham);
            }
        });

        holder.btnDatLich.setOnClickListener(v -> {
            // Xử lý đặt lịch
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<GoiKhamTrucTiepResponse> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class GoiKhamTrucTiepViewHolder extends RecyclerView.ViewHolder {
        TextView tenGoiKham, tenChuyenKhoa, giasp;
        Button btnXemChiTiet, btnDatLich;

        public GoiKhamTrucTiepViewHolder(@NonNull View itemView) {
            super(itemView);
            tenGoiKham = itemView.findViewById(R.id.tensp);
            tenChuyenKhoa = itemView.findViewById(R.id.tenChuyenKhoa);
            giasp = itemView.findViewById(R.id.giasp);
            btnXemChiTiet = itemView.findViewById(R.id.btnXemChiTiet);
            btnDatLich = itemView.findViewById(R.id.btnDatLich);
        }
    }
} 