-- Run this in phpMyAdmin > campus_event_db > SQL tab
-- Fixes the admin password back to plain text

UPDATE users SET password = 'admin123' WHERE username = 'admin';

-- Verify it worked:
SELECT * FROM users;
