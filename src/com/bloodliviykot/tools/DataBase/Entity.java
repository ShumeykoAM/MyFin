package com.bloodliviykot.tools.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;

/**
 * Created by Shumeiko on 30.09.2016.
 */
public abstract class Entity
{
  protected Entity(){}
  //+//Подкачаем сущность из базы
  protected Entity(long _id, EQ eq) throws EntityException
  {
    MySQLiteOpenHelper oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    Cursor cursor = oh.db.rawQuery(oh.getQuery(eq), new String[]{new Long(_id).toString()});
    if(cursor.moveToFirst())
    {
      this._id = cursor.getLong(cursor.getColumnIndex("_id"));
      initFromCursor(cursor);
    }
    else
      throw new EntityException();
  }

  //+//Возвращаем -1 если не удалось вставить, иначе id вставленной записи
  public long insert()
  {
    SQLiteDatabase db = MySQLiteOpenHelper.getMySQLiteOpenHelper().db;
    long _id = db.insert(getDataBaseName(), null, getContentValues());
    return _id;
  };
  //-//Обновим в записи отличающиеся поля
  public boolean update()
  {
    SQLiteDatabase db = MySQLiteOpenHelper.getMySQLiteOpenHelper().db;

    return true;
  }
  public boolean delete()
  {
    return true;
  }

  public abstract String getDataBaseName();
  public abstract ContentValues getContentValues();
  protected abstract void initFromCursor(Cursor cursor);

  public long getId()
  {
    return _id;
  }

  private long _id;

  public static class EntityException
    extends Exception
  {

  }
}
