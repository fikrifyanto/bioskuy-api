# Bioskuy API

## API Response Format

All API responses in this application follow a standardized format with a `message` field and a `data` field. This ensures consistency across all endpoints and makes it easier for clients to handle responses.

### Response Format

```json
{
  "message": "Success message or error description",
  "data": {
    "property1": "value1",
    "property2": "value2"
  }
}
```

### Implementation Details

1. **ApiResponse Class**: A generic wrapper class for all API responses.
   - Location: `src/main/java/com/bioskuy/api/common/ApiResponse.java`
   - Usage: Wrap all response data in this class to ensure a consistent format.

2. **ResponseUtil Class**: Utility methods for creating standardized API responses.
   - Location: `src/main/java/com/bioskuy/api/common/ResponseUtil.java`
   - Usage: Use the static methods to create success or error responses.

### Example Usage in Controllers

```java
@GetMapping
public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(ResponseUtil.success("Users retrieved successfully", users));
}

@PostMapping
public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
    User createdUser = userService.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseUtil.success("User created successfully", createdUser));
}

@GetMapping("/{id}")
public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
    try {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ResponseUtil.success("User retrieved successfully", user));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseUtil.error("User not found"));
    }
}
```

### OpenAPI Specification

The OpenAPI specification has been updated to reflect this standardized response format. All response schemas now include a `message` field and wrap the actual response data under a `data` field.

## Getting Started

1. Clone the repository
2. Build the project: `./mvnw clean install`
3. Run the application: `./mvnw spring-boot:run`
