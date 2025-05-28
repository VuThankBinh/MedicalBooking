package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class thanh_toan extends AppCompatActivity {
    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    private String KEY_USER_ID = "nguoi_dung_id";
    private String userId;
    SharedPreferences sharedPreferences;
    ImageView back;
    int goiKhamId=0;
    Intent intent;
    Button btnXacNhan;
    String phuongThucThanhToan="";

    private static final String MOMO_PARTNER_CODE = "YOUR_MOMO_PARTNER_CODE";
    private static final String MOMO_ACCESS_KEY = "YOUR_MOMO_ACCESS_KEY";
    private static final String MOMO_SECRET_KEY = "YOUR_MOMO_SECRET_KEY";
    
    private static final String ZALOPAY_APP_ID = "YOUR_ZALOPAY_APP_ID";
    private static final String ZALOPAY_KEY1 = "YOUR_ZALOPAY_KEY1";
    private static final String ZALOPAY_KEY2 = "YOUR_ZALOPAY_KEY2";
    
    private static final String VNPAY_TMN_CODE = "YOUR_VNPAY_TMN_CODE";
    private static final String VNPAY_HASH_SECRET = "YOUR_VNPAY_HASH_SECRET";
    private static final String VNPAY_URL = "YOUR_VNPAY_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        intent=getIntent();
        AnhXa();
        getThongTin();
        back.setOnClickListener(v->{
            finish();
        });
        btnXacNhan.setOnClickListener(v -> {
            if (phuongThucThanhToan.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (phuongThucThanhToan) {
                case "MoMo":
                    thanhToanMoMo();
                    break;
                case "ZaloPay":
                    thanhToanZaloPay();
                    break;
                case "Vnpay":
                    thanhToanVNPay();
                    break;
            }
        });
        RadioButton radioZalo = findViewById(R.id.radioZaloPay);
        RadioButton radioMoMo = findViewById(R.id.radioMoMo);
        RadioButton radioVnpay = findViewById(R.id.radioVnpay);

        RadioGroup radioGroup = findViewById(R.id.radioGroupPayments); // sửa lại đúng id

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Đổi background layout tương ứng với RadioButton được chọn
                if (checkedId == R.id.radioZaloPay) {
                    radioZalo.setBackgroundResource(R.drawable.bg_item_payment_selected);
                    radioMoMo.setBackgroundResource(R.drawable.bg_item_payment);
                    radioVnpay.setBackgroundResource(R.drawable.bg_item_payment);
                    phuongThucThanhToan="ZaloPay";
                } else if (checkedId == R.id.radioMoMo) {
                    radioMoMo.setBackgroundResource(R.drawable.bg_item_payment_selected);
                    radioZalo.setBackgroundResource(R.drawable.bg_item_payment);
                    radioVnpay.setBackgroundResource(R.drawable.bg_item_payment);
                    phuongThucThanhToan="MoMo";
                } else if (checkedId == R.id.radioVnpay) {
                    radioVnpay.setBackgroundResource(R.drawable.bg_item_payment_selected);
                    radioZalo.setBackgroundResource(R.drawable.bg_item_payment);
                    radioMoMo.setBackgroundResource(R.drawable.bg_item_payment);
                    phuongThucThanhToan="Vnpay";
                }
            }
        });


    }
    private void AnhXa(){
        back = findViewById(R.id.back);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        // Initialize all TextViews here
        tv_ho_ten = findViewById(R.id.tv_ho_ten2);
        tv_sdt = findViewById(R.id.tv_sdt2);
        tv_ngay_sinh = findViewById(R.id.tv_ngay_sinh2);
        tv_dia_chi = findViewById(R.id.tv_dia_chi2);
        tv_ten_goi = findViewById(R.id.tv_ten_goi2);
        tv_gio = findViewById(R.id.tv_gio2);
        tv_ngay = findViewById(R.id.tv_ngay2);
        tv_gia_goi = findViewById(R.id.tv_gia_goi22);
        tv_gia_goi2 = findViewById(R.id.tv_gia_goi222);
        tv_gia_goi3 = findViewById(R.id.tv_gia_goi322);
    }
    TextView tv_ten_goi, tv_gio, tv_ngay, tv_gia_goi, tv_gia_goi2, tv_gia_goi3;
    TextView tv_ho_ten, tv_sdt, tv_ngay_sinh, tv_dia_chi;
    private void getThongTin(){
        if (intent == null) {
            Log.e("ThanhToan", "Intent is null");
            return;
        }

        String goiKhamJson = intent.getStringExtra("goiKhamJson");
        String hoSoBenhNhanResponse = intent.getStringExtra("hoSoBenhNhanResponse");

        // Debug logs
        Log.d("ThanhToan", "Raw goiKhamJson: " + goiKhamJson);
        Log.d("ThanhToan", "Raw hoSoBenhNhanResponse: " + hoSoBenhNhanResponse);

        if (goiKhamJson == null || hoSoBenhNhanResponse == null) {
            Log.e("ThanhToan", "Missing JSON data");
            return;
        }

        // Fix JSON string by removing unnecessary escape characters
        if (hoSoBenhNhanResponse.startsWith("\"") && hoSoBenhNhanResponse.endsWith("\"")) {
            hoSoBenhNhanResponse = hoSoBenhNhanResponse.substring(1, hoSoBenhNhanResponse.length() - 1);
        }
        // Remove escape characters from the JSON string
        hoSoBenhNhanResponse = hoSoBenhNhanResponse.replace("\\\"", "\"");

        try {
            JSONObject goiKhamObj = new JSONObject(goiKhamJson);
            JSONObject hoSoObj = new JSONObject(hoSoBenhNhanResponse);

            // Parse data with fallbacks
            String tenGoi = goiKhamObj.optString("tenGoi", "Không có thông tin");
            String gioChon = goiKhamObj.optString("gioChon", "Không có thông tin");
            String ngayChon = goiKhamObj.optString("ngayChon", "Không có thông tin");
            double giaGoi = goiKhamObj.optDouble("giaGoi", 0);

            String hoTen = hoSoObj.optString("hoTen", "Không có thông tin");
            String soDienThoai = hoSoObj.optString("soDienThoai", "Không có thông tin");
            String ngaySinh = hoSoObj.optString("ngaySinh", "Không có thông tin");
            String diaChi = hoSoObj.optString("noiThuongTru", "Không có thông tin");

            // Format price
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(giaGoi);

            // Debug logs
            Log.d("ThanhToan", "Parsed data - " +
                    "Name: " + hoTen + ", Phone: " + soDienThoai +
                    ", Package: " + tenGoi + ", Price: " + formatted);

            // Update UI
            runOnUiThread(() -> {
                // First verify TextViews
                if (tv_ho_ten == null) {
                    Log.e("ThanhToan", "TextView not initialized!");
                    return;
                }

                try {
                    tv_ho_ten.setText(hoTen);
                    tv_sdt.setText(soDienThoai);
                    tv_ngay_sinh.setText(ngaySinh);
                    tv_dia_chi.setText(diaChi);
                    tv_ten_goi.setText(tenGoi);
                    tv_gio.setText(gioChon);
                    tv_ngay.setText(ngayChon);
                    tv_gia_goi.setText(formatted + "đ");
                    tv_gia_goi2.setText(formatted + "đ");
                    tv_gia_goi3.setText(formatted + "VND");
                } catch (Exception e) {
                    Log.e("ThanhToan", "Error setting text", e);
                }
            });

        } catch (Exception e) {
            Log.e("ThanhToan", "JSON parsing error", e);
            runOnUiThread(() -> {
                Toast.makeText(this, "Lỗi khi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void thanhToanMoMo() {
        try {
            // Lấy giá tiền từ TextView
            String giaTien = tv_gia_goi3.getText().toString().replace("VND", "").trim();
            double amount = Double.parseDouble(giaTien.replace(",", ""));
            
            // Tạo request thanh toán MoMo
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("partnerCode", MOMO_PARTNER_CODE);
            requestMap.put("accessKey", MOMO_ACCESS_KEY);
            requestMap.put("amount", String.valueOf(amount));
            requestMap.put("orderId", "ORDER_" + System.currentTimeMillis());
            requestMap.put("orderInfo", "Thanh toan goi kham benh");
            requestMap.put("returnUrl", "medicalbooking://payment");
            requestMap.put("notifyUrl", "https://your-server.com/momo/notify");
            requestMap.put("requestType", "captureMoMoWallet");
            
            // Gọi API thanh toán MoMo
            // TODO: Implement API call to MoMo payment gateway
            Toast.makeText(this, "Đang xử lý thanh toán MoMo...", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void thanhToanZaloPay() {
        try {
            // Lấy giá tiền từ TextView
            String giaTien = tv_gia_goi3.getText().toString().replace("VND", "").trim();
            double amount = Double.parseDouble(giaTien.replace(",", ""));
            
            // Tạo request thanh toán ZaloPay
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("app_id", ZALOPAY_APP_ID);
            requestMap.put("app_user", "user_" + userId);
            requestMap.put("app_time", String.valueOf(System.currentTimeMillis()));
            requestMap.put("amount", String.valueOf(amount));
            requestMap.put("app_trans_id", "ORDER_" + System.currentTimeMillis());
            requestMap.put("embed_data", "{\"redirecturl\":\"medicalbooking://payment\"}");
            requestMap.put("item", "Thanh toan goi kham benh");
            
            // Gọi API thanh toán ZaloPay
            // TODO: Implement API call to ZaloPay payment gateway
            Toast.makeText(this, "Đang xử lý thanh toán ZaloPay...", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void thanhToanVNPay() {
        try {
            // Lấy giá tiền từ TextView
            String giaTien = tv_gia_goi3.getText().toString().replace("VND", "").trim();
            double amount = Double.parseDouble(giaTien.replace(",", ""));
            
            // Tạo request thanh toán VNPAY
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("vnp_TmnCode", VNPAY_TMN_CODE);
            requestMap.put("vnp_Amount", String.valueOf((long)(amount * 100))); // Convert to smallest currency unit
            requestMap.put("vnp_Command", "pay");
            requestMap.put("vnp_CreateDate", String.valueOf(System.currentTimeMillis()));
            requestMap.put("vnp_CurrCode", "VND");
            requestMap.put("vnp_IpAddr", "127.0.0.1");
            requestMap.put("vnp_Locale", "vn");
            requestMap.put("vnp_OrderInfo", "Thanh toan goi kham benh");
            requestMap.put("vnp_OrderType", "other");
            requestMap.put("vnp_ReturnUrl", "medicalbooking://payment");
            requestMap.put("vnp_TxnRef", "ORDER_" + System.currentTimeMillis());
            requestMap.put("vnp_Version", "2.1.0");
            
            // Gọi API thanh toán VNPAY
            // TODO: Implement API call to VNPAY payment gateway
            Toast.makeText(this, "Đang xử lý thanh toán VNPAY...", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Xử lý kết quả thanh toán
        if (data != null) {
            String response = data.getStringExtra("response");
            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.optString("status");
                    if ("success".equals(status)) {
                        Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String message = jsonResponse.optString("message", "Thanh toán thất bại");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Lỗi xử lý kết quả thanh toán", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}