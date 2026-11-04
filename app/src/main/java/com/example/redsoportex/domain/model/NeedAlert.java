package com.example.redsoportex.domain.model;

public class NeedAlert {

    private final Long id;
    private final String requesterName;
    private final Long categoryId;
    private final String categoryName;
    private final String urgencyLevel;
    private final Integer maxRadiusKm;
    private final String status;
    private final String createdAt;

    public NeedAlert(Long id, Long categoryId, String categoryName,
                     String urgencyLevel, Integer maxRadiusKm,
                     String status, String createdAt) {
        this(id, null, categoryId, categoryName, urgencyLevel, maxRadiusKm, status, createdAt);
    }

    public NeedAlert(Long id, String requesterName, Long categoryId, String categoryName,
                     String urgencyLevel, Integer maxRadiusKm,
                     String status, String createdAt) {
        this.id = id;
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

    public String getRequesterName() {
        return requesterName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public Integer getMaxRadiusKm() {
        return maxRadiusKm;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }
}
