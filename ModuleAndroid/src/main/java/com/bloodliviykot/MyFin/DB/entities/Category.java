package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.Common;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.DataBase.I_Transaction;
import com.bloodliviykot.tools.DataBase.SQLTransaction;

/**
 * Created by Kot on 22.10.2016.
 */
public class Category
  extends Entity
{

  public Transact.TREND getTrend()
  {
    return trend;
  }
  public void setTrend(Transact.TREND trend) throws EntityException
  {
    if(trend == null)
      throw new EntityException();
    this.trend = trend;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name) throws EntityException
  {
    if(name == null)
      throw new EntityException();
    this.name = name;
    this.name_lower_case = name.toLowerCase();
  }

  public Category(Long _id_parent/*id непосредственного_предка*/, Transact.TREND trend, String name) throws EntityException
  {
    if(trend == null || name == null)
      throw new EntityException();
    this._id_parent = _id_parent;
    this.trend      = trend;
    this.name       = name;
    this.name_lower_case = name.toLowerCase();
  }
  private Category(long _id) throws EntityException
  {
    super(_id, EQ.CATEGORY);
  }
  public static Category getCategoryFromId(long _id) throws EntityException
  {
    return new Category(_id);
  }

  @Override
  public String getTableName()
  {
    return "Category";
  }

  @Override
  public boolean update()
  {
    Common.assertNeedTest();
    return new SQLTransaction(new I_Transaction()
    {
      @Override
      public boolean trnFunc()
      {
        boolean result = true;
        MySQLiteOpenHelper oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
        oh.db.delete("AllParents", "AllParents._id=", new String[]{new Long(getId()).toString()});
        if(_id_parent != null)
        {
          Cursor count = oh.db.rawQuery(oh.getQuery(EQ.COUNT_PARENTS), new String[]{_id_parent.toString()});
          Cursor ci = oh.db.rawQuery(oh.getQuery(EQ.INSERT_ALL_PARENT_RELATIVES),
            new String[]{new Long(getId()).toString(), _id_parent.toString()});
          result = count.getInt(count.getColumnIndex("count")) == ci.getCount();
        }
        if(result && _id_parent != null)
        {
          ContentValues cv = new ContentValues();
          cv.put("_id", getId());
          cv.put("_id_parent", _id_parent);
          cv.put("remote", 1);
          result = oh.db.insert("AllParents", null, new ContentValues()) != -1;
        }
        if(result)
          result = superUpdate();
        return result;
      }
    }).runTransaction();
  }
  private boolean superUpdate()
  {
    return super.update();
  }
  @Override
  public long insert()
  {
    final long[] _id_inserted = {-1};
    if(! new SQLTransaction(new I_Transaction()
    {
      @Override
      public boolean trnFunc()
      {
        boolean result = true;
        MySQLiteOpenHelper oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
        result = (_id_inserted[0] = superInsert()) != -1;
        if(result && _id_parent != null)
        {
          ContentValues cv = new ContentValues();
          cv.put("_id", getId());
          cv.put("_id_parent", _id_parent);
          cv.put("remote", 1);
          result = oh.db.insert("AllParents", null, cv) != -1;
        }
        return result;
      }
    }).runTransaction())
      _id_inserted[0] = -1;
    return _id_inserted[0];
  }
  private long superInsert()
  {
    return super.insert();
  }
  @Override
  protected ContentValues getContentValues() throws EntityException
  {
    ContentValues values = new ContentValues();
    values.put("trend", trend.id_db);
    values.put("name", name);
    values.put("name_lower_case", name_lower_case);
    return values;
  }

  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.trend.id_db, trend.id_db, "trend");
    compareInsert(values, original.name, name, "name");
    compareInsert(values, original.name_lower_case, name_lower_case, "name_lower_case");
    return values;
  }

  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {
    this.trend = Transact.TREND.getTREND(cursor.getLong(cursor.getColumnIndex("trend")));
    this.name  = cursor.getString(cursor.getColumnIndex("name"));
    this.name_lower_case = cursor.getString(cursor.getColumnIndex("name_lower_case"));
    MySQLiteOpenHelper oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    Cursor all_parents = oh.db.rawQuery(oh.getQuery(EQ.IMMEDIATE_PARENT), new String[]{new Long(getId()).toString()});
    if(all_parents.moveToFirst())
      this._id_parent = all_parents.getLong(all_parents.getColumnIndex("_id_parent"));
  }

  @Override
  protected void saveOriginal()
  {
    if(original == null)
      original = new Original();
    original._id_parent      = _id_parent     ;
    original.trend           = trend          ;
    original.name            = name           ;
    original.name_lower_case = name_lower_case;
  }

  private Long              _id_parent      ;  //непосредственного родителя
  private Transact.TREND    trend           ;
  private String            name            ;
  private String            name_lower_case ;

  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    Long           _id_parent      ;
    Transact.TREND trend           ;
    String         name            ;
    String         name_lower_case ;
  }
  Original original;

}
