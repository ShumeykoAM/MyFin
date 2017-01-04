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
import com.bloodliviykot.MyFin.DB.entities.Transact;
import com.bloodliviykot.tools.Common.DateTime;
import com.bloodliviykot.tools.Common.Money;

/**
 * Created by Kot on 23.09.2016.
 */
public class Register
  extends Activity
{
  private MySQLiteOpenHelper oh;
  private ListView list_transactions;
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
    setContentView(R.layout.register);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_transactions = (ListView)findViewById(R.id.transactions_payed_listView);
    button_create = (Button)findViewById(R.id.transactions_payed_create);

    //list_accounts.setAdapter();
    cursor = oh.db.rawQuery(oh.getQuery(EQ.TRANSACTIONS), null);
    list_adapter = new TransactionPayedItemAdapter(R.layout.register_item, cursor,
      new String[]{},
      new int[]{});
    list_adapter.changeCursor(cursor);
    list_transactions.setAdapter(list_adapter);
  }

  public static class TransactionPayed
  {
    TransactionPayed(Cursor cursor)
    {
      _id       = cursor.getLong(cursor.getColumnIndex("_id"));
      date_time = new DateTime(cursor.getLong(cursor.getColumnIndex("date_time")));
      sum       = new Money(cursor.getLong(cursor.getColumnIndex("sum")));
      trend     = Transact.TREND.getTREND(cursor.getLong(cursor.getColumnIndex("trend")));
      account_icon = Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(cursor.getLong(cursor.getColumnIndex("account_icon")));
      account_name = cursor.getString(cursor.getColumnIndex("account_name"));
      symbol    = cursor.getString(cursor.getColumnIndex("symbol"));
      if(!cursor.isNull(cursor.getColumnIndex("correlative_sum")))
        correlative_sum = new Money(cursor.getLong(cursor.getColumnIndex("correlative_sum")));
      if(!cursor.isNull(cursor.getColumnIndex("co_user_name")))
        co_user_name = cursor.getString(cursor.getColumnIndex("co_user_name"));
      if(!cursor.isNull(cursor.getColumnIndex("corr_acc_icon")))
        corr_acc_icon = Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(cursor.getLong(cursor.getColumnIndex("corr_acc_icon")));
      if(!cursor.isNull(cursor.getColumnIndex("corr_acc_name")))
        corr_acc_name = cursor.getString(cursor.getColumnIndex("corr_acc_name"));
      if(!cursor.isNull(cursor.getColumnIndex("corr_co_user_name")))
        corr_co_user_name = cursor.getString(cursor.getColumnIndex("corr_co_user_name"));
      if(!cursor.isNull(cursor.getColumnIndex("corr_symbol")))
        corr_symbol = cursor.getString(cursor.getColumnIndex("corr_symbol"));
    }
    public long _id;
    public DateTime date_time;
    public Money sum;
    public Transact.TREND trend;
    public Account.E_IC_TYPE_RESOURCE account_icon;
    public String account_name;
    public String symbol;
    //nullable
    public Money correlative_sum;
    public String co_user_name;
    public Account.E_IC_TYPE_RESOURCE corr_acc_icon;
    public String corr_acc_name;
    public String corr_co_user_name;
    public String corr_symbol;
  }

  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class TransactionPayedItemAdapter
    extends SimpleCursorAdapter
  {

    public TransactionPayedItemAdapter(int layout, Cursor cursor, String[] from, int[] to)
    {
      super(Common.context, layout, cursor, from, to);

    }

    boolean b = true;
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      View item = view.findViewById(R.id.register_item);
      View remittance = view.findViewById(R.id.register_remittance);
      TransactionPayed tp = new TransactionPayed(cursor);
      String plus_minus = tp.trend == Transact.TREND.CREDIT || tp.trend == Transact.TREND.CONVERSION ? "-" : "+";
      ImageView icon_from;
      TextView name_from ;
      TextView sum_from  ;
      TextView currency_from;
      TextView date      ;
      TextView time      ;
      if(tp.trend == Transact.TREND.CONVERSION)
      {
        item.setVisibility(View.GONE);
        remittance.setVisibility(View.VISIBLE);
        icon_from     = (ImageView)view.findViewById(R.id.register_remittance_icon_from);
        name_from     = (TextView)view.findViewById(R.id.register_remittance_from);
        sum_from      = (TextView)view.findViewById(R.id.register_remittance_sum_from);
        currency_from = (TextView)view.findViewById(R.id.register_remittance_currency_from);
        date          = (TextView)view.findViewById(R.id.register_item_date);
        date          = (TextView)view.findViewById(R.id.register_remittance_date);
        time          = (TextView)view.findViewById(R.id.register_remittance_time);
        ImageView icon_to = (ImageView)view.findViewById(R.id.register_remittance_icon_to);
        TextView name_to = (TextView)view.findViewById(R.id.register_remittance_to);
        TextView sum_to = (TextView)view.findViewById(R.id.register_remittance_sum_to);
        TextView currency_to = (TextView)view.findViewById(R.id.register_remittance_currency_to);
        icon_to.setImageResource(tp.corr_co_user_name == null ? tp.corr_acc_icon.R_drawable : R.drawable.ic_group);
        name_to.setText(tp.corr_co_user_name == null ? tp.corr_acc_name : tp.corr_co_user_name);
        sum_to.setText("+" + tp.correlative_sum.toString());
        currency_to.setText(tp.corr_symbol);
      }
      else
      {
        item.setVisibility(View.VISIBLE);
        remittance.setVisibility(View.GONE);
        icon_from     = (ImageView)view.findViewById(R.id.register_item_icon);
        name_from     = (TextView)view.findViewById(R.id.register_item_name);
        sum_from      = (TextView)view.findViewById(R.id.register_item_sum);
        currency_from = (TextView)view.findViewById(R.id.register_item_currency);
        date          = (TextView)view.findViewById(R.id.register_item_date);
        time          = (TextView)view.findViewById(R.id.register_item_time);
        ImageView trend_icon = (ImageView)view.findViewById(R.id.register_item_trend);
        TextView content = (TextView)view.findViewById(R.id.register_item_contents);
        int R_drawable_trend_icon = tp.trend == Transact.TREND.CREDIT ? R.drawable.ic_credit : R.drawable.ic_debit;
        trend_icon.setImageResource(R_drawable_trend_icon);
        content.setText("Контент надо доделать");
      }
      icon_from.setImageResource(tp.co_user_name == null ? tp.account_icon.R_drawable : R.drawable.ic_group);
      name_from.setText(tp.co_user_name == null ? tp.account_name : tp.co_user_name);
      sum_from.setText(plus_minus + tp.sum.toString());
      currency_from.setText(tp.symbol);
      date.setText(tp.date_time.getSDate());
      time.setText(tp.date_time.getSTime());
    }

  }
}