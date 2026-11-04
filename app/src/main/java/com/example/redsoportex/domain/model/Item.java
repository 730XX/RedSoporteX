package com.example.redsoportex.domain.model;

public class Item {
    private final Long id;
    private final Long donorId;
    private final String donorName;
    private final Long categoryId;
    private final String categoryName;
    private final String title;
    private final String description;
    private final String conservationStatus;
    private final String availabilityStatus;
    private final String imageUrl;
    private final Double approxLatitude;
    private final Double approxLongitude;
    private final String createdAt;

    public Item(Long id, Long donorId, String donorName, Long categoryId, String categoryName,
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

    public Long getDonorId() {
        return donorId;
    }

    public String getDonorName() {
        return donorName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getConservationStatus() {
        return conservationStatus;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getApproxLatitude() {
        return approxLatitude;
    }

    public Double getApproxLongitude() {
        return approxLongitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(availabilityStatus) || "DISPONIBLE".equalsIgnoreCase(availabilityStatus);
    }
}
