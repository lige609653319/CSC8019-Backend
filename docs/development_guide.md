# CSC8019 Backend Development Guide

This guide provides a comprehensive overview of the standards and best practices for developing in the CSC8019-backend project.

## 1. API Standards

### Unified Response Format (`Result<T>`)
All Controller methods MUST return the `Result<T>` wrapper to ensure a consistent response structure across the entire API.

- **Structure**:
    - `code`: Status code (e.g., 200 for success, 500 for error, 401 for unauthorized).
    - `message`: Descriptive message.
    - `data`: The actual payload (generic type `T`).

- **Example Usage**:
```java
@GetMapping("/hello")
public Result<String> hello() {
    return Result.success("Success Data");
}
```

## 2. Global Exception Handling

The project uses a global exception handler (`GlobalExceptionHandler`) to intercept and format errors.

- **`CustomException`**: Use this for business logic errors. It takes a `ResultCode` or a specific message.
- **Auto-handling**: Spring Security exceptions (401, 403) and general system exceptions (500) are automatically caught and returned in the `Result` format.

## 3. JPA Usage Guide

We follow a layered architectural pattern for data access.

### 1. Entity Module
Define your data models in the `entity` package using JPA annotations.
```java
@Entity
@Data
public class MyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
```

### 2. Repository Module
Extend `JpaRepository` for standard CRUD operations. Place these in the `repository` package.
```java
@Repository
public interface MyRepository extends JpaRepository<MyEntity, Long> {
}
```

### 3. Service Module
Always use an interface and an implementation class (`impl` package).
- **Interface**: Define the business contract.
- **Impl**: Implement the logic and inject the Repository.

## 4. Security Annotations

The project uses custom annotations for Role-Based Access Control (RBAC):

- `@IsStaff`: Restricts access to users with the `STAFF` role.
- `@IsClient`: Restricts access to users with the `CLIENT` role.

Place these annotations on Controller methods or classes that require authorization.

## 5. Module Structure

The `business` package is organized by functional domain (e.g., `menu`, `order`, `store`). Each domain should follow the `controller` -> `service` -> `repository` hierarchy.

Example: `uk.ac.ncl.csc8019backend.business.menu.controller`
