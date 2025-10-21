package com.smartwaste.service;

import com.smartwaste.enums.NotificationType;
import com.smartwaste.enums.WasteType;
import com.smartwaste.model.*;
import com.smartwaste.repo.Repository;
import com.smartwaste.util.IdGenerator;

import java.util.List;

public class WasteService {
    private final Repository<WasteRecord> wasteRepo;
    private final Repository<Notification> notifRepo;
    private final Repository<User> userRepo;

    public WasteService(Repository<WasteRecord> wasteRepo, Repository<Notification> notifRepo, Repository<User> userRepo) {
        this.wasteRepo = wasteRepo;
        this.notifRepo = notifRepo;
        this.userRepo = userRepo;
    }

    public void reportFullBin(String citizenId, String location, String message) {
        // For simplicity, just create a notification for admin (first admin)
        List<User> users = userRepo.findAll();
        User admin = users.stream().filter(u -> u.getRole().name().equals("ADMIN")).findFirst().orElse(null);
        if (admin != null) {
            Notification n = new Notification(IdGenerator.newId(), admin.getId(), NotificationType.INFO,
                    "Bin overflow reported at " + location + (message == null || message.isBlank() ? "" : (" - " + message)));
            notifRepo.save(n);
        }
    }

    public WasteRecord recordCollection(String collectorId, String citizenId, String binId, WasteType type, double kg) {
        WasteRecord wr = new WasteRecord(IdGenerator.newId(), collectorId, citizenId, binId, type, kg);
        wasteRepo.save(wr);
        if (citizenId != null) {
            Notification n = new Notification(IdGenerator.newId(), citizenId, NotificationType.COLLECTION_CONFIRMED, "Your waste pickup has been collected.");
            notifRepo.save(n);
        }
        return wr;
    }

    public List<WasteRecord> allWaste() { return wasteRepo.findAll(); }
}
