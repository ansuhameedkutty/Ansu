package com.smartwaste.service;

import com.smartwaste.enums.PickupStatus;
import com.smartwaste.enums.WasteType;
import com.smartwaste.model.*;
import com.smartwaste.repo.Repository;
import com.smartwaste.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CollectionService {
    private final Repository<PickupRequest> pickupRepo;
    private final Repository<User> userRepo;
    private final Repository<WasteRecord> wasteRepo;
    private final Repository<Notification> notifRepo;
    private final Repository<Bin> binRepo;

    public CollectionService(Repository<PickupRequest> pickupRepo, Repository<User> userRepo, Repository<WasteRecord> wasteRepo, Repository<Notification> notifRepo, Repository<Bin> binRepo) {
        this.pickupRepo = pickupRepo;
        this.userRepo = userRepo;
        this.wasteRepo = wasteRepo;
        this.notifRepo = notifRepo;
        this.binRepo = binRepo;
    }

    public PickupRequest createPickupRequest(String citizenId, String address, WasteType type, double kg) {
        PickupRequest pr = new PickupRequest(IdGenerator.newId(), citizenId, address, type, kg);
        // naive auto-assign: first available collector
        String collectorId = userRepo.findAll().stream().filter(u -> u.getRole().name().equals("COLLECTOR")).map(User::getId).findFirst().orElse(null);
        if (collectorId != null) {
            pr.setAssignedCollectorId(collectorId);
            pr.setStatus(PickupStatus.ASSIGNED);
        }
        pickupRepo.save(pr);
        return pr;
    }

    public List<PickupRequest> getAssignedPickups(String collectorId) {
        return pickupRepo.findAll().stream()
                .filter(p -> Objects.equals(p.getAssignedCollectorId(), collectorId) && p.getStatus() != PickupStatus.COMPLETED)
                .collect(Collectors.toList());
    }

    public void completePickup(String collectorId, String pickupId, double collectedKg) {
        PickupRequest pr = pickupRepo.findById(pickupId).orElseThrow(() -> new RuntimeException("Pickup not found"));
        if (!Objects.equals(pr.getAssignedCollectorId(), collectorId)) {
            throw new RuntimeException("Pickup not assigned to you");
        }
        pr.setStatus(PickupStatus.COMPLETED);
        pr.setCompletedAt(LocalDateTime.now());
        pickupRepo.save(pr);
        WasteRecord wr = new WasteRecord(IdGenerator.newId(), collectorId, pr.getCitizenId(), null, pr.getWasteType(), collectedKg);
        wasteRepo.save(wr);
        if (pr.getCitizenId() != null) {
            Notification n = new Notification(IdGenerator.newId(), pr.getCitizenId(), com.smartwaste.enums.NotificationType.COLLECTION_CONFIRMED, "Your scheduled pickup is completed.");
            notifRepo.save(n);
        }
    }
}
