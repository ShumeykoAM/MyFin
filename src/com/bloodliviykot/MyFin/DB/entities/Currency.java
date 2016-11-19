package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Kot on 29.09.2016.
 */
public class Currency
  extends Entity
{
  /*"€""$""Ք"*/
  //!!!! Есть java.util.Currency, которая как раз определяет валюту

  public Currency(String cod_ISO, long primary, String full_name, String symbol) throws EntityException
  {
    if(cod_ISO == null || full_name == null || symbol == null)
      throw new EntityException();
    this.cod_ISO   = cod_ISO  ;
    this.primary   = primary  ;
    this.full_name = full_name;
    this.symbol    = symbol   ;
  }
  private Currency(long _id) throws EntityException
  {
    super(_id, EQ.CURRENCY);
  }
  public static Currency getCurrency(long _id) throws EntityException
  {
    return new Currency(_id);
  }
  public static Currency getCurrency(String cod_ISO) throws EntityException
  {
    MySQLiteOpenHelper oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    Cursor cursor = oh.db.rawQuery(oh.getQuery(EQ.CURRENCY_COD_ISO), new String[]{cod_ISO});
    if(!cursor.moveToFirst())
      throw new EntityException();
    return new Currency(cursor.getLong(cursor.getColumnIndex("_id")));
  }

  public String getCod_ISO(){return cod_ISO;}
  public void setCod_ISO(String cod_ISO) throws EntityException
  {
    if(cod_ISO == null)
      throw new EntityException();
    this.cod_ISO = cod_ISO;
  }
  public long getPrimary(){return primary;}
  public void setPrimary(long primary){this.primary = primary;}
  public String getFullName(){return full_name;}
  public void setFullName(String full_name) throws EntityException
  {
    if(full_name == null)
      throw new EntityException();
    this.full_name = full_name;
  }
  public String getSymbol()
  {
    return symbol;
  }
  public void setSymbol(String symbol) throws EntityException
  {
    if(symbol == null)
      throw new EntityException();
    this.symbol = symbol;
  }

  @Override
  public String getTableName()
  {
    return "Currency";
  }
  @Override
  protected ContentValues getContentValues()
  {
    ContentValues values = new ContentValues();
    values.put("cod_ISO"  , this.cod_ISO  );
    values.put("prim"     , this.primary  );
    values.put("full_name", this.full_name);
    values.put("symbol"   , this.symbol   );
    return values;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.cod_ISO  , cod_ISO  , "cod_ISO"  );
    compareInsert(values, original.primary  , primary  , "prim"     );
    compareInsert(values, original.full_name, full_name, "full_name");
    compareInsert(values, original.symbol   , symbol   , "symbol"   );
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor)
  {
    this.cod_ISO   = cursor.getString(cursor.getColumnIndex("cod_ISO"));
    this.primary   = cursor.getLong(cursor.getColumnIndex("prim"));
    this.full_name = cursor.getString(cursor.getColumnIndex("full_name"));
    this.symbol    = cursor.getString(cursor.getColumnIndex("symbol"));
  }
  @Override
  protected void saveOriginal()
  {
    if(original == null)
      original = new Original();
    original.cod_ISO   = cod_ISO  ;
    original.primary   = primary  ;
    original.full_name = full_name;
    original.symbol    = symbol;
  }

  //Поля записи
  private String cod_ISO  ;
  private long   primary ;
  private String full_name;
  private String symbol;

  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    public String cod_ISO  ;
    public long   primary ;
    public String full_name;
    public String symbol;
  }
  Original original;

}
