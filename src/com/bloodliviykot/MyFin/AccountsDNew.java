package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
    icon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    ImageAdapter adapter = new ImageAdapter(GlobalWars.application_context);
    icon.setAdapter(adapter);
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
      //currency.setSelection();
    }
    return v;
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
    ImageAdapter(Context context)
    {
      super(context, R.layout.accounts_d_new_image_item, Account.E_IC_TYPE_RESOURCE.values());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(GlobalWars.application_context, R.layout.accounts_d_new_image_item, null);
      ImageView image = (ImageView)convertView.findViewById(R.id.account_item_currency);
      image.setImageResource(Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(position).R_drawable);
      image.setBackgroundColor(getResources().getColor(R.color.black));
      return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(GlobalWars.application_context, R.layout.accounts_d_new_image_item, null);
      ImageView image = (ImageView)convertView.findViewById(R.id.account_item_currency);
      image.setImageResource(Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(position).R_drawable);
      image.setBackgroundColor(getResources().getColor(R.color.black));
      return convertView;
    }

  }

}
