CREATE TABLE Account (
  Cyrrency_id integer(10) NOT NULL, 
  PRIMARY KEY (Cyrrency_id), 
  FOREIGN KEY(Cyrrency_id) REFERENCES Cyrrency(_id) ON UPDATE Cascade ON DELETE Restrict);
CREATE TABLE TypeResource (
  );
CREATE TABLE CoUser (
  );
CREATE TABLE Cyrrency (
  _id  INTEGER NOT NULL PRIMARY KEY);
