package com.smartwaste.test;

import com.smartwaste.enums.WasteType;
import com.smartwaste.model.PickupRequest;
import com.smartwaste.model.User;
import com.smartwaste.service.ServiceRegistry;
import com.smartwaste.util.DataSeeder;

public class TestRunner {
    public static void main(String[] args) {
        DataSeeder.seedDefaults();
        var auth = ServiceRegistry.getAuthService();
        var collections = ServiceRegistry.getCollectionService();
        var reports = ServiceRegistry.getReportService();

        User alice = auth.login("alice", "alice123").orElseThrow();
        PickupRequest pr = collections.createPickupRequest(alice.getId(), "12 River Rd", WasteType.RECYCLABLE, 2.5);
        System.out.println("Pickup created: " + pr.getId() + " status=" + pr.getStatus() + " scheduled=" + pr.getScheduledAt());

        String daily = reports.generateDailyCollectionReport();
        System.out.println(daily);
        System.out.println(reports.generateSystemStatistics());
        System.out.println("OK");
    }
}
