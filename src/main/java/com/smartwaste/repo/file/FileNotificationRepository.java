package com.smartwaste.repo.file;

import com.smartwaste.model.Notification;
import com.smartwaste.repo.NotificationRepository;
import com.smartwaste.util.CsvUtils;
import com.smartwaste.util.FileUtils;
import com.smartwaste.util.IdGenerator;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileNotificationRepository implements NotificationRepository {
    private final Path path = FileUtils.path("data", "notifications.csv");

    private String serialize(Notification n) {
        return CsvUtils.join(
                Long.toString(n.getId()),
                Long.toString(n.getUserId()),
                n.getMessage(),
                n.getCreatedAt() == null ? "" : n.getCreatedAt().toString(),
                Boolean.toString(n.isRead())
        );
    }

    private Notification deserialize(String line) {
        String[] p = CsvUtils.split(line);
        long id = Long.parseLong(p[0]);
        long userId = Long.parseLong(p[1]);
        String message = p[2];
        LocalDateTime created = p[3].isEmpty() ? null : LocalDateTime.parse(p[3]);
        boolean read = Boolean.parseBoolean(p[4]);
        return new Notification(id, userId, message, created, read);
    }

    @Override
    public synchronized Notification save(Notification notification) {
        List<Notification> all = findAll();
        if (notification.getId() == 0L) {
            notification.setId(IdGenerator.nextId("notification"));
            if (notification.getCreatedAt() == null) notification.setCreatedAt(LocalDateTime.now());
            all.add(notification);
        } else {
            boolean updated = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == notification.getId()) {
                    all.set(i, notification);
                    updated = true;
                    break;
                }
            }
            if (!updated) all.add(notification);
        }
        List<String> lines = new ArrayList<>();
        for (Notification n : all) lines.add(serialize(n));
        FileUtils.writeAllLines(path, lines);
        return notification;
    }

    @Override
    public synchronized Optional<Notification> findById(long id) {
        return findAll().stream().filter(n -> n.getId() == id).findFirst();
    }

    @Override
    public synchronized List<Notification> findAll() {
        List<String> lines = FileUtils.readAllLines(path);
        List<Notification> list = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            list.add(deserialize(line));
        }
        return list;
    }

    @Override
    public synchronized List<Notification> findByUserId(long userId) {
        List<Notification> out = new ArrayList<>();
        for (Notification n : findAll()) if (n.getUserId() == userId) out.add(n);
        return out;
    }
}
