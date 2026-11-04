package com.example.redsoportex.data.remote.api;

import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.data.remote.dto.auth.LoginRequest;
import com.example.redsoportex.data.remote.dto.auth.LoginResponse;
import com.example.redsoportex.data.remote.dto.auth.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("api/v1/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);

    @POST("api/v1/auth/register")
    Call<ApiResponse<Object>> register(@Body RegisterRequest request);
}
