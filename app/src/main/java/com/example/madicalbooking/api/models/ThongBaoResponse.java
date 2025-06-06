package com.example.madicalbooking.api.models;

import com.google.gson.annotations.SerializedName;

public class ThongBaoResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("message")
    private String message;

    @SerializedName("isRead")
    private boolean isRead;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("title")
    private String title;

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public String getCreatedAt() { return createdAt; }
    public String getTitle() { return title; }
} 