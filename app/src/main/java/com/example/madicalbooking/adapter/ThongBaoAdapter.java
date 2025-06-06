package com.example.madicalbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madicalbooking.R;
import com.example.madicalbooking.api.models.ThongBaoResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {
    private List<ThongBaoResponse> thongBaoList;
    private Context context;

    public ThongBaoAdapter(Context context, List<ThongBaoResponse> thongBaoList) {
        this.context = context;
        this.thongBaoList = thongBaoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_thong_bao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongBaoResponse thongBao = thongBaoList.get(position);
        
        holder.tvTieuDe.setText(thongBao.getTitle());
        holder.tvNoiDung.setText(thongBao.getMessage());
        
        // Format thời gian
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(thongBao.getCreatedAt());
            if (date != null) {
                holder.tvThoiGian.setText(outputFormat.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return thongBaoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTieuDe, tvNoiDung, tvThoiGian;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDe);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
        }
    }
} 