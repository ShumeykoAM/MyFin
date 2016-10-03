package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Kot on 02.10.2016.
 */
@SuppressLint("ValidFragment")
public class CurrenciesDNew
  extends DialogFragment //!!! внимание, наследники DialogFragment должны иметь конструктор без параметров
  implements View.OnClickListener
{
  public interface I_ResultHandlerCurrenciesDNew
  {
    void resultHandler(Bundle result_values);
  }

  private EditText code;
  private Button b_ok, b_cancel;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Добавить валюту");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(R.layout.currensies_d_new, null);
    code = (EditText)v.findViewById(R.id.currencies_d_new_code_currency);
    b_ok = (Button)v.findViewById(R.id.currencies_d_new_ok);
    b_cancel = (Button)v.findViewById(R.id.currencies_d_new_cancel);
    b_ok.setOnClickListener(this);
    b_cancel.setOnClickListener(this);
    return v;
  }

  @Override
  public void onClick(View v)
  {
    if(v == b_ok)
    {
      Bundle result_values = new Bundle();
      result_values.putLong("_id", 2);

      boolean need_call_activity = true;
      try
      {
        Fragment target = getTargetFragment();
        if(target != null)
        {
          ((I_ResultHandlerCurrenciesDNew)target).resultHandler(result_values);
          need_call_activity = false;
        }
      }catch(ClassCastException e2)
      {      }
      if(need_call_activity)
        try
        {
          Activity a = getActivity();
          ((I_ResultHandlerCurrenciesDNew)getActivity()).resultHandler(result_values);
        } catch(ClassCastException e1)
        {      }
      dismiss();
    }
    else
      dismiss();
  }




}
