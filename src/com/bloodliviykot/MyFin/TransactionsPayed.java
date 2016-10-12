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

/**
 * Created by Kot on 10.10.2016.
 */
public class TransactionsPayed
  extends Activity
{
  private MySQLiteOpenHelper oh;
  private ListView list_accounts;
  private Button button_create;

  private Cursor cursor;
  private SimpleCursorAdapter list_adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.transactions_payed);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_accounts = (ListView)findViewById(R.id.transactions_payed_listView);
    button_create = (Button)findViewById(R.id.transactions_payed_create);

    //list_accounts.setAdapter();
    cursor = oh.db.rawQuery(oh.getQuery(EQ.ACCOUNTS), null);
    list_adapter = new TransactionPayedItemAdapter(R.layout.transactions_payed, cursor,
      new String[]{},
      new int[]{R.id.accounts_d_new_image_item_icon, R.id.account_item_name, R.id.account_item_balance});
    list_adapter.changeCursor(cursor);

  }

  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class TransactionPayedItemAdapter
    extends SimpleCursorAdapter
  {

    public TransactionPayedItemAdapter(int layout, Cursor cursor, String[] from, int[] to)
    {
      super(Common.application_context, layout, cursor, from, to);

    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      Account.E_IC_TYPE_RESOURCE icon = Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(
        (int)cursor.getLong(cursor.getColumnIndex("id_icon")));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      double balance = cursor.getDouble(cursor.getColumnIndex("balance"));

      //Сопоставляем
      ImageView iv_image  = (ImageView)view.findViewById(R.id.accounts_d_new_image_item_icon);
      TextView tv_name    = (TextView)view.findViewById(R.id.account_item_name);
      TextView tv_balance = (TextView)view.findViewById(R.id.account_item_balance);
      iv_image.setImageResource(icon.R_drawable);
      tv_name.setText(name);
      tv_balance.setText(new Double(balance).toString());
    }

  }
}
