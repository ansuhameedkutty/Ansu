package com.smartwaste.model;

import com.smartwaste.enums.Role;

public class Collector extends User {
    public Collector(String id, String name, String address, String username, String passwordHash) {
        super(id, name, address, username, passwordHash, Role.COLLECTOR);
    }
}
