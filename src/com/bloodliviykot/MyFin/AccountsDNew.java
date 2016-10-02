package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.MyFin.DB.entities.Account;
import com.bloodliviykot.MyFin.DB.entities.Currency;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Shumeiko on 26.09.2016.
 */
@SuppressLint("ValidFragment")
public class AccountsDNew
  extends DialogFragment //!!! внимание, наследники DialogFragment должны иметь конструктор без параметров
  implements View.OnClickListener
{
  public interface I_ResultHandler
  {
    void resultHandler(Bundle result_values);
  }
  private Spinner icon;
  private EditText name, balance;
  private Spinner currency;
  private Button b_ok, b_cancel;

  private Account account;

  private Cursor cursor_currencies;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Создать счет");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(R.layout.accounts_d_new, null);
    icon = (Spinner)v.findViewById(R.id.accounts_d_new_icon);
    name = (EditText)v.findViewById(R.id.accounts_d_new_name);
    balance = (EditText)v.findViewById(R.id.accounts_d_new_balance);
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
    CurrencyAdapter adapter_currency = new CurrencyAdapter(cursor_currencies);
    currency.setAdapter(adapter_currency);
    Bundle params = getArguments();
    if(params.getString("Regime").equals("New"))
      try
      {
        account = new Account(new Currency(1), null, Account.E_IC_TYPE_RESOURCE.CASH, "", 0.0);

      } catch(Entity.EntityException e)
      {   }
    else
    {
      account = (Account)params.getSerializable("Account");
      icon.setSelection(account.getIcon().id);
      name.setText(account.getName());
      balance.setText(((Double)account.getBalance()).toString());
      setCurrencyCursorPositionFromId(account.getCurrency().getId());
      currency.setSelection(cursor_currencies.getPosition());
    }
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
        account.setBalance(Double.parseDouble(balance.getText().toString()));
        //account.setCurrency
        result_values.putSerializable("Account", account);
        try
        {
          ((I_ResultHandler)getActivity()).resultHandler(result_values);
        } catch(ClassCastException e)
        {
        }
        dismiss();
      }
    }
    else
      dismiss();
  }

  private boolean checkFields()
  {
    //icon.getSelectedItemPosition();
    if(name.getText().toString().equals(""))
    {
      Toast.makeText(GlobalWars.application_context, "Не задано имя счета!", Toast.LENGTH_SHORT).show();
      return false;
    }
    if(balance.getText().toString().equals(""))
    {
      Toast.makeText(GlobalWars.application_context, "Не задан баланс счета!", Toast.LENGTH_SHORT).show();
      return false;
    }
    int i = currency.getSelectedItemPosition();
    if(currency.getSelectedItemPosition() == 0)
    {
      Toast.makeText(GlobalWars.application_context, "Не задана валюта счета!", Toast.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }

  public class ImageAdapter
    extends ArrayAdapter<Account.E_IC_TYPE_RESOURCE>
  {
    ImageAdapter()
    {
      super(GlobalWars.application_context, R.layout.accounts_d_new_image_item, Account.E_IC_TYPE_RESOURCE.values());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(GlobalWars.application_context, R.layout.accounts_d_new_image_item, null);
      ImageView image = (ImageView)convertView.findViewById(R.id.accounts_d_new_image_item_icon);
      image.setImageResource(Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(position).R_drawable);
      image.setBackgroundColor(getResources().getColor(R.color.black));
      return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(GlobalWars.application_context, R.layout.accounts_d_new_image_item, null);
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
      super(GlobalWars.application_context, R.layout.accounts_d_new_currency_item,
        cursor, new String[]{},
        new int[]{R.id.accounts_d_new_currency_item_icon, R.id.accounts_d_new_currency_item_name});
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
      return prepareView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      return prepareView(position, convertView, parent);
    }
    private View prepareView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(GlobalWars.application_context, R.layout.accounts_d_new_currency_item, null);
      ImageView image = (ImageView)convertView.findViewById(R.id.accounts_d_new_currency_item_icon);
      TextView name = (TextView)convertView.findViewById(R.id.accounts_d_new_currency_item_name);
      Button another = (Button)convertView.findViewById(R.id.accounts_d_new_currency_item_button_another);
      if(cursor_currencies.moveToPosition(position))
      {
        if(cursor_currencies.getLong(cursor_currencies.getColumnIndex("is_added")) == 0)
        {
          if(!cursor_currencies.isNull(cursor_currencies.getColumnIndex("id_icon")))
          {
            image.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            another.setVisibility(View.INVISIBLE);
            image.setImageResource(Currency.E_IC_CURRENCY.getE_IC_TYPE_RESOURCE(cursor_currencies.getLong(cursor_currencies.getColumnIndex("id_icon"))).R_drawable);
            image.setBackgroundColor(getResources().getColor(R.color.black));
          }
          else
            image.setVisibility(View.INVISIBLE);
          name.setText(cursor_currencies.getString(cursor_currencies.getColumnIndex("short_name")));
        }
        else
        {
          image.setVisibility(View.INVISIBLE);
          name.setVisibility(View.INVISIBLE);
          another.setVisibility(View.VISIBLE);
          another.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
              currency.post(new Runnable(){
                @Override
                public void run()
                {
                  currency.setSelection(position, true);

                  //currency.performClick();
                }
              });

              CurrenciesDNew currenciesDNew = new CurrenciesDNew();
              currenciesDNew.show(getFragmentManager(), null);
            }
          });
        }
      }
      return convertView;
    }

  }

}
