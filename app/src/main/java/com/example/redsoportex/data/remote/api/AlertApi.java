package com.example.redsoportex.data.remote.api;

import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.data.remote.dto.alert.NeedAlertCreateRequest;
import com.example.redsoportex.data.remote.dto.alert.NeedAlertResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AlertApi {

    @POST("api/v1/alerts")
    Call<ApiResponse<NeedAlertResponse>> createAlert(@Body NeedAlertCreateRequest request);

    @GET("api/v1/alerts/mis-alertas")
    Call<ApiResponse<List<NeedAlertResponse>>> getMyAlerts();

    @PATCH("api/v1/alerts/{id}/cancelar")
    Call<ApiResponse<NeedAlertResponse>> cancelAlert(@Path("id") Long id);
}
