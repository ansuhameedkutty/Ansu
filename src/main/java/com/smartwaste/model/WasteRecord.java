package com.smartwaste.model;

public class WasteRecord {
    private long id;
    private long reportId;
    private long collectorId;
    private double biodegradableKg;
    private double nonBiodegradableKg;
    private double recyclableKg;

    public WasteRecord() {}

    public WasteRecord(long id, long reportId, long collectorId, double biodegradableKg, double nonBiodegradableKg, double recyclableKg) {
        this.id = id;
        this.reportId = reportId;
        this.collectorId = collectorId;
        this.biodegradableKg = biodegradableKg;
        this.nonBiodegradableKg = nonBiodegradableKg;
        this.recyclableKg = recyclableKg;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getReportId() { return reportId; }
    public void setReportId(long reportId) { this.reportId = reportId; }

    public long getCollectorId() { return collectorId; }
    public void setCollectorId(long collectorId) { this.collectorId = collectorId; }

    public double getBiodegradableKg() { return biodegradableKg; }
    public void setBiodegradableKg(double biodegradableKg) { this.biodegradableKg = biodegradableKg; }

    public double getNonBiodegradableKg() { return nonBiodegradableKg; }
    public void setNonBiodegradableKg(double nonBiodegradableKg) { this.nonBiodegradableKg = nonBiodegradableKg; }

    public double getRecyclableKg() { return recyclableKg; }
    public void setRecyclableKg(double recyclableKg) { this.recyclableKg = recyclableKg; }
}
