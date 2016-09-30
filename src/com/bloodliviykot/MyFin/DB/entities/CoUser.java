package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Kot on 30.09.2016.
 */
public class CoUser
  extends Entity
{

  public CoUser(long _id) throws EntityException
  {
    super(_id, EQ.CO_USER);
  }
  @Override
  public String getTableName()
  {
    return null;
  }

  @Override
  protected ContentValues getContentValues()
  {
    return null;
  }

  @Override
  protected ContentValues getContentValuesChange()
  {
    return null;
  }

  @Override
  protected void initFromCursor(Cursor cursor)
  {

  }

  @Override
  protected void saveOriginal()
  {

  }
}
