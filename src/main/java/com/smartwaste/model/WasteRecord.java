package com.smartwaste.model;

import com.smartwaste.enums.WasteType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WasteRecord implements Identifiable, Serializable {
    private final String id;
    private final String collectorId;
    private final String citizenId; // optional if from bin
    private final String binId; // optional if special pickup
    private final WasteType type;
    private final double amountKg;
    private final LocalDateTime timestamp;

    public WasteRecord(String id, String collectorId, String citizenId, String binId, WasteType type, double amountKg) {
        this.id = id;
        this.collectorId = collectorId;
        this.citizenId = citizenId;
        this.binId = binId;
        this.type = type;
        this.amountKg = amountKg;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String getId() { return id; }
    public String getCollectorId() { return collectorId; }
    public String getCitizenId() { return citizenId; }
    public String getBinId() { return binId; }
    public WasteType getType() { return type; }
    public double getAmountKg() { return amountKg; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
