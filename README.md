# OfficeSpace

**⚠️ Note:** *Delete the tests folder (optional but recommended):
Before installing the dependencies and building the project, it is recommended to delete the tests folder because the current tests are failing and would cause the process to break.*

**Note:** *The current version of the Office Space Management System is being developed on the* **development branch**. *For the most up-to-date changes and features, please ensure that you are working with this branch instead of main.*

## Overview
The Office Space Management System is a comprehensive backend solution for managing office spaces, room reservations, and resources. Built with Spring Boot, this system enables companies to efficiently manage their office spaces, track resources, and handle room reservations.

## Features

### Office Room Management
- Create, update, and delete office rooms
- Search rooms by multiple criteria:
    - Company name
    - Room status (Available, Occupied, Under Maintenance)
    - Room type
    - Capacity
    - Floor
- Filter rooms by price range and other attributes
- Check room availability for specific time slots

### Resource Management
- Add and remove resources from rooms
- Track resource status (Available, In Use, Under Maintenance)
- Manage multiple resources per room
- Validate resource availability and conditions

### Reservation System
- Book rooms for specific time slots
- Check for booking conflicts
- Track reservation status
- Manage multiple reservations per room

## Technical Stack

## Authentication & Security
- JSON Web Token (JWT) based authentication
- Role-based access control (ADMIN, USER)
- Secure password hashing
- Protected endpoints with Spring Security

## External Integrations
### Google Calendar
- Automatic calendar event creation for room reservations
- Real-time synchronization with Google Calendar
- Meeting invitations sent to participants

### Email Service
- Gmail SMTP server integration
- Automated email notifications for:
    - Room reservations
    - Reservation confirmations
    - Reservation updates/cancellations
    - Resource status changes
    - System notifications

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- ModelMapper for DTO mapping
- Jakarta Persistence
- UUID for unique identifiers
- Spring Security with JWT Authentication
- Google Calendar API Integration
- Gmail SMTP Server Integration
- PostgreSQL Database

### Database
- JPA/Hibernate for ORM
- Transaction management support

### Architecture
- RESTful API design
- DTO pattern for data transfer
- Custom exception handling
- Service-oriented architecture

## Project Structure

```
javawizzards.officespace/
├── Auth/
│   └── GoogleCalendarAuth
├── configuration/                 # Configuration classes
├── controller/                    # REST API controllers
├── DatabaseInitializers/         
│   └── DatabaseInitializer.java  # Database seeding
├── dto/                          # Data Transfer Objects
├── entity/                       # JPA entities
├── enumerations/                 # Enum classes
├── exception/                    # Custom exceptions
│   ├── Company/
│   ├── Department/
│   ├── GoogleCendar/
│   ├── Notification/
│   ├── OfficeRoom/
│   ├── Payment/
│   ├── Reservation/
│   ├── Resource/
│   ├── Ticket/
│   └── User/
├── logs/                        # Application logs
├── repository/                  # JPA repositories
└── service/                     # Business logic
    ├── Company/
    ├── Department/
    ├── Email/
    │   └── EmailService.java
    ├── GoogleCalendar/ 
    ├── JwtService/
    ├── Notification/
    ├── OfficeRoom/
    ├── RequestAndResponse/
    ├── Reservation/
    ├── Resource/
    ├── Role/
    └── Stripe/
```

## API Documentation

### Authentication Controller
```http
POST   /auth/register           # Register new user
POST   /auth/login             # User login
POST   /auth/refresh-token     # Refresh JWT token
POST   /auth/google-register   # Register with Google
POST   /auth/google-login      # Login with Google
PUT    /auth/change-password   # Change user password
GET    /auth/message           # Test auth endpoint
GET    /auth/get-current-user  # Get current user info
```

### Reservation Controller
```http
POST   /reservations/create                    # Create new reservation
PUT    /reservations/update/{id}              # Update reservation
GET    /reservations/{id}                     # Get reservation by ID
GET    /reservations/user/{userId}            # Get user's reservations
GET    /reservations/office-room/{officeRoomId} # Get room's reservations
GET    /reservations/get-statuses             # Get all reservation statuses
DELETE /reservations/delete/{id}              # Delete reservation
```

### Office Room Controller
```http
POST   /office-rooms/create                    # Create new office room
POST   /office-rooms/{officeRoomId}/resources  # Add resources to room
POST   /office-rooms/{officeRoomId}/resources/{resourceId} # Add single resource
PUT    /office-rooms/update/{id}               # Update office room
GET    /office-rooms                           # Get all office rooms
GET    /office-rooms/{id}                      # Get room by ID
GET    /office-rooms/get-types                 # Get room types
GET    /office-rooms/get-statuses              # Get room statuses
GET    /office-rooms/filter                    # Filter rooms by criteria
GET    /office-rooms/availability              # Check room availability
DELETE /office-rooms/delete/{id}               # Delete office room
DELETE /office-rooms/{officeRoomId}/resources/{resourceId} # Remove resource
```

### Admin Controller
```http
PUT    /admin/update-user/{id}        # Update user
PUT    /admin/update-google-user      # Update Google user
PUT    /admin/update-company/{id}     # Update company
GET    /admin/get-users              # Get all users
GET    /admin/get-reservations       # Get all reservations
GET    /admin/get-company/{id}       # Get company by ID
GET    /admin/get-companies          # Get all companies
DELETE /admin/delete-user/{id}       # Delete user
DELETE /admin/delete-company/{id}    # Delete company
```

### Ticket Controller
```http
POST   /tickets/create                      # Create support ticket
PATCH  /tickets/update-status/{id}/status   # Update ticket status
GET    /tickets/all/{userId}                # Get user's tickets
DELETE /tickets/delete/{id}                 # Delete ticket
```

### Payment Controller
```http
POST   /payment/create-session          # Create payment session
POST   /payment/confirm                 # Confirm payment
POST   /payment/refund/{chargeId}       # Process refund
POST   /payment/resend-receipt/{chargeId} # Resend receipt
GET    /payment/{id}                    # Get payment by ID
GET    /payment/email/{email}           # Get payments by email
```

### Notification Controller
```http
POST   /notification/send-email                    # Send notification email
PATCH  /notification/read/{notificationId}         # Mark notification as read
GET    /notification/notifications/{userId}        # Get user's notifications
```

### User Controller
```http
GET    /users/get-data/{id}     # Get user data
GET    /users/email/{email}     # Get user by email
```

## Error Handling
The system implements a comprehensive exception handling mechanism with custom exceptions:

- `OfficeRoomNotFoundException`
- `OfficeRoomCreationFailedException`
- `InvalidOfficeRoomDataException`
- `ResourceCustomException`
- And more specific exceptions for various scenarios

Each exception includes:
- Specific error message
- HTTP status code
- Error details when applicable

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Your preferred IDE (IntelliJ IDEA recommended)
- PostgreSQL/MySQL (based on your configuration)

### Installation

1. Clone the repository
```bash
git clone https://github.com/your-username/office-space-management.git
```

2. Navigate to project directory
```bash
cd office-space-management
```

3. Install dependencies
```bash
mvn install
```
If you encounter any issues with dependencies, try the following commands to resolve them:
- Clean the project:
```bash
mvn clean
```
- Purge local repository dependencies:
```bash
mvn dependency:purge-local-repository
```
- Resolve dependencies:
```bash
mvn dependency:resolve
```
- Reinstall dependencies:
```bash
mvn install
```
- Reload Maven projects

4. Configure database connection in `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/officespace
spring.datasource.username=your_username
spring.datasource.password=your_password
```

5. Google Calendar API Configuration
- Set up your Google Calendar API by configuring the following properties in the application.properties file:
```properties
google.calendar.application-name=${GOOGLE_CALENDAR_APPLICATION_NAME}
google.calendar.tokens-directory-path=${GOOGLE_CALENDAR_TOKENS_DIRECTORY_PATH}
google.calendar.credentials-directory-path=${GOOGLE_CALENDAR_CREDENTIALS_DIRECTORY_PATH}
```

4. Run the application
```bash
mvn spring-boot:run
```

## Usage Examples

### Creating an Office Room
```java
POST /api/office-rooms
{
    "officeRoomName": "Meeting Room A",
    "address": "123 Business Street",
    "building": "Tower 1",
    "floor": "15",
    "type": "MEETING_ROOM",
    "capacity": 10,
    "pricePerHour": 50.00,
    "companyId": "uuid-here"
}
```
