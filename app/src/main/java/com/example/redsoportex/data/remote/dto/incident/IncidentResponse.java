package com.example.redsoportex.data.remote.dto.incident;

import com.google.gson.annotations.SerializedName;

public class IncidentResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("lendingId")
    private Long lendingId;

    @SerializedName("itemTitle")
    private String itemTitle;

    @SerializedName("reporterId")
    private Long reporterId;

    @SerializedName("reporterName")
    private String reporterName;

    @SerializedName("incidentType")
    private String incidentType;

    @SerializedName("description")
    private String description;

    @SerializedName("reportedAt")
    private String reportedAt;

    @SerializedName("resolutionStatus")
    private String resolutionStatus;

    public IncidentResponse() {
    }

    public IncidentResponse(Long id, Long lendingId, String itemTitle, Long reporterId,
                            String reporterName, String incidentType, String description,
                            String reportedAt, String resolutionStatus) {
        this.id = id;
        this.lendingId = lendingId;
        this.itemTitle = itemTitle;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.incidentType = incidentType;
        this.description = description;
        this.reportedAt = reportedAt;
        this.resolutionStatus = resolutionStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLendingId() {
        return lendingId;
    }

    public void setLendingId(Long lendingId) {
        this.lendingId = lendingId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
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

    public String getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(String reportedAt) {
        this.reportedAt = reportedAt;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(String resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }
}
