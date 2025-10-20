package com.smartwaste.model;

import java.time.LocalDateTime;

public class CollectionTask {
    private long id;
    private long reportId; // links to WasteReport
    private long collectorId;
    private LocalDateTime scheduledAt;
    private boolean completed;

    public CollectionTask() {}

    public CollectionTask(long id, long reportId, long collectorId, LocalDateTime scheduledAt, boolean completed) {
        this.id = id;
        this.reportId = reportId;
        this.collectorId = collectorId;
        this.scheduledAt = scheduledAt;
        this.completed = completed;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getReportId() { return reportId; }
    public void setReportId(long reportId) { this.reportId = reportId; }

    public long getCollectorId() { return collectorId; }
    public void setCollectorId(long collectorId) { this.collectorId = collectorId; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
