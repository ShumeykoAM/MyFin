package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
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

  public Currency getCurrency(){return currency;}
  public void setCurrency(Currency currency) throws EntityException
  {
    if(currency == null)
      throw new EntityException();
    this.currency = currency;
  }
  public CoUser getCoUser(){return co_user;}
  public void setCoUser(CoUser co_user){this.co_user = co_user;}
  public E_IC_TYPE_RESOURCE getIcon(){return icon;}
  public void setIcon(E_IC_TYPE_RESOURCE icon) throws EntityException
  {
    if(icon == null)
      throw new EntityException();
    this.icon = icon;
  }
  public String getName(){return name;}
  public void setName(String name) throws EntityException
  {
    if(name == null)
      throw new EntityException();
    this.name = name;
  }
  public double getBalance(){return balance;}
  public void setBalance(double balance){this.balance = balance;}

  public Account(Currency currency, CoUser co_user, E_IC_TYPE_RESOURCE icon, String name, double balance) throws EntityException
  {
    if(currency == null || icon == null || name == null)
      throw new EntityException();
    this.currency = currency;
    this.co_user  = co_user ;
    this.icon     = icon    ;
    this.name     = name    ;
    this.balance  = balance ;
  }
  public Account(long _id) throws EntityException
  {
    super(_id, EQ.ACCOUNT);
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
    if(currency.getId() == 0)
      throw new EntityException();
    values.put("_id_currency" , this.currency.getId());
    if(this.co_user != null)
      values.put("_id_co_user", this.co_user.getId());
    values.put("id_icon"      , this.icon.id_db     );
    values.put("name"         , this.name           );
    values.put("balance"      , this.balance        );
    return values;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.currency.getId(), currency.getId(), "_id_currency");
    compareInsert(values, original.co_user != null ? original.co_user.getId() : null, co_user != null ? co_user.getId() : null, "_id_co_user");
    compareInsert(values, original.icon.id_db, icon.id_db, "id_icon");
    compareInsert(values, original.name    , name    , "name");
    compareInsert(values, original.balance , balance , "balance");
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {
    this.currency = new Currency(cursor.getLong(cursor.getColumnIndex("_id_currency")));
    if(!cursor.isNull(cursor.getColumnIndex("_id_co_user")))
      this.co_user = new CoUser(cursor.getLong(cursor.getColumnIndex("_id_co_user")));
    this.icon = E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(cursor.getLong(cursor.getColumnIndex("id_icon")));
    this.name = cursor.getString(cursor.getColumnIndex("name"));
    this.balance = cursor.getDouble(cursor.getColumnIndex("balance"));
  }
  @Override
  protected void saveOriginal()
  {
    if(original == null)
      original = new Original();
    original.currency = currency;
    original.co_user  = co_user ;
    original.icon     = icon    ;
    original.name     = name    ;
    original.balance  = balance ;
  }

  //Поля записи
  private Currency           currency;
  private CoUser             co_user ;
  private E_IC_TYPE_RESOURCE icon    ;
  private String             name    ;
  private double             balance ;

  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    Currency           currency;
    CoUser             co_user ;
    E_IC_TYPE_RESOURCE icon    ;
    String             name    ;
    double             balance ;
  }
  Original original;

}
