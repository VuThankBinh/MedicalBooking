package com.example.madicalbooking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.madicalbooking.api.ApiConfig;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.GoiKhamResponse;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class chon_thong_tin_goi_kham extends AppCompatActivity {

    TextView tvNameGoi, ngayChon, gioChon;
    LinearLayout tvChonNgay;
    LinearLayout tvChonGio;
    ImageView back;
    Button btnTiepTuc;
    private int goiKhamid;
    String tenGoi,giaGoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chon_thong_tin_goi_kham);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        anhXa();
        tvChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        tvChonGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeOptions();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            goiKhamid = intent.getIntExtra("goiKhamId", 1);
            System.out.println("goiKhamId3: "+goiKhamid);
            getGoiKhamDetail(goiKhamid);
        }
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chon_thong_tin_goi_kham.this, chon_ho_so.class);
                intent.putExtra("goiKhamId", goiKhamid);
                intent.putExtra("dichVu","GoiKham");
                Gson gson = new Gson();
               goiKhamJson goiKhamJson = new goiKhamJson(tenGoi,giaGoi,ngayChon.getText().toString(),gioChon.getText().toString());
                String json = gson.toJson(goiKhamJson);
                intent.putExtra("goiKhamJson",json);
                startActivity(intent);
            }
        });

    }
    private void getGoiKhamDetail(int goiKhamId) {
        RetrofitClient
                .getInstance()
                .getApi()
                .getGoiKhamDetail(goiKhamId)
                .enqueue(new Callback<GoiKhamResponse>() {
                    @Override
                    public void onResponse(Call<GoiKhamResponse> call, Response<GoiKhamResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            GoiKhamResponse goiKham = response.body();
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            System.out.println("trả về: " + json);

                            // Hiển thị giá theo format tiền Việt Nam
                            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                            String formatted = formatter.format(goiKham.getGia());
                            tvNameGoi.setText(goiKham.getTenGoi() +" - " + formatted +" đ");
                            tenGoi=goiKham.getTenGoi();
                            giaGoi=String.valueOf(goiKham.getGia());
                        }
                        else {
                            Toast.makeText(chon_thong_tin_goi_kham.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            System.out.println("Lỗi kết nối: "+ json);
                        }

                    }

                    @Override
                    public void onFailure(Call<GoiKhamResponse> call, Throwable t) {
                        System.out.println("Lỗi gọi API: " + t.getMessage());
                    }
                });
    }
    private void showDatePicker() {
        Locale locale = new Locale("vi", "VN");
        Locale.setDefault(locale);

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, month, dayOfMonth);

                    int dayOfWeek = selectedCal.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                        Toast.makeText(this, "Vui lòng chọn từ Thứ 2 đến Thứ 6", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Định dạng ngày tiếng Việt
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy", locale);
                    String selectedDate = sdf.format(selectedCal.getTime());

                    ngayChon.setText(capitalizeFirstLetter(selectedDate)); // để viết hoa chữ cái đầu
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 7);

        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }

    // Viết hoa chữ cái đầu (ví dụ: "thứ hai, 22/05/2025" -> "Thứ hai, 22/05/2025")
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.length() == 0) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


    private void showTimeOptions() {
        final String[] timeSlots = {
                "08:00 - 09:00",
                "09:00 - 10:00",
                "10:00 - 11:00",
                "13:00 - 14:00",
                "14:00 - 15:00",
                "15:00 - 16:00"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn giờ khám");
        builder.setItems(timeSlots, (dialog, which) -> {
            gioChon.setText(timeSlots[which]);
        });
        builder.show();
    }
    private void anhXa(){
        tvNameGoi = findViewById(R.id.tvNameGoi);
        ngayChon = findViewById(R.id.ngayChon);
        gioChon = findViewById(R.id.gioChon);
        tvChonNgay = findViewById(R.id.tvChonNgay);
        tvChonGio = findViewById(R.id.tvChonGio);
        back=findViewById(R.id.back);
        btnTiepTuc=findViewById(R.id.btnTiepTuc);
    }
    public class goiKhamJson{
        private  String tenGoi;
        private  String giaGoi;
        private  String ngayChon;
        private  String gioChon;

        public goiKhamJson(String tenGoi, String giaGoi, String ngayChon, String gioChon) {
            this.tenGoi = tenGoi;
            this.giaGoi = giaGoi;
            this.ngayChon = ngayChon;
            this.gioChon = gioChon;
        }

        public String getTenGoi() {
            return tenGoi;
        }

        public void setTenGoi(String tenGoi) {
            this.tenGoi = tenGoi;
        }

        public String getGiaGoi() {
            return giaGoi;
        }

        public void setGiaGoi(String giaGoi) {
            this.giaGoi = giaGoi;
        }

        public String getNgayChon() {
            return ngayChon;
        }

        public void setNgayChon(String ngayChon) {
            this.ngayChon = ngayChon;
        }

        public String getGioChon() {
            return gioChon;
        }

        public void setGioChon(String gioChon) {
            this.gioChon = gioChon;
        }
    }

}