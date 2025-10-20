package com.smartwaste.model;

public class Citizen extends User {
    public Citizen() {
        this.role = Role.CITIZEN;
    }

    public Citizen(long id, String name, Address address, String username, String passwordHash) {
        super(id, name, address, username, passwordHash, Role.CITIZEN);
    }
}
