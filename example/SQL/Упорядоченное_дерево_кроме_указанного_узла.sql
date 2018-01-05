--Дерево исключая один узел
WITH RECURSIVE category_tree(id, name, path, dscr)
AS (
  SELECT id, title, ("-" || CAST(id AS TEXT) || "-"),                        0                       AS dscr
    FROM test_table
    WHERE pid IS NULL AND test_table.id<>dscr
  UNION ALL
    SELECT test_table.id, test_table.title, path || ("-" || CAST(test_table.id AS TEXT) || "-"), dscr
      FROM category_tree
      INNER JOIN test_table ON test_table.pid=category_tree.id
      WHERE test_table.id<>dscr
)
SELECT * FROM category_tree ORDER BY path;