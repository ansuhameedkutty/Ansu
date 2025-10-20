package com.smartwaste.repo.file;

import com.smartwaste.model.WasteRecord;
import com.smartwaste.repo.WasteRecordRepository;
import com.smartwaste.util.CsvUtils;
import com.smartwaste.util.FileUtils;
import com.smartwaste.util.IdGenerator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileWasteRecordRepository implements WasteRecordRepository {
    private final Path path = FileUtils.path("data", "waste_records.csv");

    private String serialize(WasteRecord r) {
        return CsvUtils.join(
                Long.toString(r.getId()),
                Long.toString(r.getReportId()),
                Long.toString(r.getCollectorId()),
                Double.toString(r.getBiodegradableKg()),
                Double.toString(r.getNonBiodegradableKg()),
                Double.toString(r.getRecyclableKg())
        );
    }

    private WasteRecord deserialize(String line) {
        String[] p = CsvUtils.split(line);
        long id = Long.parseLong(p[0]);
        long reportId = Long.parseLong(p[1]);
        long collectorId = Long.parseLong(p[2]);
        double bio = Double.parseDouble(p[3]);
        double nonBio = Double.parseDouble(p[4]);
        double rec = Double.parseDouble(p[5]);
        return new WasteRecord(id, reportId, collectorId, bio, nonBio, rec);
    }

    @Override
    public synchronized WasteRecord save(WasteRecord record) {
        List<WasteRecord> all = findAll();
        if (record.getId() == 0L) {
            record.setId(IdGenerator.nextId("record"));
            all.add(record);
        } else {
            boolean updated = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == record.getId()) {
                    all.set(i, record);
                    updated = true;
                    break;
                }
            }
            if (!updated) all.add(record);
        }
        List<String> lines = new ArrayList<>();
        for (WasteRecord r : all) lines.add(serialize(r));
        FileUtils.writeAllLines(path, lines);
        return record;
    }

    @Override
    public synchronized Optional<WasteRecord> findById(long id) {
        return findAll().stream().filter(r -> r.getId() == id).findFirst();
    }

    @Override
    public synchronized List<WasteRecord> findAll() {
        List<String> lines = FileUtils.readAllLines(path);
        List<WasteRecord> list = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            list.add(deserialize(line));
        }
        return list;
    }

    @Override
    public synchronized List<WasteRecord> findByCollectorId(long collectorId) {
        List<WasteRecord> out = new ArrayList<>();
        for (WasteRecord r : findAll()) if (r.getCollectorId() == collectorId) out.add(r);
        return out;
    }
}
