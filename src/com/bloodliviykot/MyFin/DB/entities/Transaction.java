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
    public static TYPE_TRANSACTION getTYPE_TRANSACTION(long id_db)
    {
      return TYPE_TRANSACTION.values()[(int)id_db];
    }
  }
  //Направление транзакции
  public enum TREND
  {
    CREDIT     (0), //расход
    DEBIT      (1), //Приход
    CONVERSION (2); //конверсия

    public final long id_db;
    private TREND(long id_db)
    {
      this.id_db = id_db;
    }
    public static TREND getTREND(long id_db)
    {
      return TREND.values()[(int)id_db];
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
  public TREND getTrend()
  {
    return trend;
  }
  public void setTrend(TREND trend) throws EntityException
  {
    if(trend == null)
      throw new EntityException();
    this.trend = trend;
  }
  public Account getCorrelativeAccount()
  {
    return correlative_account;
  }
  public void setCorrelativeAccount(Account correlative_account)
  {
    this.correlative_account = correlative_account;
  }
  public Money getCorrelativeSum()
  {
    return correlative_sum;
  }
  public void setCorrelativeSum(Money correlativeSum)
  {
    this.correlative_sum = correlativeSum;
  }

  public Transaction(Account account, DateTime date_time, TYPE_TRANSACTION type_transaction, Money sum,
                     TREND trend, Account correlative_account, Money correlative_sum) throws EntityException
  {
    if(account == null || account.getId() == 0 || date_time == null || type_transaction == null || sum == null ||
      trend == null || correlative_account != null && correlative_account.getId() == 0 || correlative_sum == null)
      throw new EntityException();
    this.account              = account            ;
    this.date_time            = date_time          ;
    this.sum                  = sum                ;
    this.trend                = trend              ;
    this.correlative_account  = correlative_account;
    this.correlative_sum      = correlative_sum    ;
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
    values.put("_id_account", account.getId());
    values.put("date_time", date_time.getTimeInMillis());
    values.put("sum", sum.getLongValue());
    values.put("trend", trend.id_db);
    if(correlative_account != null)
      values.put("_id_correlative_account", correlative_account.getId());
    if(correlative_sum != null)
      values.put("correlative_sum", correlative_sum.getLongValue());
    return null;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.account.getId(), account.getId(), "_id_account");
    compareInsert(values, original.date_time.getTimeInMillis(), date_time.getTimeInMillis(), "date_time");
    compareInsert(values, original.sum.getLongValue(), sum.getLongValue(), "sum");
    compareInsert(values, original.trend.id_db, trend.id_db, "trend");
    compareInsert(values, original.correlative_account != null ? original.correlative_account.getId() : null,
      correlative_account != null ? correlative_account.getId() : null, "_id_correlative_account");
    compareInsert(values, original.correlative_sum != null ? original.correlative_sum.getLongValue() : null,
      correlative_sum != null ? correlative_sum.getLongValue() : null, "correlative_sum");
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {
    this.account              = Account.getAccountFromId(cursor.getLong(cursor.getColumnIndex("_id_account")));
    this.date_time            = new DateTime(cursor.getLong(cursor.getColumnIndex("date_time")));
    this.sum                  = new Money(cursor.getLong(cursor.getColumnIndex("sum")));
    this.trend                = TREND.getTREND(cursor.getLong(cursor.getColumnIndex("trend")));
    if(!cursor.isNull(cursor.getColumnIndex("correlative_account")))
      this.correlative_account = Account.getAccountFromId(cursor.getLong(cursor.getColumnIndex("_id_correlative_account")));
    if(!cursor.isNull(cursor.getColumnIndex("correlative_sum")))
      this.correlative_sum     = new Money(cursor.getLong(cursor.getColumnIndex("correlative_sum")));
  }
  @Override
  protected void saveOriginal()
  {
    if(original == null)
      original = new Original();
    original.account             = account            ;
    original.date_time           = date_time          ;
    original.sum                 = sum                ;
    original.trend               = trend              ;
    original.correlative_account = correlative_account;
    original.correlative_sum     = correlative_sum    ;
  }

  //Поля записи
  private Account          account             ;
  private DateTime         date_time           ;
  private Money            sum                 ;
  private TREND            trend               ;
  private Account          correlative_account ;
  private Money            correlative_sum     ;

  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    Account          account             ;
    DateTime         date_time           ;
    Money            sum                 ;
    TREND            trend               ;
    Account          correlative_account ;
    Money            correlative_sum     ;
  }
  Original original;

}
