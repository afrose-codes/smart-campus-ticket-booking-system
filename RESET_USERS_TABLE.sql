-- ============================================================
-- RUN THIS IN phpMyAdmin > campus_event_db > SQL TAB
-- BEFORE restarting the Spring Boot app
-- ============================================================

DROP TABLE IF EXISTS users;

-- Hibernate will auto-recreate the table on next app startup
-- with all correct columns:
--   id, full_name, username, email, password, role, created_at, enabled

-- ============================================================
-- OPTIONAL: Insert an ADMIN user manually after app starts
-- (BCrypt hash below = "admin123")
-- ============================================================
-- INSERT INTO users (full_name, username, email, password, role, enabled)
-- VALUES ('Admin User', 'admin', 'admin@campus.com',
--   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
--   'ADMIN', true);
