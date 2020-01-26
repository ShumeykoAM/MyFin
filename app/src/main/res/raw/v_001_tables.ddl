CREATE TABLE Currency
(
  _id                  INTEGER PRIMARY KEY,
  cod_ISO              TEXT NOT NULL,
  prim                 INTEGER NOT NULL,
  full_name            TEXT NOT NULL,
  symbol               TEXT NOT NULL
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
  trend                INTEGER NOT NULL,
  name                 TEXT NOT NULL,
  name_lower_case      TEXT NOT NULL
);

CREATE UNIQUE INDEX unique_name_lower_case ON Category
(
  name_lower_case
);

CREATE TABLE AllParents
(
  _id                  INTEGER NOT NULL,
  _id_parent           INTEGER NOT NULL,
  remote               INTEGER NOT NULL,
  PRIMARY KEY (_id,_id_parent),
  FOREIGN KEY (_id) REFERENCES Category (_id),
  FOREIGN KEY (_id_parent) REFERENCES Category (_id)
);

CREATE UNIQUE INDEX AllParentsLevel ON AllParents
(
  _id,
  _id_parent,
  remote
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

CREATE TABLE Transact
(
  _id                  INTEGER PRIMARY KEY,
  _id_account          INTEGER NOT NULL,
  date_time            INTEGER NOT NULL,
  sum                  INTEGER NOT NULL,
  trend                INTEGER NOT NULL,
  _id_correlative_account INTEGER,
  correlative_sum      INTEGER,
  FOREIGN KEY (_id_account) REFERENCES Account (_id),
  FOREIGN KEY (_id_correlative_account) REFERENCES Account (_id)
);

CREATE TABLE Document
(
  _id                  INTEGER PRIMARY KEY,
  _id_transact         INTEGER,
  _id_category         INTEGER NOT NULL,
  _id_event            INTEGER,
  price                INTEGER,
  of                   REAL NOT NULL DEFAULT 1,
  of_id_unit           INTEGER NOT NULL,
  count                REAL NOT NULL,
  id_unit              INTEGER NOT NULL,
  FOREIGN KEY (_id_event) REFERENCES Event (_id),
  FOREIGN KEY (_id_category) REFERENCES Category (_id),
  FOREIGN KEY (_id_transact) REFERENCES Transact (_id)
);