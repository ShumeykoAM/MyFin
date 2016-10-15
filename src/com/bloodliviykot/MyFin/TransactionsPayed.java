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
import com.bloodliviykot.tools.Common.DateTime;
import com.bloodliviykot.tools.Common.Money;

/**
 * Created by Kot on 10.10.2016.
 */
public class TransactionsPayed
  extends Activity
{
  private MySQLiteOpenHelper oh;
  private ListView list_transactions;
  private Button button_create;

  private Cursor cursor;
  private SimpleCursorAdapter list_adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.transactions_payed);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_transactions = (ListView)findViewById(R.id.transactions_payed_listView);
    button_create = (Button)findViewById(R.id.transactions_payed_create);

    //list_accounts.setAdapter();
    cursor = oh.db.rawQuery(oh.getQuery(EQ.TRANSACTIONS), null);
    list_adapter = new TransactionPayedItemAdapter(R.layout.transactions_payed_item, cursor,
      new String[]{},
      new int[]{});
    list_adapter.changeCursor(cursor);
    list_transactions.setAdapter(list_adapter);
  }

  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class TransactionPayedItemAdapter
    extends SimpleCursorAdapter
  {

    public TransactionPayedItemAdapter(int layout, Cursor cursor, String[] from, int[] to)
    {
      super(Common.application_context, layout, cursor, from, to);

    }

    boolean b = true;
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      View item = view.findViewById(R.id.transaction_payed_item);
      View remittance = view.findViewById(R.id.transaction_payed_remittance);
      if(!cursor.isNull(cursor.getColumnIndex("co_user_name")))
      {
        item.setVisibility(View.GONE);
        remittance.setVisibility(View.VISIBLE);

      }
      else
      {
        item.setVisibility(View.VISIBLE);
        remittance.setVisibility(View.GONE);

        Account.E_IC_TYPE_RESOURCE icon = Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(
          (int)cursor.getLong(cursor.getColumnIndex("id_icon")));

        ImageView item_icon   = (ImageView)view.findViewById(R.id.transaction_payed_item_icon);
        TextView item_name    = (TextView)view.findViewById(R.id.transaction_payed_item_name);
        TextView item_sum     = (TextView)view.findViewById(R.id.transaction_payed_item_sum);
        TextView date_time    = (TextView)view.findViewById(R.id.transaction_payed_item_date_time);
        TextView contents     = (TextView)view.findViewById(R.id.transaction_payed_item_contents);
        ImageView image_trend = (ImageView)view.findViewById(R.id.transaction_payed_item_trend);

        item_icon.setImageResource(icon.R_drawable);
        item_name.setText(cursor.getString(cursor.getColumnIndex("account_name")));
        item_sum.setText(new Money(cursor.getLong(cursor.getColumnIndex("sum"))).toString());
        date_time.setText(new DateTime(cursor.getLong(cursor.getColumnIndex("date_time"))).getSDateTime());

      }

      /*
      Account.E_IC_TYPE_RESOURCE icon = Account.E_IC_TYPE_RESOURCE.getTYPE_TRANSACTION(
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
      */
    }

  }
}
