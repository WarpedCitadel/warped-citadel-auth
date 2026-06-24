INSERT INTO
    wc01.app_user_audit (app_user_id)
SELECT id
FROM wc01.app_user
WHERE user_uuid = ?::uuid;