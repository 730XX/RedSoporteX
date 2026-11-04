package com.example.redsoportex.data.remote.api;

import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.data.remote.dto.item.ItemCreateRequest;
import com.example.redsoportex.data.remote.dto.item.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemApi {

    @GET("api/v1/items")
    Call<ApiResponse<List<ItemResponse>>> getItems(@Query("categoryId") Long categoryId);

    @GET("api/v1/items/{id}")
    Call<ApiResponse<ItemResponse>> getItemById(@Path("id") Long id);

    @POST("api/v1/items")
    Call<ApiResponse<ItemResponse>> createItem(@Body ItemCreateRequest request);
}
