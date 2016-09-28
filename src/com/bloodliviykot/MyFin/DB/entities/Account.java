package com.bloodliviykot.MyFin.DB.entities;

import com.bloodliviykot.MyFin.R;

import java.io.Serializable;

/**
 * Created by Kot on 25.09.2016.
 */
public class Account
  implements Serializable
{
  //Доступные иконки для счетов
  public enum E_IC_TYPE_RESOURCE
  {
    CASH       (0, R.drawable.ic_cash       ),
    DEBIT_CARD (1, R.drawable.ic_debet_card ),
    CREDIT_CARD(2, R.drawable.ic_credit_card),
    CONTRACT   (3, R.drawable.ic_contract   ),
    MONEY_BOX  (4, R.drawable.ic_money_box  ),
    SAFE       (5, R.drawable.ic_safe       );

    public final int R_drawable;
    public final int id_db;
    private E_IC_TYPE_RESOURCE(int id_db, int R_drawable)
    {
      this.R_drawable = R_drawable;
      this.id_db = id_db;
    }
    public static E_IC_TYPE_RESOURCE getE_IC_TYPE_RESOURCE(int id_db)
    {
      return E_IC_TYPE_RESOURCE.values()[id_db];
    }
  }

  private long  _id;
  private double balance;
  //private Currency currency; !!!! Есть java.util.Currency, которая как раз определяет валюту
  private String name;
  //private CoUser co_user;
  E_IC_TYPE_RESOURCE icon;

  public long getId(){return _id;}

  public double getBalance(){return balance;}
  public void setBalance(double balance){this.balance = balance;}

  public String getName(){return name;}
  public void setName(String name){this.name = name;}

  public E_IC_TYPE_RESOURCE getIcon(){return icon;}
  public void setIcon(E_IC_TYPE_RESOURCE icon){this.icon = icon;}

}
