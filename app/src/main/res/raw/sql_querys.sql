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
SELECT Category._id, Category.trend, Category.name, Category.name_lower_case
  FROM Category
  WHERE Category._id=?;

--DOCUMENT
SELECT Document._id, Document._id_transact, Document._id_category, Document._id_event, Document.price,
  Document.of, Document.of_id_unit, Document.count, Document.id_unit
  FROM Document
  WHERE Document._id=?;

-- Другие запросы
-- ACCOUNTS
SELECT Account._id, Account._id_currency, Account.id_icon, Account.name, Account.balance
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
SELECT Document._id, Category.name, Document.count, Document.id_unit, Category._id AS CATEGORY_ID
  FROM Category, Document
  WHERE Category._id=Document._id_category AND Document._id_transact IS NULL;

--ALL_CATEGORIES_LIKE Все категории c указанным трендом и указанной подстрокй
SELECT Category._id, Category.name
  FROM Category
  WHERE Category.trend=? AND Category.name_lower_case LIKE ?
        AND EXISTS (SELECT 1 FROM AllParents WHERE AllParents._id = Category._id LIMIT 1)
  ORDER BY Category.name_lower_case ASC;

--SUB_CATEGORIES_LIKE Все подкатегории указанного родителя c указанным трендом и указанной подстрокй, включая под под и т.д.
SELECT Category._id, Category.name
  FROM Category, AllParents
  WHERE AllParents._id_parent=? AND
        Category._id=AllParents._id AND Category.name_lower_case LIKE ?
        AND EXISTS (SELECT 1 FROM AllParents WHERE AllParents._id = Category._id LIMIT 1)
  ORDER BY Category.name_lower_case ASC;

--ALL_PARENTS Все родительские категории указанной категории начиная с корневой
SELECT Category._id, Category.name
  FROM Category, AllParents
  WHERE AllParents._id=? AND Category._id=AllParents._id_parent
        AND EXISTS (SELECT 1 FROM AllParents WHERE AllParents._id = Category._id LIMIT 1)
  ORDER BY AllParents.remote DESC;

--PLANNED_FROM_CATEGORY Запланированный документ для указанной категории
SELECT Document._id
  FROM Document
  WHERE Document._id_category=? AND Document._id_transact IS NULL;

--PLANNED_OR_LAST_DOCUMENT
SELECT Document.count, Document.id_unit
  FROM Document, Transact
  WHERE Document._id_category=? AND (Document._id_transact IS NULL OR Document._id_transact = Transact._id)
  ORDER BY Document._id_transact IS NULL DESC, Transact.date_time DESC
  LIMIT 1;

--ROOT_CATEGORY
SELECT Category._id
  FROM Category
  WHERE Category.trend=? AND NOT EXISTS (SELECT 1 FROM AllParents WHERE AllParents._id = Category._id LIMIT 1);

--IMMEDIATE_PARENT
SELECT AllParents._id_parent
  FROM AllParents
  WHERE AllParents._id=? AND AllParents.remote=1;

--INSERT_ALL_PARENT_RELATIVES (id категории и Id_parent категории)
INSERT INTO AllParents (_id, _id_parent, remote)
  SELECT ?, AllParents._id_parent, AllParents.remote+1
  FROM AllParents
  WHERE AllParents._id=?;

--COUNT_PARENTS
SELECT COUNT(_id) AS count
  FROM AllParents
  WHERE AllParents._id=?;

--CURRENCIES_ALL_ACCOUNTS Все разнообразие валют по существующим счетам кроме указанной валюты
SELECT distinct Currency._id, Currency.symbol
  FROM Currency, Account
  WHERE Account._id_co_user IS NULL AND Currency._id=Account._id_currency AND Currency._id!=?
  ORDER BY Currency.prim DESC;

--COUNT_CURRENCIES_ALL_ACCOUNTS Список всего разнообразия валют по существующим счетам
SELECT COUNT(DISTINCT Account._id_currency) AS count
  FROM Account
  WHERE Account._id_co_user IS NULL;

-- ACCOUNTS_FOR_CURRENCY все счета для указанной валюты
SELECT Account._id, Account.id_icon, Account.name, Account.balance
FROM Account
  WHERE Account._id_co_user IS NULL AND Account._id_currency=?
  ORDER BY Account.id_icon;

--IS_ROOT_CATEGORY вернет 1 если это одна из двух корневых категорий
SELECT NOT EXISTS(SELECT _id FROM AllParents WHERE AllParents._id = ?) AS is_root;


--Дерево категорий еще доделать конечно же надо
--Дерево исключая один узел
--WITH RECURSIVE category_tree(id, name, path, dscr)
--AS (
--  SELECT id, title, ("-" || CAST(id AS TEXT) || "-"),                        0                       AS dscr
--    FROM test_table
--    WHERE pid IS NULL AND test_table.id<>dscr
--  UNION ALL
--    SELECT test_table.id, test_table.title, path || ("-" || CAST(test_table.id AS TEXT) || "-"), dscr
--      FROM category_tree
--      INNER JOIN test_table ON test_table.pid=category_tree.id
--      WHERE test_table.id<>dscr
--)
--SELECT * FROM category_tree ORDER BY path;