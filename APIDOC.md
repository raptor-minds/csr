# CSR API Documentation

## Overview
This document describes the REST API endpoints for the CSR (Corporate Social Responsibility) system. The API uses JWT-based authentication with role-based access control.

## Base Information
- **Base URL**: `http://localhost:8080`
- **Authentication**: Bearer Token (JWT)
- **Content-Type**: `application/json`
- **Response Format**: All responses follow the standardized format with `code`, `message`, and `data` fields

## Standard Response Format
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    // Response data here
  }
}
```

## Error Response Format
```json
{
  "code": 400,
  "message": "Error message",
  "data": null
}
```

---

## Authentication APIs

### 1. User Registration
Register a new user with USER role.

**Endpoint**: `POST /api/auth/register`  
**Authentication**: None  
**Authorization**: Public

#### Request
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

#### Response
```json
{
  "code": 200,
  "message": "User registered successfully",
  "data": null
}
```

#### Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| username | string | Yes | Unique username (max 45 characters) |
| password | string | Yes | User password (min 6 characters) |

---

### 2. Admin Registration
Register a new user with ADMIN role.

**Endpoint**: `POST /api/auth/register/admin`  
**Authentication**: None  
**Authorization**: Public (Note: In production, this should be restricted)

#### Request
```json
{
  "username": "admin_user",
  "password": "admin123"
}
```

#### Response
```json
{
  "code": 200,
  "message": "Admin user registered successfully",
  "data": null
}
```

#### Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| username | string | Yes | Unique username (max 45 characters) |
| password | string | Yes | User password (min 6 characters) |

---

### 3. User Login
Authenticate user and receive JWT tokens.

**Endpoint**: `POST /api/auth/login`  
**Authentication**: None  
**Authorization**: Public

#### Request
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

#### Response
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 300
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| accessToken | string | JWT access token for API authentication |
| refreshToken | string | JWT refresh token for obtaining new access tokens |
| tokenType | string | Token type, always "Bearer" |
| expiresIn | number | Access token expiration time in seconds (300 = 5 minutes) |

---

### 4. User Logout
Invalidate the current refresh token.

**Endpoint**: `POST /api/auth/logout`  
**Authentication**: Bearer Token  
**Authorization**: Authenticated users

#### Request
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Response
```json
{
  "code": 200,
  "message": "Logged out successfully",
  "data": null
}
```

#### Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| refreshToken | string | Yes | The refresh token to invalidate |

---

### 5. Refresh Token
Obtain a new access token using refresh token.

**Endpoint**: `POST /api/auth/refresh`  
**Authentication**: None  
**Authorization**: Valid refresh token required

#### Request
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Response
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 300
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| accessToken | string | New JWT access token |
| tokenType | string | Token type, always "Bearer" |
| expiresIn | number | New access token expiration time in seconds |

---

## User Management APIs

### 6. List Users
Get paginated list of all users with filtering and sorting options.

**Endpoint**: `GET /api/users`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Request Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number (1-based, default: 1) |
| pageSize | integer | No | Items per page (default: 10) |
| username | string | No | Filter by username (partial match, case-insensitive) |
| sortField | string | No | Field to sort by (id, username, createTime, etc.) |
| sortOrder | string | No | Sort direction: "ascend" or "descend" |

#### Request Example
```
GET /api/users?page=1&pageSize=10&username=john&sortField=createTime&sortOrder=descend
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "data": [
      {
        "id": 1,
        "username": "john_doe",
        "role": "User",
        "location": "上海",
        "reviewer": "李明",
        "createTime": "2024-01-15 10:30:00",
        "eventCount": 5,
        "activityCount": 12
      },
      {
        "id": 2,
        "username": "admin_user",
        "role": "Administrator",
        "location": "深圳",
        "reviewer": "王强",
        "createTime": "2024-01-16 14:20:00",
        "eventCount": 0,
        "activityCount": 0
      }
    ],
    "total": 25,
    "page": 1,
    "pageSize": 10
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|------------|
| data | array | Array of user objects |
| data[].id | integer | User unique identifier |
| data[].username | string | User's username |
| data[].role | string | User role |
| data[].location | string | User's location |
| data[].reviewer | string | User's reviewer name |
| data[].createTime | string | User creation timestamp (YYYY-MM-DD HH:mm:ss) |
| data[].eventCount | integer | Number of events user participated in |
| data[].activityCount | integer | Number of activities user participated in |
| total | integer | Total number of users matching the filter |
| page | integer | Current page number (1-based) |
| pageSize | integer | Number of items per page |

---

### 7. Reset User Password
Reset password for a specific user (Admin only).

**Endpoint**: `PUT /api/users/{id}/reset-password`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Request
```json
{
  "password": "newPassword123"
}
```

#### Response
```json
{
  "code": 200,
  "message": "Password reset successful.",
  "data": null
}
```

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | User ID to reset password for |

#### Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| password | string | Yes | New password (minimum 6 characters) |

---

## Test & Utility APIs

### 8. Test Authentication
Test user authentication and get user details.

**Endpoint**: `POST /testAuth`  
**Authentication**: Bearer Token  
**Authorization**: Any authenticated user

#### Request
```
POST /testAuth
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response
```json
{
  "code": 200,
  "message": "Authentication successful",
  "data": {
    "username": "john_doe",
    "role": "USER",
    "isAdmin": false,
    "authorities": [
      {
        "authority": "ROLE_USER"
      }
    ]
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| username | string | Authenticated user's username |
| role | string | User's role (USER or ADMIN) |
| isAdmin | boolean | Whether user has admin privileges |
| authorities | array | Spring Security authorities granted to user |

---

### 9. Admin Test
Test endpoint accessible only by administrators.

**Endpoint**: `GET /admin/test`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Request
```
GET /admin/test
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response
```json
{
  "code": 200,
  "message": "Admin access granted",
  "data": "Hello Admin! This endpoint is only accessible by ADMIN role."
}
```

---

### 10. User Test
Test endpoint accessible by users and administrators.

**Endpoint**: `GET /user/test`  
**Authentication**: Bearer Token  
**Authorization**: USER or ADMIN role required

#### Request
```
GET /user/test
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response
```json
{
  "code": 200,
  "message": "User access granted",
  "data": "Hello User! This endpoint is accessible by USER and ADMIN roles."
}
```

---

### 11. Get User Profile
Get current authenticated user's profile information.

**Endpoint**: `GET /profile`  
**Authentication**: Bearer Token  
**Authorization**: Any authenticated user

#### Request
```
GET /profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response
```json
{
  "code": 200,
  "message": "Profile retrieved",
  "data": {
    "username": "john_doe",
    "role": "USER",
    "isAdmin": false
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| username | string | Current user's username |
| role | string | Current user's role (USER or ADMIN) |
| isAdmin | boolean | Whether current user has admin privileges |

---

## Error Codes

| HTTP Status | Code | Description |
|-------------|------|-------------|
| 200 | 200 | Success |
| 400 | 400 | Bad Request - Invalid input or validation error |
| 401 | 401 | Unauthorized - Authentication required or failed |
| 403 | 403 | Forbidden - Insufficient permissions |
| 404 | 404 | Not Found - Resource not found |
| 500 | 500 | Internal Server Error - Server error |

## Common Error Responses

### Validation Error
```json
{
  "code": 400,
  "message": "Validation failed",
  "data": {
    "password": "Password must be at least 6 characters long",
    "username": "Username cannot be blank"
  }
}
```

### Unauthorized Access
```json
{
  "code": 401,
  "message": "Invalid username or password",
  "data": null
}
```

### Insufficient Permissions
```json
{
  "code": 403,
  "message": "Access denied",
  "data": null
}
```

### Resource Not Found
```json
{
  "code": 404,
  "message": "User not found with id: 123",
  "data": null
}
```

---

## Authentication Flow

1. **Register**: Use `/api/auth/register` or `/api/auth/register/admin` to create an account
2. **Login**: Use `/api/auth/login` to get access and refresh tokens
3. **API Calls**: Include `Authorization: Bearer <access_token>` header in subsequent requests
4. **Token Refresh**: Use `/api/auth/refresh` when access token expires
5. **Logout**: Use `/api/auth/logout` to invalidate refresh token

## Notes

- Access tokens expire in 5 minutes
- Refresh tokens expire in 7 days
- All timestamps are in format: `YYYY-MM-DD HH:mm:ss`
- Role-based access is enforced at both URL and method levels
- Pagination uses 1-based page numbering for frontend compatibility
- Username filtering supports partial, case-insensitive matching 