package com.example.redsoportex.data.remote.dto.item;

import com.google.gson.annotations.SerializedName;

public class ItemResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("donorId")
    private Long donorId;

    @SerializedName("donorName")
    private String donorName;

    @SerializedName("categoryId")
    private Long categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("conservationStatus")
    private String conservationStatus;

    @SerializedName("availabilityStatus")
    private String availabilityStatus;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("approxLatitude")
    private Double approxLatitude;

    @SerializedName("approxLongitude")
    private Double approxLongitude;

    @SerializedName("createdAt")
    private String createdAt;

    public ItemResponse() {
    }

    public ItemResponse(Long id, Long donorId, String donorName, Long categoryId, String categoryName,
                        String title, String description, String conservationStatus,
                        String availabilityStatus, String imageUrl, Double approxLatitude,
                        Double approxLongitude, String createdAt) {
        this.id = id;
        this.donorId = donorId;
        this.donorName = donorName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.title = title;
        this.description = description;
        this.conservationStatus = conservationStatus;
        this.availabilityStatus = availabilityStatus;
        this.imageUrl = imageUrl;
        this.approxLatitude = approxLatitude;
        this.approxLongitude = approxLongitude;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
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

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
