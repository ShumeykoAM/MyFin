--Сущности по _id
--CURRENCY
SELECT Currency._id, Currency.cod_ISO, Currency.prim, Currency.full_name, Currency.symbol
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

--CATEGORY
SELECT Category._id, Category._id_parent, Category.trend, Category.name, Category.name_lower_case
  FROM Category
  WHERE Category._id=?;

--DOCUMENT
SELECT Document._id, Document._id_transact, Document._id_category, Document._id_event, Document.sum,
  Document.count, Document.id_unit
  FROM Document
  WHERE Document._id=?;

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
SELECT Currency._id, Currency.cod_ISO, Currency.full_name, Currency.symbol
  FROM Currency
  ORDER BY Currency.prim DESC, Currency.full_name ASC;

--TRANSACTIONS
SELECT Transact._id, Transact.date_time, Transact.sum, Transact.trend, Transact.correlative_sum,
  Acc.id_icon AS account_icon, Acc.name AS account_name, CoUserAcc.name AS co_user_name, Curr.symbol,
  CorrAcc.id_icon AS corr_acc_icon, CorrAcc.name AS corr_acc_name, CorrCoUser.name AS corr_co_user_name,
  CorrCurr.symbol AS corr_symbol
  FROM Transact, Account Acc, Currency Curr
  LEFT JOIN CoUser CoUserAcc ON CoUserAcc._id=Acc._id_co_user
  LEFT JOIN Account CorrAcc ON CorrAcc._id=Transact._id_correlative_account
  LEFT JOIN CoUser CorrCoUser ON CorrCoUser._id=CorrAcc._id_co_user
  LEFT JOIN Currency CorrCurr ON CorrCurr._id=CorrAcc._id_currency
  WHERE Acc._id=Transact._id_account AND Curr._id=Acc._id_currency AND
        (co_user_name IS NULL OR corr_co_user_name IS NULL)
  ORDER BY Transact.date_time DESC;

--CURRENCY_COD_ISO
SELECT Currency._id
  FROM Currency
  WHERE Currency.cod_ISO=?;

--ROOT_CATEGORIES Все корневые категории
SELECT Category._id, Category.trend, Category.name, Category.name_lower_case
  FROM Category
  WHERE Category._id NOT IN(SELECT AllParents._id FROM AllParents WHERE AllParents._id=Category._id)
  ORDER BY Category.name_lower_case ASC;

-- SUB_CATEGORIES Подкатегории указанного родителя, только перове колено
SELECT Category._id, Category.trend, Category.name, Category.name_lower_case
  FROM Category, AllParents
  WHERE AllParents._id_parent=? AND Category._id=AllParents._id AND AllParents.remote=1
  ORDER BY Category.name_lower_case ASC;

--ALL_SUB_CATEGORIES Все подкатегории указанного родителя, включая под под и т.д.
SELECT Category._id, Category.trend, Category.name, Category.name_lower_case
  FROM Category, AllParents
  WHERE AllParents._id_parent=? AND Category._id=AllParents._id
  ORDER BY AllParents.remote ASC, Category.name_lower_case ASC;

--EXIST_SUB_CATEGORY Возвращает одну строку если есть подкатегории у указанной категории иначе не возвращает ни одной строки
SELECT AllParents._id
  FROM AllParents
  WHERE AllParents._id_parent=?
  LIMIT 1;

--PLANNED
SELECT Document._id, Category.name, Document.count, Document.id_unit
  FROM Category, Document
  WHERE Category._id=Document._id_category AND Document._id_transact IS NULL;