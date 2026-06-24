WITH sel_app_user_cte AS
(
    SELECT
        id,
        username
    FROM wc01.app_user
    WHERE email = ?
),
ins_email_verification_code_cte AS
(
    INSERT INTO wc01.email_verification_token
        (app_user_id,
        token,
        passcode)
    SELECT
        au.id,
        ?,
        ?
    FROM sel_app_user_cte au
    RETURNING app_user_id
)
SELECT au.username
FROM sel_app_user_cte au;