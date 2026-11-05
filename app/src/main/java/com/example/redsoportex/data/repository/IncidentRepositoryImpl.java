package com.example.redsoportex.data.repository;

import androidx.annotation.NonNull;

import com.example.redsoportex.core.network.ApiClient;
import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.core.network.ErrorParser;
import com.example.redsoportex.data.remote.api.IncidentApi;
import com.example.redsoportex.data.remote.dto.incident.IncidentCreateRequest;
import com.example.redsoportex.data.remote.dto.incident.IncidentResponse;
import com.example.redsoportex.domain.model.Incident;
import com.example.redsoportex.domain.repository.IncidentRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class IncidentRepositoryImpl implements IncidentRepository {

    private final IncidentApi incidentApi;

    public IncidentRepositoryImpl() {
        this.incidentApi = ApiClient.createService(IncidentApi.class);
    }

    public IncidentRepositoryImpl(@NonNull IncidentApi incidentApi) {
        this.incidentApi = incidentApi;
    }

    @Override
    public void reportIncident(Long lendingId, String type, String description, Callback<Incident> callback) {
        IncidentCreateRequest request = new IncidentCreateRequest(lendingId, type, description);
        incidentApi.reportIncident(request).enqueue(new retrofit2.Callback<ApiResponse<IncidentResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<IncidentResponse>> call,
                                   @NonNull Response<ApiResponse<IncidentResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    IncidentResponse dto = response.body().getData();
                    if (dto != null) {
                        callback.onSuccess(mapToDomain(dto));
                        return;
                    }
                }
                String msg = ErrorParser.parseError(response, "Error al reportar incidencia");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<IncidentResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void getIncidentsByLending(Long lendingId, Callback<List<Incident>> callback) {
        incidentApi.getIncidentsByLending(lendingId).enqueue(new retrofit2.Callback<ApiResponse<List<IncidentResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<IncidentResponse>>> call,
                                   @NonNull Response<ApiResponse<List<IncidentResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<IncidentResponse> dtos = response.body().getData();
                    List<Incident> incidents = new ArrayList<>();
                    if (dtos != null) {
                        for (IncidentResponse dto : dtos) {
                            incidents.add(mapToDomain(dto));
                        }
                    }
                    callback.onSuccess(incidents);
                    return;
                }
                String msg = ErrorParser.parseError(response, "Error al obtener incidencias");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<IncidentResponse>>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void getMyReportedIncidents(Callback<List<Incident>> callback) {
        incidentApi.getMyReportedIncidents().enqueue(new retrofit2.Callback<ApiResponse<List<IncidentResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<IncidentResponse>>> call,
                                   @NonNull Response<ApiResponse<List<IncidentResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<IncidentResponse> dtos = response.body().getData();
                    List<Incident> incidents = new ArrayList<>();
                    if (dtos != null) {
                        for (IncidentResponse dto : dtos) {
                            incidents.add(mapToDomain(dto));
                        }
                    }
                    callback.onSuccess(incidents);
                    return;
                }
                String msg = ErrorParser.parseError(response, "Error al obtener mis incidencias");
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<IncidentResponse>>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    private Incident mapToDomain(@NonNull IncidentResponse dto) {
        return new Incident(
                dto.getId(),
                dto.getLendingId(),
                dto.getItemTitle(),
                dto.getIncidentType(),
                dto.getDescription(),
                dto.getReportedAt(),
                dto.getResolutionStatus()
        );
    }
}
