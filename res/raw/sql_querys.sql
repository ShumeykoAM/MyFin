-- ACCOUNTS
SELECT Account._id, Account.id_icon, Account.name, Account.balance
  FROM Account
  WHERE Account._id_co_user IS NULL;

-- USER_ACCOUNT_ACTIYE
-- SELECT user_account._id, user_account.login, user_account.password, user_account.is_active, user_account.timestamp,
--  user_account.current_rev FROM user_account
--  WHERE user_account.is_active = ?;