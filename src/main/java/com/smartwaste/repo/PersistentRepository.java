package com.smartwaste.repo;

import com.smartwaste.model.Identifiable;
import com.smartwaste.storage.FileDatabase;

import java.util.*;

public class PersistentRepository<T extends Identifiable> extends InMemoryRepository<T> {
    private final FileDatabase db;
    private final String name;

    public PersistentRepository(FileDatabase db, String name) {
        this.db = db;
        this.name = name;
        Map<String, T> loaded = db.load(name);
        store.putAll(loaded);
    }

    @Override
    public synchronized T save(T entity) {
        T saved = super.save(entity);
        db.save(name, store);
        return saved;
    }

    @Override
    public synchronized void deleteById(String id) {
        super.deleteById(id);
        db.save(name, store);
    }
}
