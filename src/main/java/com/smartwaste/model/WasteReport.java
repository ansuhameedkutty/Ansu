package com.smartwaste.model;

import java.time.LocalDateTime;

public class WasteReport {
    private long id;
    private long citizenId;
    private String locationDescription;
    private boolean specialPickup;
    private WasteType wasteType; // citizen's classification or system-determined
    private String description;
    private ReportStatus status;
    private Long assignedCollectorId; // nullable
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WasteReport() {}

    public WasteReport(long id, long citizenId, String locationDescription, boolean specialPickup, WasteType wasteType,
                       String description, ReportStatus status, Long assignedCollectorId, LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
        this.id = id;
        this.citizenId = citizenId;
        this.locationDescription = locationDescription;
        this.specialPickup = specialPickup;
        this.wasteType = wasteType;
        this.description = description;
        this.status = status;
        this.assignedCollectorId = assignedCollectorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getCitizenId() { return citizenId; }
    public void setCitizenId(long citizenId) { this.citizenId = citizenId; }

    public String getLocationDescription() { return locationDescription; }
    public void setLocationDescription(String locationDescription) { this.locationDescription = locationDescription; }

    public boolean isSpecialPickup() { return specialPickup; }
    public void setSpecialPickup(boolean specialPickup) { this.specialPickup = specialPickup; }

    public WasteType getWasteType() { return wasteType; }
    public void setWasteType(WasteType wasteType) { this.wasteType = wasteType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public Long getAssignedCollectorId() { return assignedCollectorId; }
    public void setAssignedCollectorId(Long assignedCollectorId) { this.assignedCollectorId = assignedCollectorId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
