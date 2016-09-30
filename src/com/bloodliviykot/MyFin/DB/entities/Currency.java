package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import com.bloodliviykot.MyFin.R;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Kot on 29.09.2016.
 */
public class Currency
  extends Entity
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
    public final int id_db;
    private E_IC_CURRENCY(int id_db, int R_drawable)
    {
      this.R_drawable = R_drawable;
      this.id_db = id_db;
    }
    public static E_IC_CURRENCY getE_IC_TYPE_RESOURCE(int id_db)
    {
      return E_IC_CURRENCY.values()[id_db];
    }
  }

  public Currency(String short_name, E_IC_CURRENCY icon)
  {
    setShort_name(short_name);
    setIcon(icon);
  }
  public String getShort_name(){return short_name;}
  public void setShort_name(String short_name)
  {
    this.short_name = short_name;
    this.short_name_lover = short_name.toLowerCase();
  }
  public E_IC_CURRENCY getIcon(){return icon;}
  public void setIcon(E_IC_CURRENCY icon){this.icon = icon;}

  @Override
  public String getDataBaseName()
  {
    return "Currency";
  }

  @Override
  public ContentValues getContentValues()
  {
    ContentValues values = new ContentValues();
    values.put("short_name_lower", this.short_name_lover);
    values.put("short_name"      , this.short_name);
    if(this.icon != null)
      values.put("id_icon"       , this.icon.id_db);
    return values;
  }


  //Поля записи
  private String short_name_lover;
  private String short_name;
  private E_IC_CURRENCY icon;

}
