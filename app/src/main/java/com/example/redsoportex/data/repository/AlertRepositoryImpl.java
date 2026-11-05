package com.example.redsoportex.data.repository;

import androidx.annotation.NonNull;

import com.example.redsoportex.core.network.ApiClient;
import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.core.network.ErrorParser;
import com.example.redsoportex.data.remote.api.AlertApi;
import com.example.redsoportex.data.remote.dto.alert.NeedAlertCreateRequest;
import com.example.redsoportex.data.remote.dto.alert.NeedAlertResponse;
import com.example.redsoportex.domain.model.NeedAlert;
import com.example.redsoportex.domain.repository.AlertRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AlertRepositoryImpl implements AlertRepository {

    private final AlertApi alertApi;

    public AlertRepositoryImpl() {
        this.alertApi = ApiClient.createService(AlertApi.class);
    }

    public AlertRepositoryImpl(@NonNull AlertApi alertApi) {
        this.alertApi = alertApi;
    }

    @Override
    public void createAlert(Long categoryId, String urgency, int radiusKm, Callback<NeedAlert> callback) {
        NeedAlertCreateRequest request = new NeedAlertCreateRequest(categoryId, urgency, radiusKm);
        alertApi.createAlert(request).enqueue(new retrofit2.Callback<ApiResponse<NeedAlertResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<NeedAlertResponse>> call,
                                   @NonNull Response<ApiResponse<NeedAlertResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    NeedAlertResponse dto = response.body().getData();
                    if (dto != null) {
                        callback.onSuccess(mapToDomain(dto));
                        return;
                    }
                }
                String msg = ErrorParser.parseError(response, "Error al crear alerta");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<NeedAlertResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void getMyAlerts(Callback<List<NeedAlert>> callback) {
        alertApi.getMyAlerts().enqueue(new retrofit2.Callback<ApiResponse<List<NeedAlertResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<NeedAlertResponse>>> call,
                                   @NonNull Response<ApiResponse<List<NeedAlertResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<NeedAlertResponse> dtos = response.body().getData();
                    List<NeedAlert> alerts = new ArrayList<>();
                    if (dtos != null) {
                        for (NeedAlertResponse dto : dtos) {
                            alerts.add(mapToDomain(dto));
                        }
                    }
                    callback.onSuccess(alerts);
                    return;
                }
                String msg = ErrorParser.parseError(response, "Error al obtener alertas");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<NeedAlertResponse>>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void cancelAlert(Long id, Callback<NeedAlert> callback) {
        alertApi.cancelAlert(id).enqueue(new retrofit2.Callback<ApiResponse<NeedAlertResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<NeedAlertResponse>> call,
                                   @NonNull Response<ApiResponse<NeedAlertResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    NeedAlertResponse dto = response.body().getData();
                    if (dto != null) {
                        callback.onSuccess(mapToDomain(dto));
                        return;
                    }
                }
                String msg = ErrorParser.parseError(response, "Error al cancelar alerta");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<NeedAlertResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    private NeedAlert mapToDomain(@NonNull NeedAlertResponse dto) {
        return new NeedAlert(
                dto.getId(),
                dto.getRequesterName(),
                dto.getCategoryId(),
                dto.getCategoryName(),
                dto.getUrgencyLevel(),
                dto.getMaxRadiusKm(),
                dto.getStatus(),
                dto.getCreatedAt()
        );
    }
}
