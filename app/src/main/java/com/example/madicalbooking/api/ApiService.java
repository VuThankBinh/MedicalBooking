package com.example.madicalbooking.api;

import com.example.madicalbooking.api.models.GoiKhamResponse;
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
import com.example.madicalbooking.api.models.VerifyOtpRequest;
import com.example.madicalbooking.api.models.ChangePasswordRequest;
import com.example.madicalbooking.api.models.UpdateProfileRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("/api/hoso-benh-nhan/nguoi-dung/{nguoi_dung_id}")
    Call<List<HoSoBenhNhanResponse>> getHoSoBenhNhanByNguoiDungId(@Path("nguoi_dung_id") String nguoi_dung_id);

    @DELETE("/api/hoso-benh-nhan/{id}")
    Call<Void> deleteHoSoBenhNhan(@Path("id") int id);

    @GET("/api/goi-kham")
    Call<List<GoiKhamResponse>> getGoiKhamAll();

    @GET("/api/goi-kham/{id}")
    Call<GoiKhamResponse> getGoiKhamDetail(@Path("id") int id);

    @POST("api/auth/change-password")
    Call<Void> changePassword( @Body ChangePasswordRequest request);

    @PUT("api/auth/update-profile")
    Call<Void> updateProfile(@Body UpdateProfileRequest request);
}
