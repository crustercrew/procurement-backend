INSERT INTO users (department_id, email, password_hash, full_name, role, is_active, created_at, updated_at)
VALUES
    (1, 'requester@company.com', '$2a$10$SXz0NSdigOglBJZHUdb2VeB5r3eRar1Pb3z2a4Zb3s93WoXc83Wd.', 'Staff Pengadaan', 'REQUESTER', TRUE, now(), now()),
    (1, 'manager@company.com', '$2a$10$4obizFbgbRkhdCME9BLQYuaNCyeCAHNrEKogfuessYLDBss6rqhii', 'Manager Procurement', 'MANAGER', TRUE, now(), now()),
    (NULL, 'sales@dell.co.id', '$2a$10$1i3YSSBoS.WQKmsGccwbbunwtdpvPIt/eF.KtRR4OvpKEy6wNyX8.', 'PT Dell Indonesia', 'VENDOR', TRUE, now(), now()),
    (NULL, 'admin@company.com', '$2a$10$.GVyMFdqGV8LGBln0AQcUOAcvO3TeZp9yf0pCArCttl9KK9mMXQ2W', 'System Administrator', 'ADMIN', TRUE, now(), now())
ON CONFLICT (email) DO NOTHING;
