package com.example.madicalbooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madicalbooking.R;
import com.example.madicalbooking.api.ApiConfig;
import com.example.madicalbooking.api.models.GoiKhamResponse;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class GoiKhamAdapter extends RecyclerView.Adapter<GoiKhamAdapter.GoiKhamViewHolder>{
    private List<GoiKhamResponse> list;
    private Context context;
    private OnGoiKhamClickListener listener;

    // Interface để callback về activity khi xóa xong
    public interface OnGoiKhamClickListener {
        void GoiKhamClickListener(GoiKhamResponse goiKham);
    }

    public GoiKhamAdapter(Context context, List<GoiKhamResponse> list, OnGoiKhamClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    @NonNull
    @Override
    public GoiKhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_goi_kham, parent, false);
        return new GoiKhamViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onBindViewHolder(@NonNull GoiKhamViewHolder holder, int position) {
        GoiKhamResponse goikham = list.get(position);
        holder.tensp.setText(goikham.getTenGoi());

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formatted = formatter.format(goikham.getGia());
        holder.gia.setText(formatted + " đ");

        holder.btnXN.setOnClickListener(view -> {
            if (listener != null) {
                listener.GoiKhamClickListener(goikham);
            }
        });
        String imageUrl =  ApiConfig.Base_url+ "uploads/images/" + goikham.getAnh();

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.anhnengoi)
                .error(R.drawable.anhnengoi)
                .centerCrop()
                .into(holder.anhsp);



    }
    public static class GoiKhamViewHolder extends RecyclerView.ViewHolder {
        TextView tensp, gia;
        ImageView anhsp;
        Button btnXN;

        public GoiKhamViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.tensp);
            gia = itemView.findViewById(R.id.giasp);
            anhsp = itemView.findViewById(R.id.anhsp);
            btnXN=itemView.findViewById(R.id.btnXN);
        }
    }
}
