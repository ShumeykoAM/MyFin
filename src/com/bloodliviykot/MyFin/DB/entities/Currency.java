package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.R;
import com.bloodliviykot.tools.DataBase.Entity;

import java.io.Serializable;

/**
 * Created by Kot on 29.09.2016.
 */
public class Currency
  extends Entity
  implements Serializable
{
  /*"€""$""Ք"*/
  //!!!! Есть java.util.Currency, которая как раз определяет валюту

  //Иконки основных валют
  public enum E_IC_CURRENCY
  {
    RUB (0, R.drawable.ic_curr_rub ),
    USD (1, R.drawable.ic_curr_usd ),
    EUR (2, R.drawable.ic_curr_eur );

    public final int R_drawable;
    public final long id_db;
    public final int id;
    private E_IC_CURRENCY(long id_db, int R_drawable)
    {
      this.R_drawable = R_drawable;
      this.id_db = id_db;
      this.id = (int)id_db;
    }
    public static E_IC_CURRENCY getE_IC_TYPE_RESOURCE(long id_db)
    {
      return E_IC_CURRENCY.values()[(int)id_db];
    }
  }
  public Currency(String short_name, E_IC_CURRENCY icon) throws EntityException
  {
    if(short_name == null)
      throw new EntityException();
    setShort_name(short_name);
    setIcon(icon);
    is_added = false;
  }
  public Currency(long _id) throws EntityException
  {
    super(_id, EQ.CURRENCY);
  }
  public String getShort_name(){return short_name;}
  public void setShort_name(String short_name) throws EntityException
  {
    if(short_name == null)
      throw new EntityException();
    this.short_name = short_name;
    this.short_name_lower = short_name.toLowerCase();
  }
  public E_IC_CURRENCY getIcon(){return icon;}
  public void setIcon(E_IC_CURRENCY icon){this.icon = icon;}

  @Override
  public String getTableName()
  {
    return "Currency";
  }
  @Override
  protected ContentValues getContentValues()
  {
    ContentValues values = new ContentValues();
    values.put("short_name_lower", this.short_name_lower);
    values.put("short_name"      , this.short_name);
    if(this.icon != null)
      values.put("id_icon"       , this.icon.id_db);
    values.put("is_added"        , this.is_added ? 1 : 0);
    return values;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.short_name_lower, short_name_lower, "short_name_lower");
    compareInsert(values, original.short_name, short_name, "short_name");
    compareInsert(values, original.icon != null ? original.icon.id_db : null, icon != null ? icon.id_db : null, "id_icon");
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor)
  {
    this.short_name_lower = cursor.getString(cursor.getColumnIndex("short_name_lower"));
    this.short_name       = cursor.getString(cursor.getColumnIndex("short_name"));
    if(!cursor.isNull(cursor.getColumnIndex("id_icon")))
      this.icon = E_IC_CURRENCY.getE_IC_TYPE_RESOURCE(cursor.getLong(cursor.getColumnIndex("id_icon")));
    this.is_added         = cursor.getLong(cursor.getColumnIndex("is_added")) == 1;
  }
  @Override
  protected void saveOriginal()
  {
    if(original == null)
      original = new Original();
    original.short_name_lower = short_name_lower;
    original.short_name = short_name;
    original.icon = icon;
  }


  //Поля записи
  private String short_name_lower;
  private String short_name      ;
  private E_IC_CURRENCY icon     ;
  private boolean is_added       ;//это не пункт это кнопка для добавления новой валюты
  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    public String short_name_lower;
    public String short_name      ;
    public E_IC_CURRENCY icon     ;
  }
  Original original;

}
