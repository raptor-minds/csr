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

### 10. Change User Reviewer
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

### 11. Batch Delete Users
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

### 12. Get User Events
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

### 13. Get User Activities
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
      "duration": "30 Minute(s)"
    },
    {
      "id": 2,
      "name": "AI Trends Sharing",
      "eventName": "Annual Tech Conference",
      "duration": "1 Hour(s)"
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
| page | number | No | Page number |
| pageSize | number | No | Page size |

#### Response Example
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "Annual Tech Conference",
      "startTime": "2024-03-20 09:00",
      "endTime": "2024-03-20 18:00",
      "is_display": true,
      "bgImage": "https://example.com/bg.jpg",
      "activities": [
        {
          "id": 1,
          "name": "Opening Speech",
          "description": "CEO opening remarks",
          "startTime": "2024-03-20 09:00",
          "endTime": "2024-03-20 09:30",
          "status": "registering"
        }
      ]
    }
  ]
}
```

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
    "total_time": 480,
    "icon": "/icons/tech-conference.png",
    "description": "Annual company tech conference, inviting experts from all departments to share the latest achievements...",
    "is_display": true,
    "visibleLocations": ["Shanghai", "Shenzhen"],
    "visibleRoles": ["admin", "user"]
  }
}
```

---

### 3. Create Event
**Endpoint**: `POST /api/events`

#### Request Body
```json
{
  "name": "New Event Name",
  "totalTime": 240,
  "icon": "/icons/new-event.png",
  "description": "Detailed event description...",
  "isDisplay": true,
  "visibleLocations": ["Shanghai"],
  "visibleRoles": ["admin", "user"]
}
```

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
Same as Create Event

#### Response Example
```json
{
  "code": 200,
  "message": "Update successful"
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