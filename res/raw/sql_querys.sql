--Сущности по _id
--CURRENCY
SELECT Currency._id, Currency.cod_ISO, Currency.prim, Currency.full_name
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

--TRANSACTION
SELECT Transact._id, Transact._id_account, Transact.date_time, Transact.type_transaction, Transact.sum
  FROM Transact
  WHERE _id=?;

-- Другие запросы
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
SELECT Currency._id, Currency.cod_ISO, Currency.full_name
  FROM Currency
  ORDER BY Currency.prim DESC, Currency.full_name ASC;

--TRANSACTIONS
SELECT Transact._id, Account.id_icon, Account.name AS account_name, CoUser.name AS co_user_name, Transact.sum,
  Transact.date_time, Transact.trend, Transact._id_correlative_account, Transact.correlative_sum
  FROM Transact, Account
  LEFT OUTER JOIN CoUser ON CoUser._id=Account._id_co_user
  WHERE Account._id=Transact._id_account
  ORDER BY Transact.date_time DESC;

--CURRENCY_COD_ISO
SELECT Currency._id
  FROM Currency
  WHERE Currency.cod_ISO=?;