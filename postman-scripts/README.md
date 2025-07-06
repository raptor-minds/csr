# CSR API Postman 测试脚本

## 项目简介

这是一个企业社会责任(CSR)管理系统的API测试集合，包含了完整的用户管理、事件管理、活动管理等功能模块的测试脚本。

## 系统架构

- **后端框架**: Spring Boot
- **数据库**: MySQL
- **认证方式**: JWT Token
- **API风格**: RESTful

## 功能模块

### 1. 认证管理 (/api/auth)
- 用户注册
- 管理员注册
- 用户登录
- Token刷新
- 用户登出

### 2. 用户管理 (/api/users)
- 获取用户列表（管理员）
- 获取用户详情
- 更新个人资料
- 重置密码
- 更新用户信息（管理员）
- 更改审核员
- 批量删除用户
- 获取用户事件
- 获取用户活动

### 3. 事件管理 (/api/events)
- 获取事件列表
- 获取事件详情
- 创建事件
- 更新事件
- 更新事件显示状态
- 删除事件

### 4. 活动管理 (/api/activities)
- 获取活动列表
- 获取活动详情
- 创建活动
- 更新活动
- 删除活动
- 报名活动
- 退出活动
- 获取用户在指定事件下的活动

### 5. 模板管理 (/api/templates)
- 获取模板列表
- 搜索模板（按名称）

### 6. 测试接口
- 测试认证
- 管理员测试
- 用户测试
- 获取用户资料

## 使用说明

### 1. 环境配置

在Postman中创建环境变量：

| 变量名 | 描述 | 示例值 |
|--------|------|--------|
| baseUrl | API基础URL | http://localhost:8080 |
| username | 测试用户名 | testuser |
| password | 测试密码 | password123 |
| accessToken | JWT访问令牌 | (登录后自动设置) |
| refreshToken | JWT刷新令牌 | (登录后自动设置) |
| userId | 用户ID | (登录后自动设置) |
| eventId | 事件ID | 1 |
| activityId | 活动ID | 1 |

### 2. 导入步骤

1. 打开Postman
2. 点击"Import"按钮
3. 选择`CSR_API_Collection.json`文件
4. 创建新的环境并设置上述环境变量
5. 选择刚创建的环境

### 3. 测试流程

#### 基础测试流程：

1. **注册用户**
   - 执行"用户注册"请求创建测试用户
   - 或执行"管理员注册"创建管理员账户

2. **用户登录**
   - 执行"用户登录"请求
   - 登录成功后会自动设置`accessToken`、`refreshToken`和`userId`

3. **测试其他功能**
   - 现在可以测试需要认证的API接口
   - Token会自动添加到请求头中

#### 管理员测试流程：

1. 使用管理员账户登录
2. 测试管理员专用接口（如用户管理、批量操作等）

### 4. 数据格式说明

#### 用户注册请求体：
```json
{
    "username": "testuser",
    "password": "password123",
    "nickname": "测试用户",
    "realName": "张三",
    "gender": "MALE"
}
```

#### 事件创建请求体：
```json
{
    "name": "企业社会责任活动",
    "startTime": "2024-12-01 09:00",
    "endTime": "2024-12-01 18:00",
    "icon": "https://example.com/icon.png",
    "description": "这是一个企业社会责任活动",
    "isDisplay": true,
    "visibleLocations": ["北京", "上海", "广州"],
    "visibleRoles": ["USER", "ADMIN"]
}
```

#### 活动创建请求体：
```json
{
    "name": "志愿者服务活动",
    "description": "为社区提供志愿者服务",
    "startTime": "2024-12-01 10:00",
    "endTime": "2024-12-01 16:00",
    "status": "ACTIVE",
    "eventId": 1
}
```

#### 模板API接口

**获取模板列表**
- **URL**: `GET /api/templates`
- **参数**: 
  - `name` (可选): 模板名称搜索关键词
- **响应**: 模板数组

**请求示例**:
```
GET /api/templates
GET /api/templates?name=志愿者
```

**响应格式**:
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {
            "id": 1,
            "name": "志愿者服务模板",
            "totalTime": 120,
            "fileLink": "https://example.com/template.pdf",
            "detail": "{\"description\": \"志愿者服务活动模板\", \"requirements\": [\"年龄18岁以上\", \"身体健康\"]}"
        }
    ]
}
```

**字段说明**:
- `id`: 模板唯一标识
- `name`: 模板名称
- `totalTime`: 模板预计总时间（分钟）
- `fileLink`: 模板文件下载链接
- `detail`: JSON格式的详细信息

### 5. 响应格式

所有API响应都遵循统一的格式：

```json
{
    "code": 200,
    "message": "success",
    "data": {
        // 具体数据
    }
}
```

### 6. 错误处理

- **400**: 请求参数错误
- **401**: 未认证或Token无效
- **403**: 权限不足
- **404**: 资源不存在
- **500**: 服务器内部错误

### 7. 注意事项

1. **Token管理**: 登录后会自动设置Token，Token有效期为5分钟
2. **权限控制**: 某些接口需要管理员权限，请确保使用管理员账户测试
3. **数据依赖**: 某些测试需要先创建事件或活动，请按顺序执行
4. **环境变量**: 确保所有环境变量都已正确设置

### 8. 常见问题

**Q: Token过期怎么办？**
A: 使用"刷新Token"接口获取新的访问令牌

**Q: 如何测试管理员功能？**
A: 先使用"管理员注册"创建管理员账户，然后登录测试

**Q: 批量操作失败？**
A: 确保要删除的用户ID存在，且当前用户具有管理员权限

## 开发环境要求

- Java 17+
- Spring Boot 3.x
- MySQL 8.0+
- Maven 3.6+

## 部署说明

1. 配置数据库连接
2. 设置JWT密钥
3. 启动Spring Boot应用
4. 导入Postman集合开始测试

## 联系方式

如有问题，请联系开发团队。 