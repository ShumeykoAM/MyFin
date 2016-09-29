package com.bloodliviykot.MyFin.DB.entities;

import com.bloodliviykot.MyFin.R;

/**
 * Created by Kot on 29.09.2016.
 */
public class Currency
{
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

  private long  _id;
  String short_name;
  E_IC_CURRENCY icon;

  public long getId(){return _id;}
  public E_IC_CURRENCY getIcon(){return icon;}
  public void setIcon(E_IC_CURRENCY icon){this.icon = icon;}

}
