package com.smartwaste.model;

import com.smartwaste.enums.Role;

public class Admin extends User {
    public Admin(String id, String name, String address, String username, String passwordHash) {
        super(id, name, address, username, passwordHash, Role.ADMIN);
    }
}
