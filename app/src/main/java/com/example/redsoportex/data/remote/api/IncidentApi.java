package com.example.redsoportex.data.remote.api;

import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.data.remote.dto.incident.IncidentCreateRequest;
import com.example.redsoportex.data.remote.dto.incident.IncidentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IncidentApi {

    @POST("api/v1/incidents")
    Call<ApiResponse<IncidentResponse>> reportIncident(@Body IncidentCreateRequest request);

    @GET("api/v1/incidents/lending/{lendingId}")
    Call<ApiResponse<List<IncidentResponse>>> getIncidentsByLending(@Path("lendingId") Long lendingId);

    @GET("api/v1/incidents/mis-reportes")
    Call<ApiResponse<List<IncidentResponse>>> getMyReportedIncidents();
}
