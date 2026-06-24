SELECT
	au.id,
	evt.token,
	evt.passcode,
	evt.isused
FROM wc01.app_user au
	LEFT JOIN wc01.email_verification_token evt
ON evt.app_user_id = au.id
WHERE evt.token = ? AND
(CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC') < evt.expires_dtm;