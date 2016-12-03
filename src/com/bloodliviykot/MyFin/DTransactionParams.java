package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bloodliviykot.tools.Common.DateTime;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Shumeiko on 01.12.2016.
 */
@SuppressLint("ValidFragment")
public class DTransactionParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
  implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

  public DTransactionParams()
  {
    super(R.layout.d_transaction_params);
  }
  public DTransactionParams(DateTime date_time)
  {
    super(R.layout.d_transaction_params);
    this.date_time = date_time;

  }

  private EditText date, time;
  private EditText cost;
  private Spinner cost_currency_s;
  private TextView cost_currency_t;
  private TextView  another_cost;
  private Spinner another_cost_currency_s;
  private TextView another_cost_currency_t;
  private Spinner account;
  private Button cancel, ok;

  private DateTime date_time; private static final String DATE_TIME = "date_time";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Оплатит продукты");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
    (date = (EditText)v.findViewById(R.id.d_tran_params_date)).setOnClickListener(this);
    (time = (EditText)v.findViewById(R.id.d_tran_params_time)).setOnClickListener(this);
    cost = (EditText)v.findViewById(R.id.d_tran_params_cost);
    cost_currency_s = (Spinner)v.findViewById(R.id.d_tran_params_cost_currency_s);
    cost_currency_t = (TextView)v.findViewById(R.id.d_tran_params_cost_currency_t);
    another_cost = (TextView)v.findViewById(R.id.d_tran_params_another_cost);
    another_cost_currency_s = (Spinner)v.findViewById(R.id.d_tran_params_another_cost_currency_s);
    another_cost_currency_t = (TextView)v.findViewById(R.id.d_tran_params_another_cost_currency_t);
    account = (Spinner)v.findViewById(R.id.d_tran_params_account);
    (cancel = (Button)v.findViewById(R.id.d_tran_params_cancel)).setOnClickListener(this);
    (ok = (Button)v.findViewById(R.id.d_tran_params_ok)).setOnClickListener(this);

    if(savedInstanceState != null)
    {
      date_time = new DateTime(savedInstanceState.getLong(DATE_TIME));

    }

    date.setText(date_time.getSDate());
    time.setText(date_time.getSTime());

    return v;
  }

  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    outState.putLong(DATE_TIME, date_time.getTimeInMillis());

    super.onSaveInstanceState(outState);
  }

  @Override
  public void onClick(View v)
  {
    if(v == date)
    {
      DatePickerDialog dpd = new DatePickerDialog(v.getContext(), this, date_time.getYear(),
        date_time.getMonth(), date_time.getDayOfMonth());
      dpd.show();
    }
    else if(v == time)
    {
      TimePickerDialog tpd = new TimePickerDialog(v.getContext(), this, date_time.getHours(),
        date_time.getMinutes(), !date_time.isAMPMFormat());
      tpd.show();
    }
    else if(v == cancel)
    {

    }
    else if(v == ok)
    {

    }

  }
  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
  {
    date_time.setDT(year, monthOfYear, dayOfMonth, date_time.getHours(), date_time.getMinutes());
    date.setText(date_time.getSDate());
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute)
  {
    date_time.setDT(date_time.getYear(), date_time.getMonth(), date_time.getDayOfMonth(), hourOfDay, minute);
    time.setText(date_time.getSTime());
  }
}
