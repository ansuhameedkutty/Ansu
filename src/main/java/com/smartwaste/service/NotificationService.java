package com.smartwaste.service;

import com.smartwaste.model.Notification;
import com.smartwaste.repo.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationService {
    private final Repository<Notification> notifRepo;

    public NotificationService(Repository<Notification> notifRepo) {
        this.notifRepo = notifRepo;
    }

    public List<Notification> getNotifications(String userId) {
        return notifRepo.findAll().stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isRead())
                .collect(Collectors.toList());
    }

    public void markAllRead(String userId) {
        notifRepo.findAll().forEach(n -> {
            if (n.getUserId().equals(userId)) {
                n.setRead(true);
                notifRepo.save(n);
            }
        });
    }
}
