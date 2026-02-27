# User Login and API Call Guide

This guide describes how to authenticate and use JWT tokens to call protected API endpoints.

## 1. Authentication (Login)

Send a `POST` request to the `/api/auth/login` endpoint with your credentials.

- **URL**: `http://localhost:8080/api/auth/login`
- **Method**: `POST`
- **Body (JSON)**:
```json
{
    "username": "your_username",
    "password": "your_password"
}
```

### Response Example
If successful, you will receive a response containing the token:
```json
{
    "code": 200,
    "message": "Login successful",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "tokenHead": "Bearer "
    }
}
```

## 2. Calling Protected Endpoints

For any subsequent requests to protected endpoints (e.g., `/api/staff/hello`), you must include the token in the `Authorization` header.

- **Header Name**: `Authorization`
- **Header Value**: `Bearer <your_token>`

### Example using cURL
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." http://localhost:8080/api/staff/hello
```

### Example Response (Authorized)
```json
{
    "code": 200,
    "message": "Success",
    "data": "Hello from staff-only endpoint!"
}
```

### Example Response (Unauthorized/Missing Token)
```json
{
    "code": 401,
    "message": "Unauthorized",
    "data": null
}
```

## 3. Role-Based Access

- **STAFF Only**: Endpoints annotated with `@IsStaff` require a token from a user with the `STAFF` role.
- **CLIENT Only**: Endpoints annotated with `@IsClient` require a token from a user with the `CLIENT` role.
- **Public**: Endpoints starting with `/api/public/` do not require a token.
