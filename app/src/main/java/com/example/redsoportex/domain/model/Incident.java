package com.example.redsoportex.domain.model;

public class Incident {

    private final Long id;
    private final Long lendingId;
    private final String itemTitle;
    private final String incidentType;
    private final String description;
    private final String reportedAt;
    private final String resolutionStatus;

    public Incident(Long id, Long lendingId, String itemTitle,
                    String incidentType, String description,
                    String reportedAt, String resolutionStatus) {
        this.id = id;
        this.lendingId = lendingId;
        this.itemTitle = itemTitle;
        this.incidentType = incidentType;
        this.description = description;
        this.reportedAt = reportedAt;
        this.resolutionStatus = resolutionStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getLendingId() {
        return lendingId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getDescription() {
        return description;
    }

    public String getReportedAt() {
        return reportedAt;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }
}
