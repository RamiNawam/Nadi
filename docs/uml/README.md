# UML Documentation

This directory contains PlantUML diagrams for the Nadi sports venue reservation platform.

## Diagrams

- **use-case.puml** - Use case diagram showing actors and their interactions
- **class-diagram.puml** - Entity relationship diagram with MongoDB ObjectIds
- **component-diagram.puml** - System architecture and component relationships
- **sequence-booking.puml** - Booking flow sequence diagram (hold → confirm → expire)
- **deployment-diagram.puml** - Deployment architecture and infrastructure

## How to View

### Online (Recommended)
1. Copy the content of any `.puml` file
2. Go to [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
3. Paste the content and view the rendered diagram

### VS Code Extension
1. Install "PlantUML" extension
2. Open any `.puml` file
3. Use `Ctrl+Shift+P` → "PlantUML: Preview Current Diagram"

### Command Line
```bash
# Install PlantUML
npm install -g node-plantuml

# Generate PNG
puml generate docs/uml/use-case.puml

# Generate SVG
puml generate docs/uml/use-case.puml -tsvg
```

## Key Design Notes

- **Geospatial Search**: Uses MongoDB 2dsphere index on Venue.location
- **Race Safety**: Booking flow uses MongoDB transactions for overlap checking
- **Hold Expiration**: Scheduled job expires holds every minute (not TTL deletion)
- **No Payments**: V1 focuses on hold-confirm-cancel flow without payment processing
- **Role-Based Access**: Different endpoints for Users, Venue Owners, Developers, Super Admins
