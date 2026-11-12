-- Seed Initial Administrative and Teacher Accounts
INSERT INTO users (username, email, password, full_name, is_active, created_at, updated_at)
VALUES 
    ('admin', 'admin@school.edu.kh', '$2a$10$M.ZK8ONMN18y1DgjwpXlrOR.9bRQhMA7u4wWKve1GUIIx8zf45m.y', 'System Administrator', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, email, password, full_name, is_active, created_at, updated_at)
VALUES 
    ('teacher', 'teacher@school.edu.kh', '$2a$10$3roTcDsnwIAoNRp7whGei.LjUTgZzuzqFOAKwjEVwgOsaG4uGabMm', 'Instructor Chan', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role)
SELECT u.id, 'ROLE_ADMIN' 
FROM users u 
WHERE u.username = 'admin' 
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'ROLE_ADMIN'
  );

INSERT INTO user_roles (user_id, role)
SELECT u.id, 'ROLE_TEACHER' 
FROM users u 
WHERE u.username = 'teacher' 
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role = 'ROLE_TEACHER'
  );
