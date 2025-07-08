# Postman测试指南 - 用户活动API

## 接口信息
- **请求方法**: `GET`
- **URL**: `http://localhost:8080/api/users/{id}/activities`
- **用途**: 获取用户参与的活动记录
- **权限**: 需要认证（USER或ADMIN角色）

## 测试步骤

### 1. 准备测试数据
首先在数据库中执行 `test_data.sql` 脚本，插入测试数据：

```sql
-- 执行test_data.sql中的SQL语句
-- 这会创建测试用户、事件、活动和用户活动关联数据
```

### 2. 用户注册（如果还没有用户）
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser001",
  "password": "password123"
}
```

### 3. 用户登录获取Token
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser001",
  "password": "password123"
}
```

**响应示例**:
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

### 4. 调用用户活动API

#### 4.1 基本配置
- **请求方法**: `GET`
- **URL**: `http://localhost:8080/api/users/1/activities`

#### 4.2 Headers配置
| Key | Value |
|-----|-------|
| Authorization | Bearer [从步骤3获取的accessToken] |
| Content-Type | application/json |

#### 4.3 完整Postman配置

**URL栏**:
```
http://localhost:8080/api/users/1/activities
```

**Headers标签页**:
| Key | Value |
|-----|-------|
| Authorization | Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9... |
| Content-Type | application/json |

**Body标签页**: 无需配置（GET请求）

### 5. 预期响应

#### 成功响应 (200)
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "开场致辞",
      "eventName": "年度技术分享大会",
      "duration": "30分钟"
    },
    {
      "id": 2,
      "name": "技术分享：AI发展趋势",
      "eventName": "年度技术分享大会",
      "duration": "60分钟"
    },
    {
      "id": 4,
      "name": "CSR实践案例分享",
      "eventName": "企业社会责任论坛",
      "duration": "45分钟"
    },
    {
      "id": 5,
      "name": "区块链应用展示",
      "eventName": "区块链技术研讨会",
      "duration": "120分钟"
    }
  ]
}
```

#### 错误响应示例

**用户不存在 (500)**
```json
{
  "code": 500,
  "message": "Failed to retrieve user activities",
  "data": null
}
```

**未认证 (401)**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication failed",
  "path": "/api/users/1/activities"
}
```

**权限不足 (403)**
```json
{
  "code": 403,
  "message": "Access denied",
  "data": null
}
```

## 测试用例

### 测试用例1: 获取用户1的活动
- **URL**: `GET http://localhost:8080/api/users/1/activities`
- **预期**: 返回用户1参与的所有活动

### 测试用例2: 获取不存在的用户活动
- **URL**: `GET http://localhost:8080/api/users/999/activities`
- **预期**: 返回错误信息

### 测试用例3: 未认证访问
- **URL**: `GET http://localhost:8080/api/users/1/activities`
- **Headers**: 不包含Authorization
- **预期**: 返回401未认证错误

### 测试用例4: 获取用户2的活动（无活动）
- **URL**: `GET http://localhost:8080/api/users/2/activities`
- **预期**: 返回空数组

## 注意事项

1. **Token有效期**: Access Token只有5分钟有效期，过期需要重新登录或使用Refresh Token
2. **用户ID**: 确保使用正确的用户ID，测试数据中用户1有活动记录
3. **数据库连接**: 确保数据库连接正常，测试数据已正确插入
4. **项目启动**: 确保Spring Boot应用已启动并运行在localhost:8080
5. **日期时间格式**: 所有日期时间字段使用 `yyyy-MM-dd HH:mm` 格式，例如：`2025-07-21 09:00`

## 故障排除

### 常见问题

1. **401 Unauthorized**: 检查Token是否有效，是否包含"Bearer "前缀
2. **500 Internal Server Error**: 检查数据库连接和测试数据是否正确插入
3. **404 Not Found**: 检查URL是否正确，项目是否正常启动
4. **空数据返回**: 检查用户ID是否存在，该用户是否有活动记录

### 调试技巧

1. 查看应用日志，了解具体错误信息
2. 使用数据库客户端直接查询验证数据
3. 检查JWT Token是否过期
4. 确认Security配置是否正确 