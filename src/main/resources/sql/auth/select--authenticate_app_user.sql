SELECT
    au.username,
    au.user_uuid,
    r.role_type
FROM wc01.app_user au
INNER JOIN wc01.role r
    ON au.role = r.id
WHERE LOWER(username) = LOWER(?);