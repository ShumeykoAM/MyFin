package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.MyFin.DB.entities.Account;
import com.bloodliviykot.MyFin.DB.entities.Currency;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.widget.DialogFragmentEx;
import com.kot.myfin.R;

/**
 * Created by Kot on 23.09.2016.
 */
public class Accounts
  extends Activity
  implements DialogFragmentEx.I_ResultHandler<Bundle>, AdapterView.OnItemClickListener, View.OnClickListener
{
  private MySQLiteOpenHelper oh;
  private ListView list_accounts;
  private Button button_create;

  private Cursor cursor;
  private SimpleCursorAdapter list_adapter;
  
  private static boolean need_notifyDataSetInvalidated = false;
  public static void notifyAllDataSetInvalidated()
  {
    need_notifyDataSetInvalidated = true;
  }
  @Override
  protected void onResume()
  {
    super.onResume();
    if(need_notifyDataSetInvalidated && cursor != null && list_adapter != null)
    {
      need_notifyDataSetInvalidated = false;
      cursor.requery();
      list_adapter.notifyDataSetInvalidated();
    }
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.accounts);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_accounts = (ListView)findViewById(R.id.accounts_listView);
    button_create = (Button)findViewById(R.id.accounts_create);

    cursor = oh.db.rawQuery(oh.getQuery(EQ.ACCOUNTS), null);
    list_adapter = new AccountsItemAdapter(R.layout.accounts_item, cursor,
      new String[]{},
      new int[]{R.id.accounts_d_new_image_item_icon, R.id.account_item_name, R.id.account_item_balance, R.id.account_item_currency});
    list_adapter.changeCursor(cursor);
    list_accounts.setAdapter(list_adapter);
    list_accounts.setOnItemClickListener(this);

    button_create.setOnClickListener(this);
  }

  //Обработчик результата диалога AccountsDNew
  @Override
  public void resultHandler(int R_layout, Bundle result_values)
  {
    if(result_values.containsKey("Button") && result_values.getString("Button").equals("Ok"))
    {
      try
      {
        final Account account = Account.getAccountFromId(result_values.getLong("_id_Account"));
        if(cursor.requery())
        {
          list_accounts.post(new Runnable()
          {
            @Override
            public void run()
            {
              list_adapter.notifyDataSetInvalidated();
              for(boolean result = cursor.moveToFirst(); result; result = cursor.moveToNext())
                if(cursor.getLong(cursor.getColumnIndex("_id")) == account.getId())
                {
                  list_accounts.smoothScrollToPosition(cursor.getPosition());
                  break;
                }
            }
          });
        }
      } catch(Entity.EntityException e)
      {     }
    }
  }

  @Override
  public void onClick(View v)
  {
    DAccountsNew accounts_d_new = new DAccountsNew();
    Bundle params = new Bundle();
    params.putString("Regime", "New");
    accounts_d_new.setArguments(params);
    accounts_d_new.show(getFragmentManager(), null);
  }
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id)
  {
    DAccountsNew accounts_d_new = new DAccountsNew();
    Bundle params = new Bundle();
    params.putString("Regime", "Edit");
    params.putLong("_id_Account", id);
    accounts_d_new.setArguments(params);
    accounts_d_new.show(getFragmentManager(), null);
  }

  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class AccountsItemAdapter
    extends SimpleCursorAdapter
  {

    public AccountsItemAdapter(int layout, Cursor cursor, String[] from, int[] to)
    {
      super(Common.context, layout, cursor, from, to);

    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      try
      {
        Account.E_IC_TYPE_RESOURCE icon = Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(
          (int)cursor.getLong(cursor.getColumnIndex("id_icon")));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        Money balance = new Money(cursor.getLong(cursor.getColumnIndex("balance")));
        Currency currency = Currency.getCurrency(cursor.getLong(cursor.getColumnIndex("_id_currency")));
  
        //Сопоставляем
        ImageView iv_image  = (ImageView)view.findViewById(R.id.accounts_d_new_image_item_icon);
        TextView tv_name    = (TextView)view.findViewById(R.id.account_item_name);
        TextView tv_balance = (TextView)view.findViewById(R.id.account_item_balance);
        TextView tv_currency = (TextView)view.findViewById(R.id.account_item_currency);
        iv_image.setImageResource(icon.R_drawable);
        tv_name.setText(name);
        tv_balance.setText(balance.toString());
        tv_currency.setText(currency.getSymbol());
      } catch(Entity.EntityException e)
      {      }
    }

  }
}