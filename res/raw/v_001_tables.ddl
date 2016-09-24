CREATE TABLE Currency
(
  _id                  INTEGER PRIMARY KEY,
  short_name           TEXT NOT NULL,
  id_icon              INTEGER
);

CREATE TABLE TypeResource
(
  _id                  INTEGER PRIMARY KEY,
  name                 TEXT NOT NULL,
  id_icon              INTEGER NOT NULL
);

CREATE TABLE CoUser
(
  _id                  INTEGER PRIMARY KEY,
  _id_type_resource    INTEGER NOT NULL,
  name                 TEXT NOT NULL,
  FOREIGN KEY (_id_type_resource) REFERENCES TypeResource (_id)
);

CREATE TABLE Account
(
  _id                  INTEGER PRIMARY KEY,
  _id_currency         INTEGER NOT NULL,
  _id_type_resource    INTEGER NOT NULL,
  _id_co_user          INTEGER,
  name                 TEXT NOT NULL,
  balance              REAL NOT NULL,
  FOREIGN KEY (_id_currency) REFERENCES Currency (_id),
  FOREIGN KEY (_id_type_resource) REFERENCES TypeResource (_id),
  FOREIGN KEY (_id_co_user) REFERENCES CoUser (_id)
);
