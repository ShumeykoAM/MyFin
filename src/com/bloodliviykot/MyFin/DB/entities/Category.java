package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.tools.DataBase.Entity;

import java.io.Serializable;

/**
 * Created by Kot on 22.10.2016.
 */
public class Category
  extends Entity
  implements Serializable
{

  public Category getParent() throws EntityException
  {
    return _id_parent != null ? Category.getCategoryFromId(_id_parent) : null;
  }
  public void setParent(Category parent)
  {
    this._id_parent = parent.getId();
  }
  public Transaction.TREND getTrend()
  {
    return trend;
  }
  public void setTrend(Transaction.TREND trend) throws EntityException
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

  public Category(Category parent, Transaction.TREND trend, String name) throws EntityException
  {
    if(trend == null || name == null)
      throw new EntityException();
    if(parent != null)
      this._id_parent = parent.getId();
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
  protected ContentValues getContentValues() throws EntityException
  {
    ContentValues values = new ContentValues();
    if(_id_parent != null)
      values.put("_id_parent", _id_parent);
    values.put("trend", trend.id_db);
    values.put("name", name);
    values.put("name_lower_case", name_lower_case);
    return values;
  }

  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original._id_parent, _id_parent, "_id_parent");
    compareInsert(values, original.trend.id_db, trend.id_db, "trend");
    compareInsert(values, original.name, name, "name");
    compareInsert(values, original.name_lower_case, name_lower_case, "name_lower_case");
    return values;
  }

  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {
    if(!cursor.isNull(cursor.getColumnIndex("_id_parent")))
      this._id_parent = cursor.getLong(cursor.getColumnIndex("_id_parent"));
    this.trend = Transaction.TREND.getTREND(cursor.getLong(cursor.getColumnIndex("trend")));
    this.name  = cursor.getString(cursor.getColumnIndex("name"));
    this.name_lower_case = cursor.getString(cursor.getColumnIndex("name_lower_case"));
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

  private Long              _id_parent      ;  //Хранить ссылку на сам объект не будем так как это загрузит все дерево категорий
  private Transaction.TREND trend           ;
  private String            name            ;
  private String            name_lower_case ;

  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    Long              _id_parent      ;
    Transaction.TREND trend           ;
    String            name            ;
    String            name_lower_case ;
  }
  Original original;

}