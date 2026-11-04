package com.example.redsoportex.data.remote.dto.incident;

import com.google.gson.annotations.SerializedName;

public class IncidentCreateRequest {

    @SerializedName("lendingId")
    private Long lendingId;

    @SerializedName("incidentType")
    private String incidentType;

    @SerializedName("description")
    private String description;

    public IncidentCreateRequest() {
    }

    public IncidentCreateRequest(Long lendingId, String incidentType, String description) {
        this.lendingId = lendingId;
        this.incidentType = incidentType;
        this.description = description;
    }

    public Long getLendingId() {
        return lendingId;
    }

    public void setLendingId(Long lendingId) {
        this.lendingId = lendingId;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
