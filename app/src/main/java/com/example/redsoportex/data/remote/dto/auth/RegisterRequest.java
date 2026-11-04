package com.example.redsoportex.data.remote.dto.auth;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("fullName")
    private final String fullName;

    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    @SerializedName("role")
    private final String role;

    @SerializedName("phone")
    private final String phone;

    @SerializedName("approxAddress")
    private final String approxAddress;

    @SerializedName("approxLatitude")
    private final Double approxLatitude;

    @SerializedName("approxLongitude")
    private final Double approxLongitude;

    public RegisterRequest(String fullName, String email, String password, String role,
                           String phone, String approxAddress, Double approxLatitude, Double approxLongitude) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.approxAddress = approxAddress;
        this.approxLatitude = approxLatitude;
        this.approxLongitude = approxLongitude;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
}
