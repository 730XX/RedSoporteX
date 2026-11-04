package com.example.redsoportex.data.remote.dto.auth;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("role")
    private String role;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("userId")
    private Long userId;

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType != null ? tokenType : "Bearer";
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getUserId() {
        return userId;
    }
}
