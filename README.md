# Smart Campus API

## Overview
A JAX-RS RESTful API built with Jersey and Grizzly for managing 
university rooms and sensors.

Base URL: http://localhost:8080/api/v1

## How to Build and Run
1. Clone the repository
   git clone https://github.com/YOURUSERNAME/SmartCampusAPI.git
2. Open in NetBeans
3. Right-click pom.xml → Download Dependencies
4. Right-click Main.java → Run File
5. API is live at http://localhost:8080/api/v1

## Sample curl Commands

### 1. Get all rooms
curl -X GET http://localhost:8080/api/v1/rooms

### 2. Create a room
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"HALL-001","name":"Main Hall","capacity":200}'

### 3. Get sensors filtered by type
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"

### 4. Add a sensor reading
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":24.5}'

### 5. Delete a room with sensors (409 error)
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301

---

## Question Answers

### Part 1 - JAX-RS Lifecycle
By default, JAX-RS creates a new instance of a resource class for every incoming HTTP request (per-request lifecycle). This means instance variables are not shared between requests.
To safely share in-memory data structures like HashMaps across requests, a singleton class (DataStore) must be used instead of storing data inside the resource class itself. Without this, data would be lost after every request.

### Part 1 - HATEOAS
Hypermedia (HATEOAS – Hypermedia as the Engine of Application State) is a key principle of RESTful APIs where responses include links to related resources.

This approach allows clients to dynamically navigate the API without relying solely on external documentation. For example, a discovery endpoint can provide links to /rooms and /sensors.

The main benefit is improved flexibility and decoupling. Clients do not need to hardcode endpoints, making the API more maintainable and scalable over time.

### Part 2 - IDs vs Full Objects
Returning only IDs is more bandwidth-efficient but forces the client to make additional requests to fetch details, increasing latency. Returning full objects increases payload size but reduces round trips. For small collections, full objects are preferred. For large collections, returning IDs or a summary is better to avoid overloading the network.

### Part 2 - DELETE Idempotency
Yes, DELETE is idempotent. The first call removes the room and returns 204. Every subsequent call for the same room ID returns 404 since the room no longer exists. The server state does not change after the first deletion, satisfying the idempotency requirement — multiple identical requests produce the same result.

### Part 3 - @Consumes Mismatch
If a client sends data as text/plain or application/xml when the endpoint declares @Consumes(MediaType.APPLICATION_JSON), JAX-RS automatically returns a 415 Unsupported Media Type error without even invoking the resource method. The framework handles the rejection before your code runs.

### Part 3 - QueryParam vs Path Param
Using @QueryParam (e.g., /sensors?type=CO2) is more flexible for filtering because:

It allows optional filtering
It supports combining multiple filters
It keeps the resource path clean and consistent

Using path parameters (e.g., /sensors/type/CO2) is less flexible and harder to extend for additional filters.

Therefore, query parameters are the preferred approach for search and filtering operations.

### Part 4 - Sub-Resource Locator Pattern
The sub-resource locator pattern delegates handling of nested paths to separate dedicated classes. This keeps each class focused on one responsibility, making the code easier to maintain and test. In large APIs with many nested resources, putting all logic in one class creates an unmanageable controller. Separate classes also allow independent versioning and reuse.

### Part 5 - 422 vs 404
404 means the requested URL/resource was not found. 422 means the request was understood and the URL was valid, but the data inside the request body was semantically incorrect. When a sensor references a non-existent roomId, the /sensors endpoint itself exists (not a 404), but the payload contains an invalid reference — making 422 more accurate.

### Part 5 - Stack Trace Security Risk
Exposing stack traces reveals internal class names, package structure, library versions, and file paths. An attacker can use this to identify known vulnerabilities in specific library versions, understand the application architecture, and craft targeted exploits. The global exception mapper prevents this by returning only a generic error message to the client while logging the real error server-side.

### Part 5 - Filters vs Manual Logging
Filters implement cross-cutting concerns in one place. If logging were added manually to every resource method, any change to the log format would require editing every method. Filters are also guaranteed to run for every request and response regardless of which endpoint is called, ensuring no requests are missed. This follows the DRY (Don't Repeat Yourself) principle.
