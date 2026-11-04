package com.example.redsoportex.data.remote.dto.alert;

import com.google.gson.annotations.SerializedName;

public class NeedAlertCreateRequest {

    @SerializedName("categoryId")
    private Long categoryId;

    @SerializedName("urgencyLevel")
    private String urgencyLevel;

    @SerializedName("maxRadiusKm")
    private Integer maxRadiusKm;

    public NeedAlertCreateRequest() {
    }

    public NeedAlertCreateRequest(Long categoryId, String urgencyLevel, Integer maxRadiusKm) {
        this.categoryId = categoryId;
        this.urgencyLevel = urgencyLevel;
        this.maxRadiusKm = maxRadiusKm;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
}
