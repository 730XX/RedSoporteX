package com.example.redsoportex.data.remote.dto.item;

import com.google.gson.annotations.SerializedName;

public class ItemCreateRequest {

    @SerializedName("categoryId")
    private Long categoryId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("conservationStatus")
    private String conservationStatus;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("approxLatitude")
    private Double approxLatitude;

    @SerializedName("approxLongitude")
    private Double approxLongitude;

    public ItemCreateRequest() {
    }

    public ItemCreateRequest(Long categoryId, String title, String description,
                             String conservationStatus, String imageUrl,
                             Double approxLatitude, Double approxLongitude) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.conservationStatus = conservationStatus;
        this.imageUrl = imageUrl;
        this.approxLatitude = approxLatitude;
        this.approxLongitude = approxLongitude;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConservationStatus() {
        return conservationStatus;
    }

    public void setConservationStatus(String conservationStatus) {
        this.conservationStatus = conservationStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getApproxLatitude() {
        return approxLatitude;
    }

    public void setApproxLatitude(Double approxLatitude) {
        this.approxLatitude = approxLatitude;
    }

    public Double getApproxLongitude() {
        return approxLongitude;
    }

    public void setApproxLongitude(Double approxLongitude) {
        this.approxLongitude = approxLongitude;
    }
}
