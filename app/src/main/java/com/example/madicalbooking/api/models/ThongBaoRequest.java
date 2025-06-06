package com.example.madicalbooking.api.models;

import com.google.gson.annotations.SerializedName;

public class ThongBaoRequest {
    @SerializedName("userId")
    private int userId;

    @SerializedName("message")
    private String message;

    @SerializedName("title")
    private String title;

    public ThongBaoRequest(int userId, String message, String title) {
        this.userId = userId;
        this.message = message;
        this.title = title;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getTitle() { return title; }
} 