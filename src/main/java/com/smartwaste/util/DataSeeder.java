package com.smartwaste.util;

import com.smartwaste.enums.WasteType;
import com.smartwaste.model.*;
import com.smartwaste.service.ServiceRegistry;

public final class DataSeeder {
    private static boolean seeded = false;

    private DataSeeder() {}

    public static synchronized void seedDefaults() {
        if (seeded) return;
        var auth = ServiceRegistry.getAuthService();
        var users = ServiceRegistry.getUserService();
        var bins = ServiceRegistry.getBinService();
        var collections = ServiceRegistry.getCollectionService();
        var waste = ServiceRegistry.getWasteService();

        // Create default admin and one collector
        auth.registerAdmin("Admin", "HQ", "admin", "admin123");
        users.createCollector("John Collector", "Depot A", "collector", "collector123");

        // Create some bins
        bins.createBin("Main Street 1", "Ward-1");
        bins.createBin("Central Park", "Ward-2");
        bins.createBin("Market Square", "Ward-1");

        // Create a demo citizen
        auth.registerCitizen("Alice", "12 River Rd", "alice", "alice123");

        // Create a sample pickup request for demo citizen
        var citizen = ServiceRegistry.getAuthService().login("alice", "alice123").orElse(null);
        if (citizen != null) {
            var pr = collections.createPickupRequest(citizen.getId(), citizen.getAddress(), WasteType.RECYCLABLE, 3.0);
        }

        seeded = true;
    }
}
