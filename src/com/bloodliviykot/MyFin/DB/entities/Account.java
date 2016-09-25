package com.bloodliviykot.MyFin.DB.entities;

import com.bloodliviykot.MyFin.R;

/**
 * Created by Kot on 25.09.2016.
 */
public class Account
{
  //Доступные иконки для счетов
  public enum E_IC_TYPE_RESOURCE
  {
    DEBET_CARD (0, R.drawable.ic_credit_card),
    CREDIT_CARD(1, R.drawable.ic_debet_card ),
    CASH       (2, R.drawable.ic_cash       ),
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



}
