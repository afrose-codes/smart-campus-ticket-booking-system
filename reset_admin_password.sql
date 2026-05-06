-- Run this in phpMyAdmin BEFORE restarting the Spring Boot app
-- This deletes the old plain-text admin row so UserSeeder recreates it with BCrypt hash

DELETE FROM users WHERE username = 'admin';

-- After running this SQL, restart the Spring Boot app.
-- UserSeeder will auto-insert admin with BCrypt hashed password.
-- Login with: admin / admin123
