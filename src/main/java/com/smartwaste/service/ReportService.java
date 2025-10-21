package com.smartwaste.service;

import com.smartwaste.enums.WasteType;
import com.smartwaste.model.*;
import com.smartwaste.repo.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {
    private final Repository<WasteRecord> wasteRepo;
    private final Repository<PickupRequest> pickupRepo;
    private final Repository<Bin> binRepo;
    private final Repository<User> userRepo;

    public ReportService(Repository<WasteRecord> wasteRepo, Repository<PickupRequest> pickupRepo, Repository<Bin> binRepo, Repository<User> userRepo) {
        this.wasteRepo = wasteRepo;
        this.pickupRepo = pickupRepo;
        this.binRepo = binRepo;
        this.userRepo = userRepo;
    }

    public static class AnalysisResult {
        public final double totalCollectedKg;
        public final double percentRecycled;
        public final List<String> topCleanAreas;
        public AnalysisResult(double totalCollectedKg, double percentRecycled, List<String> topCleanAreas) {
            this.totalCollectedKg = totalCollectedKg;
            this.percentRecycled = percentRecycled;
            this.topCleanAreas = topCleanAreas;
        }
    }

    public AnalysisResult getRecyclingAnalysis() {
        List<WasteRecord> all = wasteRepo.findAll();
        double total = all.stream().mapToDouble(WasteRecord::getAmountKg).sum();
        double recycled = all.stream().filter(w -> w.getType() == WasteType.RECYCLABLE).mapToDouble(WasteRecord::getAmountKg).sum();
        double percent = total > 0 ? (recycled * 100.0 / total) : 0.0;

        Map<String, Long> areaCounts = binRepo.findAll().stream()
                .collect(Collectors.groupingBy(Bin::getArea, Collectors.counting()));
        List<String> topAreas = areaCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return new AnalysisResult(total, percent, topAreas);
    }

    public static class UserStats {
        public final double totalCollectedKg;
        public final double percentRecycled;
        public UserStats(double totalCollectedKg, double percentRecycled) {
            this.totalCollectedKg = totalCollectedKg;
            this.percentRecycled = percentRecycled;
        }
    }

    public UserStats getUserRecyclingStats(String citizenId) {
        List<WasteRecord> records = wasteRepo.findAll().stream().filter(w -> Objects.equals(w.getCitizenId(), citizenId)).collect(Collectors.toList());
        double total = records.stream().mapToDouble(WasteRecord::getAmountKg).sum();
        double recycled = records.stream().filter(w -> w.getType() == WasteType.RECYCLABLE).mapToDouble(WasteRecord::getAmountKg).sum();
        double percent = total > 0 ? (recycled * 100.0 / total) : 0.0;
        return new UserStats(total, percent);
    }

    public String generateDailyCollectionReport() {
        LocalDate today = LocalDate.now();
        var todayRecords = wasteRepo.findAll().stream()
                .filter(w -> w.getTimestamp().toLocalDate().equals(today))
                .collect(Collectors.toList());
        double total = todayRecords.stream().mapToDouble(WasteRecord::getAmountKg).sum();
        double recycled = todayRecords.stream().filter(w -> w.getType() == WasteType.RECYCLABLE).mapToDouble(WasteRecord::getAmountKg).sum();
        return "Daily Collection Report (" + today + ")\n" +
                "Total collected: " + String.format("%.2f", total) + " kg\n" +
                "Recycled: " + String.format("%.2f", recycled) + " kg\n" +
                "Pickups completed: " + pickupRepo.findAll().stream().filter(p -> p.getCompletedAt() != null && p.getCompletedAt().toLocalDate().equals(today)).count();
    }

    public String generateCollectorPerformanceReport() {
        Map<String, Double> byCollector = new HashMap<>();
        for (WasteRecord wr : wasteRepo.findAll()) {
            byCollector.merge(wr.getCollectorId(), wr.getAmountKg(), Double::sum);
        }
        StringBuilder sb = new StringBuilder("Collector Performance\n");
        byCollector.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> sb.append(e.getKey()).append(": ").append(String.format("%.2f kg", e.getValue())).append('\n'));
        return sb.toString();
    }

    public String generateSystemStatistics() {
        long users = userRepo.findAll().size();
        long bins = binRepo.findAll().size();
        long pickups = pickupRepo.findAll().size();
        double total = wasteRepo.findAll().stream().mapToDouble(WasteRecord::getAmountKg).sum();
        return "System Statistics\nUsers: " + users + "\nBins: " + bins + "\nPickups: " + pickups + "\nTotal Waste: " + String.format("%.2f kg", total);
    }
}
