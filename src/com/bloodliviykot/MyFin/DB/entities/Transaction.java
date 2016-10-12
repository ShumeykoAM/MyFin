package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.tools.Common.DateTime;
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

  public Account getAccount()
  {
    return account;
  }
  public void setAccount(Account account) throws EntityException
  {
    if(account != null && account.getId() == 0)
      throw new EntityException();
    this.account = account;
  }
  public DateTime getDateTime()
  {
    return date_time;
  }
  public void setDateTime(DateTime date_time) throws EntityException
  {
    if(date_time == null)
      throw new EntityException();
    this.date_time = date_time;
  }
  public TYPE_TRANSACTION getTypeTransaction()
  {
    return type_transaction;
  }
  public void setTypeTransaction(TYPE_TRANSACTION type_transaction) throws EntityException
  {
    if(type_transaction == null)
      throw new EntityException();
    this.type_transaction = type_transaction;
  }
  public Money getSum()
  {
    return sum;
  }
  public void setSum(Money sum) throws EntityException
  {
    if(sum == null)
      throw new EntityException();
    this.sum = sum;
  }

  public Transaction(Account account, DateTime date_time, TYPE_TRANSACTION type_transaction, Money sum) throws EntityException
  {
    if(account != null && account.getId() == 0 || date_time == null || type_transaction == null || sum == null)
      throw new EntityException();
    this.account          = account         ;
    this.date_time        = date_time       ;
    this.type_transaction = type_transaction;
    this.sum              = sum             ;
  }
  private Transaction(long _id) throws EntityException
  {
    super(_id, EQ.TRANSACTION);
  }
  public static Transaction getTransactionFromId(long _id) throws EntityException
  {
    return new Transaction(_id);
  }

  @Override
  public String getTableName()
  {
    return "Transact";
  }
  @Override
  protected ContentValues getContentValues() throws EntityException
  {
    ContentValues values = new ContentValues();
    if(account.getId() == 0)
      throw new EntityException();
    if(account != null)
      values.put("_id_account", account.getId());
    values.put("date_time", date_time.getTimeInMillis());
    values.put("type_transaction", type_transaction.id_db);
    values.put("sum", sum.getLongValue());
    return null;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.account != null ? original.account.getId() : null, account != null ? account.getId() : null, "_id_account");
    compareInsert(values, original.date_time.getTimeInMillis(), date_time.getTimeInMillis(), "date_time");
    compareInsert(values, original.type_transaction.id_db, type_transaction.id_db, "type_transaction");
    compareInsert(values, original.sum.getLongValue(), sum.getLongValue(), "sum");
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {
    if(!cursor.isNull(cursor.getColumnIndex("_id_account")))
      this.account          = Account.getAccountFromId(cursor.getLong(cursor.getColumnIndex("_id_account")));
    this.date_time        = new DateTime(cursor.getLong(cursor.getColumnIndex("date_time")));
    this.type_transaction = TYPE_TRANSACTION.getE_IC_TYPE_RESOURCE(cursor.getLong(cursor.getColumnIndex("type_transaction")));
    this.sum              = new Money(cursor.getLong(cursor.getColumnIndex("sum")));
  }
  @Override
  protected void saveOriginal()
  {
    original.account          = account         ;
    original.date_time        = date_time       ;
    original.type_transaction = type_transaction;
    original.sum              = sum             ;
  }

  //Поля записи
  private Account          account         ;
  private DateTime         date_time       ;
  private TYPE_TRANSACTION type_transaction;
  private Money            sum             ;

  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    Account          account         ;
    DateTime         date_time       ;
    TYPE_TRANSACTION type_transaction;
    Money            sum             ;
  }
  Original original;

}
