-- Run this in phpMyAdmin > campus_event_db > SQL tab
-- Drops old users table and lets Hibernate recreate with correct schema

DROP TABLE IF EXISTS users;

-- Hibernate will auto-create this on next app startup:
-- CREATE TABLE users (
--   id BIGINT AUTO_INCREMENT PRIMARY KEY,
--   full_name VARCHAR(100) NOT NULL,
--   username VARCHAR(50) UNIQUE NOT NULL,
--   email VARCHAR(100) UNIQUE NOT NULL,
--   password VARCHAR(255) NOT NULL,
--   role VARCHAR(20) NOT NULL,
--   created_at DATETIME,
--   enabled BIT NOT NULL
-- );
