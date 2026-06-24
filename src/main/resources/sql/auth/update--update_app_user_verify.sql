UPDATE wc01.app_user
SET isverified = TRUE::BOOLEAN
WHERE id = ?;