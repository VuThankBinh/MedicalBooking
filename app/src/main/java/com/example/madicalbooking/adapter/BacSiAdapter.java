package com.example.madicalbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madicalbooking.R;
import com.example.madicalbooking.api.ApiConfig;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.BacSiResponse;
import com.example.madicalbooking.api.models.KhoaResponse;
import com.example.madicalbooking.thong_tin_bac_si;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BacSiAdapter extends RecyclerView.Adapter<BacSiAdapter.ViewHolder> {
    private List<BacSiResponse> bacSiList;
    private Context context;
    private OnBacSiClickListener listener;

    public interface OnBacSiClickListener {
        void onBacSiClick(BacSiResponse bacSi);
    }

    public BacSiAdapter(Context context, List<BacSiResponse> bacSiList, OnBacSiClickListener listener) {
        this.context = context;
        this.bacSiList = bacSiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_bac_si, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BacSiResponse bacSi = bacSiList.get(position);
        
        holder.tvTenBacSi.setText(bacSi.getHoTen());
        
        // Load ảnh bác sĩ
        if (bacSi.getAnhDaiDien() != null && !bacSi.getAnhDaiDien().isEmpty()) {
            Glide.with(context)
                .load(ApiConfig.Base_url + "uploads/images/" + bacSi.getAnhDaiDien())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgBacSi);
        } else {
            holder.imgBacSi.setImageResource(R.drawable.ic_launcher_background);
        }

        // Lấy thông tin khoa
        RetrofitClient.getInstance()
            .getApi()
            .getKhoaById(bacSi.getIdKhoa())
            .enqueue(new Callback<KhoaResponse>() {
                @Override
                public void onResponse(Call<KhoaResponse> call, Response<KhoaResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        KhoaResponse khoa = response.body();
                        holder.tvKhoa.setText(khoa.getTenKhoa());
                    }
                }

                @Override
                public void onFailure(Call<KhoaResponse> call, Throwable t) {
                    Toast.makeText(context, "Lỗi khi lấy thông tin khoa", Toast.LENGTH_SHORT).show();
                }
            });
        
        holder.btnTuVan.setOnClickListener(v -> {
            Intent intent = new Intent(context, thong_tin_bac_si.class);
            intent.putExtra("bacSiId", bacSi.getIdBacSi());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bacSiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBacSi;
        TextView tvTenBacSi, tvKhoa;
        Button btnTuVan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBacSi = itemView.findViewById(R.id.anhsp);
            tvTenBacSi = itemView.findViewById(R.id.tensp);
            tvKhoa = itemView.findViewById(R.id.tvKhoa);
            btnTuVan = itemView.findViewById(R.id.btnLogin);
        }
    }
} 