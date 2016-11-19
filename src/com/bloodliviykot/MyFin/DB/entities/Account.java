package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.R;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Kot on 25.09.2016.
 */
public class Account
  extends Entity
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

  private Fields f;
  private Fields original;
  private void init()
  {
    f = new Fields();
    original = new Fields();
  }

  public Currency getCurrency(){return f.currency;}
  public void setCurrency(Currency currency) throws EntityException
  {
    if(currency == null || currency.getId() == 0)
      throw new EntityException();
    f.currency = currency;
  }
  public CoUser getCoUser(){return f.co_user;}
  public void setCoUser(CoUser co_user){f.co_user = co_user;}
  public E_IC_TYPE_RESOURCE getIcon(){return f.icon;}
  public void setIcon(E_IC_TYPE_RESOURCE icon) throws EntityException
  {
    if(icon == null)
      throw new EntityException();
    f.icon = icon;
  }
  public String getName(){return f.name;}
  public void setName(String name) throws EntityException
  {
    if(name == null)
      throw new EntityException();
    f.name = name;
  }
  public Money getBalance(){return f.balance;}
  public void setBalance(Money balance){f.balance = balance;}

  public Account(Currency currency, CoUser co_user, E_IC_TYPE_RESOURCE icon, String name, Money balance) throws EntityException
  {
    init();
    if(currency == null || currency.getId() == 0 || icon == null || name == null || balance == null)
      throw new EntityException();
    f.currency = currency;
    f.co_user  = co_user ;
    f.icon     = icon    ;
    f.name     = name    ;
    f.balance  = balance ;
  }
  private Account(long _id) throws EntityException
  {
    super(_id, EQ.ACCOUNT);
  }
  public static Account getAccountFromId(long _id) throws EntityException
  {
    return new Account(_id);
  }

  @Override
  public String getTableName()
  {
    return "Account";
  }
  @Override
  protected ContentValues getContentValues() throws EntityException
  {
    ContentValues values = new ContentValues();
    if(f.currency.getId() == 0)
      throw new EntityException();
    values.put("_id_currency" , f.currency.getId());
    if(f.co_user != null)
      values.put("_id_co_user", f.co_user.getId());
    values.put("id_icon"      , f.icon.id_db     );
    values.put("name"         , f.name           );
    values.put("balance"      , f.balance.getLongValue());
    return values;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.currency.getId(), f.currency.getId(), "_id_currency");
    compareInsert(values, original.co_user != null ? original.co_user.getId() : null, f.co_user != null ? f.co_user.getId() : null, "_id_co_user");
    compareInsert(values, original.icon.id_db, f.icon.id_db, "id_icon");
    compareInsert(values, original.name, f.name    , "name");
    compareInsert(values, original.balance.getLongValue(), f.balance.getLongValue() , "balance");
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {
    init();
    f.currency = Currency.getCurrency(cursor.getLong(cursor.getColumnIndex("_id_currency")));
    if(!cursor.isNull(cursor.getColumnIndex("_id_co_user")))
      f.co_user = CoUser.getCoUserFromId(cursor.getLong(cursor.getColumnIndex("_id_co_user")));
    f.icon = E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(cursor.getLong(cursor.getColumnIndex("id_icon")));
    f.name = cursor.getString(cursor.getColumnIndex("name"));
    f.balance = new Money(cursor.getLong(cursor.getColumnIndex("balance")));
  }
  @Override
  protected void saveOriginal()
  {
    original.currency = f.currency;
    original.co_user  = f.co_user ;
    original.icon     = f.icon    ;
    original.name     = f.name    ;
    original.balance  = f.balance ;
  }

  //Поля записи
  public static class Fields
    implements Parcelable
  {
    public Currency           currency;
    public CoUser             co_user;
    public E_IC_TYPE_RESOURCE icon;
    public String             name;
    public Money              balance;

    // Parcelable
    @Override
    public int describeContents()
    { return 0; }
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
      //dest.writeParcelable(currency);
    }
  }

}
