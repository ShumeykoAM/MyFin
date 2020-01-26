package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Kot on 30.10.2016.
 */
public class Event
  extends Entity
{

  public static Event getEventFromId(long id)
  {
    return null;
  }

  @Override
  public String getTableName()
  {
    return null;
  }

  @Override
  protected ContentValues getContentValues() throws EntityException
  {
    return null;
  }

  @Override
  protected ContentValues getContentValuesChange()
  {
    return null;
  }

  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {

  }

  @Override
  protected void saveOriginal()
  {

  }
}
