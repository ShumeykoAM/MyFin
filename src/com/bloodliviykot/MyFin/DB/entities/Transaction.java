package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.DataBase.Entity;

import java.io.Serializable;

/**
 * Created by Kot on 05.10.2016.
 */
public class Transaction
  extends Entity
  implements Serializable
{
  //Типы транзакций
  public enum TYPE_TRANSACTION
  {
    PAYED      (0),
    PLANNED    (1),
    PERIODICAL (2);

    public final long id_db;
    private TYPE_TRANSACTION(long id_db)
    {
      this.id_db = id_db;
    }
    public static TYPE_TRANSACTION getE_IC_TYPE_RESOURCE(long id_db)
    {
      return TYPE_TRANSACTION.values()[(int)id_db];
    }
  }


  @Override
  public String getTableName()
  {
    return "Transaction";
  }

  @Override
  protected ContentValues getContentValues() throws EntityException
  {
    return null;
  }

  @Override
  protected ContentValues getContentValuesChange()
  {
    return null;
  }

  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {

  }

  @Override
  protected void saveOriginal()
  {

  }

  private Account account;
  private Currency currency;
  private long date_time; //Кол-во сек с 70го года
  private TYPE_TRANSACTION type_transaction;
  private Money sum;

}
