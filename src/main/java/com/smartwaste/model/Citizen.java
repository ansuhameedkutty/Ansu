package com.smartwaste.model;

import com.smartwaste.enums.Role;

public class Citizen extends User {
    public Citizen(String id, String name, String address, String username, String passwordHash) {
        super(id, name, address, username, passwordHash, Role.CITIZEN);
    }
}
