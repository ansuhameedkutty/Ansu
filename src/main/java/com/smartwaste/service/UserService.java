package com.smartwaste.service;

import com.smartwaste.model.Collector;
import com.smartwaste.model.User;
import com.smartwaste.repo.Repository;
import com.smartwaste.util.IdGenerator;
import com.smartwaste.util.PasswordHasher;

public class UserService {
    private final Repository<User> userRepo;

    public UserService(Repository<User> userRepo) {
        this.userRepo = userRepo;
    }

    public Collector createCollector(String name, String address, String username, String password) {
        String id = IdGenerator.newId();
        Collector c = new Collector(id, name, address, username, PasswordHasher.hash(password));
        userRepo.save(c);
        return c;
    }
}
