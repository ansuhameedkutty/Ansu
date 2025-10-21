package com.smartwaste.model;

import com.smartwaste.enums.NotificationType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Identifiable, Serializable {
    private final String id;
    private final String userId;
    private final NotificationType type;
    private final String message;
    private final LocalDateTime timestamp;
    private boolean read;

    public Notification(String id, String userId, NotificationType type, String message) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    @Override
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public NotificationType getType() { return type; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
