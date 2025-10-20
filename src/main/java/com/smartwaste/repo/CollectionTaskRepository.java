package com.smartwaste.repo;

import com.smartwaste.model.CollectionTask;
import java.util.List;
import java.util.Optional;

public interface CollectionTaskRepository {
    CollectionTask save(CollectionTask task);
    Optional<CollectionTask> findById(long id);
    List<CollectionTask> findAll();
    List<CollectionTask> findByCollectorId(long collectorId);
}
