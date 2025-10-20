package com.smartwaste.repo.file;

import com.smartwaste.model.ReportStatus;
import com.smartwaste.model.WasteReport;
import com.smartwaste.model.WasteType;
import com.smartwaste.repo.WasteReportRepository;
import com.smartwaste.util.CsvUtils;
import com.smartwaste.util.FileUtils;
import com.smartwaste.util.IdGenerator;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileWasteReportRepository implements WasteReportRepository {
    private final Path path = FileUtils.path("data", "waste_reports.csv");

    private String serialize(WasteReport r) {
        String created = r.getCreatedAt() == null ? "" : r.getCreatedAt().toString();
        String updated = r.getUpdatedAt() == null ? "" : r.getUpdatedAt().toString();
        return CsvUtils.join(
                Long.toString(r.getId()),
                Long.toString(r.getCitizenId()),
                r.getLocationDescription(),
                Boolean.toString(r.isSpecialPickup()),
                r.getWasteType() == null ? "" : r.getWasteType().name(),
                r.getDescription(),
                r.getStatus() == null ? "" : r.getStatus().name(),
                r.getAssignedCollectorId() == null ? "" : Long.toString(r.getAssignedCollectorId()),
                created,
                updated
        );
    }

    private WasteReport deserialize(String line) {
        String[] p = CsvUtils.split(line);
        long id = Long.parseLong(p[0]);
        long citizenId = Long.parseLong(p[1]);
        String loc = p[2];
        boolean special = Boolean.parseBoolean(p[3]);
        WasteType wt = p[4].isEmpty() ? null : WasteType.valueOf(p[4]);
        String desc = p[5];
        ReportStatus st = p[6].isEmpty() ? null : ReportStatus.valueOf(p[6]);
        Long collectorId = p[7].isEmpty() ? null : Long.parseLong(p[7]);
        LocalDateTime created = p[8].isEmpty() ? null : LocalDateTime.parse(p[8]);
        LocalDateTime updated = p[9].isEmpty() ? null : LocalDateTime.parse(p[9]);
        return new WasteReport(id, citizenId, loc, special, wt, desc, st, collectorId, created, updated);
    }

    @Override
    public synchronized WasteReport save(WasteReport report) {
        List<WasteReport> all = findAll();
        if (report.getId() == 0L) {
            report.setId(IdGenerator.nextId("report"));
            if (report.getCreatedAt() == null) report.setCreatedAt(LocalDateTime.now());
            report.setUpdatedAt(report.getCreatedAt());
            all.add(report);
        } else {
            report.setUpdatedAt(LocalDateTime.now());
            boolean updated = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == report.getId()) {
                    all.set(i, report);
                    updated = true;
                    break;
                }
            }
            if (!updated) all.add(report);
        }
        List<String> lines = new ArrayList<>();
        for (WasteReport r : all) lines.add(serialize(r));
        FileUtils.writeAllLines(path, lines);
        return report;
    }

    @Override
    public synchronized Optional<WasteReport> findById(long id) {
        return findAll().stream().filter(r -> r.getId() == id).findFirst();
    }

    @Override
    public synchronized List<WasteReport> findAll() {
        List<String> lines = FileUtils.readAllLines(path);
        List<WasteReport> list = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            list.add(deserialize(line));
        }
        return list;
    }

    @Override
    public synchronized List<WasteReport> findByCitizenId(long citizenId) {
        List<WasteReport> out = new ArrayList<>();
        for (WasteReport r : findAll()) {
            if (r.getCitizenId() == citizenId) out.add(r);
        }
        return out;
    }
}
