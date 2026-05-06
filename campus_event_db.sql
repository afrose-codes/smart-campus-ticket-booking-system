-- ============================================================
-- SMART CAMPUS EVENT MANAGEMENT SYSTEM
-- Complete MySQL Setup Script
-- Run this in: phpMyAdmin > campus_event_db > SQL tab > Go
-- ============================================================

-- Step 1: Create and select the database
CREATE DATABASE IF NOT EXISTS campus_event_db;
USE campus_event_db;

-- ============================================================
-- DROP EXISTING TABLES (clean slate)
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS registrations;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- TABLE 1: users
-- ============================================================
CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name  VARCHAR(100),
    username   VARCHAR(50)  UNIQUE,
    email      VARCHAR(100) UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'STUDENT',
    created_at DATETIME,
    enabled    TINYINT(1)   NOT NULL DEFAULT 1
);

-- ============================================================
-- TABLE 2: events
-- ============================================================
CREATE TABLE events (
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
    created_at      DATETIME
);

-- ============================================================
-- TABLE 3: registrations
-- ============================================================
CREATE TABLE registrations (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_name    VARCHAR(150) NOT NULL,
    email           VARCHAR(150) NOT NULL,
    department      VARCHAR(100) NOT NULL,
    tickets_booked  INT          NOT NULL,
    registered_at   DATETIME,
    event_id        BIGINT       NOT NULL,
    CONSTRAINT fk_reg_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-- ============================================================
-- TABLE 4: feedbacks
-- ============================================================
CREATE TABLE feedbacks (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(150) NOT NULL,
    rating       INT          NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment      TEXT         NOT NULL,
    submitted_at DATETIME,
    event_id     BIGINT       NOT NULL,
    CONSTRAINT fk_fb_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-- ============================================================
-- SAMPLE DATA: Admin User
-- Password = admin123 (BCrypt encoded)
-- ============================================================
INSERT INTO users (full_name, username, email, password, role, created_at, enabled)
VALUES (
    'Admin User',
    'admin',
    'admin@smartcampus.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ADMIN',
    NOW(),
    1
);

-- ============================================================
-- SAMPLE DATA: Student User
-- Password = student123 (BCrypt encoded)
-- ============================================================
INSERT INTO users (full_name, username, email, password, role, created_at, enabled)
VALUES (
    'Test Student',
    'student1',
    'student@smartcampus.com',
    '$2a$10$8K1p/a0dR1xqM2LtPgcHKOsuTpFwFkD3P5Os5PqFSoUJ7pxIiV7Iq',
    'STUDENT',
    NOW(),
    1
);

-- ============================================================
-- SAMPLE DATA: 6 Campus Events
-- ============================================================
INSERT INTO events (title, department, type, description, event_date, venue, capacity, available_seats, ticket_price, image_url, created_at)
VALUES
(
    'National Tech Fest 2025',
    'Computer Science',
    'Technical Fest',
    'A grand national-level technical festival featuring hackathons, coding contests, robotics, AI showcases, and project exhibitions. Open to all engineering students across departments.',
    DATE_ADD(NOW(), INTERVAL 15 DAY),
    'Main Auditorium & Tech Block',
    500,
    500,
    199.00,
    'https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=600',
    NOW()
),
(
    'AI & Machine Learning Workshop',
    'Computer Science',
    'Workshop',
    'Hands-on workshop on Artificial Intelligence and Machine Learning fundamentals. Learn Python, TensorFlow, and build your first ML model with guidance from industry experts.',
    DATE_ADD(NOW(), INTERVAL 7 DAY),
    'CS Lab 301, Block B',
    60,
    60,
    99.00,
    'https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=600',
    NOW()
),
(
    'Entrepreneurship & Startup Summit',
    'Management',
    'Seminar',
    'Connect with successful entrepreneurs, venture capitalists, and startup founders. Pitch your ideas, attend panel discussions, and network with the startup ecosystem.',
    DATE_ADD(NOW(), INTERVAL 20 DAY),
    'Seminar Hall, Admin Block',
    200,
    200,
    0.00,
    'https://images.unsplash.com/photo-1559136555-9303baea8ebd?w=600',
    NOW()
),
(
    'Cultural Night - Rhythm & Beats',
    'All Departments',
    'Cultural Event',
    'Annual cultural extravaganza featuring music, dance, drama, and art performances by students. A night to celebrate talent, creativity, and campus spirit.',
    DATE_ADD(NOW(), INTERVAL 30 DAY),
    'Open Air Amphitheatre',
    1000,
    1000,
    49.00,
    'https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=600',
    NOW()
),
(
    'Web Development Bootcamp',
    'Computer Science',
    'Workshop',
    'Intensive 2-day bootcamp covering HTML, CSS, JavaScript, React, and Spring Boot. Build a full-stack project from scratch with mentorship from senior developers.',
    DATE_ADD(NOW(), INTERVAL 10 DAY),
    'Innovation Lab, Block C',
    40,
    40,
    149.00,
    'https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=600',
    NOW()
),
(
    'Research Paper Presentation',
    'Electronics',
    'Conference',
    'Annual research symposium where students and faculty present research papers on emerging technologies in electronics, IoT, and embedded systems.',
    DATE_ADD(NOW(), INTERVAL 25 DAY),
    'Conference Hall, Research Block',
    150,
    150,
    0.00,
    'https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=600',
    NOW()
);

-- ============================================================
-- SAMPLE DATA: 3 Registrations
-- ============================================================
INSERT INTO registrations (student_name, email, department, tickets_booked, registered_at, event_id)
VALUES
    ('Sara Priya',    'sara@veltech.edu.in',    'Computer Science', 2, NOW(), 1),
    ('Rahul Kumar',   'rahul@veltech.edu.in',   'Electronics',      1, NOW(), 2),
    ('Ananya Singh',  'ananya@veltech.edu.in',  'Management',       3, NOW(), 3);

-- Update available seats to reflect registrations
UPDATE events SET available_seats = available_seats - 2 WHERE id = 1;
UPDATE events SET available_seats = available_seats - 1 WHERE id = 2;
UPDATE events SET available_seats = available_seats - 3 WHERE id = 3;

-- ============================================================
-- SAMPLE DATA: 3 Feedback entries
-- ============================================================
INSERT INTO feedbacks (student_name, rating, comment, submitted_at, event_id)
VALUES
    ('Sara Priya',   5, 'Amazing event! Very well organized. Learned a lot about AI and the future of technology. Highly recommend to all CS students.', NOW(), 2),
    ('Rahul Kumar',  4, 'Great workshop with hands-on sessions. The instructors were knowledgeable. Would have liked more time for practice.', NOW(), 2),
    ('Ananya Singh', 5, 'The startup summit was incredibly inspiring. Met amazing founders and got great feedback on my business idea.', NOW(), 3);

-- ============================================================
-- VERIFY: Check all tables
-- ============================================================
SELECT 'users'         AS table_name, COUNT(*) AS row_count FROM users
UNION ALL
SELECT 'events',        COUNT(*) FROM events
UNION ALL
SELECT 'registrations', COUNT(*) FROM registrations
UNION ALL
SELECT 'feedbacks',     COUNT(*) FROM feedbacks;

-- ============================================================
-- LOGIN CREDENTIALS AFTER RUNNING THIS SCRIPT:
--
--   ADMIN LOGIN:
--   URL      : http://localhost:9090/login
--   Username : admin
--   Password : admin123
--
--   STUDENT LOGIN:
--   URL      : http://localhost:9090/login
--   Username : student1
--   Password : student123
--
--   OR create your own account at:
--   URL      : http://localhost:9090/user-register
-- ============================================================
