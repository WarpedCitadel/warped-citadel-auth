UPDATE wc01.email_verification_token
SET isused = true
WHERE token = ?;