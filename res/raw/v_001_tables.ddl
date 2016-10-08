CREATE TABLE Currency
(
  _id                  INTEGER PRIMARY KEY,
  short_name_lower     TEXT NOT NULL,
  short_name           TEXT NOT NULL,
  id_icon              INTEGER,
  is_added             INTEGER NOT NULL
);

CREATE UNIQUE INDEX unique_short_name ON Currency
(
  short_name_lower
);

CREATE TABLE Rate
(
  _id                  INTEGER PRIMARY KEY,
  _id_sell             INTEGER NOT NULL,
  _id_buy              INTEGER NOT NULL,
  rate                 INTEGER NOT NULL,
  date_time            INTEGER NOT NULL,
  FOREIGN KEY (_id_sell) REFERENCES Currency (_id),
  FOREIGN KEY (_id_buy) REFERENCES Currency (_id)
);

CREATE TABLE Event
(
  _id                  INTEGER PRIMARY KEY,
  type_event           INTEGER NOT NULL,
  period_repeat        INTEGER NOT NULL,
  date_time            INTEGER NOT NULL
);

CREATE TABLE Category
(
  _id                  INTEGER PRIMARY KEY,
  _id_parent           INTEGER,
  type_category        INTEGER NOT NULL,
  name                 TEXT NOT NULL,
  FOREIGN KEY (_id_parent) REFERENCES Category (_id)
);

CREATE TABLE CoUser
(
  _id                  INTEGER PRIMARY KEY,
  name                 TEXT NOT NULL
);

CREATE TABLE Account
(
  _id                  INTEGER PRIMARY KEY,
  _id_currency         INTEGER NOT NULL,
  _id_co_user          INTEGER,
  id_icon              INTEGER NOT NULL,
  name                 TEXT NOT NULL,
  balance              INTEGER NOT NULL,
  FOREIGN KEY (_id_currency) REFERENCES Currency (_id),
  FOREIGN KEY (_id_co_user) REFERENCES CoUser (_id)
);

CREATE TABLE Transaction
(
  _id                  INTEGER PRIMARY KEY,
  _id_account          INTEGER NOT NULL,
  _id_currency         INTEGER NOT NULL,
  date_time            INTEGER NOT NULL,
  type_transaction     INTEGER NOT NULL,
  sum                  INTEGER NOT NULL,
  FOREIGN KEY (_id_currency) REFERENCES Currency (_id),
  FOREIGN KEY (_id_account) REFERENCES Account (_id)
);

CREATE TABLE Document
(
  _id                  INTEGER PRIMARY KEY,
  _id_transaction      INTEGER NOT NULL,
  _id_category         INTEGER NOT NULL,
  _id_event            INTEGER,
  sum                  INTEGER NOT NULL,
  FOREIGN KEY (_id_event) REFERENCES Event (_id),
  FOREIGN KEY (_id_category) REFERENCES Category (_id),
  FOREIGN KEY (_id_transaction) REFERENCES Transaction (_id)
);