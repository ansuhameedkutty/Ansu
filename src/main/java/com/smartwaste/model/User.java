package com.smartwaste.model;

import java.util.Objects;

public abstract class User {
    protected long id;
    protected String name;
    protected Address address;
    protected String username;
    protected String passwordHash;
    protected Role role;

    protected User() {}

    protected User(long id, String name, Address address, String username, String passwordHash, Role role) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
