# Sinch SMS Router Service

A Spring Boot-based SMS routing microservice designed as a take-home technical assignment for Sinch. The system supports message routing based on country code, opt-out management, and simulated message status transitions with in-memory storage.

## Features

- Route SMS to different carriers based on phone number prefixes (+61, +64, etc.)
- In-memory message tracking and status transitions (PENDING → SENT → DELIVERED/BLOCKED)
- Opt-out endpoint to block further messages to specific numbers
- Basic validation and format checking
- Modular architecture: Controller → Service → Repository
- JUnit tests (unit and controller integration)

---

## Setup Instructions

### Requirements

- Java 17 (tested with 17.0.14)
- Maven 3+
- Spring Boot 3.3.12

### Running the App

```bash
# 1. Clone the repository
git clone https://github.com/chenglinj/sms-router.git
cd sms-router

# 2. Build
mvn clean install

# 3. Run
mvn spring-boot:run
```

App starts on [http://localhost:8080](http://localhost:8080)

---

## API Endpoints

### 1. Send SMS

```http
POST /messages
```

**Request Body**:
```json
{
  "destinationNumber": "+61400000000",
  "content": "Hello from Sinch!",
  "format": "SMS"
}
```

**Response**:
```json
{
  "id": "c60d7fbe-xxxx-xxxx-xxxx-60d7fbe4fd7d",
  "carrier": "Telstra",
  "status": "PENDING"
}
```

---

### 2. Get Message Status

```http
GET /messages/{id}
```

Get the message status for the specific message ID.

**Example**:
```bash
GET /messages/c60d7fbe-xxxx-xxxx-xxxx-60d7fbe4fd7d
```

**Response**:
```json
{
  "id": "c60d7fbe-xxxx-xxxx-xxxx-60d7fbe4fd7d",
  "carrier": "Telstra",
  "status": "PENDING"
}
```

Returns current message delivery status:
- `PENDING`
- `SENT`
- `DELIVERED`
- `BLOCKED` (if opted-out)

---

### 3. Opt-out Number

```http
POST /optout/{phoneNumber}
```

Blocks SMS messages to the specific number.

**Example**:
```bash
POST /optout/+61400000000
```

**Response**:
```
Phone number +61400000000 has been opted out.
```

---

### 4. Cancel Opt-out

```http
DELETE /optout/{phoneNumber}
```

Removes the number from the opt-out list, allowing messages to be sent again.

**Example**:
```bash
DELETE /optout/+61400000000
```

**Response**:
```
Phone number +61400000000 has been removed from opt-out list.
```

---

## Simulated Routing & Status Logic

### Carrier Routing

- `+61`: Australian → Alternates between **Telstra** / **Optus**
- `+64`: New Zealand → **Spark**
- All others → **Global**

### Message Lifecycle

- `PENDING → SENT`: 10 seconds after creation
- `SENT → DELIVERED`: 10 seconds after previous update
- If the number is opted-out: status becomes `BLOCKED` instead of `SENT`

Handled via a scheduled task running every 5 seconds.

---

## Project Structure

```
com.sinch.sms
├── controller        # REST APIs
├── service           # Business logic & routing
├── repository        # In-memory data stores
└── model             # DTOs, VOs, constatns, enums, entity
```

---

## Testing

```bash
# Run all tests
mvn test
```

Includes:
- Unit tests for routing & validation logic
- Controller layer integration tests with MockMvc

---

## Assumptions

- Real SMS sending is **simulated** for this demo
- In-memory data structures are used (no persistence)
- Only SMS format is supported, others will be rejected
- No authentication or rate limiting is implemented
- Designed to demonstrate clean architecture and modularity

---

## Usage of AI

- Generation of regular expressions related to phone number validation
- Generation of the outline of unit tests
- Generation of the outline of this README.md file