package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.bloodliviykot.tools.Common.DateTime;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Shumeiko on 01.12.2016.
 */
@SuppressLint("ValidFragment")
public class DTransactionParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
  implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

  //!!! внимание, наследники DialogFragment должны иметь конструктор без параметров
  public DTransactionParams()
  {
    super(R.layout.d_transaction_params);
  }
  public DTransactionParams(DateTime date_time, Money total_amount)
  {
    super(R.layout.d_transaction_params);
    this.date_time    = date_time;
    this.total_amount = total_amount;
  }

  private EditText date, time;
  private EditText cost;
  private Spinner cost_currency_s;
  private TextView cost_currency_t;
  private LinearLayout another_layout;
  private TextView  another_cost;
  private Spinner another_cost_currency_s;
  private TextView another_cost_currency_t;
  private Spinner account;
  private Button cancel, ok;

  private DateTime date_time; private static final String DATE_TIME = "date_time";
  private Money total_amount; private static final String TOTAL_AMOUNT = "total_amount";
  MySQLiteOpenHelper oh;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Оплатит продукты");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
    (date = (EditText)v.findViewById(R.id.d_tran_params_date)).setOnClickListener(this);
    (time = (EditText)v.findViewById(R.id.d_tran_params_time)).setOnClickListener(this);
    (cost = (EditText)v.findViewById(R.id.d_tran_params_cost)).addTextChangedListener(new Common.MoneyWatcher(cost));
    cost_currency_s = (Spinner)v.findViewById(R.id.d_tran_params_cost_currency_s);
    cost_currency_t = (TextView)v.findViewById(R.id.d_tran_params_cost_currency_t);
    another_layout = (LinearLayout)v.findViewById(R.id.d_tran_params_another_Layout);
    another_cost = (TextView)v.findViewById(R.id.d_tran_params_another_cost);
    another_cost_currency_s = (Spinner)v.findViewById(R.id.d_tran_params_another_cost_currency_s);
    another_cost_currency_t = (TextView)v.findViewById(R.id.d_tran_params_another_cost_currency_t);
    account = (Spinner)v.findViewById(R.id.d_tran_params_account);
    (cancel = (Button)v.findViewById(R.id.d_tran_params_cancel)).setOnClickListener(this);
    (ok = (Button)v.findViewById(R.id.d_tran_params_ok)).setOnClickListener(this);
    cost.setOnFocusChangeListener(Common.getOnFocusChangeListener());

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    if(savedInstanceState != null)
    {
      date_time = savedInstanceState.getParcelable(DATE_TIME);
      total_amount = savedInstanceState.getParcelable(TOTAL_AMOUNT);
    }

    date.setText(date_time.getSDate());
    time.setText(date_time.getSTime());

    cost.setText(total_amount.toString());

    prepareFieldsCosts();

    //Клавиатуру для конкретного view можно корректно вызвать только так
    cost.post(new Runnable(){
      @Override
      public void run()
      {
        InputMethodManager imm = (InputMethodManager)
          cost.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(cost, InputMethodManager.SHOW_IMPLICIT);
        cost.requestFocus();
      }
    });
    return v;
  }

  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    outState.putParcelable(DATE_TIME, date_time);
    outState.putParcelable(TOTAL_AMOUNT, total_amount);
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

  private void prepareFieldsCosts()
  {
    //Если валют по счетам несколько, то для общей суммы выводим спиннер иначе текст с единственной валютой
    //  если счетов вообще нет, то сразу надо вывести окно для ввода нового счета
    Cursor cursor_currencies_of_acc = oh.db.rawQuery(oh.getQuery(EQ.CURRENCIES_OF_ACCOUNTS), new String[]{"0"});
    int count_currencies = cursor_currencies_of_acc.getCount();
    if(count_currencies == 0)
    {

    }
    else if(count_currencies == 1)
    {
      cost_currency_s.setVisibility(View.GONE);
      another_layout.setVisibility(View.GONE);

    }
    else if(count_currencies == 2)
    {
      cost_currency_t.setVisibility(View.GONE);


    }
    else
    {
      Cursor cursor_another_of_acc = oh.db.rawQuery(oh.getQuery(EQ.CURRENCIES_OF_ACCOUNTS), new String[]{"0"});
      cost_currency_t.setVisibility(View.GONE);

    }



    //Если валюта одна, то скрываем пункт в другой валюте,
    //  если валют две, то выводим общую стоимость в другой валюте и прячем спиннер другой валюты
    //  если валют три и более, то выводим общую стоимость в другой валюте и показываем спиннер другой валюты

  }


}
