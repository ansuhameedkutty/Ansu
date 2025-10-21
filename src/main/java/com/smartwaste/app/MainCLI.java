package com.smartwaste.app;

import com.smartwaste.model.*;
import com.smartwaste.enums.*;
import com.smartwaste.service.*;
import com.smartwaste.util.*;

import java.util.*;

public class MainCLI {
    private final Scanner scanner = new Scanner(System.in);

    private final AuthService authService = ServiceRegistry.getAuthService();
    private final UserService userService = ServiceRegistry.getUserService();
    private final CollectionService collectionService = ServiceRegistry.getCollectionService();
    private final NotificationService notificationService = ServiceRegistry.getNotificationService();
    private final ReportService reportService = ServiceRegistry.getReportService();

    public void start() {
        DataSeeder.seedDefaults();
        while (true) {
            System.out.println("\n=== Smart Waste Management & Recycling Tracker ===");
            System.out.println("1) Register (Citizen)");
            System.out.println("2) Login");
            System.out.println("3) View Recycling Analysis (public)");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": registerCitizen(); break;
                case "2": loginFlow(); break;
                case "3": showPublicAnalysis(); break;
                case "0": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice");
            }
        }
    }

    private void registerCitizen() {
        System.out.println("\n-- Register Citizen --");
        System.out.print("Name: "); String name = scanner.nextLine().trim();
        System.out.print("Address: "); String address = scanner.nextLine().trim();
        System.out.print("Username: "); String username = scanner.nextLine().trim();
        System.out.print("Password: "); String password = scanner.nextLine();
        try {
            User user = authService.registerCitizen(name, address, username, password);
            System.out.println("Registered successfully. Your id: " + user.getId());
        } catch (RuntimeException ex) {
            System.out.println("Registration failed: " + ex.getMessage());
        }
    }

    private void loginFlow() {
        System.out.println("\n-- Login --");
        System.out.print("Username: "); String username = scanner.nextLine().trim();
        System.out.print("Password: "); String password = scanner.nextLine();
        Optional<User> userOpt = authService.login(username, password);
        if (userOpt.isEmpty()) {
            System.out.println("Invalid credentials or inactive user.");
            return;
        }
        User user = userOpt.get();
        switch (user.getRole()) {
            case CITIZEN: citizenMenu((Citizen) user); break;
            case COLLECTOR: collectorMenu((Collector) user); break;
            case ADMIN: adminMenu((Admin) user); break;
        }
    }

    private void showPublicAnalysis() {
        System.out.println("\n-- Recycling Analysis (Public) --");
        var analysis = reportService.getRecyclingAnalysis();
        System.out.printf("Total waste collected: %.2f kg\n", analysis.totalCollectedKg);
        System.out.printf("Recycled: %.2f%%\n", analysis.percentRecycled);
        System.out.println("Top areas for cleanliness: " + String.join(", ", analysis.topCleanAreas));
    }

    private void citizenMenu(Citizen citizen) {
        while (true) {
            System.out.println("\n-- Citizen Menu --");
            System.out.println("1) Report Full Bin");
            System.out.println("2) Request Special Pickup");
            System.out.println("3) View Recycling Progress");
            System.out.println("4) View Notifications");
            System.out.println("0) Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Bin location/address: "); String loc = scanner.nextLine().trim();
                    System.out.print("Message (optional): "); String msg = scanner.nextLine();
                    ServiceRegistry.getWasteService().reportFullBin(citizen.getId(), loc, msg);
                    System.out.println("Reported. Thank you.");
                    break;
                case "2":
                    System.out.print("Pickup address: "); String addr = scanner.nextLine().trim();
                    System.out.print("Waste type (BIODEGRADABLE/NON_BIODEGRADABLE/RECYCLABLE/AUTO): ");
                    String typeStr = scanner.nextLine().trim().toUpperCase();
                    WasteType type;
                    if ("AUTO".equals(typeStr)) {
                        System.out.print("Short description (for classifier): ");
                        String desc = scanner.nextLine();
                        type = com.smartwaste.util.WasteClassifier.classify(desc);
                        System.out.println("Classified as: " + type);
                    } else {
                        type = WasteType.valueOf(typeStr);
                    }
                    System.out.print("Approx amount (kg): "); double kg = Double.parseDouble(scanner.nextLine().trim());
                    var pr = ServiceRegistry.getCollectionService().createPickupRequest(citizen.getId(), addr, type, kg);
                    System.out.println("Request created with id: " + pr.getId() + ", scheduled at: " + pr.getScheduledAt());
                    break;
                case "3":
                    var stats = reportService.getUserRecyclingStats(citizen.getId());
                    System.out.printf("Your collected waste: %.2f kg; recycled: %.2f%%\n", stats.totalCollectedKg, stats.percentRecycled);
                    break;
                case "4":
                    var notes = notificationService.getNotifications(citizen.getId());
                    if (notes.isEmpty()) System.out.println("No notifications.");
                    for (var n : notes) {
                        System.out.printf("[%s] %s - %s\n", n.getTimestamp(), n.getType(), n.getMessage());
                    }
                    notificationService.markAllRead(citizen.getId());
                    break;
                case "0": return;
                default: System.out.println("Invalid choice");
            }
        }
    }

    private void collectorMenu(Collector collector) {
        while (true) {
            System.out.println("\n-- Collector Menu --");
            System.out.println("1) View Assigned Pickups");
            System.out.println("2) Update Collection Status (complete)");
            System.out.println("0) Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    var assigned = collectionService.getAssignedPickups(collector.getId());
                    if (assigned.isEmpty()) System.out.println("No assigned pickups.");
                    for (var p : assigned) {
                        System.out.printf("%s | %s | %s | %.2f kg | %s\n", p.getId(), p.getAddress(), p.getWasteType(), p.getAmountKg(), p.getStatus());
                    }
                    break;
                case "2":
                    System.out.print("Pickup ID to complete: "); String pid = scanner.nextLine().trim();
                    System.out.print("Collected amount (kg): "); double kg = Double.parseDouble(scanner.nextLine().trim());
                    try {
                        collectionService.completePickup(collector.getId(), pid, kg);
                        System.out.println("Marked completed.");
                    } catch (RuntimeException ex) {
                        System.out.println("Failed: " + ex.getMessage());
                    }
                    break;
                case "0": return;
                default: System.out.println("Invalid choice");
            }
        }
    }

    private void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n-- Admin Menu --");
            System.out.println("1) Create Collector");
            System.out.println("2) Manage Bins (create)");
            System.out.println("3) Generate Reports");
            System.out.println("4) View System Statistics");
            System.out.println("0) Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Name: "); String name = scanner.nextLine().trim();
                    System.out.print("Address: "); String address = scanner.nextLine().trim();
                    System.out.print("Username: "); String username = scanner.nextLine().trim();
                    System.out.print("Password: "); String password = scanner.nextLine();
                    userService.createCollector(name, address, username, password);
                    System.out.println("Collector created.");
                    break;
                case "2":
                    System.out.print("Bin location/address: "); String loc = scanner.nextLine().trim();
                    System.out.print("Area (e.g., Ward-1): "); String area = scanner.nextLine().trim();
                    ServiceRegistry.getBinService().createBin(loc, area);
                    System.out.println("Bin created.");
                    break;
                case "3":
                    var daily = reportService.generateDailyCollectionReport();
                    System.out.println(daily);
                    var perf = reportService.generateCollectorPerformanceReport();
                    System.out.println(perf);
                    break;
                case "4":
                    var sys = reportService.generateSystemStatistics();
                    System.out.println(sys);
                    break;
                case "0": return;
                default: System.out.println("Invalid choice");
            }
        }
    }
}
