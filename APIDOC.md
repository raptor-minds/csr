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
  "password": "password123",
  "nickname": "Johnny",
  "realName": "John Doe",
  "gender": "male"
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
| nickname | string | No | User's nickname (max 50 characters) |
| realName | string | No | User's real name (max 50 characters) |
| gender | string | No | User's gender. Valid values: "male", "female", "other" |

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
  "password": "admin123",
  "nickname": "Administrator",
  "realName": "System Admin",
  "gender": "other"
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
| nickname | string | No | User's nickname (max 50 characters) |
| realName | string | No | User's real name (max 50 characters) |
| gender | string | No | User's gender. Valid values: "male", "female", "other" |

---

### 3. User Login
Authenticate user and receive JWT tokens along with user information.

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
    "expiresIn": 300,
    "id": 1,
    "username": "john_doe"
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
| id | number | Unique identifier of the authenticated user |
| username | string | Username of the authenticated user |

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
        "reviewerId": 2,
        "reviewerName": "李明",
        "createTime": "2024-01-15 10:30:00",
        "eventCount": 5,
        "activityCount": 12
      },
      {
        "id": 2,
        "username": "admin_user",
        "role": "Administrator",
        "location": "深圳",
        "reviewerId": null,
        "reviewerName": null,
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
| data[].reviewerId | integer | ID of the user's reviewer |
| data[].reviewerName | string | Name of the user's reviewer |
| data[].createTime | string | User creation timestamp (YYYY-MM-DD HH:mm:ss) |
| data[].eventCount | integer | Number of events user participated in |
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

### 8. Get User Details
Get detailed information about a specific user by their ID.

**Endpoint**: `GET /api/users/{id}`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN users can access any user's details; USER can only access their own details

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | User ID to retrieve details for |

#### Request Example
```
GET /api/users/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "username": "用户1",
    "role": "admin",
    "location": "上海",
    "reviewerId": 3,
    "reviewerName": "孙雄鹰",
    "createTime": "2024-01-15 10:30:00",
    "eventCount": 5,
    "activityCount": 12
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| id | integer | User unique identifier |
| username | string | User's username |
| role | string | User role ("admin" or "user") |
| location | string | User's location |
| reviewerId | integer | ID of the user's reviewer |
| reviewerName | string | Name of the user's reviewer |
| createTime | string | User creation timestamp (YYYY-MM-DD HH:mm:ss) |
| eventCount | integer | Number of events user participated in |

#### Error Responses

##### User Not Found (404)
```json
{
  "code": 404,
  "message": "User not found with id: 123",
  "data": null
}
```

##### Unauthorized Access (403)
```json
{
  "code": 403,
  "message": "Access denied",
  "data": null
}
```

---

### 9. Update User
Update user information including username, role, and location.

**Endpoint**: `PUT /api/users/{id}`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | User ID to update |

#### Request Body
```json
{
  "username": "new_username",
  "role": "user",
  "location": "SH"
}
```

#### Request Example
```
PUT /api/users/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "username": "updated_user",
  "role": "admin",
  "location": "SZ"
}
```

#### Response
```json
{
  "code": 200,
  "message": "Update user successful",
  "data": null
}
```

#### Request Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| username | string | Yes | New username (max 45 characters, must be unique) |
| role | string | Yes | User role: "admin" or "user" |
| location | string | Yes | User location: "SH" (Shanghai) or "SZ" (Shenzhen) |

#### Error Responses

##### User Not Found (404)
```json
{
  "code": 404,
  "message": "User not found with id: 123",
  "data": null
}
```

##### Username Already Exists (400)
```json
{
  "code": 400,
  "message": "Username 'existing_user' already exists",
  "data": null
}
```

##### Validation Error (400)
```json
{
  "code": 400,
  "message": "Validation failed",
  "data": {
    "role": "Role must be either 'admin' or 'user'",
    "location": "Location must be either 'SH' or 'SZ'"
  }
}
```

---

### 10. Update Profile
Update user's profile information including nickname, real name, and gender. Users can only update their own profile.

**Endpoint**: `PUT /api/profile`  
**Authentication**: Bearer Token  
**Authorization**: USER or ADMIN role required

#### Request Body
```json
{
  "nickname": "新昵称",
  "realName": "真实姓名",
  "gender": "male"
}
```

#### Request Example
```
PUT /api/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "nickname": "新昵称",
  "realName": "真实姓名",
  "gender": "male"
}
```

#### Response
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

#### Request Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| nickname | string | No | User's nickname (max 50 characters) |
| realName | string | No | User's real name (max 50 characters) |
| gender | string | No | User's gender: "male", "female", or "other" |

#### Notes
- All fields are optional - you can update only the fields you want to change
- The user is automatically identified from the JWT token
- Only the authenticated user can update their own profile

#### Error Responses

##### User Not Found (400)
```json
{
  "code": 400,
  "message": "User not found with id: 123",
  "data": null
}
```

##### Validation Error (400)
```json
{
  "code": 400,
  "message": "Invalid gender value: invalid_gender. Must be one of: male, female, other",
  "data": null
}
```

##### Field Length Error (400)
```json
{
  "code": 400,
  "message": "Validation failed",
  "data": {
    "nickname": "Nickname cannot exceed 50 characters",
    "realName": "Real name cannot exceed 50 characters"
  }
}
```

---

### 11. Change User Reviewer
Change the reviewer for a specific user. Only works for normal users (not administrators).

**Endpoint**: `PUT /api/users/{id}/reviewer`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | User ID to change reviewer for |

#### Request Body
```json
{
  "reviewerId": 2
}
```

#### Request Example
```
PUT /api/users/1/reviewer
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "reviewerId": 2
}
```

#### Response
```json
{
  "code": 200,
  "message": "Change reviewer successful",
  "data": null
}
```

#### Request Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| reviewerId | integer | Yes | User ID of the new reviewer |

#### Error Responses

##### User Not Found (400)
```json
{
  "code": 400,
  "message": "User not found with id: 123",
  "data": null
}
```

##### Admin User Error (400)
```json
{
  "code": 400,
  "message": "An administrator does not need a reviewer",
  "data": null
}
```

##### Reviewer Not Found (400)
```json
{
  "code": 400,
  "message": "Reviewer not found with id: 456",
  "data": null
}
```

##### Validation Error (400)
```json
{
  "code": 400,
  "message": "Validation failed",
  "data": {
    "reviewerId": "Reviewer ID cannot be null"
  }
}
```

---

### 12. Batch Delete Users
Delete multiple users at once by their IDs.

**Endpoint**: `DELETE /api/users/batch-delete`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Request Body
```json
{
  "userIds": [1, 2, 3, 4]
}
```

#### Request Example
```
DELETE /api/users/batch-delete
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "userIds": [1, 2, 3, 4]
}
```

#### Response
```json
{
  "code": 200,
  "message": "Batch deletion success.",
  "data": null
}
```

#### Request Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| userIds | array | Yes | Array of user IDs to delete (cannot be null or empty) |

#### Error Responses

##### Users Not Found (400)
```json
{
  "code": 400,
  "message": "Users not found with IDs: [5, 6]",
  "data": null
}
```

##### Empty Request (400)
```json
{
  "code": 400,
  "message": "User IDs list cannot be null or empty",
  "data": null
}
```

##### Validation Error (400)
```json
{
  "code": 400,
  "message": "Validation failed",
  "data": {
    "userIds": "User IDs cannot be empty"
  }
}
```

---

### 13. Get User Events
Fetches the list of events a user has participated in.

**Endpoint**: `GET /api/users/{id}/events`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN can access any user's events; USER can only access their own.

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | User ID to fetch events for |

#### Request Example
```
GET /api/users/1/events
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "Event name",
      "type": "offline",
      "duration": "8 Hour(s) 30 Minute(s)",
      "status": "active"
    }
  ]
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| id | integer | Event ID |
| name | string | Event name |
| type | string | Event type: "offline", "online", or "hybrid" |
| duration | string | Formatted event duration (e.g., "8 Hour(s) 30 Minute(s)") |
| status | string | Event status: "active" or "ended" |

#### Error Responses

##### User Not Found (404)
```json
{
  "code": 404,
  "message": "User not found with id: 123",
  "data": null
}
```

---

### 14. Get User Activities
Retrieve the list of activities a user has participated in.

**Endpoint**: `GET /api/users/{id}/activities`  
**Authentication**: Bearer Token  
**Authorization**: USER or ADMIN role required

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | User ID to retrieve activities for |

#### Request Example
```
GET /api/users/1/activities
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Opening Speech",
      "eventName": "Annual Tech Conference",
      "duration": "30 Minute(s)",
      "state": "SIGNED_UP"
    },
    {
      "id": 2,
      "name": "AI Trends Sharing",
      "eventName": "Annual Tech Conference",
      "duration": "1 Hour(s)",
      "state": "WITHDRAWN"
    }
  ]
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| id | integer | Activity ID |
| name | string | Activity name |
| eventName | string | Name of the event |
| duration | string | Formatted activity duration (e.g. "30 Minute(s)") |
| state | string | User's participation state: "SIGNED_UP" or "WITHDRAWN" |

#### Error Response
```json
{
  "code": 500,
  "message": "Failed to retrieve user activities",
  "data": null
}
```

---

## Test & Utility APIs

### 14. Test Authentication
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

### 15. Admin Test
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

### 16. User Test
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

### 17. Get User Profile
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

---

## Event Management APIs

### 1. Get Event List
**Endpoint**: `GET /api/events`

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | number | No | Page number (default: 1) |
| pageSize | number | No | Page size (default: 10) |
| needsTotal | boolean | No | Include total participants and total time (default: false) |
| eventName | string | No | Filter events by name (case-insensitive partial match) |

#### Request Examples
```
GET /api/events?page=1&pageSize=10&needsTotal=true
GET /api/events?eventName=tech&page=1&pageSize=10
GET /api/events?eventName=Annual&needsTotal=true
```

#### Response Example (with needsTotal=true)
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "data": [
      {
        "id": 1,
        "name": "Annual Tech Conference",
        "startTime": "2024-03-20 09:00",
        "endTime": "2024-03-20 18:00",
        "isDisplay": true,
        "bgImage": "https://example.com/bg.jpg",
        "createdAt": "2024-03-15 14:30",
        "activities": [
          {
            "id": 1,
            "name": "Opening Speech",
            "description": "CEO opening remarks",
            "startTime": "2024-03-20 09:00",
            "endTime": "2024-03-20 09:30",
            "status": "ACTIVE",
            "createdAt": "2024-03-15 14:45",
            "totalParticipants": 25,
            "totalTime": 750
          },
          {
            "id": 2,
            "name": "Tech Workshop",
            "description": "Hands-on technical workshop",
            "startTime": "2024-03-20 10:00",
            "endTime": "2024-03-20 12:00",
            "status": "ACTIVE",
            "createdAt": "2024-03-15 14:50",
            "totalParticipants": 15,
            "totalTime": 1800
          }
        ],
        "totalParticipants": 25,
        "totalTime": 1200
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 10
  }
}
```

#### Response Example (with needsTotal=false or omitted)
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "data": [
      {
        "id": 1,
        "name": "Annual Tech Conference",
        "startTime": "2024-03-20 09:00",
        "endTime": "2024-03-20 18:00",
        "isDisplay": true,
        "bgImage": "https://example.com/bg.jpg",
        "createdAt": "2024-03-15 14:30",
        "activities": [
          {
            "id": 1,
            "name": "Opening Speech",
            "description": "CEO opening remarks",
            "startTime": "2024-03-20 09:00",
            "endTime": "2024-03-20 09:30",
            "status": "ACTIVE",
            "createdAt": "2024-03-15 14:45"
          }
        ]
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 10
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| id | integer | Event ID |
| name | string | Event name |
| startTime | string | Event start time (yyyy-MM-dd HH:mm format) |
| endTime | string | Event end time (yyyy-MM-dd HH:mm format) |
| isDisplay | boolean | Whether the event is displayed |
| bgImage | string | Background image URL |
| createdAt | string | Event creation timestamp (yyyy-MM-dd HH:mm format) |
| activities | array | List of activities within the event |
| totalParticipants | integer | **[Enhanced]** Total unique participants across all activities (only when needsTotal=true) |
| totalTime | integer | **[Enhanced]** Sum of total time from all activities in minutes (only when needsTotal=true) |

#### Activity Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| id | integer | Activity ID |
| name | string | Activity name |
| description | string | Activity description |
| startTime | string | Activity start time (yyyy-MM-dd HH:mm format) |
| endTime | string | Activity end time (yyyy-MM-dd HH:mm format) |
| status | string | Activity status |
| createdAt | string | Activity creation timestamp (yyyy-MM-dd HH:mm format) |
| totalParticipants | integer | **[Enhanced]** Total number of users signed up for this activity (only when needsTotal=true) |
| totalTime | integer | **[Enhanced]** Total time for this activity in minutes (totalParticipants × duration, only when needsTotal=true) |

#### Business Rules
- **Event Filtering**: When `eventName` is provided, only events with names containing the search term (case-insensitive) are returned
- **Event Level**: The `totalParticipants` field counts unique users across all activities in the event (de-duplicated)
- **Activity Level**: Each activity's `totalParticipants` field counts users signed up for that specific activity
- Only users with "SIGNED_UP" state in the user_activity table are counted
- **Event Level**: The `totalTime` field is the sum of (participants × duration) for each activity in the event
- **Activity Level**: Each activity's `totalTime` field is calculated as (activity participants × activity duration)
- Enhanced fields (`totalParticipants` and `totalTime`) are only included for both events and activities when `needsTotal=true`
- If `needsTotal=false` or omitted, the response will not include the enhanced fields for better performance
- Pagination is 1-based (page=1 is the first page)
- Pagination works correctly with event name filtering - `total` reflects the count of filtered results

---

### 2. Get Event Details
**Endpoint**: `GET /api/events/{id}`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | number | Yes | Event ID |

#### Response Example
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "name": "Annual Tech Conference",
    "startTime": "2024-07-01 09:00",
    "endTime": "2024-07-01 17:00",
    "icon": "/icons/tech-conference.png",
    "description": "Annual company tech conference, inviting experts from all departments to share the latest achievements...",
    "is_display": true,
    "visibleLocations": ["Shanghai", "Shenzhen"],
    "visibleRoles": ["admin", "user"],
    "createdAt": "2024-06-28 10:15"
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| id | integer | Event ID |
| name | string | Event name |
| startTime | string | Event start time (yyyy-MM-dd HH:mm format) |
| endTime | string | Event end time (yyyy-MM-dd HH:mm format) |
| icon | string | Event icon URL or path |
| description | string | Event description |
| is_display | boolean | Whether the event is displayed |
| visibleLocations | array | Locations where this event is visible |
| visibleRoles | array | User roles that can see this event |
| createdAt | string | Event creation timestamp (yyyy-MM-dd HH:mm format) |

---

### 3. Create Event
**Endpoint**: `POST /api/events`

#### Request Body
```json
{
  "name": "New Event Name",
  "startTime": "2024-07-01 09:00",
  "endTime": "2024-07-01 17:00",
  "icon": "/icons/new-event.png",
  "description": "Detailed event description...",
  "isDisplay": true,
  "visibleLocations": ["Shanghai"],
  "visibleRoles": ["admin", "user"]
}
```

#### 字段说明
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 是 | 事件名称 |
| startTime | string | 是 | 开始时间，格式yyyy-MM-dd HH:mm |
| endTime | string | 是 | 结束时间，格式yyyy-MM-dd HH:mm |
| icon | string | 是 | 事件图标 |
| description | string | 是 | 事件描述 |
| isDisplay | boolean | 是 | 是否展示 |
| visibleLocations | array | 是 | 可见地区 |
| visibleRoles | array | 是 | 可见角色 |

**Note**: The `createdAt` field is automatically set to the current system time when creating an event and cannot be specified in the request.

#### Response Example
```json
{
  "code": 200,
  "message": "Event created successfully"
}
```

---

### 4. Update Event
**Endpoint**: `PUT /api/events/{id}`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | number | Yes | Event ID |

#### Request Body
同上（与创建事件一致）

#### Response Example
```json
{
  "code": 200,
  "message": "Update successful"
}
```

#### Business Rules
- All event fields can be updated except for `createdAt`
- The `createdAt` field cannot be modified after event creation

---

### 事件详情返回示例
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "name": "Annual Tech Conference",
    "startTime": "2024-07-01 09:00",
    "endTime": "2024-07-01 17:00",
    "icon": "/icons/tech-conference.png",
    "description": "Annual company tech conference, inviting experts from all departments to share the latest achievements...",
    "is_display": true,
    "visibleLocations": ["Shanghai", "Shenzhen"],
    "visibleRoles": ["admin", "user"],
    "createdAt": "2024-06-28 10:15"
  }
}
```

---

### 5. Update Event Display Status
**Endpoint**: `PUT /api/events/{id}/display`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | number | Yes | Event ID |

#### Request Body
```json
{
  "isDisplay": false
}
```

#### Response Example
```json
{
  "code": 200,
  "message": "Update successful"
}
```

---

### 6. Delete Event
**Endpoint**: `DELETE /api/events/{id}`

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | number | Yes | Event ID |

#### Response Example
```json
{
  "code": 200,
  "message": "Delete successful"
}
```

---

## Activity Management APIs

### 1. Get Activities List
Retrieve a list of activities with optional filtering and enhanced information.

**Endpoint**: `GET /api/activities`  
**Authentication**: Bearer Token  
**Authorization**: All authenticated users

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| eventId | integer | No | Filter activities by event ID |
| page | integer | No | Page number for pagination (1-based) |
| pageSize | integer | No | Number of items per page (default: 10) |
| needsTotal | boolean | No | Include total participants and total time (default: false) |

#### Request Example
```
GET /api/activities?eventId=1&page=1&pageSize=10&needsTotal=true
```

#### Response Example (with needsTotal=true)
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Community Cleanup",
      "eventId": 1,
      "templateId": 1,
      "duration": 120,
      "icon": "cleanup-icon",
      "description": "Help clean up the local park",
      "startTime": "2024-01-15T09:00:00",
      "endTime": "2024-01-15T11:00:00",
      "status": "ACTIVE",
      "visibleLocations": ["New York", "Brooklyn"],
      "visibleRoles": ["USER", "ADMIN"],
      "createdAt": "2024-01-15T08:30:00",
      "totalParticipants": 15,
      "totalTime": 1800
    }
  ]
}
```

#### Response Example (with needsTotal=false or omitted)
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Community Cleanup",
      "eventId": 1,
      "templateId": 1,
      "duration": 120,
      "icon": "cleanup-icon",
      "description": "Help clean up the local park",
      "startTime": "2024-01-15T09:00:00",
      "endTime": "2024-01-15T11:00:00",
      "status": "ACTIVE",
      "visibleLocations": ["New York", "Brooklyn"],
      "visibleRoles": ["USER", "ADMIN"],
      "createdAt": "2024-01-15T08:30:00"
    }
  ]
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| id | integer | Activity ID |
| name | string | Activity name |
| eventId | integer | Associated event ID |
| templateId | integer | Template ID used for this activity |
| duration | integer | Activity duration in minutes |
| icon | string | Icon identifier for the activity |
| description | string | Activity description |
| startTime | string | Activity start time (ISO 8601 format) |
| endTime | string | Activity end time (ISO 8601 format) |
| status | string | Activity status |
| visibleLocations | array | Locations where this activity is visible |
| visibleRoles | array | User roles that can see this activity |
| createdAt | string | Activity creation timestamp (ISO 8601 format) |
| totalParticipants | integer | **[Enhanced]** Total number of users signed up (only when needsTotal=true) |
| totalTime | integer | **[Enhanced]** Total time in minutes (totalParticipants × duration, only when needsTotal=true) |

#### Business Rules
- The `totalParticipants` field counts only users with "SIGNED_UP" state in the user_activity table
- The `totalTime` field is calculated as `totalParticipants × duration`
- Enhanced fields (`totalParticipants` and `totalTime`) are only included when `needsTotal=true`
- If `needsTotal=false` or omitted, the response will not include the enhanced fields for better performance

---

### 2. Get Activity Details
Retrieve detailed information about a specific activity.

**Endpoint**: `GET /api/activities/{id}`  
**Authentication**: Bearer Token  
**Authorization**: All authenticated users

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Activity ID |

#### Request Example
```
GET /api/activities/1
```

#### Response Example
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": 1,
    "name": "Community Cleanup",
    "eventId": 1,
    "templateId": 1,
    "duration": 120,
    "icon": "cleanup-icon",
    "description": "Help clean up the local park",
    "startTime": "2024-01-15T09:00:00",
    "endTime": "2024-01-15T11:00:00",
    "status": "ACTIVE",
    "visibleLocations": ["New York", "Brooklyn"],
    "visibleRoles": ["USER", "ADMIN"],
    "createdAt": "2024-01-15T08:30:00"
  }
}
```

#### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| id | integer | Activity ID |
| name | string | Activity name |
| eventId | integer | Associated event ID |
| templateId | integer | Template ID used for this activity |
| duration | integer | Activity duration in minutes |
| icon | string | Icon identifier for the activity |
| description | string | Activity description |
| startTime | string | Activity start time (ISO 8601 format) |
| endTime | string | Activity end time (ISO 8601 format) |
| status | string | Activity status |
| visibleLocations | array | Locations where this activity is visible |
| visibleRoles | array | User roles that can see this activity |
| createdAt | string | Activity creation timestamp (ISO 8601 format) |

#### Error Responses

##### Activity Not Found (400)
```json
{
  "code": 400,
  "message": "Activity not found",
  "data": null
}
```

---

### 3. Create Activity
Create a new activity within an event.

**Endpoint**: `POST /api/activities`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Request Body
```json
{
  "name": "Community Cleanup",
  "eventId": 1,
  "templateId": 1,
  "duration": 120,
  "icon": "cleanup-icon",
  "description": "Help clean up the local park",
  "startTime": "2024-01-15T09:00:00",
  "endTime": "2024-01-15T11:00:00",
  "status": "not_registered",
  "visibleLocations": ["New York", "Brooklyn"],
  "visibleRoles": ["USER", "ADMIN"]
}
```

#### Response Example
```json
{
  "code": 200,
  "message": "Success",
  "data": 1
}
```

#### Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| name | string | Yes | Activity name (max 45 characters) |
| eventId | integer | Yes | Associated event ID |
| templateId | integer | Yes | Template ID to use for this activity |
| duration | integer | No | Activity duration in minutes (default: 0) |
| icon | string | Yes | Icon identifier (max 45 characters) |
| description | string | Yes | Activity description (max 1000 characters) |
| startTime | string | Yes | Activity start time (ISO 8601 format) |
| endTime | string | Yes | Activity end time (ISO 8601 format) |
| status | string | Yes | Activity status. Valid values: "not_registered", "registering", "full", "ended" |
| visibleLocations | array | Yes | Locations where this activity is visible (at least 1 required) |
| visibleRoles | array | Yes | User roles that can see this activity (at least 1 required) |

**Note**: The `createdAt` field is automatically set to the current system time when creating an activity and cannot be specified in the request.

#### Error Responses

##### Validation Error (400)
```json
{
  "code": 400,
  "message": "名称不能为空",
  "data": null
}
```

##### Access Denied (403)
```json
{
  "code": 403,
  "message": "Access denied. Admin role required.",
  "data": null
}
```

---

### 4. Update Activity
Update an existing activity's information.

**Endpoint**: `PUT /api/activities/{id}`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Activity ID to update |

#### Request Body
```json
{
  "name": "Updated Community Cleanup",
  "duration": 150,
  "icon": "new-cleanup-icon",
  "description": "Updated description for the park cleanup",
  "startTime": "2024-01-15T10:00:00",
  "endTime": "2024-01-15T12:30:00",
  "status": "registering",
  "visibleLocations": ["New York", "Brooklyn", "Queens"],
  "visibleRoles": ["USER", "ADMIN"]
}
```

#### Response Example
```json
{
  "code": 200,
  "message": "Success",
  "data": null
}
```

#### Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| name | string | No | Activity name (max 45 characters) |
| eventId | integer | No | Associated event ID (cannot be updated) |
| templateId | integer | No | Template ID (cannot be updated) |
| duration | integer | No | Activity duration in minutes |
| icon | string | No | Icon identifier (max 45 characters) |
| description | string | No | Activity description (max 1000 characters) |
| startTime | string | No | Activity start time (ISO 8601 format) |
| endTime | string | No | Activity end time (ISO 8601 format) |
| status | string | No | Activity status. Valid values: "not_registered", "registering", "full", "ended" |
| visibleLocations | array | No | Locations where this activity is visible (at least 1 required if provided) |
| visibleRoles | array | No | User roles that can see this activity (at least 1 required if provided) |

#### Error Responses

##### Invalid Activity ID (400)
```json
{
  "code": 400,
  "message": "Invalid activity ID",
  "data": null
}
```

##### Activity Not Found (400)
```json
{
  "code": 400,
  "message": "Activity not found",
  "data": null
}
```

##### Validation Error (400)
```json
{
  "code": 400,
  "message": "无效的状态值",
  "data": null
}
```

##### Access Denied (403)
```json
{
  "code": 403,
  "message": "Access denied. Admin role required.",
  "data": null
}
```

#### Business Rules
- Only provided fields will be updated (partial update supported)
- eventId, templateId, and createdAt cannot be modified after creation
- At least one location and role must be specified if visibleLocations or visibleRoles are provided

---

### 5. Delete Activity
Delete an existing activity.

**Endpoint**: `DELETE /api/activities/{id}`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role required

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Activity ID to delete |

#### Request Example
```
DELETE /api/activities/1
```

#### Response Example
```json
{
  "code": 200,
  "message": "Success",
  "data": null
}
```

#### Error Responses

##### Activity Not Found (400)
```json
{
  "code": 400,
  "message": "Activity not found",
  "data": null
}
```

##### Access Denied (403)
```json
{
  "code": 403,
  "message": "Access denied. Admin role required.",
  "data": null
}
```

#### Business Rules
- Deleting an activity will also affect related user_activity records
- This operation is irreversible
- Only administrators can delete activities

---

### 6. User Signup for Activity
Allow users to sign up for a specific activity. Records are stored in the user_activity table with SIGNED_UP status.

**Endpoint**: `POST /api/activities/{activityId}/signup`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role or the user themselves (JWT userId must match request userId)

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| activityId | number | Yes | Activity ID to sign up for |

#### Request Body
```json
{
  "userId": 1
}
```

#### Response Example
```json
{
  "code": 200,
  "message": "Signup successful",
  "data": null
}
```

#### Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| userId | number | Yes | ID of the user signing up for the activity |

#### Error Responses

##### Activity Not Found (400)
```json
{
  "code": 400,
  "message": "Activity not found",
  "data": null
}
```

##### User Not Found (400)
```json
{
  "code": 400,
  "message": "User not found",
  "data": null
}
```

##### Already Signed Up (400)
```json
{
  "code": 400,
  "message": "User has already signed up for this activity",
  "data": null
}
```

##### Access Denied (403)
```json
{
  "code": 403,
  "message": "Access denied. You can only signup for yourself.",
  "data": null
}
```

#### Business Rules
- Only administrators can sign up other users for activities
- Regular users can only sign up themselves (JWT userId must match request userId)
- Users cannot sign up for the same activity multiple times
- The signup record is created with state "SIGNED_UP"
- Fields chain_id, detail, endorsed_at, and endorsed_by are initially set to null

---

### 7. User Withdraw from Activity
Allow users to withdraw from a specific activity. Changes the state from SIGNED_UP to WITHDRAWN in the user_activity table.

**Endpoint**: `POST /api/activities/{activityId}/withdraw`  
**Authentication**: Bearer Token  
**Authorization**: ADMIN role or the user themselves (JWT userId must match request userId)

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| activityId | number | Yes | Activity ID to withdraw from |

#### Request Body
```json
{
  "userId": 1
}
```

#### Response Example
```json
{
  "code": 200,
  "message": "Withdraw successful",
  "data": null
}
```

#### Field Descriptions
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| userId | number | Yes | ID of the user withdrawing from the activity |

#### Error Responses

##### Activity Not Found (400)
```json
{
  "code": 400,
  "message": "Activity not found",
  "data": null
}
```

##### User Not Found (400)
```json
{
  "code": 400,
  "message": "User not found",
  "data": null
}
```

##### Not Signed Up (400)
```json
{
  "code": 400,
  "message": "User has not signed up for this activity",
  "data": null
}
```

##### Not Currently Signed Up (400)
```json
{
  "code": 400,
  "message": "User is not currently signed up for this activity",
  "data": null
}
```

##### Access Denied (403)
```json
{
  "code": 403,
  "message": "Access denied. You can only withdraw for yourself.",
  "data": null
}
```

#### Business Rules
- Only administrators can withdraw other users from activities
- Regular users can only withdraw themselves (JWT userId must match request userId)
- Users can only withdraw from activities they are currently signed up for (state must be "SIGNED_UP")
- After withdrawal, the state is changed to "WITHDRAWN"
- Users who have withdrawn can sign up again (the signup API will change state back to "SIGNED_UP")