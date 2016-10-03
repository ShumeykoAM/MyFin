package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.DataBase.I_Transaction;
import com.bloodliviykot.tools.DataBase.SQLTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kot on 30.09.2016.
 */
public class CoUser
  extends Entity
  implements Serializable, I_Transaction
{

  public CoUser(String name) throws EntityException
  {
    if(name == null)
      throw new EntityException();
  }
  public CoUser(long _id) throws EntityException
  {
    super(_id, EQ.CO_USER);
  }
  public String getName(){return name;}
  public void setName(String name) throws EntityException
  {
    if(name == null)
      throw new EntityException();
    this.name = name;
  }
  public AccountsCoUser getAccountsCoUser()
  {
    if(accounts == null)
      accounts = new AccountsCoUser();
     return accounts;
  }
  //Методы добавления счетов и удаления, в базе изменится после update
  public void addAccount(Account account)
  {
    if(for_add == null)
      for_add = new ArrayList<Account>();
    for_add.add(account);
  }
  public void removeAccount(Account account)
  {
    if(for_remove == null)
      for_remove = new ArrayList<Account>();
    for_remove.add(account);
  }

  @Override
  public String getTableName()
  {
    return "CoUser";
  }
  @Override
  protected ContentValues getContentValues()
  {
    ContentValues values = new ContentValues();
    values.put("name", this.name);
    return values;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.name, name, "name");
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor)
  {
    this.name = cursor.getString(cursor.getColumnIndex("name"));
  }
  @Override
  protected void saveOriginal()
  {
    if(original == null)
      original = new Original();
    original.name = name;
  }
  @Override
  public boolean update()
  {
    SQLTransaction sql_transaction = new SQLTransaction(this);
    return sql_transaction.runTransaction();
  }
  @Override
  public boolean trnFunc()
  {
    boolean result = true;
    if(result)
      for(Account account : for_add)
      {
        account.setCoUser(this);
        if(account.getId() != 0)
        {
          if(result = account.update())
            break;
        }
        else
          if(result = account.insert() != -1)
            break;
      }
    if(result)
      for(Account account : for_remove)
      {
        account.setCoUser(this);
        if(account.getId() != 0)
        {
          if(result = account.update())
            break;
        }
        else
        if(result = account.insert() != -1)
          break;
      }
    if(result)
      result = super.update();
    return result;
  }

  //Класс коллекция счетов сопользователя, счета подкачиваются из базы
  public class AccountsCoUser
    implements Iterable<Account>
  {
    public class IteratorAccount
      implements Iterator<Account>
    {
      private Cursor cursor;
      private boolean first = true;
      public IteratorAccount()
      {
        MySQLiteOpenHelper oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
        cursor = oh.db.rawQuery(oh.getQuery(EQ.USER_ACCOUNTS), new String[]{new Long(getId()).toString()});
      }
      @Override
      public boolean hasNext()
      {
        return !cursor.isLast();
      }
      @Override
      public Account next()
      {
        Account ret_value = null;
        boolean result = first ? cursor.moveToFirst() : cursor.moveToNext();
        if(result)
        {
          first = false;
          try
          {
            ret_value = new Account(cursor.getLong(cursor.getColumnIndex("_id")));
          } catch(EntityException e)
          {
            throw new IllegalStateException();
          }
        }
        else
          throw new IllegalStateException();
        return ret_value;
      }
      @Override
      public void remove()
      {
        throw new UnsupportedOperationException();
      }
    }

    @Override
    public Iterator<Account> iterator()
    {
      return new IteratorAccount();
    }
  }

  //Поля записи
  private String name;
  private AccountsCoUser accounts;  //--
  private List<Account> for_add;    //  | Работаем со списком счетов
  private List<Account> for_remove; //--

  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    String name;
  }
  Original original;

}
