package com.example.redsoportex.data.remote.api;

import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.data.remote.dto.category.CategoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {

    @GET("api/v1/categories")
    Call<ApiResponse<List<CategoryResponse>>> getCategories();
}
