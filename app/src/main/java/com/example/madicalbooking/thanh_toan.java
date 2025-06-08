package com.example.madicalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madicalbooking.api.CreateOrder;
import com.example.madicalbooking.api.DangKyGoiKham;
import com.example.madicalbooking.api.RetrofitClient;
import com.example.madicalbooking.api.models.ThongBaoRequest;
import com.example.madicalbooking.api.models.ThongBaoResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class thanh_toan extends AppCompatActivity {
    private static final String PREF_NAME = "ZimStayPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    private String KEY_USER_ID = "nguoi_dung_id";
    private String userId;
    SharedPreferences sharedPreferences;
    ImageView back;
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

    // Thêm class HoSoBenhNhan
    private static class HoSoBenhNhan {
        private String email;
        private String gioiTinh;
        private String hoTen;
        private String maDinhDanh;
        private int maHoSo;
        private String ngayCap;
        private String ngaySinh;
        private String ngheNghiep;
        private int nguoiDungId;
        private String noiThuongTru;
        private String soDienThoai;

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getGioiTinh() { return gioiTinh; }
        public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
        
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        
        public String getMaDinhDanh() { return maDinhDanh; }
        public void setMaDinhDanh(String maDinhDanh) { this.maDinhDanh = maDinhDanh; }
        
        public int getMaHoSo() { return maHoSo; }
        public void setMaHoSo(int maHoSo) { this.maHoSo = maHoSo; }
        
        public String getNgayCap() { return ngayCap; }
        public void setNgayCap(String ngayCap) { this.ngayCap = ngayCap; }
        
        public String getNgaySinh() { return ngaySinh; }
        public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
        
        public String getNgheNghiep() { return ngheNghiep; }
        public void setNgheNghiep(String ngheNghiep) { this.ngheNghiep = ngheNghiep; }
        
        public int getNguoiDungId() { return nguoiDungId; }
        public void setNguoiDungId(int nguoiDungId) { this.nguoiDungId = nguoiDungId; }
        
        public String getNoiThuongTru() { return noiThuongTru; }
        public void setNoiThuongTru(String noiThuongTru) { this.noiThuongTru = noiThuongTru; }
        
        public String getSoDienThoai() { return soDienThoai; }
        public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    }

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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        String giaGoi= tv_gia_goi3.getText().toString().replace("VND", "").trim();

        // Xử lý Intent từ ZaloPay
        handleIntent(getIntent());

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
                    System.out.println("giaGoi: "+giaGoi.replace(".",""));
                    thanhToanZaloPay(giaGoi);
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
            Log.e("ThanhToan", "Missing data");
            return;
        }

        try {
            // Xử lý chuỗi goiKhamJson
            String cleanGoiKham = goiKhamJson;
            if (cleanGoiKham.startsWith("\"") && cleanGoiKham.endsWith("\"")) {
                cleanGoiKham = cleanGoiKham.substring(1, cleanGoiKham.length() - 1);
            }
            cleanGoiKham = cleanGoiKham.replace("\\\"", "\"")
                                     .replace("\\\\", "\\")
                                     .replace("\\n", "\n")
                                     .replace("\\r", "\r")
                                     .replace("\\t", "\t")
                                     .replace("\\/", "/");

            // Lấy thông tin từ goiKhamJson
            String tenGoi = getValueFromString(cleanGoiKham, "tenGoi");
            String gioChon = getValueFromString(cleanGoiKham, "gioChon");
            String ngayChon = getValueFromString(cleanGoiKham, "ngayChon");
            String giaGoiStr = getValueFromString(cleanGoiKham, "giaGoi");
            double giaGoi = giaGoiStr != null ? Double.parseDouble(giaGoiStr) : 0;

            // Xử lý chuỗi hoSoBenhNhanResponse
            String cleanHoSo = hoSoBenhNhanResponse;
            if (cleanHoSo.startsWith("\"") && cleanHoSo.endsWith("\"")) {
                cleanHoSo = cleanHoSo.substring(1, cleanHoSo.length() - 1);
            }
            cleanHoSo = cleanHoSo.replace("\\\"", "\"")
                                .replace("\\\\", "\\")
                                .replace("\\n", "\n")
                                .replace("\\r", "\r")
                                .replace("\\t", "\t")
                                .replace("\\/", "/");

            // Lấy thông tin từ hoSoBenhNhanResponse
            String hoTen = getValueFromString(cleanHoSo, "hoTen");
            String soDienThoai = getValueFromString(cleanHoSo, "soDienThoai");
            String ngaySinh = getValueFromString(cleanHoSo, "ngaySinh");
            String diaChi = getValueFromString(cleanHoSo, "noiThuongTru");
            String nguoiDungIdStr = getValueFromString(cleanHoSo, "nguoiDungId");
            userId = nguoiDungIdStr != null ? nguoiDungIdStr : "0";

            Log.d("ThanhToan", "UserId: " + userId);

            // Format price
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(giaGoi);

            // Debug logs
            Log.d("ThanhToan", "Parsed data - " +
                    "Name: " + hoTen + ", Phone: " + soDienThoai +
                    ", Package: " + tenGoi + ", Price: " + formatted);

            // Update UI
            runOnUiThread(() -> {
                try {
                    tv_ho_ten.setText(hoTen != null ? hoTen : "Không có thông tin");
                    tv_sdt.setText(soDienThoai != null ? soDienThoai : "Không có thông tin");
                    tv_ngay_sinh.setText(ngaySinh != null ? ngaySinh : "Không có thông tin");
                    tv_dia_chi.setText(diaChi != null ? diaChi : "Không có thông tin");
                    tv_ten_goi.setText(tenGoi != null ? tenGoi : "Không có thông tin");
                    tv_gio.setText(gioChon != null ? gioChon : "Không có thông tin");
                    tv_ngay.setText(ngayChon != null ? ngayChon : "Không có thông tin");
                    tv_gia_goi.setText(formatted + "đ");
                    tv_gia_goi2.setText(formatted + "đ");
                    tv_gia_goi3.setText(formatted + "VND");
                } catch (Exception e) {
                    Log.e("ThanhToan", "Error setting text", e);
                }
            });

        } catch (Exception e) {
            Log.e("ThanhToan", "Data parsing error", e);
            System.out.println("Lỗi xử lý dữ liệu: " + e.getMessage());
            runOnUiThread(() -> {
                Toast.makeText(this, "Lỗi khi xử lý dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Hàm helper để lấy giá trị từ chuỗi
    private String getValueFromString(String jsonString, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int startIndex = jsonString.indexOf(searchKey);
            if (startIndex == -1) return null;

            startIndex += searchKey.length();
            while (startIndex < jsonString.length() && jsonString.charAt(startIndex) == ' ') {
                startIndex++;
            }

            if (startIndex >= jsonString.length()) return null;

            int endIndex;
            if (jsonString.charAt(startIndex) == '"') {
                // Nếu giá trị là string
                startIndex++;
                endIndex = jsonString.indexOf("\"", startIndex);
                if (endIndex == -1) return null;
            } else {
                // Nếu giá trị là số hoặc boolean
                endIndex = jsonString.indexOf(",", startIndex);
                if (endIndex == -1) {
                    endIndex = jsonString.indexOf("}", startIndex);
                }
                if (endIndex == -1) return null;
            }

            return jsonString.substring(startIndex, endIndex);
        } catch (Exception e) {
            Log.e("ThanhToan", "Error getting value for key " + key + ": " + e.getMessage());
            return null;
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

    private void handleIntent(Intent intent) {
        if (intent != null) {
            Log.d("ZaloPay", "handleIntent called with action: " + intent.getAction());
            ZaloPaySDK.getInstance().onResult(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("ZaloPay", "onNewIntent called with action: " + intent.getAction());
        setIntent(intent);
        handleIntent(intent);
    }

    private void thanhToanZaloPay(String amount) {
        CreateOrder orderApi = new CreateOrder();
        try {
            Log.d("ZaloPay", "Bắt đầu tạo đơn hàng với số tiền: " + amount);
            JSONObject data = orderApi.createOrder(amount);
            Log.d("ZaloPay", "Kết quả tạo đơn hàng: " + data.toString());
            
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                Log.d("ZaloPay", "Token thanh toán: " + token);
                
                // Sửa lại scheme URL để quay về app
                String scheme = "demozpdk://app";
                Log.d("ZaloPay", "Bắt đầu thanh toán với scheme: " + scheme);
                
                // Tạo Intent cho ZaloPay với returnUrl
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(scheme));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                
                ZaloPaySDK.getInstance().payOrder(thanh_toan.this, token, scheme, new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Log.d("ZaloPay", "onPaymentSucceeded called");
                        Log.d("ZaloPay", "Transaction ID: " + s);
                        Log.d("ZaloPay", "Amount: " + s1);
                        Log.d("ZaloPay", "Description: " + s2);
                        
                        // Tự động quay lại app
                        Intent returnIntent = new Intent(thanh_toan.this, thanh_toan.class);
                        returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(returnIntent);
                        
                        runOnUiThread(() -> {
                            try {
                                // Hiển thị Toast trước
                                Toast.makeText(thanh_toan.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                
                                // Đợi Toast hiển thị xong
                                new Handler().postDelayed(() -> {
                                    try {
                                        Log.d("ZaloPay", "Bắt đầu xử lý sau thanh toán");
                                        
                                        // Gọi API đăng ký gói khám sau khi thanh toán thành công
                                        Log.d("ZaloPay", "Đang tạo lịch hẹn...");
                                        createAppointment("CHUA_THANH_TOAN");
                                        
                                        // Gửi thông báo
                                        Log.d("ZaloPay", "Đang gửi thông báo...");
                                        sendNotification(userId, "Đã thanh toán gói khám" +tv_ten_goi.getText().toString() + " thành công", "Thông báo");
                                        
                                        // Chuyển về MainActivity
                                        Log.d("ZaloPay", "Đang chuyển về MainActivity...");
                                        Intent mainIntent = new Intent(thanh_toan.this, MainActivity.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        
                                        // Đóng activity hiện tại
                                        Log.d("ZaloPay", "Đang đóng activity...");
                                        finish();
                                        
                                    } catch (Exception e) {
                                        Log.e("ZaloPay", "Lỗi trong xử lý sau thanh toán: " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                }, 2000); // Delay 2 giây để đảm bảo Toast hiển thị đủ thời gian
                                
                            } catch (Exception e) {
                                Log.e("ZaloPay", "Lỗi trong onPaymentSucceeded: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Log.d("ZaloPay", "onPaymentCanceled called");
                        Log.d("ZaloPay", "Transaction ID: " + s);
                        Log.d("ZaloPay", "Description: " + s1);
                        
                        // Tự động quay lại app
                        Intent returnIntent = new Intent(thanh_toan.this, thanh_toan.class);
                        returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(returnIntent);
                        
                        runOnUiThread(() -> {
                            try {
                                Toast.makeText(thanh_toan.this, "Hủy thanh toán", Toast.LENGTH_SHORT).show();
                                createAppointment("CHUA_THANH_TOAN");
                                finish();
                            } catch (Exception e) {
                                Log.e("ZaloPay", "Lỗi trong onPaymentCanceled: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Log.e("ZaloPay", "onPaymentError called");
                        Log.e("ZaloPay", "Error: " + zaloPayError.toString());
                        Log.e("ZaloPay", "Transaction ID: " + s);
                        Log.e("ZaloPay", "Description: " + s1);
                        
                        // Tự động quay lại app
                        Intent returnIntent = new Intent(thanh_toan.this, thanh_toan.class);
                        returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(returnIntent);
                        
                        runOnUiThread(() -> {
                            try {
                                Toast.makeText(thanh_toan.this, "Lỗi thanh toán: " + zaloPayError.toString(), Toast.LENGTH_SHORT).show();
                                createAppointment("CHUA_THANH_TOAN");
                                finish();
                            } catch (Exception e) {
                                Log.e("ZaloPay", "Lỗi trong onPaymentError: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    }
                });
            } else {
                Log.e("ZaloPay", "Lỗi tạo đơn hàng: " + data.toString());
                Toast.makeText(thanh_toan.this, "Lỗi tạo đơn hàng", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("ZaloPay", "Lỗi: " + e.getMessage());
            System.out.println("lỗi e: "+ e.getMessage());
            e.printStackTrace();
            Toast.makeText(thanh_toan.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void sendNotification(String userId, String content, String title) {
        try {
            if (userId == null || userId.isEmpty() || userId.equals("0")) {
                Log.e("Notification", "UserId không hợp lệ: " + userId);
                return;
            }

            int userIdInt = Integer.parseInt(userId);
            Log.d("Notification", "Đang gửi thông báo cho userId: " + userIdInt);
            
            ThongBaoRequest request = new ThongBaoRequest(userIdInt, content, title);
            
            RetrofitClient.getInstance()
                .getApi()
                .sendNotification(request)
                .enqueue(new Callback<ThongBaoResponse>() {
                    @Override
                    public void onResponse(Call<ThongBaoResponse> call, Response<ThongBaoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("Notification", "Gửi thông báo thành công");
                        } else {
                            Log.e("Notification", "Lỗi khi gửi thông báo: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ThongBaoResponse> call, Throwable t) {
                        Log.e("Notification", "Lỗi kết nối: " + t.getMessage());
                    }
                });
        } catch (NumberFormatException e) {
            Log.e("Notification", "Lỗi chuyển đổi userId: " + e.getMessage());
        } catch (Exception e) {
            Log.e("Notification", "Lỗi không xác định: " + e.getMessage());
        }
    }
    int goiKhamId1;
    private void createAppointment(String status) {
        try {
            // Lấy thông tin từ chuỗi goiKhamJson
            String goiKhamJson = intent.getStringExtra("goiKhamJson");
            System.out.println("goiKhamJson: "+ goiKhamJson);
            String hoSoBenhNhanResponse = intent.getStringExtra("hoSoBenhNhanResponse");
            System.out.println("hoSoBenhNhanResponse: "+ hoSoBenhNhanResponse);

            if (goiKhamJson == null || hoSoBenhNhanResponse == null) {
                Log.e("DangKyGoiKham", "Missing data");
                return;
            }

            // Parse JSON từ goiKhamJson
            JSONObject goiKhamObj = new JSONObject(goiKhamJson);
            String ngayThucHien = goiKhamObj.optString("ngayChon");
            String gioKham = goiKhamObj.optString("gioChon");
            double giaTien = goiKhamObj.optDouble("giaGoi", 0.0);
            String tenGoi = goiKhamObj.optString("tenGoi");

            // Xử lý chuỗi hoSoBenhNhanResponse
            String cleanHoSo = hoSoBenhNhanResponse;
            if (cleanHoSo.startsWith("\"") && cleanHoSo.endsWith("\"")) {
                cleanHoSo = cleanHoSo.substring(1, cleanHoSo.length() - 1);
            }
            cleanHoSo = cleanHoSo.replace("\\\"", "\"")
                                .replace("\\\\", "\\")
                                .replace("\\n", "\n")
                                .replace("\\r", "\r")
                                .replace("\\t", "\t")
                                .replace("\\/", "/");

            // Lấy thông tin từ hoSoBenhNhanResponse bằng string
            String maHoSoStr = getValueFromString(cleanHoSo, "maHoSo");
            String nguoiDungIdStr = getValueFromString(cleanHoSo, "nguoiDungId");
            int maHoSo = maHoSoStr != null ? Integer.parseInt(maHoSoStr) : 0;

            // Tạo ngày đăng ký hiện tại
            String ngayDangKy = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .format(new java.util.Date());

            // Định dạng lại ngày thực hiện
            if (ngayThucHien != null) {
                try {
                    // Xử lý trường hợp ngày có định dạng "Thứ năm, dd/MM/yyyy"
                    if (ngayThucHien.contains("Thứ") && ngayThucHien.contains("/")) {
                        // Tách lấy phần ngày tháng năm
                        String[] parts = ngayThucHien.split(",");
                        if (parts.length > 1) {
                            String datePart = parts[1].trim();
                            String[] dateParts = datePart.split("/");
                            if (dateParts.length == 3) {
                                // Chuyển từ dd/MM/yyyy sang yyyy-MM-dd
                                ngayThucHien = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
                            }
                        }
                    }
                    // Nếu ngày có định dạng dd/MM/yyyy
                    else if (ngayThucHien.contains("/")) {
                        String[] parts = ngayThucHien.split("/");
                        if (parts.length == 3) {
                            ngayThucHien = parts[2] + "-" + parts[1] + "-" + parts[0];
                        }
                    }
                    
                    // Đảm bảo định dạng cuối cùng là yyyy-MM-dd
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = inputFormat.parse(ngayThucHien);
                    ngayThucHien = outputFormat.format(date);
                    
                    Log.d("DangKyGoiKham", "Ngày sau khi xử lý: " + ngayThucHien);
                } catch (Exception e) {
                    Log.e("DangKyGoiKham", "Lỗi định dạng ngày: " + e.getMessage());
                }
            }
            Intent intent = getIntent();
            int goiKhamId = intent.getIntExtra("goiKhamid", -1);
            System.out.println("goiKhamId TT: "+goiKhamId);
            System.out.println("lấy ra mã hồ sơ và mã gói");
            Log.d("DangKyGoiKham", "Thông tin đăng ký: " +
                    "maHoSo=" + maHoSo +
                    ", maGoi=" + goiKhamId +
                    ", ngayDangKy=" + ngayDangKy +
                    ", ngayThucHien=" + ngayThucHien +
                    ", status=" + status +
                    ", giaTien=" + giaTien +
                    ", gioKham=" + gioKham +
                    ", userId=" + nguoiDungIdStr);

            // Gọi API đăng ký gói khám
            JSONObject response = DangKyGoiKham.dangKyGoiKham(
                maHoSo,
                    goiKhamId,
                ngayDangKy,
                ngayThucHien,
                status,
                giaTien,
                gioKham,
                userId
            );


            if (response != null) {
                Log.d("DangKyGoiKham", "Đăng ký thành công: " + response.toString());
                Toast.makeText(this, "Đăng ký gói khám thành công", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("DangKyGoiKham", "Đăng ký thất bại");
                Toast.makeText(this, "Đăng ký gói khám thất bại", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DangKyGoiKham", "Lỗi: " + e.getMessage());
            e.printStackTrace();
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