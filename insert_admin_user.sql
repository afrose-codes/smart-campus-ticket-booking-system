-- Run this in phpMyAdmin after starting the Spring Boot app once
-- (so Hibernate creates the users table first via ddl-auto=update)

INSERT INTO users (username, password, role)
VALUES ('admin', 'admin123', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;
