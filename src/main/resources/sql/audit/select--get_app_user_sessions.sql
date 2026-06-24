SELECT
	aua.lastactive_dtm
FROM wc01.app_user au
LEFT JOIN wc01.app_user_audit aua
	ON aua.app_user_id  = au.id
WHERE au.user_uuid = ?::uuid