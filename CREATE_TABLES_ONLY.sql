-- ============================================================
-- SMART CAMPUS EVENT MANAGEMENT SYSTEM
-- Table Creation Script (Manual Setup)
-- ============================================================
-- Run this in: phpMyAdmin > campus_event_db > SQL tab > Go
-- ============================================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS campus_event_db;
USE campus_event_db;

-- ============================================================
-- TABLE 1: users
-- Stores admin and student accounts
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name  VARCHAR(100),
    username   VARCHAR(50),
    email      VARCHAR(100),
    password   VARCHAR(255),
    role       VARCHAR(20),
    created_at DATETIME,
    enabled    TINYINT(1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 2: events
-- Stores all campus events (workshops, seminars, fests, etc.)
-- ============================================================
CREATE TABLE IF NOT EXISTS events (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    department      VARCHAR(100) NOT NULL,
    type            VARCHAR(50)  NOT NULL,
    description     TEXT         NOT NULL,
    event_date      DATETIME     NOT NULL,
    venue           VARCHAR(200) NOT NULL,
    capacity        INT          NOT NULL,
    available_seats INT          NOT NULL,
    ticket_price    DECIMAL(10,2),
    image_url       VARCHAR(500),
    created_at      DATETIME     NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 3: registrations
-- Stores student event registrations
-- ============================================================
CREATE TABLE IF NOT EXISTS registrations (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_name   VARCHAR(150) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    department     VARCHAR(100) NOT NULL,
    tickets_booked INT          NOT NULL,
    registered_at  DATETIME     NOT NULL,
    event_id       BIGINT       NOT NULL,
    CONSTRAINT fk_registration_event
        FOREIGN KEY (event_id) REFERENCES events(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- TABLE 4: feedbacks
-- Stores student feedback for attended events
-- ============================================================
CREATE TABLE IF NOT EXISTS feedbacks (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(150) NOT NULL,
    rating       INT          NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment      TEXT         NOT NULL,
    submitted_at DATETIME     NOT NULL,
    event_id     BIGINT       NOT NULL,
    CONSTRAINT fk_feedback_event
        FOREIGN KEY (event_id) REFERENCES events(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- VERIFY TABLES CREATED
-- ============================================================
SHOW TABLES;

-- ============================================================
-- NOTES:
-- 1. After running this SQL, start the Spring Boot app in Eclipse
-- 2. DataSeeder will auto-insert 6 sample events on first startup
-- 3. Create your admin account at: http://localhost:9090/user-register
-- 4. Or insert manually using the SQL below
-- ============================================================

-- ============================================================
-- OPTIONAL: Insert Admin User Manually
-- Password = admin123 (BCrypt hash)
-- ============================================================
-- INSERT INTO users (full_name, username, email, password, role, created_at, enabled)
-- VALUES (
--     'Admin User',
--     'admin',
--     'admin@smartcampus.com',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
--     'ADMIN',
--     NOW(),
--     1
-- );
