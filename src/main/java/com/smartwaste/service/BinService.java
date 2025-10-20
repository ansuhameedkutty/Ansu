package com.smartwaste.service;

import com.smartwaste.model.Bin;
import com.smartwaste.repo.Repository;
import com.smartwaste.util.IdGenerator;

import java.util.List;

public class BinService {
    private final Repository<Bin> binRepo;

    public BinService(Repository<Bin> binRepo) {
        this.binRepo = binRepo;
    }

    public Bin createBin(String location, String area) {
        Bin bin = new Bin(IdGenerator.newId(), location, area);
        binRepo.save(bin);
        return bin;
    }

    public List<Bin> getAll() { return binRepo.findAll(); }
}
