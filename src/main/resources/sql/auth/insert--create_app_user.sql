WITH ins_app_user_cte AS
(
	INSERT INTO wc01.app_user
    	(username,
    	password_hash,
   		email)
	VALUES
		( ?, ?, ?)
	returning id
),
ins_email_verification_code_cte AS
(
	INSERT INTO wc01.email_verification_token
	(app_user_id, token, passcode)
	SELECT au.id,
	?,
	?
	FROM ins_app_user_cte au
	returning app_user_id,
	token,
	passcode
)
SELECT
token,
passcode
FROM ins_email_verification_code_cte;