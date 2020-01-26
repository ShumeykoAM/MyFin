package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.MyFin.DB.entities.*;
import com.bloodliviykot.tools.Common.DateTime;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.DataBase.I_Transaction;
import com.bloodliviykot.tools.DataBase.SQLTransaction;
import com.bloodliviykot.tools.widget.DialogFragmentEx;
import com.kot.myfin.R;

import java.util.*;

/**
 * Created by Kot on 23.09.2016.
 */
public class Planned
  extends Activity
  implements View.OnClickListener, DialogFragmentEx.I_ResultHandler<Bundle>
{
  private Cursor cursor;
  private ListView list_planned;
  private SimpleCursorAdapter list_adapter;
  private MySQLiteOpenHelper oh;
  private Button add_debit;
  private Button add_credit;
  private Button pay_documents;
  private Set<Long> chose_state = new TreeSet<>();

  private static final String TAG_CHOOSES = "chooses";

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.planned);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_planned = (ListView)findViewById(R.id.planned_list_view);
    (add_debit = (Button)findViewById(R.id.planned_add_debit)).setOnClickListener(this);
    (add_credit = (Button)findViewById(R.id.planned_add_credit)).setOnClickListener(this);
    (pay_documents = (Button)findViewById(R.id.planned_add_pay)).setOnClickListener(this);

    cursor = oh.db.rawQuery(oh.getQuery(EQ.PLANNED), null);
    list_adapter = new PlannedItemAdapter(R.layout.planned_item, cursor,
      new String[]{},
      new int[]{R.id.planned_item_name, R.id.planned_item_count, R.id.planned_item_choose});
    list_adapter.changeCursor(cursor);
    list_planned.setAdapter(list_adapter);

  }

  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
    long chooses[] = new long[chose_state.size()];
    int i = 0;
    for(Long id : chose_state)
      chooses[i++] = id;
    outState.putLongArray(TAG_CHOOSES, chooses);
    super.onSaveInstanceState(outState);
  }
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState)
  {
    chose_state.clear();
    if(savedInstanceState != null && savedInstanceState.containsKey(TAG_CHOOSES))
      for(long id : savedInstanceState.getLongArray(TAG_CHOOSES))
        chose_state.add(id);
  }

  public static class Chooses
    implements Parcelable
  {
    public final long _id;
    public final double count;
    public final Unit unit;
    public Chooses(long _id, double count, Unit unit)
    {
      this._id = _id;
      this.count = count;
      this.unit = unit;
    }

    //Parcelable
    public Chooses(Parcel in) throws Entity.EntityException
    {
      this._id = in.readLong();
      this.count = in.readDouble();
      this.unit = Unit.getUnitFromId(in.readLong());
    }
    @Override
    public int describeContents()
    {
      return 0;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
      public Chooses createFromParcel(Parcel in)
      {
        try
        {
          return new Chooses(in);
        } catch(Entity.EntityException e)
        {
          return null;
        }
      }
      public Chooses[] newArray(int size)
      {
        return new Chooses[size];
      }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
      dest.writeLong(_id);
      dest.writeDouble(count);
      dest.writeLong(unit.getId());
    }
  }

  @Override
  public void onClick(View v)
  {
    if(v == add_credit)
    {
      Intent intent = new Intent(Common.context, ChooseCategories.class);
      intent.putExtra("TREND", Transact.TREND.CREDIT);
      ArrayList<Chooses> chooses = new ArrayList<Chooses>();
      for(boolean cursor_status = cursor.moveToFirst(); cursor_status; cursor_status = cursor.moveToNext())
        try
        {
          chooses.add(new Chooses(cursor.getLong(cursor.getColumnIndex("CATEGORY_ID")), cursor.getDouble(cursor.getColumnIndex("count")), Unit.getUnitFromId(cursor.getLong(cursor.getColumnIndex("id_unit")))));
        } catch(Entity.EntityException e)
        {     }
      intent.putParcelableArrayListExtra("chooses", chooses);
      startActivityForResult(intent, R.layout.choose_categories_list); //Запуск активности с onActivityResult
    }
    else if(v == add_debit)
    {

    }
    else if(v == pay_documents)
    {
      if(chose_state.size() == 0)
      {
        Toast.makeText(Common.context, getString(R.string.err_cart_is_empty), Toast.LENGTH_SHORT).show();
        return;
      }
      DTransactionParams transaction_params = null;
      try
      {
        transaction_params = new DTransactionParams(new DateTime(), new Money(0));
      } catch(DTransactionParams.NotAccountException e)
      {
        //Значит нету ни одного счета
        
      }
      Bundle params = new Bundle();
      params.putBoolean("IsNew", true);
      transaction_params.setArguments(params);
      transaction_params.show(getFragmentManager(), null);
    }
  }

  private Document getPlanDocumentFromCategoryId(Long _id_category) throws Entity.EntityException
  {
    Cursor cursor = oh.db.rawQuery(oh.getQuery(EQ.PLANNED_FROM_CATEGORY), new String[]{_id_category.toString()});
    if(cursor.moveToFirst())
      return Document.getDocumentFromId(cursor.getLong(cursor.getColumnIndex("_id")));
    else
      throw new Entity.EntityException();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    switch(requestCode)
    {
      case R.layout.choose_categories_list:
        if(resultCode == RESULT_OK)
        {
          Map<Long, Pair<Double, Unit>> chooses = new TreeMap<>();
          ArrayList<Planned.Chooses> arr_chooses = data.getExtras().getParcelableArrayList("chooses");
          for(Planned.Chooses choose : arr_chooses)
            chooses.put(choose._id, new Pair<>(choose.count, choose.unit));
          //Перезапишем запланированные документы
          long _id;
          for(boolean cursor_status = cursor.moveToFirst(); cursor_status; cursor_status = cursor.moveToNext())
            if(!chooses.containsKey(_id = cursor.getLong(cursor.getColumnIndex("CATEGORY_ID"))))
              try
              {
                getPlanDocumentFromCategoryId(_id).delete();
              } catch(Entity.EntityException e)
              {   }
          for(Map.Entry<Long, Pair<Double, Unit>> entry : chooses.entrySet() )
          {
            try
            {
              getPlanDocumentFromCategoryId(entry.getKey()).setCount(entry.getValue().first).
                setUnit(entry.getValue().second).update();
            } catch(Entity.EntityException e)
            {
              try
              {
                Document document = new Document(null, Category.getCategoryFromId(entry.getKey()), null,
                  null, null, null, entry.getValue().first, entry.getValue().second);
                document.insert();
              } catch(Entity.EntityException e1)
              {     }
            }
          }
          cursor.requery();
          list_adapter.notifyDataSetInvalidated();
        }
        break;
    }
  }

  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class PlannedItemAdapter
    extends SimpleCursorAdapter
  {

    public PlannedItemAdapter(int layout, Cursor cursor, String[] from, int[] to)
    {
      super(Common.context, layout, cursor, from, to);

    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      long _id = cursor.getLong(cursor.getColumnIndex("_id"));
      TextView name = (TextView)view.findViewById(R.id.planned_item_name);
      TextView count = (TextView)view.findViewById(R.id.planned_item_count);
      view.setOnTouchListener(new OnTouch(count, _id));
      TextView unit = (TextView)view.findViewById(R.id.planned_item_unit);
      ImageView choose = (ImageView)view.findViewById(R.id.planned_item_choose);
      choose.setOnClickListener(new OnChoose(_id, choose));
      if(chose_state.contains(_id))
        choose.setImageResource(R.drawable.ic_basket_full);
      else
        choose.setImageResource(R.drawable.ic_basket_empty);
      String s_name = cursor.getString(cursor.getColumnIndex("name"));
      Double d_count = new Double(cursor.getDouble(cursor.getColumnIndex("count")));
      Unit u_unit = null;
      try
      {
        u_unit = Unit.getUnitFromId(cursor.getLong(cursor.getColumnIndex("id_unit")));
      } catch(Entity.EntityException e)
      {   }

      name.setText(s_name);
      count.setText(d_count.toString());
      unit.setText(u_unit.getName());
      CountUnitParams count_unit_params = new CountUnitParams(_id, s_name, d_count, u_unit);
      count.setOnClickListener(count_unit_params);
      unit.setOnClickListener(count_unit_params);
    }
    private class OnTouch
      implements View.OnTouchListener
    {
      Float start = null;
      double cur_count;
      TextView count;
      long _id;
      OnTouch(TextView count, long _id)
      {
        this.count = count;
        this._id = _id;
      }
      @Override
      public boolean onTouch(View v, MotionEvent event)
      {
        switch(event.getAction())
        {
          case MotionEvent.ACTION_DOWN:
            start = event.getX();
            cur_count = Double.parseDouble(count.getText().toString());
            break;
          case MotionEvent.ACTION_MOVE:
            if(start != null)
            {
              int sub = (int)((event.getX() - start) / 50);
              count.setText(new Double(cur_count + sub).toString());
            }
            break;
          case MotionEvent.ACTION_UP:
            start = null;
            try
            {
              if(Document.getDocumentFromId(_id).setCount(Double.parseDouble(count.getText().toString())).update())
              {
                cursor.requery();
                list_adapter.notifyDataSetInvalidated();
              }
            } catch(Entity.EntityException e)
            {
              e.printStackTrace();
            }
            break;
        }
        return true;
      }
    }
    private class OnChoose
      implements View.OnClickListener
    {
      private long _id;
      private ImageView iv;
      public OnChoose(long _id, ImageView iv)
      {
        this._id = _id;
        this.iv = iv;
      }
      @Override
      public void onClick(View v)
      {
        if(chose_state.contains(_id))
        {
          chose_state.remove(_id);
          iv.setImageResource(R.drawable.ic_basket_empty);
        }
        else
        {
          chose_state.add(_id);
          iv.setImageResource(R.drawable.ic_basket_full);
        }
      }
    }
    private class CountUnitParams
      implements View.OnClickListener
    {
      private long _id;
      private String s_name;
      private Double d_count;
      private Unit u_unit;

      private int cursor_position;
      private String name;
      public CountUnitParams(long _id, String s_name, Double d_count, Unit u_unit)
      {
        this._id = _id;
        this.s_name = s_name;
        this.d_count = d_count;
        this.u_unit = u_unit;
      }
      @Override
      public void onClick(View v)
      {
        if(cursor.moveToPosition(cursor_position))
        {
          DDocumentParams document_params = new DDocumentParams();
          Bundle params = new Bundle();
          params.putLong(DDocumentParams.ID, _id);
          document_params.setArguments(params);
          document_params.show(getFragmentManager(), null);
        }
      }
    }
  }
  @Override
  public void resultHandler(int R_layout, Bundle result_values)
  {
    if(R_layout == R.layout.d_choose_category_params)
    {
      Planned.Chooses result_count_unit = result_values.getParcelable("count_unit");
      Pair<Double, Unit> count_unit = new Pair<>(result_count_unit.count, result_count_unit.unit);
      try
      {
        if(Document.getDocumentFromId(result_count_unit._id).setCount(result_count_unit.count).setUnit(result_count_unit.unit).update())
        {
          cursor.requery();
          list_adapter.notifyDataSetInvalidated();
        }
        Document document = Document.getDocumentFromId(result_count_unit._id);
      } catch(Entity.EntityException e)
      {      }
    }
    else if(R_layout == R.layout.d_transaction_params)
    {
      final DateTime date_time = result_values.getParcelable("date_time");
      final Money total_amount = result_values.getParcelable("total_amount");
      Long id_currency = result_values.getLong("id_currency");
      final Long id_account = result_values.getLong("id_account");
      if(new SQLTransaction(new I_Transaction(){
        @Override
        public boolean trnFunc()
        {
          boolean result = true;
          try
          {
            Account account;
            Transact transact = new Transact(account = Account.getAccountFromId(id_account), date_time, Transact.TYPE_TRANSACTION.PAYED,
              total_amount, Transact.TREND.CREDIT, null, null);
            result = transact.insert() != -1;
            for(Long _id : chose_state)
              result = result && Document.getDocumentFromId(_id).setTransact(transact).update();
            result = result && account.setBalance(account.getBalance().sub(total_amount)).update();
          } catch(Entity.EntityException e)
          {
            result = false;
          }
          if(result)
          {
            chose_state.clear();
            cursor.requery();
            list_adapter.notifyDataSetInvalidated();
          }
          return result;
        }
      }).runTransaction())
      {
        Register.notifyAllDataSetInvalidated();
        Accounts.notifyAllDataSetInvalidated();
      }
      
    }
    
  }
}