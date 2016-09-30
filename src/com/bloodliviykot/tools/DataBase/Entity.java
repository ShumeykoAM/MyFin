package com.bloodliviykot.tools.DataBase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;

/**
 * Created by Shumeiko on 30.09.2016.
 */
public abstract class Entity
{
  private long _id;

  //boolean select(int _id); наверное у сущности должен быть конструктор, который кидает EntityException
  public long insert()// 0 если не удалось вставить, иначе id вставленной записи
  {
    SQLiteDatabase db = MySQLiteOpenHelper.getMySQLiteOpenHelper().db;
    long _id = db.insert(getDataBaseName(), null, getContentValues());
    return _id;
  };
  public abstract String getDataBaseName();
  public abstract ContentValues getContentValues();

  public boolean update()
  {
    SQLiteDatabase db = MySQLiteOpenHelper.getMySQLiteOpenHelper().db;

    return true;
  }
  public boolean delete()
  {
    return true;
  }
  public long getId()
  {
    return _id;
  }

  public static class EntityException
    extends Exception
  {

  }
}
