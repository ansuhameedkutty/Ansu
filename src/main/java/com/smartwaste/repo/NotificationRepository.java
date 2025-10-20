package com.smartwaste.repo;

import com.smartwaste.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(long id);
    List<Notification> findAll();
    List<Notification> findByUserId(long userId);
}
