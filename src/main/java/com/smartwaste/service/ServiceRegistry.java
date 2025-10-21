package com.smartwaste.service;

import com.smartwaste.repo.PersistentRepository;
import com.smartwaste.storage.FileDatabase;
import com.smartwaste.model.*;

import java.nio.file.Path;

public final class ServiceRegistry {
    private static final FileDatabase DB = new FileDatabase(Path.of("data"));

    private static final PersistentRepository<User> USER_REPO = new PersistentRepository<>(DB, "users");
    private static final PersistentRepository<Bin> BIN_REPO = new PersistentRepository<>(DB, "bins");
    private static final PersistentRepository<PickupRequest> PICKUP_REPO = new PersistentRepository<>(DB, "pickups");
    private static final PersistentRepository<WasteRecord> WASTE_REPO = new PersistentRepository<>(DB, "waste");
    private static final PersistentRepository<Notification> NOTIF_REPO = new PersistentRepository<>(DB, "notifications");

    private static final AuthService AUTH_SERVICE = new AuthService(USER_REPO);
    private static final UserService USER_SERVICE = new UserService(USER_REPO);
    private static final BinService BIN_SERVICE = new BinService(BIN_REPO);
    private static final WasteService WASTE_SERVICE = new WasteService(WASTE_REPO, NOTIF_REPO, USER_REPO);
    private static final CollectionService COLLECTION_SERVICE = new CollectionService(PICKUP_REPO, USER_REPO, WASTE_REPO, NOTIF_REPO, BIN_REPO);
    private static final NotificationService NOTIFICATION_SERVICE = new NotificationService(NOTIF_REPO);
    private static final ReportService REPORT_SERVICE = new ReportService(WASTE_REPO, PICKUP_REPO, BIN_REPO, USER_REPO);

    private ServiceRegistry() {}

    public static AuthService getAuthService() { return AUTH_SERVICE; }
    public static UserService getUserService() { return USER_SERVICE; }
    public static BinService getBinService() { return BIN_SERVICE; }
    public static WasteService getWasteService() { return WASTE_SERVICE; }
    public static CollectionService getCollectionService() { return COLLECTION_SERVICE; }
    public static NotificationService getNotificationService() { return NOTIFICATION_SERVICE; }
    public static ReportService getReportService() { return REPORT_SERVICE; }
}
