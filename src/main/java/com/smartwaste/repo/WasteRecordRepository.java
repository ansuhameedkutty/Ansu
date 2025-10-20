package com.smartwaste.repo;

import com.smartwaste.model.WasteRecord;
import java.util.List;
import java.util.Optional;

public interface WasteRecordRepository {
    WasteRecord save(WasteRecord record);
    Optional<WasteRecord> findById(long id);
    List<WasteRecord> findAll();
    List<WasteRecord> findByCollectorId(long collectorId);
}
