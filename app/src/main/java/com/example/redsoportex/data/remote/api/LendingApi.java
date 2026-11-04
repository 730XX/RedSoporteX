package com.example.redsoportex.data.remote.api;

import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.data.remote.dto.lending.LendingCreateRequest;
import com.example.redsoportex.data.remote.dto.lending.LendingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LendingApi {

    @POST("api/v1/lendings")
    Call<ApiResponse<LendingResponse>> requestLending(@Body LendingCreateRequest request);

    @GET("api/v1/lendings/mis-prestamos")
    Call<ApiResponse<List<LendingResponse>>> getMyLendings();

    @PATCH("api/v1/lendings/{id}/check-in")
    Call<ApiResponse<LendingResponse>> checkIn(@Path("id") Long id);

    @PATCH("api/v1/lendings/{id}/check-out")
    Call<ApiResponse<LendingResponse>> checkOut(@Path("id") Long id);
}
