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
      saveOriginal();
    }
    else
      throw new EntityException();
  }

  //+//Возвращаем -1 если не удалось вставить, иначе id вставленной записи
  public long insert()
  {
    SQLiteDatabase db = MySQLiteOpenHelper.getMySQLiteOpenHelper().db;
    long _id_inserted = -1;
    try
    {
      _id_inserted = db.insert(getTableName(), null, getContentValues());
    } catch(EntityException e)
    {   }
    if(_id_inserted != -1)
    {
      _id = _id_inserted;
      saveOriginal();
    }
    return _id_inserted;
  };
  //+//Обновим в записи отличающиеся поля
  public boolean update()
  {
    boolean result = _id != 0;
    if(result)
    {
      ContentValues values = getContentValuesChange();
      if(values.size() > 0)
      {
        SQLiteDatabase db = MySQLiteOpenHelper.getMySQLiteOpenHelper().db;
        result = db.update(getTableName(), values, "_id=?", new String[]{((Long)_id).toString()}) == 1;
        if(result)
          saveOriginal();
      }
    }
    return result;
  }
  public boolean delete()
  {
    boolean result = _id != 0;
    if(result)
    {
      SQLiteDatabase db = MySQLiteOpenHelper.getMySQLiteOpenHelper().db;
      result = db.delete(getTableName(), "_id = ?", new String[]{((Long)_id).toString()}) == 1;
    }
    if(result)
      _id = 0;
    return result;
  }

  protected void compareInsert(ContentValues values, String original, String object, String name_field)
  {
    if(original != null && object == null)
      values.putNull(name_field);
    else if(original == null && object != null ||
      original != null && object != null &&
        original.compareTo(object)!=0)
      values.put(name_field, object);
  }
  protected void compareInsert(ContentValues values, Long original, Long object, String name_field)
  {
    if(original != null && object == null)
      values.putNull(name_field);
    else if(original == null && object != null ||
      original != null && object != null &&
        original.compareTo(object)!=0)
      values.put(name_field, object);
  }
  protected void compareInsert(ContentValues values, Double original, Double object, String name_field)
  {
    if(original != null && object == null)
      values.putNull(name_field);
    else if(original == null && object != null ||
      original != null && object != null &&
        original.compareTo(object)!=0)
      values.put(name_field, object);
  }

  public abstract String getTableName();
  protected abstract ContentValues getContentValues() throws EntityException;
  protected abstract ContentValues getContentValuesChange();
  protected abstract void initFromCursor(Cursor cursor) throws EntityException;
  protected abstract void saveOriginal();

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
