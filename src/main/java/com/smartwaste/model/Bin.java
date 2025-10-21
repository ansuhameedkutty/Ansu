package com.smartwaste.model;

import java.io.Serializable;

public class Bin implements Identifiable, Serializable {
    private final String id;
    private final String location;
    private final String area;
    private boolean overflowReported;

    public Bin(String id, String location, String area) {
        this.id = id;
        this.location = location;
        this.area = area;
        this.overflowReported = false;
    }

    @Override
    public String getId() { return id; }
    public String getLocation() { return location; }
    public String getArea() { return area; }
    public boolean isOverflowReported() { return overflowReported; }
    public void setOverflowReported(boolean overflowReported) { this.overflowReported = overflowReported; }
}
