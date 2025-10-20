package com.smartwaste.model;

import com.smartwaste.enums.Role;

import java.io.Serializable;

public abstract class User implements Identifiable, Serializable {
    private final String id;
    private final String name;
    private final String address;
    private final String username;
    private final String passwordHash;
    private final Role role;
    private boolean active;

    protected User(String id, String name, String address, String username, String passwordHash, Role role) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = true;
    }

    @Override
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
