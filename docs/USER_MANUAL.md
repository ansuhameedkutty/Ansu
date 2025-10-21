## User Manual - Smart Waste Management & Recycling Tracker

### Running the App
- Build: `bash scripts/build.sh`
- Run CLI: `bash scripts/run.sh`
- Run demo tests: `bash scripts/test.sh`

A default admin (`admin/admin123`), collector (`collector/collector123`), and citizen (`alice/alice123`) are seeded.

### Registration & Login
- Choose "Register (Citizen)" to create a citizen account.
- Login with your username and password. After login, you will see a role-specific menu.

### Citizen Features
- Report Full Bin: Provide bin location and optional message. Admin is notified.
- Request Special Pickup: Provide address, choose waste type or AUTO and amount. The system auto-assigns a collector and schedules next-day 9AM.
- View Recycling Progress: Shows your total collected kg and percent recycled.
- View Notifications: Shows unread notifications, marks them as read.

### Collector Features
- View Assigned Pickups: See your assigned requests.
- Update Collection Status: Complete a pickup by id and enter collected kg. The citizen is notified.

### Admin Features
- Create Collector: Add new collectors.
- Manage Bins: Create bins with location and area.
- Generate Reports: Daily collection and collector performance reports.
- View System Statistics: Overall stats including users, bins, pickups, and total waste collected.
