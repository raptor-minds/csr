-- CSR API 测试数据初始化脚本

-- 插入模板测试数据
INSERT INTO template (name, total_time, file_link, detail) VALUES 
('志愿者服务模板', 120, 'https://example.com/volunteer-template.pdf', '{"description": "志愿者服务活动模板", "requirements": ["年龄18岁以上", "身体健康", "有责任心"], "activities": ["社区清洁", "老人陪伴", "儿童教育"]}'),
('环保活动模板', 180, 'https://example.com/eco-template.pdf', '{"description": "环保主题活动模板", "requirements": ["热爱环保", "团队合作"], "activities": ["垃圾分类宣传", "植树造林", "环保讲座"]}'),
('教育支持模板', 240, 'https://example.com/education-template.pdf', '{"description": "教育支持活动模板", "requirements": ["有教学经验", "耐心细致"], "activities": ["课后辅导", "技能培训", "图书捐赠"]}'),
('医疗援助模板', 300, 'https://example.com/medical-template.pdf', '{"description": "医疗援助活动模板", "requirements": ["医疗相关专业", "有相关证书"], "activities": ["健康检查", "医疗咨询", "药品捐赠"]}'),
('扶贫济困模板', 360, 'https://example.com/poverty-template.pdf', '{"description": "扶贫济困活动模板", "requirements": ["有爱心", "愿意长期参与"], "activities": ["物资捐赠", "技能培训", "就业帮扶"]}');

-- 插入用户测试数据（如果需要）
INSERT INTO user (username, password, nickname, real_name, gender, created_at, updated_at) VALUES 
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '测试用户', '张三', 'MALE', NOW(), NOW()),
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '管理员', '管理员', 'MALE', NOW(), NOW());

-- 插入角色数据（如果需要）
INSERT INTO role (name, description) VALUES 
('USER', '普通用户'),
('ADMIN', '管理员');

-- 插入用户角色映射（如果需要）
INSERT INTO user_role_map (user_id, role_id) VALUES 
(1, 1), -- testuser -> USER
(2, 2); -- admin -> ADMIN

-- 插入事件测试数据（如果需要）
INSERT INTO event (name, start_time, end_time, avatar, description, is_display, visible_locations, visible_roles, created_at) VALUES 
('企业社会责任活动', '2024-12-01 09:00:00', '2024-12-01 18:00:00', 'https://example.com/event-icon.png', '这是一个企业社会责任活动，旨在促进社会公益事业发展。', true, '["北京", "上海", "广州"]', '["USER", "ADMIN"]', NOW());

-- 插入活动测试数据（如果需要）
INSERT INTO activity (name, description, start_time, end_time, status, event_id, created_at) VALUES 
('志愿者服务活动', '为社区提供志愿者服务', '2024-12-01 10:00:00', '2024-12-01 16:00:00', 'ACTIVE', 1, NOW()),
('环保宣传活动', '宣传环保理念，推广绿色生活', '2024-12-01 14:00:00', '2024-12-01 17:00:00', 'ACTIVE', 1, NOW()); 