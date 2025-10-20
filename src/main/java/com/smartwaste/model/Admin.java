package com.smartwaste.model;

public class Admin extends User {
    public Admin() {
        this.role = Role.ADMIN;
    }

    public Admin(long id, String name, Address address, String username, String passwordHash) {
        super(id, name, address, username, passwordHash, Role.ADMIN);
    }
}
