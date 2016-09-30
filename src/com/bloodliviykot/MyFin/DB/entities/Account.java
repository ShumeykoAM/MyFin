package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.R;
import com.bloodliviykot.tools.DataBase.Entity;

import java.io.Serializable;

/**
 * Created by Kot on 25.09.2016.
 */
public class Account
  extends Entity
  implements Serializable
{
  //Доступные иконки для счетов
  public enum E_IC_TYPE_RESOURCE
  {
    CASH       (0, R.drawable.ic_cash       ),
    DEBIT_CARD (1, R.drawable.ic_debit_card ),
    CREDIT_CARD(2, R.drawable.ic_credit_card),
    CONTRACT   (3, R.drawable.ic_contract   ),
    MONEY_BOX  (4, R.drawable.ic_money_box  ),
    SAFE       (5, R.drawable.ic_safe       );

    public final int R_drawable;
    public final long id_db;
    public final int id;
    private E_IC_TYPE_RESOURCE(long id_db, int R_drawable)
    {
      this.R_drawable = R_drawable;
      this.id_db = id_db;
      this.id = (int)id_db;
    }
    public static E_IC_TYPE_RESOURCE getE_IC_TYPE_RESOURCE(long id_db)
    {
      return E_IC_TYPE_RESOURCE.values()[(int)id_db];
    }
  }

  public double getBalance(){return balance;}
  public void setBalance(double balance){this.balance = balance;}

  public String getName(){return name;}
  public void setName(String name){this.name = name;}

  public E_IC_TYPE_RESOURCE getIcon(){return icon;}
  public void setIcon(E_IC_TYPE_RESOURCE icon){this.icon = icon;}

  @Override
  public String getDataBaseName()
  {
    return null;
  }
  @Override
  public ContentValues getContentValues()
  {
    return null;
  }
  @Override
  protected void initFromCursor(Cursor cursor)
  {

  }

  //Поля записи
  private double balance;
  //private Currency currency;
  private String name;
  //private CoUser co_user;
  E_IC_TYPE_RESOURCE icon;
}
