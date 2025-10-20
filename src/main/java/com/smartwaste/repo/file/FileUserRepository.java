package com.smartwaste.repo.file;

import com.smartwaste.model.*;
import com.smartwaste.repo.UserRepository;
import com.smartwaste.util.CsvUtils;
import com.smartwaste.util.FileUtils;
import com.smartwaste.util.IdGenerator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserRepository implements UserRepository {
    private final Path path = FileUtils.path("data", "users.csv");

    private String serialize(User u) {
        Address a = u.getAddress();
        String addr = CsvUtils.join(
                a == null ? "" : a.getLine1(),
                a == null ? "" : a.getLine2(),
                a == null ? "" : a.getCity(),
                a == null ? "" : a.getState(),
                a == null ? "" : a.getPostalCode()
        );
        return CsvUtils.join(
                Long.toString(u.getId()),
                u.getName(),
                addr,
                u.getUsername(),
                u.getPasswordHash(),
                u.getRole().name(),
                (u instanceof Collector) ? ((Collector) u).getRouteArea() : ""
        );
    }

    private User deserialize(String line) {
        String[] parts = CsvUtils.split(line);
        long id = Long.parseLong(parts[0]);
        String name = parts[1];
        String[] addr = CsvUtils.split(parts[2]);
        Address address = new Address(
                addr.length > 0 ? addr[0] : "",
                addr.length > 1 ? addr[1] : "",
                addr.length > 2 ? addr[2] : "",
                addr.length > 3 ? addr[3] : "",
                addr.length > 4 ? addr[4] : ""
        );
        String username = parts[3];
        String passwordHash = parts[4];
        Role role = Role.valueOf(parts[5]);
        String routeArea = parts.length > 6 ? parts[6] : "";
        return switch (role) {
            case CITIZEN -> new Citizen(id, name, address, username, passwordHash);
            case COLLECTOR -> new Collector(id, name, address, username, passwordHash, routeArea);
            case ADMIN -> new Admin(id, name, address, username, passwordHash);
        };
    }

    @Override
    public synchronized User save(User user) {
        List<User> all = findAll();
        if (user.getId() == 0L) {
            user.setId(IdGenerator.nextId("user"));
            all.add(user);
        } else {
            boolean updated = false;
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == user.getId()) {
                    all.set(i, user);
                    updated = true;
                    break;
                }
            }
            if (!updated) all.add(user);
        }
        List<String> lines = new ArrayList<>();
        for (User u : all) lines.add(serialize(u));
        FileUtils.writeAllLines(path, lines);
        return user;
    }

    @Override
    public synchronized Optional<User> findById(long id) {
        return findAll().stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public synchronized Optional<User> findByUsername(String username) {
        return findAll().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    @Override
    public synchronized List<User> findAll() {
        List<String> lines = FileUtils.readAllLines(path);
        List<User> list = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            list.add(deserialize(line));
        }
        return list;
    }
}
