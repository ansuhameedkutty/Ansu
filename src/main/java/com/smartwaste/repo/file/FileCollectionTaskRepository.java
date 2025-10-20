package com.smartwaste.repo.file;

import com.smartwaste.model.CollectionTask;
import com.smartwaste.repo.CollectionTaskRepository;
import com.smartwaste.util.CsvUtils;
import com.smartwaste.util.FileUtils;
import com.smartwaste.util.IdGenerator;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileCollectionTaskRepository implements CollectionTaskRepository {
    private final Path path = FileUtils.path("data", "collection_tasks.csv");

    private String serialize(CollectionTask t) {
        return CsvUtils.join(
                Long.toString(t.getId()),
                Long.toString(t.getReportId()),
                Long.toString(t.getCollectorId()),
                t.getScheduledAt() == null ? "" : t.getScheduledAt().toString(),
                Boolean.toString(t.isCompleted())
        );
    }

    private CollectionTask deserialize(String line) {
        String[] p = CsvUtils.split(line);
        long id = Long.parseLong(p[0]);
        long reportId = Long.parseLong(p[1]);
        long collectorId = Long.parseLong(p[2]);
        LocalDateTime sched = p[3].isEmpty() ? null : LocalDateTime.parse(p[3]);
        boolean completed = Boolean.parseBoolean(p[4]);
        return new CollectionTask(id, reportId, collectorId, sched, completed);
    }

    @Override
    public synchronized CollectionTask save(CollectionTask task) {
        List<CollectionTask> all = findAll();
        if (task.getId() == 0L) {
            task.setId(IdGenerator.nextId("task"));
            all.add(task);
        } else {
            boolean updated = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == task.getId()) {
                    all.set(i, task);
                    updated = true;
                    break;
                }
            }
            if (!updated) all.add(task);
        }
        List<String> lines = new ArrayList<>();
        for (CollectionTask t : all) lines.add(serialize(t));
        FileUtils.writeAllLines(path, lines);
        return task;
    }

    @Override
    public synchronized Optional<CollectionTask> findById(long id) {
        return findAll().stream().filter(t -> t.getId() == id).findFirst();
    }

    @Override
    public synchronized List<CollectionTask> findAll() {
        List<String> lines = FileUtils.readAllLines(path);
        List<CollectionTask> list = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            list.add(deserialize(line));
        }
        return list;
    }

    @Override
    public synchronized List<CollectionTask> findByCollectorId(long collectorId) {
        List<CollectionTask> out = new ArrayList<>();
        for (CollectionTask t : findAll()) if (t.getCollectorId() == collectorId) out.add(t);
        return out;
    }
}
