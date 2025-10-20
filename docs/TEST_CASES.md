## Test Cases

### 1) Waste Reporting (Full Bin)
- Step: Citizen logs in -> Report Full Bin at "Central Park".
- Expected: Admin receives INFO notification about bin overflow.

### 2) Pickup Assignment
- Step: Citizen requests special pickup (AUTO classify with description "plastic bottles").
- Expected: PickupRequest created, status=ASSIGNED (collector auto-assigned), scheduledAt set to next day 09:00.

### 3) Report Generation
- Step: Run `TestRunner`.
- Expected: Shows daily collection report, system statistics, and ends with "OK".

### 4) Collector Completion Flow
- Step: Collector lists assigned pickups, completes one with 2.0 kg recorded.
- Expected: Pickup status becomes COMPLETED, WasteRecord saved, citizen receives COLLECTION_CONFIRMED notification.
