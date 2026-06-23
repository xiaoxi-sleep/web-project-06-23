-- 创建数据库
CREATE DATABASE IF NOT EXISTS course_selection DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE course_selection;

-- 统一用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `role` VARCHAR(20) NOT NULL COMMENT '角色: admin/teacher/student',
    `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_role` (`role`),
    INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 院系字典表
CREATE TABLE IF NOT EXISTS `department` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL COMMENT '院系名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '院系描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院系表';

-- 学生信息表
CREATE TABLE IF NOT EXISTS `student` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE COMMENT '关联用户ID',
    `student_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '学号',
    `department` VARCHAR(100) DEFAULT NULL COMMENT '院系',
    `major` VARCHAR(100) DEFAULT NULL COMMENT '专业',
    `grade` VARCHAR(20) DEFAULT NULL COMMENT '年级',
    `class_name` VARCHAR(50) DEFAULT NULL COMMENT '班级',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_student_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 教师信息表
CREATE TABLE IF NOT EXISTS `teacher` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE COMMENT '关联用户ID',
    `teacher_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '工号',
    `department` VARCHAR(100) DEFAULT NULL COMMENT '所属院系',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_teacher_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师表';

-- 课程信息表
CREATE TABLE IF NOT EXISTS `course` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `course_no` VARCHAR(30) NOT NULL UNIQUE COMMENT '课程编号',
    `course_name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `teacher_id` BIGINT NOT NULL COMMENT '授课教师ID',
    `credit` DECIMAL(3,1) DEFAULT 2.0 COMMENT '学分',
    `capacity` INT DEFAULT 60 COMMENT '课程容量',
    `enrolled_count` INT DEFAULT 0 COMMENT '已选人数',
    `semester` VARCHAR(30) DEFAULT NULL COMMENT '学期',
    `classroom` VARCHAR(50) DEFAULT NULL COMMENT '上课教室',
    `schedule` VARCHAR(100) DEFAULT NULL COMMENT '上课时间',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '课程描述',
    `department` VARCHAR(100) DEFAULT NULL COMMENT '开课院系',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1可选 0停开',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_teacher` (`teacher_id`),
    INDEX `idx_status` (`status`),
    CONSTRAINT `fk_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 选课记录表（多对多关联）
CREATE TABLE IF NOT EXISTS `selection` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `course_id` BIGINT NOT NULL COMMENT '课程ID',
    `select_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    `status` VARCHAR(20) DEFAULT 'selected' COMMENT '选课状态: selected已选/dropped已退',
    UNIQUE KEY `uk_student_course` (`student_id`, `course_id`),
    INDEX `idx_course` (`course_id`),
    CONSTRAINT `fk_selection_student` FOREIGN KEY (`student_id`) REFERENCES `student`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_selection_course` FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课记录表';

-- =====================================================
-- 初始化测试数据
-- =====================================================

-- 密码均为 123456（BCrypt 加密）
INSERT INTO `user` VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin', '系统管理员', '13800000001', 1, NOW()),
(2, 'teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'teacher', '张教授', '13800000002', 1, NOW()),
(3, 'teacher2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'teacher', '李副教授', '13800000003', 1, NOW()),
(4, 'student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'student', '张三', '13800000004', 1, NOW()),
(5, 'student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'student', '李四', '13800000005', 1, NOW());

INSERT INTO `department` VALUES
(1, '计算机科学与技术学院', '涵盖计算机科学、软件工程等方向', NOW()),
(2, '数学与统计学院', '涵盖数学与应用数学、统计学等方向', NOW()),
(3, '外国语学院', '涵盖英语、日语、翻译等方向', NOW());

INSERT INTO `teacher` VALUES
(1, 2, 'T2024001', '计算机科学与技术学院', '教授', NOW()),
(2, 3, 'T2024002', '数学与统计学院', '副教授', NOW());

INSERT INTO `student` VALUES
(1, 4, 'S2024001', '计算机科学与技术学院', '软件工程', '2024级', '软件2401班', NOW()),
(2, 5, 'S2024002', '数学与统计学院', '数学与应用数学', '2024级', '数学2401班', NOW());

INSERT INTO `course` VALUES
(1, 'CS101', 'Java程序设计', 1, 3.0, 60, 0, '2026春季', '教一301', '周一 1-2节', 'Java语言基础、面向对象编程、集合框架等', '计算机科学与技术学院', 1, NOW()),
(2, 'CS102', '数据库原理', 1, 2.0, 50, 0, '2026春季', '教一302', '周二 3-4节', '关系数据库、SQL、事务管理等', '计算机科学与技术学院', 1, NOW()),
(3, 'MATH101', '高等数学', 2, 4.0, 80, 0, '2026春季', '教二201', '周三 1-2节', '微积分、级数、微分方程等', '数学与统计学院', 1, NOW()),
(4, 'CS103', 'Web前端开发', 1, 2.5, 45, 0, '2026春季', '教一403', '周四 5-6节', 'HTML/CSS/JavaScript/Vue.js等', '计算机科学与技术学院', 1, NOW());
