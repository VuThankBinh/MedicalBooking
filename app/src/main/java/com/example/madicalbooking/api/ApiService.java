package com.example.madicalbooking.api;

import com.example.madicalbooking.api.models.GoiKhamResponse;
import com.example.madicalbooking.api.models.GoiKhamTrucTiepResponse;
import com.example.madicalbooking.api.models.HoSoBenhNhanResponse;
import com.example.madicalbooking.api.models.HoSoBenhNhanRequest;
import com.example.madicalbooking.api.models.LoginRequest;
import com.example.madicalbooking.api.models.LoginResponse;
import com.example.madicalbooking.api.models.OtpRequest;
import com.example.madicalbooking.api.models.OtpResponse;
import com.example.madicalbooking.api.models.RegisterRequest;
import com.example.madicalbooking.api.models.RegisterResponse;
import com.example.madicalbooking.api.models.ResetPasswordRequest;
import com.example.madicalbooking.api.models.TokenResponse;
import com.example.madicalbooking.api.models.VaccineResponse;
import com.example.madicalbooking.api.models.VerifyOtpRequest;
import com.example.madicalbooking.api.models.ChangePasswordRequest;
import com.example.madicalbooking.api.models.UpdateProfileRequest;
import com.example.madicalbooking.api.models.BacSiResponse;
import com.example.madicalbooking.api.models.KhoaResponse;
import com.example.madicalbooking.api.models.ThongBaoResponse;
import com.example.madicalbooking.api.models.ThongBaoRequest;
import com.example.madicalbooking.model.ApiResponse;
import com.example.madicalbooking.model.PhieuKham;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.PUT;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("api/otp/send")
    Call<OtpResponse> sendOtp(@Body OtpRequest otpRequest);

    @POST("api/otp/verify")
    Call<OtpResponse> verifyOtp(@Body VerifyOtpRequest verifyOtpRequest);

    @POST("api/auth/reset-password")
    Call<Void> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("api/otp/sendOTPreset")
    Call<OtpResponse> sendOTPreset(@Body OtpRequest otpRequest);

    @GET("api/auth/me")
    Call<TokenResponse> checkToken(@Header("Authorization") String token);

    @POST("api/hoso-benh-nhan")
    Call<HoSoBenhNhanResponse> createHoSoBenhNhan( @Body HoSoBenhNhanRequest hoSoBenhNhanRequest);

    @GET("api/hoso-benh-nhan/nguoi-dung/{nguoi_dung_id}")
    Call<List<HoSoBenhNhanResponse>> getHoSoBenhNhanByNguoiDungId(@Path("nguoi_dung_id") String nguoi_dung_id);

    @DELETE("api/hoso-benh-nhan/{id}")
    Call<Void> deleteHoSoBenhNhan(@Path("id") int id);

    @GET("api/goi-kham")
    Call<List<GoiKhamResponse>> getGoiKhamAll();

    @GET("api/goi-kham/{id}")
    Call<GoiKhamResponse> getGoiKhamDetail(@Path("id") int id);

    @POST("api/auth/change-password")
    Call<Void> changePassword( @Body ChangePasswordRequest request);

    @PUT("api/auth/update-profile")
    Call<TokenResponse> updateProfile(@Body UpdateProfileRequest request);


    @Multipart
    @POST("api/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @GET("api/dang-ky-goi-kham/user/{maHoSo}/trang-thai/{trangThai}")
    Call<List<PhieuKham>> getPhieuKham(
        @Path("maHoSo") String maHoSo,
        @Path("trangThai") String trangThai
    );

    @GET("api/bac-si")
    Call<List<BacSiResponse>> getBacSiAll();

    @GET("api/khoa/{id}")
    Call<KhoaResponse> getKhoaById(@Path("id") int id);

    @GET("api/bac-si/{id}")
    Call<BacSiResponse> getBacSiById(@Path("id") int id);

    @GET("api/notifications/user/{userId}")
    Call<List<ThongBaoResponse>> getThongBaoByUserId(@Path("userId") int userId);

    @POST("api/notifications")
    Call<ThongBaoResponse> sendNotification(@Body ThongBaoRequest thongBaoRequest);

    @GET("api/goi-kham-truc-tiep")
    Call<List<GoiKhamTrucTiepResponse>> getGoiKhamTrucTiep();

    @GET("api/goi-kham-truc-tiep/{id}")
    Call<GoiKhamTrucTiepResponse> getGoiKhamTrucTiepDetail(@Path("id") int id);

    @GET("api/hoso-benh-nhan/{id}")
    Call<HoSoBenhNhanResponse> getHoSoBenhNhanDetail(@Path("id") int id);

    @GET("api/vaccines")
    Call<List<VaccineResponse>> getVaccines();
}
