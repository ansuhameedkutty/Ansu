package com.smartwaste.repo;

import com.smartwaste.model.Identifiable;

import java.util.*;

public interface Repository<T extends Identifiable> {
    T save(T entity);
    Optional<T> findById(String id);
    List<T> findAll();
    void deleteById(String id);
}
