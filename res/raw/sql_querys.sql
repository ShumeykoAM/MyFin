--Сущности по _id
--CURRENCY
SELECT Currency._id, Currency.short_name_lower, Currency.short_name, Currency.id_icon, Currency.is_added
  FROM Currency
  WHERE _id=?;

--ACCOUNT
SELECT Account._id, Account._id_currency, Account._id_co_user, Account.id_icon, Account.name, Account.balance
  FROM Account
  WHERE _id=?;

--CO_USER
SELECT CoUser._id, CoUser.name
  FROM CoUser
  WHERE _id=?;

-- ACCOUNTS
SELECT Account._id, Account.id_icon, Account.name, Account.balance
  FROM Account
  WHERE Account._id_co_user IS NULL
  ORDER BY Account.id_icon;

-- USER_ACCOUNTS
SELECT Account._id
  FROM Account
  WHERE Account._id_co_user=?
  ORDER BY Account.id_icon;

-- CURRENCIES
SELECT Currency._id, Currency.id_icon, Currency.short_name, Currency.is_added
  FROM Currency
  ORDER BY Currency.is_added ASC, Currency._id ASC;