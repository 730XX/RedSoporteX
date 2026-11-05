package com.example.redsoportex.data.repository;

import androidx.annotation.NonNull;

import com.example.redsoportex.core.network.ApiClient;
import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.core.network.ErrorParser;
import com.example.redsoportex.data.remote.api.LendingApi;
import com.example.redsoportex.data.remote.dto.lending.LendingCreateRequest;
import com.example.redsoportex.data.remote.dto.lending.LendingResponse;
import com.example.redsoportex.domain.model.Lending;
import com.example.redsoportex.domain.repository.LendingRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LendingRepositoryImpl implements LendingRepository {

    private final LendingApi lendingApi;

    public LendingRepositoryImpl() {
        this.lendingApi = ApiClient.createService(LendingApi.class);
    }

    public LendingRepositoryImpl(@NonNull LendingApi lendingApi) {
        this.lendingApi = lendingApi;
    }

    @Override
    public void requestLending(Long itemId, int expectedReturnDays, String handoverNotes, Callback<Lending> callback) {
        LendingCreateRequest request = new LendingCreateRequest(itemId, expectedReturnDays, handoverNotes);
        lendingApi.requestLending(request).enqueue(new retrofit2.Callback<ApiResponse<LendingResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LendingResponse>> call,
                                   @NonNull Response<ApiResponse<LendingResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    LendingResponse dto = response.body().getData();
                    if (dto != null) {
                        callback.onSuccess(mapToDomain(dto, expectedReturnDays));
                        return;
                    }
                }
                String msg = ErrorParser.parseError(response, "Error al solicitar préstamo");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LendingResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void getMyLendings(Callback<List<Lending>> callback) {
        lendingApi.getMyLendings().enqueue(new retrofit2.Callback<ApiResponse<List<LendingResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<LendingResponse>>> call,
                                   @NonNull Response<ApiResponse<List<LendingResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<LendingResponse> dtos = response.body().getData();
                    List<Lending> lendings = new ArrayList<>();
                    if (dtos != null) {
                        for (LendingResponse dto : dtos) {
                            lendings.add(mapToDomain(dto, 0));
                        }
                    }
                    callback.onSuccess(lendings);
                    return;
                }
                String msg = ErrorParser.parseError(response, "Error al obtener préstamos");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<LendingResponse>>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void checkIn(Long lendingId, Callback<Lending> callback) {
        lendingApi.checkIn(lendingId).enqueue(new retrofit2.Callback<ApiResponse<LendingResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LendingResponse>> call,
                                   @NonNull Response<ApiResponse<LendingResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    LendingResponse dto = response.body().getData();
                    if (dto != null) {
                        callback.onSuccess(mapToDomain(dto, 0));
                        return;
                    }
                }
                String msg = ErrorParser.parseError(response, "Error al confirmar entrega");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LendingResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void checkOut(Long lendingId, Callback<Lending> callback) {
        lendingApi.checkOut(lendingId).enqueue(new retrofit2.Callback<ApiResponse<LendingResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LendingResponse>> call,
                                   @NonNull Response<ApiResponse<LendingResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    LendingResponse dto = response.body().getData();
                    if (dto != null) {
                        callback.onSuccess(mapToDomain(dto, 0));
                        return;
                    }
                }
                String msg = ErrorParser.parseError(response, "Error al confirmar devolución");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LendingResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void getLendingById(Long lendingId, Callback<Lending> callback) {
        getMyLendings(new Callback<List<Lending>>() {
            @Override
            public void onSuccess(List<Lending> result) {
                if (result != null) {
                    for (Lending l : result) {
                        if (l.getId() != null && l.getId().equals(lendingId)) {
                            callback.onSuccess(l);
                            return;
                        }
                    }
                }
                callback.onError("Préstamo no encontrado");
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    private Lending mapToDomain(LendingResponse dto, int fallbackDays) {
        return new Lending(
                dto.getId(),
                dto.getItemId(),
                dto.getItemTitle(),
                dto.getRequesterId(),
                dto.getRequesterName(),
                dto.getRequesterPhone(),
                dto.getDonorPhone(),
                dto.getRequestedAt(),
                dto.getStartedAt(),
                dto.getExpectedReturnAt(),
                dto.getActualReturnAt(),
                dto.getStatus(),
                dto.getHandoverNotes(),
                fallbackDays
        );
    }
}
