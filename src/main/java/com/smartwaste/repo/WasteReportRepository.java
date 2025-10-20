package com.smartwaste.repo;

import com.smartwaste.model.WasteReport;
import java.util.List;
import java.util.Optional;

public interface WasteReportRepository {
    WasteReport save(WasteReport report);
    Optional<WasteReport> findById(long id);
    List<WasteReport> findAll();
    List<WasteReport> findByCitizenId(long citizenId);
}
