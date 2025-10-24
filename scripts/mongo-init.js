// MongoDB initialization script for Nadi database
// Creates indexes for efficient queries

db = db.getSiblingDB('nadi');

// Create geospatial index on venues collection
db.venues.createIndex({ "location": "2dsphere" });

// Create unique indexes on users collection
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "phone": 1 }, { unique: true });

// Create compound index on reservations for overlap checking
db.reservations.createIndex({ "courtId": 1, "startTime": 1, "endTime": 1 });

// Create index on hold expiration for cleanup
db.reservations.createIndex({ "holdExpiresAt": 1 });

// Create index on reservations by user
db.reservations.createIndex({ "userId": 1 });

// Create index on reservations by court
db.reservations.createIndex({ "courtId": 1 });

// Create index on reservations by status
db.reservations.createIndex({ "status": 1 });

// Create index on courts by venue
db.courts.createIndex({ "venueId": 1 });

// Create index on courts by sport
db.courts.createIndex({ "sportId": 1 });

// Create index on price rules by court
db.court_price_rules.createIndex({ "courtId": 1 });

// Create index on API keys by user
db.api_keys.createIndex({ "userId": 1 });

// Create unique index on API key
db.api_keys.createIndex({ "key": 1 }, { unique: true });

print("MongoDB indexes created successfully for Nadi database");
