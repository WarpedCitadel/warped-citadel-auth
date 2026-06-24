SELECT
    au.user_uuid,
    au.username,
    au.password_hash,
    au.email,
    r.role_type,
    au.isactive,
    au.isverified
FROM wc01.app_user au
INNER JOIN wc01.role r
    ON au.role = r.id
WHERE LOWER(username) = LOWER(?);