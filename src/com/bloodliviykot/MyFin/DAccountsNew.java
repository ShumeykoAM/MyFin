package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.MyFin.DB.entities.Account;
import com.bloodliviykot.MyFin.DB.entities.Currency;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Shumeiko on 26.09.2016.
 */
@SuppressLint("ValidFragment")
public class DAccountsNew
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
  implements View.OnClickListener
{
  private Spinner icon;
  private EditText name, balance;
  private Spinner currency;
  private Button b_ok, b_cancel;

  private Account account;

  private Cursor cursor_currencies;
  private CurrencyAdapter adapter_currency;
  private boolean regime_new;

  public DAccountsNew()
  {
    super(R.layout.d_accounts_new);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Создать счет");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
    icon = (Spinner)v.findViewById(R.id.accounts_d_new_icon);
    name = (EditText)v.findViewById(R.id.accounts_d_new_name);
    (balance = (EditText)v.findViewById(R.id.accounts_d_new_balance)).setOnFocusChangeListener(Common.getOnFocusChangeListener());
    currency = (Spinner)v.findViewById(R.id.accounts_d_new_currency);
    b_ok     = (Button)v.findViewById(R.id.accounts_d_new_ok);     b_ok.setOnClickListener(this);
    b_cancel = (Button)v.findViewById(R.id.accounts_d_new_cansel); b_cancel.setOnClickListener(this);
    //Зададим программно, а то в редакторе плохо выглядит
    icon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT));
    ImageAdapter adapter_icon = new ImageAdapter();
    icon.setAdapter(adapter_icon);
    MySQLiteOpenHelper oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    cursor_currencies = oh.db.rawQuery(oh.getQuery(EQ.CURRENCIES), null);
    adapter_currency = new CurrencyAdapter(cursor_currencies);
    currency.setAdapter(adapter_currency);
    Bundle params = getArguments();
    if(params.getString("Regime").equals("New"))
      try
      {
        regime_new = true;
        account = new Account(Currency.getCurrency(1), null, Account.E_IC_TYPE_RESOURCE.CASH, "", new Money(0));
      } catch(Entity.EntityException e)
      {   }
    else
    {
      regime_new = false;
      try
      {
        account = Account.getAccountFromId(params.getLong("_id_Account"));
      } catch(Entity.EntityException e)
      {     }
      icon.setSelection(account.getIcon().id);
      name.setText(account.getName());
      balance.setText(account.getBalance().toString());
      setCurrencyCursorPositionFromId(account.getCurrency().getId());
      currency.setSelection(cursor_currencies.getPosition());
    }
    //Клавиатуру для конкретного view можно корректно вызвать только так
    name.post(new Runnable(){
      @Override
      public void run()
      {
        InputMethodManager imm = (InputMethodManager)
          name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
        name.requestFocus();
      }
    });
    balance.addTextChangedListener(new Common.MoneyWatcher(balance));
    return v;
  }
  private void setCurrencyCursorPositionFromId(long _id_currency)
  {
    for(boolean result = cursor_currencies.moveToFirst(); result; result = cursor_currencies.moveToNext())
    {
      if(cursor_currencies.getLong(cursor_currencies.getColumnIndex("_id")) == _id_currency)
        break;
    }
  }

  @Override
  public void onClick(View v)
  {
    if(v == b_ok)
    {
      if(checkFields())
      {
        Bundle result_values = new Bundle();
        result_values.putString("Button", "Ok");
        try
        {
          account.setIcon(Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(icon.getSelectedItemPosition()));
          account.setName(name.getText().toString());
        }
        catch(Entity.EntityException ee)
        {   }
        account.setBalance(new Money(balance.getText().toString()));
        cursor_currencies.moveToPosition(currency.getSelectedItemPosition());
        try
        {
          account.setCurrency(Currency.getCurrency(cursor_currencies.getLong(cursor_currencies.getColumnIndex("_id"))));
        } catch(Entity.EntityException e)
        {   }
        if( (regime_new ? account.insert() == -1 : !account.update()) )
        {
          Toast.makeText(Common.context, "Ошибка", Toast.LENGTH_SHORT).show();
          return;
        }
        result_values.putLong("_id_Account", account.getId());
        dismiss();
        handleResult(result_values);
      }
    }
    else
      dismiss();
  }

  private boolean checkFields()
  {
    if(name.getText().toString().equals(""))
    {
      Toast.makeText(Common.context, "Не задано имя счета!", Toast.LENGTH_SHORT).show();
      return false;
    }
    if(balance.getText().toString().equals(""))
    {
      Toast.makeText(Common.context, "Не задан баланс счета!", Toast.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }

  public class ImageAdapter
    extends ArrayAdapter<Account.E_IC_TYPE_RESOURCE>
  {
    ImageAdapter()
    {
      super(Common.context, R.layout.d_accounts_new_image_item, Account.E_IC_TYPE_RESOURCE.values());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(Common.context, R.layout.d_accounts_new_image_item, null);
      ImageView image = (ImageView)convertView.findViewById(R.id.accounts_d_new_image_item_icon);
      image.setImageResource(Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(position).R_drawable);
      image.setBackgroundColor(getResources().getColor(R.color.black));
      return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(Common.context, R.layout.d_accounts_new_image_item, null);
      ImageView image = (ImageView)convertView.findViewById(R.id.accounts_d_new_image_item_icon);
      image.setImageResource(Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(position).R_drawable);
      image.setBackgroundColor(getResources().getColor(R.color.black));
      return convertView;
    }

  }

  public class CurrencyAdapter
    extends SimpleCursorAdapter
  {
    CurrencyAdapter(Cursor cursor)
    {
      super(Common.context, R.layout.d_accounts_new_currency_item,
        cursor, new String[]{},
        new int[]{R.id.accounts_d_new_currency_item_symbol, R.id.accounts_d_new_currency_item_full_name});
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(Common.context, R.layout.d_accounts_new_currency_item, null);
      TextView symbol = (TextView)convertView.findViewById(R.id.accounts_d_new_currency_item_symbol);
      TextView full_name = (TextView)convertView.findViewById(R.id.accounts_d_new_currency_item_full_name);
      cursor_currencies.moveToPosition(position);
      full_name.setText(cursor_currencies.getString(cursor_currencies.getColumnIndex("full_name")));
      symbol.setText(cursor_currencies.getString(cursor_currencies.getColumnIndex("symbol")));
      return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(Common.context, R.layout.d_accounts_new_currency_item, null);
      TextView symbol = (TextView)convertView.findViewById(R.id.accounts_d_new_currency_item_symbol);
      TextView full_name = (TextView)convertView.findViewById(R.id.accounts_d_new_currency_item_full_name);
      cursor_currencies.moveToPosition(position);
      full_name.setVisibility(View.GONE);
      symbol.setText(cursor_currencies.getString(cursor_currencies.getColumnIndex("symbol")));
      return convertView;
    }

  }

}
