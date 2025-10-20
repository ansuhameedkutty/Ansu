package com.smartwaste.model;

public class Collector extends User {
    private String routeArea;

    public Collector() {
        this.role = Role.COLLECTOR;
    }

    public Collector(long id, String name, Address address, String username, String passwordHash, String routeArea) {
        super(id, name, address, username, passwordHash, Role.COLLECTOR);
        this.routeArea = routeArea;
    }

    public String getRouteArea() { return routeArea; }
    public void setRouteArea(String routeArea) { this.routeArea = routeArea; }
}
