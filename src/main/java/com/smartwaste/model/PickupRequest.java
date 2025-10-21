package com.smartwaste.model;

import com.smartwaste.enums.PickupStatus;
import com.smartwaste.enums.WasteType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class PickupRequest implements Identifiable, Serializable {
    private final String id;
    private final String citizenId;
    private final String address;
    private final WasteType wasteType;
    private final double amountKg;
    private PickupStatus status;
    private String assignedCollectorId;
    private LocalDateTime createdAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;

    public PickupRequest(String id, String citizenId, String address, WasteType wasteType, double amountKg) {
        this.id = id;
        this.citizenId = citizenId;
        this.address = address;
        this.wasteType = wasteType;
        this.amountKg = amountKg;
        this.status = PickupStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        // naive auto-schedule: next day 9AM
        this.scheduledAt = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 0));
    }

    @Override
    public String getId() { return id; }
    public String getCitizenId() { return citizenId; }
    public String getAddress() { return address; }
    public WasteType getWasteType() { return wasteType; }
    public double getAmountKg() { return amountKg; }
    public PickupStatus getStatus() { return status; }
    public void setStatus(PickupStatus status) { this.status = status; }
    public String getAssignedCollectorId() { return assignedCollectorId; }
    public void setAssignedCollectorId(String assignedCollectorId) { this.assignedCollectorId = assignedCollectorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
}
