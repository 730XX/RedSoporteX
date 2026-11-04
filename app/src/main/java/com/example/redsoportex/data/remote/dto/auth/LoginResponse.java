package com.example.redsoportex.data.remote.dto.auth;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("expiresInMs")
    private Long expiresInMs;

    @SerializedName("user")
    private UserDto user;

    @SerializedName("role")
    private String role;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("userId")
    private Long userId;

    @SerializedName("phone")
    private String phone;

    @SerializedName("approxAddress")
    private String approxAddress;

    @SerializedName("reputationScore")
    private Double reputationScore;

    public static class UserDto {
        @SerializedName("id")
        private Long id;

        @SerializedName("fullName")
        private String fullName;

        @SerializedName("email")
        private String email;

        @SerializedName("role")
        private String role;

        @SerializedName("phone")
        private String phone;

        @SerializedName("approxAddress")
        private String approxAddress;

        @SerializedName("approxLatitude")
        private Double approxLatitude;

        @SerializedName("approxLongitude")
        private Double approxLongitude;

        @SerializedName("reputationScore")
        private Double reputationScore;

        public Long getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public String getPhone() {
            return phone;
        }

        public String getApproxAddress() {
            return approxAddress;
        }

        public Double getApproxLatitude() {
            return approxLatitude;
        }

        public Double getApproxLongitude() {
            return approxLongitude;
        }

        public Double getReputationScore() {
            return reputationScore;
        }
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType != null ? tokenType : "Bearer";
    }

    public Long getExpiresInMs() {
        return expiresInMs;
    }

    public UserDto getUser() {
        return user;
    }

    public String getRole() {
        if (user != null && user.getRole() != null) {
            return user.getRole();
        }
        return role;
    }

    public String getEmail() {
        if (user != null && user.getEmail() != null) {
            return user.getEmail();
        }
        return email;
    }

    public String getFullName() {
        if (user != null && user.getFullName() != null) {
            return user.getFullName();
        }
        return fullName;
    }

    public Long getUserId() {
        if (user != null && user.getId() != null) {
            return user.getId();
        }
        return userId;
    }

    public String getPhone() {
        if (user != null && user.getPhone() != null) {
            return user.getPhone();
        }
        return phone;
    }

    public String getApproxAddress() {
        if (user != null && user.getApproxAddress() != null) {
            return user.getApproxAddress();
        }
        return approxAddress;
    }

    public Double getReputationScore() {
        if (user != null && user.getReputationScore() != null) {
            return user.getReputationScore();
        }
        return reputationScore;
    }
}
