package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

import java.util.Date;

/**
 * Created by Shumeiko on 01.12.2016.
 */
@SuppressLint("ValidFragment")
public class DTransactionParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
  implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
  public static final int YEAR_CORRECTOR = 1900;

  private EditText date, time;
  private EditText cost;
  private Spinner cost_currency_s;
  private TextView cost_currency_t;
  private TextView  another_cost;
  private Spinner another_cost_currency_s;
  private TextView another_cost_currency_t;
  private Spinner account;
  private Button cancel, ok;

  public DTransactionParams()
  {
    super(R.layout.d_transaction_params);
  }

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

    return v;
  }

  @Override
  public void onClick(View v)
  {
    if(v == date)
    {
      Date date = new Date(/*date_time*/);
      DatePickerDialog dpd = new DatePickerDialog(v.getContext(), this, date.getYear() + YEAR_CORRECTOR,
        date.getMonth(), date.getDate());
      dpd.show();
    }
    else if(v == time)
    {
      Date date = new Date(/*date_time*/);
      TimePickerDialog tpd = new TimePickerDialog(v.getContext(), this, date.getHours(),
        date.getMinutes(), false);
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
    /*
    Date date = new Date(date_time);
    date.setYear(year - YEAR_CORRECTOR);
    date.setMonth(monthOfYear);
    date.setDate(dayOfMonth);
    date_time = date.getTime();
    updateView();
    */
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute)
  {
    /*
    Date date = new Date(date_time);
    date.setHours(hourOfDay);
    date.setMinutes(minute);
    date_time = date.getTime();
    updateView();
    */
  }
}
