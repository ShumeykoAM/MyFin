package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.MyFin.DB.entities.Account;

/**
 * Created by Kot on 23.09.2016.
 */
public class Accounts
  extends Activity
{
  private MySQLiteOpenHelper oh;
  private ListView list_accounts;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.accounts);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_accounts = (ListView)findViewById(R.id.accounts_listView);
    //list_accounts.setAdapter();
    Cursor cursor = oh.db.rawQuery(oh.getQuery(EQ.ACCOUNTS), null);
    SimpleCursorAdapter list_adapter = new AccountsItemAdapter(R.layout.accounts_item, cursor,
      new String[]{},
      new int[]{R.id.account_item_image, R.id.account_item_name, R.id.account_item_balance});

    list_adapter.changeCursor(cursor);
    list_accounts.setAdapter(list_adapter);
  }
  @Override
  protected void onPause()
  {
    super.onPause();

  }
  @Override
  protected void onResume()
  {
    super.onResume();
  }


  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class AccountsItemAdapter
    extends SimpleCursorAdapter
  {

    public AccountsItemAdapter(int layout, Cursor cursor, String[] from, int[] to)
    {
      super(GlobalWars.application_context, layout, cursor, from, to);

    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      Account.E_IC_TYPE_RESOURCE icon = Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(
        (int)cursor.getLong(cursor.getColumnIndex("id_icon")));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      double balance = cursor.getDouble(cursor.getColumnIndex("balance"));

      //Сопоставляем
      ImageView iv_image  = (ImageView)view.findViewById(R.id.account_item_image);
      TextView tv_name    = (TextView)view.findViewById(R.id.account_item_name);
      TextView tv_balance = (TextView)view.findViewById(R.id.account_item_balance);
      iv_image.setImageResource(icon.R_drawable);
      tv_name.setText(name);
      tv_balance.setText(new Double(balance).toString());
    }

  }
}