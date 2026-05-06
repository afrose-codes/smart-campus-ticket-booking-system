-- ================================================================
-- STEP 1: Run this SQL in phpMyAdmin BEFORE starting the app
-- Go to: phpMyAdmin > campus_event_db > SQL tab > paste > Go
-- ================================================================

-- Drop old users table (removes old schema with missing columns)
DROP TABLE IF EXISTS users;

-- Create fresh users table with correct schema
CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name  VARCHAR(100),
    username   VARCHAR(50),
    email      VARCHAR(100),
    password   VARCHAR(255),
    role       VARCHAR(20),
    created_at DATETIME,
    enabled    TINYINT(1) DEFAULT 1
);

-- ================================================================
-- STEP 2: Start the Spring Boot app in Eclipse
-- STEP 3: Open http://localhost:9090/register
-- STEP 4: Create your account
-- STEP 5: Login at http://localhost:9090/login
-- ================================================================
