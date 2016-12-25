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
import com.bloodliviykot.MyFin.DB.entities.Account;
import com.bloodliviykot.tools.Common.DateTime;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Shumeiko on 01.12.2016.
 */
@SuppressLint("ValidFragment")
public class DTransactionParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
  implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
  AdapterView.OnItemSelectedListener
{
  public class NotAccountException extends Exception {  }
  
  //!!! внимание, наследники DialogFragment должны иметь конструктор без параметров
  public DTransactionParams() throws NotAccountException
  {
    super(R.layout.d_transaction_params);
    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    Cursor cursor = oh.db.rawQuery(oh.getQuery(EQ.COUNT_CURRENCIES_ALL_ACCOUNTS), new String[]{});
    try
    {
      if(!cursor.moveToFirst() || (count_currencies = cursor.getInt(cursor.getColumnIndex("count"))) == 0)
        throw new NotAccountException();
    }
    finally
    {
      cursor.close();
    }
  }
  public DTransactionParams(DateTime date_time, Money total_amount) throws NotAccountException
  {
    this(); //Вызовем другой конструктор
    this.date_time    = date_time;
    this.total_amount = total_amount;
  }

  private EditText date, time;
  private EditText cost;
  private Spinner cost_currency_s; private static final String CURRENCY_SELECTED = "currency_selected";
  private TextView cost_currency_t;
  private LinearLayout another_layout;
  private LinearLayout linearLayout_gone;
  private TextView  another_cost;
  private Spinner another_cost_currency_s;
  private TextView another_cost_currency_t;
  private Spinner account_s; private static final String ACCOUNT_SELECTED = "account_selected"; private Integer account_selected = null;
  private AccountsItemAdapter account_adapter;
  private Button cancel, ok;

  private DateTime date_time; private static final String DATE_TIME = "date_time";
  private Money total_amount; private static final String TOTAL_AMOUNT = "total_amount";
  private MySQLiteOpenHelper oh;
  private Cursor c_currencies_all_acc, c_other_currencies, c_accounts;
  private int count_currencies;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Оплатить продукты");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
    (date = (EditText)v.findViewById(R.id.d_tran_params_date)).setOnClickListener(this);
    (time = (EditText)v.findViewById(R.id.d_tran_params_time)).setOnClickListener(this);
    (cost = (EditText)v.findViewById(R.id.d_tran_params_cost)).addTextChangedListener(new Common.MoneyWatcher(cost));
    cost_currency_s = (Spinner)v.findViewById(R.id.d_tran_params_cost_currency_s);
    cost_currency_t = (TextView)v.findViewById(R.id.d_tran_params_cost_currency_t);
    another_layout = (LinearLayout)v.findViewById(R.id.d_tran_params_another_Layout);
    linearLayout_gone = (LinearLayout)v.findViewById(R.id.d_tran_params_gone_Layout);
    another_cost = (TextView)v.findViewById(R.id.d_tran_params_another_cost);
    another_cost_currency_s = (Spinner)v.findViewById(R.id.d_tran_params_another_cost_currency_s);
    another_cost_currency_t = (TextView)v.findViewById(R.id.d_tran_params_another_cost_currency_t);
    account_s = (Spinner)v.findViewById(R.id.d_tran_params_account);
    (cancel = (Button)v.findViewById(R.id.d_tran_params_cancel)).setOnClickListener(this);
    (ok = (Button)v.findViewById(R.id.d_tran_params_ok)).setOnClickListener(this);
    c_currencies_all_acc = oh.db.rawQuery(oh.getQuery(EQ.CURRENCIES_ALL_ACCOUNTS), new String[]{"0"});
    cost_currency_s.setAdapter(new SimpleCursorAdapter(Common.context, R.layout.d_spinner_currency_item,
      c_currencies_all_acc, new String[]{"symbol"}, new int[]{R.id.d_spinner_currency_item}));
    account_adapter = new AccountsItemAdapter(null);
    account_s.setAdapter(account_adapter);
    
    if(savedInstanceState != null)
    {
      date_time = savedInstanceState.getParcelable(DATE_TIME);
      total_amount = savedInstanceState.getParcelable(TOTAL_AMOUNT);
      if(count_currencies != 1)
        cost_currency_s.setSelection(savedInstanceState.getInt(CURRENCY_SELECTED));
      account_selected = savedInstanceState.getInt(ACCOUNT_SELECTED);
    }
    else
    {
      cost.setOnFocusChangeListener(Common.getOnFocusChangeListener());
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
    }
    if(count_currencies == 1)
      prepareFieldsCosts();
    else
    {
      cost_currency_s.setOnItemSelectedListener(this);
    }
    date.setText(date_time.getSDate());
    time.setText(date_time.getSTime());
    cost.setText(total_amount.toString());
    return v;
  }
  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
  {
    if(parent == cost_currency_s)
    {
      prepareFieldsCosts();
      if(account_selected != null)
      {
        account_s.setSelection(account_selected);
        account_selected = null;
      }
    }
    if(parent == another_cost_currency_s)
      calcAnotherCurrency(id);
  }
  private void calcAnotherCurrency(Long _id_another)
  {
    //Пересчитаем по курсу
    another_cost.setText(_id_another.toString());
    //....
    
  }
  @Override
  public void onNothingSelected(AdapterView<?> parent)
  {   }
  
  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    outState.putParcelable(DATE_TIME, date_time);
    outState.putParcelable(TOTAL_AMOUNT, total_amount);
    if(count_currencies != 1)
      outState.putInt(CURRENCY_SELECTED, cost_currency_s.getSelectedItemPosition());
    outState.putInt(ACCOUNT_SELECTED, account_s.getSelectedItemPosition());
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
      dismiss();
    }
    else if(v == ok)
    {
      total_amount = new Money(cost.getText().toString());
      if(total_amount.equals(Money.NULL_MONEY))
      {
        Toast.makeText(Common.context, getString(R.string.err_not_set_total_amount), Toast.LENGTH_SHORT).show();
        return;
      }
      Bundle result_values = new Bundle();
      result_values.putParcelable("date_time", date_time);
      result_values.putParcelable("total_amount", total_amount);
      long id_currency;
      if(count_currencies == 1)
        id_currency = cost_currency_s.getSelectedItemId();
      else
        id_currency = c_currencies_all_acc.getLong(c_currencies_all_acc.getColumnIndex("_id"));
      result_values.putLong("id_currency", id_currency);
      result_values.putLong("id_account", account_s.getSelectedItemId());
      dismiss();
      handleResult(result_values);
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
    //Если валюта одна, то скрываем пункт в другой валюте,
    //  если валют две, то выводим общую стоимость в другой валюте и прячем спиннер другой валюты
    //  если валют три и более, то выводим общую стоимость в другой валюте и показываем спиннер другой валюты
    
    Long _id;
    //Если валют по счетам несколько, то для общей суммы выводим спиннер иначе текст с единственной валютой
    if(count_currencies == 1)
    {
      c_currencies_all_acc.moveToFirst();
      _id = c_currencies_all_acc.getLong(c_currencies_all_acc.getColumnIndex("_id"));
      cost_currency_s.setVisibility(View.GONE);
      cost_currency_t.setVisibility(View.VISIBLE);
      another_layout.setVisibility(View.GONE);
      if(linearLayout_gone != null)
        linearLayout_gone.setVisibility(View.VISIBLE);
      cost_currency_t.setText(c_currencies_all_acc.getString(c_currencies_all_acc.getColumnIndex("symbol")));
    }
    else
    {
      _id = cost_currency_s.getSelectedItemId();
      cost_currency_s.setVisibility(View.VISIBLE);
      cost_currency_t.setVisibility(View.GONE);
      another_layout.setVisibility(View.VISIBLE);
      if(linearLayout_gone != null)
        linearLayout_gone.setVisibility(View.GONE);
      c_other_currencies = c_currencies_all_acc = oh.db.rawQuery(oh.getQuery(EQ.CURRENCIES_ALL_ACCOUNTS),
        new String[]{_id.toString()});
      if(count_currencies == 2)
      {
        another_cost_currency_t.setVisibility(View.VISIBLE);
        another_cost_currency_s.setVisibility(View.GONE);
        if(c_other_currencies.moveToFirst())
          another_cost_currency_t.setText(c_other_currencies.getString(c_other_currencies.getColumnIndex("symbol")));
        calcAnotherCurrency(c_other_currencies.getLong(c_other_currencies.getColumnIndex("_id")));
      }
      else
      {
        another_cost_currency_t.setVisibility(View.GONE);
        another_cost_currency_s.setVisibility(View.VISIBLE);
        another_cost_currency_s.setAdapter(new SimpleCursorAdapter(Common.context, R.layout.d_spinner_currency_item,
          c_other_currencies, new String[]{"symbol"}, new int[]{R.id.d_spinner_currency_item}));
        another_cost_currency_s.setOnItemSelectedListener(this);
      }
    }
    c_accounts = oh.db.rawQuery(oh.getQuery(EQ.ACCOUNTS_FOR_CURRENCY),
      new String[]{_id.toString()});
    account_adapter.changeCursor(c_accounts);
  
  }
  
  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class AccountsItemAdapter
    extends SimpleCursorAdapter
  {
    public AccountsItemAdapter(Cursor cursor)
    {
      super(Common.context, R.layout.d_spinner_account, cursor, new String[]{},
        new int[]{R.id.d_spinner_account_item_image, R.id.d_spinner_account_item_name});
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      Account.E_IC_TYPE_RESOURCE icon = Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(
        (int)cursor.getLong(cursor.getColumnIndex("id_icon")));
      String name = cursor.getString(cursor.getColumnIndex("name"));
      
      //Сопоставляем
      ImageView iv_image  = (ImageView)view.findViewById(R.id.d_spinner_account_item_image);
      TextView tv_name    = (TextView)view.findViewById(R.id.d_spinner_account_item_name);
      iv_image.setImageResource(icon.R_drawable);
      tv_name.setText(name);
    }
    
  }

}
