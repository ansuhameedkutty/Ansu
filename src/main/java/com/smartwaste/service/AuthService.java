package com.smartwaste.service;

import com.smartwaste.enums.Role;
import com.smartwaste.model.*;
import com.smartwaste.repo.Repository;
import com.smartwaste.util.IdGenerator;
import com.smartwaste.util.PasswordHasher;

import java.util.List;
import java.util.Optional;

public class AuthService {
    private final Repository<User> userRepo;

    public AuthService(Repository<User> userRepo) {
        this.userRepo = userRepo;
    }

    public User registerCitizen(String name, String address, String username, String password) {
        ensureUsernameUnique(username);
        String id = IdGenerator.newId();
        Citizen citizen = new Citizen(id, name, address, username, PasswordHasher.hash(password));
        return userRepo.save(citizen);
    }

    public User registerAdmin(String name, String address, String username, String password) {
        ensureUsernameUnique(username);
        String id = IdGenerator.newId();
        Admin admin = new Admin(id, name, address, username, PasswordHasher.hash(password));
        return userRepo.save(admin);
    }

    public Optional<User> login(String username, String password) {
        String hash = PasswordHasher.hash(password);
        List<User> all = userRepo.findAll();
        return all.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPasswordHash().equals(hash) && u.isActive())
                .findFirst();
    }

    private void ensureUsernameUnique(String username) {
        boolean exists = userRepo.findAll().stream().anyMatch(u -> u.getUsername().equals(username));
        if (exists) throw new RuntimeException("Username already exists");
    }
}
