
WITH RECURSIVE category_tree(id, pid, name, sort_pred) AS
(
  SELECT id, pid, title,
    ---
    (
      WITH RECURSIVE cat_path(id, pid ,path2) AS
      (
        SELECT id, pid, ("-" || CAST(id AS TEXT) || "-") FROM test_table WHERE id=tt.id
        UNION ALL
        SELECT test_table.id, test_table.pid, ("-" || CAST(test_table.id AS TEXT) || "-") || path2
         FROM cat_path INNER JOIN test_table ON test_table.id=cat_path.pid
      )
      SELECT path2 FROM cat_path
        WHERE LENGTH(path2) = (SELECT MAX(LENGTH(path2)) FROM cat_path)
    )
    ---
    FROM test_table AS tt
      WHERE tt.title LIKE "%%" --неточный поиск
    UNION ALL
      SELECT tt2.id, tt2.pid, tt2.title,
        ---
        (
          WITH RECURSIVE cat_path(id, pid ,path2) AS
          (
            SELECT id, pid, ("-" || CAST(id AS TEXT) || "-") FROM test_table WHERE id=tt2.id
            UNION ALL
            SELECT test_table.id, test_table.pid, ("-" || CAST(test_table.id AS TEXT) || "-") || path2
              FROM cat_path INNER JOIN test_table ON test_table.id=cat_path.pid
          )
          SELECT path2 FROM cat_path
            WHERE LENGTH(path2) = (SELECT MAX(LENGTH(path2)) FROM cat_path)
        )
        ---
        FROM category_tree
          INNER JOIN test_table AS tt2 ON tt2.id=category_tree.pid
)
SELECT DISTINCT id, sort_pred, name
  FROM category_tree AS t1
  ORDER BY sort_pred;


