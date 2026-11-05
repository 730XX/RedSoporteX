package com.example.redsoportex.data.repository;

import androidx.annotation.NonNull;

import com.example.redsoportex.core.network.ApiClient;
import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.core.network.ErrorParser;
import com.example.redsoportex.data.remote.api.CategoryApi;
import com.example.redsoportex.data.remote.api.ItemApi;
import com.example.redsoportex.data.remote.dto.category.CategoryResponse;
import com.example.redsoportex.data.remote.dto.item.ItemCreateRequest;
import com.example.redsoportex.data.remote.dto.item.ItemResponse;
import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.Item;
import com.example.redsoportex.domain.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRepositoryImpl implements ItemRepository {

    private final CategoryApi categoryApi;
    private final ItemApi itemApi;

    public ItemRepositoryImpl() {
        this.categoryApi = ApiClient.createService(CategoryApi.class);
        this.itemApi = ApiClient.createService(ItemApi.class);
    }

    public ItemRepositoryImpl(@NonNull CategoryApi categoryApi, @NonNull ItemApi itemApi) {
        this.categoryApi = categoryApi;
        this.itemApi = itemApi;
    }

    @Override
    public void getCategories(Callback<List<Category>> callback) {
        categoryApi.getCategories().enqueue(new retrofit2.Callback<ApiResponse<List<CategoryResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<CategoryResponse>>> call,
                                   @NonNull Response<ApiResponse<List<CategoryResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<CategoryResponse> dtos = response.body().getData();
                    List<Category> categories = new ArrayList<>();
                    if (dtos != null) {
                        for (CategoryResponse dto : dtos) {
                            categories.add(mapToDomain(dto));
                        }
                    }
                    callback.onSuccess(categories);
                    return;
                }
                String msg = ErrorParser.parseError(response, "Error al obtener categorías");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<CategoryResponse>>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void getItems(Long categoryId, Callback<List<Item>> callback) {
        itemApi.getItems(categoryId).enqueue(new retrofit2.Callback<ApiResponse<List<ItemResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<ItemResponse>>> call,
                                   @NonNull Response<ApiResponse<List<ItemResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<ItemResponse> dtos = response.body().getData();
                    List<Item> items = new ArrayList<>();
                    if (dtos != null) {
                        for (ItemResponse dto : dtos) {
                            items.add(mapToDomain(dto));
                        }
                    }
                    callback.onSuccess(items);
                    return;
                }
                String msg = ErrorParser.parseError(response, "Error al obtener artículos");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<ItemResponse>>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void getItemById(Long id, Callback<Item> callback) {
        itemApi.getItemById(id).enqueue(new retrofit2.Callback<ApiResponse<ItemResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ItemResponse>> call,
                                   @NonNull Response<ApiResponse<ItemResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ItemResponse dto = response.body().getData();
                    if (dto != null) {
                        callback.onSuccess(mapToDomain(dto));
                        return;
                    }
                }
                String msg = ErrorParser.parseError(response, "Error al obtener el detalle del artículo");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ItemResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void createItem(Long categoryId, String title, String description, String conservationStatus,
                           String imageUrl, Double approxLatitude, Double approxLongitude, Callback<Item> callback) {
        ItemCreateRequest request = new ItemCreateRequest(
                categoryId,
                title,
                description,
                conservationStatus,
                imageUrl,
                approxLatitude,
                approxLongitude
        );

        itemApi.createItem(request).enqueue(new retrofit2.Callback<ApiResponse<ItemResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ItemResponse>> call,
                                   @NonNull Response<ApiResponse<ItemResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ItemResponse dto = response.body().getData();
                    if (dto != null) {
                        callback.onSuccess(mapToDomain(dto));
                        return;
                    }
                }
                String msg = ErrorParser.parseError(response, "Error al publicar artículo");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ItemResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    private Category mapToDomain(CategoryResponse dto) {
        return new Category(
                dto.getId(),
                dto.getName(),
                dto.getDescription()
        );
    }

    private Item mapToDomain(ItemResponse dto) {
        return new Item(
                dto.getId(),
                dto.getDonorId(),
                dto.getDonorName(),
                dto.getCategoryId(),
                dto.getCategoryName(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getConservationStatus(),
                dto.getAvailabilityStatus(),
                dto.getImageUrl(),
                dto.getApproxLatitude(),
                dto.getApproxLongitude(),
                dto.getCreatedAt()
        );
    }
}
