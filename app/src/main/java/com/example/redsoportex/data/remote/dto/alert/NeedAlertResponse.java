package com.example.redsoportex.data.remote.dto.alert;

import com.google.gson.annotations.SerializedName;

public class NeedAlertResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("requesterId")
    private Long requesterId;

    @SerializedName("requesterName")
    private String requesterName;

    @SerializedName("categoryId")
    private Long categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("urgencyLevel")
    private String urgencyLevel;

    @SerializedName("maxRadiusKm")
    private Integer maxRadiusKm;

    @SerializedName("status")
    private String status;

    @SerializedName("createdAt")
    private String createdAt;

    public NeedAlertResponse() {
    }

    public NeedAlertResponse(Long id, Long requesterId, String requesterName,
                             Long categoryId, String categoryName, String urgencyLevel,
                             Integer maxRadiusKm, String status, String createdAt) {
        this.id = id;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.urgencyLevel = urgencyLevel;
        this.maxRadiusKm = maxRadiusKm;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public Integer getMaxRadiusKm() {
        return maxRadiusKm;
    }

    public void setMaxRadiusKm(Integer maxRadiusKm) {
        this.maxRadiusKm = maxRadiusKm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
