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
import com.bloodliviykot.MyFin.DB.entities.Category;
import com.bloodliviykot.MyFin.DB.entities.Document;
import com.bloodliviykot.MyFin.DB.entities.Transact;
import com.bloodliviykot.MyFin.DB.entities.Unit;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

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
  private Set<Long> chose_state = new TreeSet<>();

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.planned);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_planned = (ListView)findViewById(R.id.planned_list_view);
    (add_debit = (Button)findViewById(R.id.planned_add_debit)).setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        DDocumentParams document_params = new DDocumentParams();
        Bundle params = new Bundle();
        params.putString("Regime", "New");
        document_params.setArguments(params);
        document_params.show(getFragmentManager(), null);
      }
    });
    (add_credit = (Button)findViewById(R.id.planned_add_credit)).setOnClickListener(this);


    cursor = oh.db.rawQuery(oh.getQuery(EQ.PLANNED), null);
    list_adapter = new PlannedItemAdapter(R.layout.planned_item, cursor,
      new String[]{},
      new int[]{R.id.planned_item_name, R.id.planned_item_count, R.id.planned_item_choose});
    list_adapter.changeCursor(cursor);
    list_planned.setAdapter(list_adapter);

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
    Intent intent = new Intent(Common.application_context, ChooseCategories.class);
    if(add_credit == v)
      intent.putExtra("TREND", Transact.TREND.CREDIT);
    else// if(add_debit == v)
      intent.putExtra("TREND", Transact.TREND.DEBIT);
    ArrayList<Chooses> chooses = new ArrayList<Chooses>();
    for(boolean cursor_status = cursor.moveToFirst(); cursor_status; cursor_status = cursor.moveToNext())
      try
      {
        chooses.add(new Chooses(cursor.getLong(cursor.getColumnIndex("CATEGORY_ID")), cursor.getDouble(cursor.getColumnIndex("count")),
          Unit.getUnitFromId(cursor.getLong(cursor.getColumnIndex("id_unit")))));
      } catch(Entity.EntityException e)
      {      }
    intent.putParcelableArrayListExtra("chooses", chooses);
    startActivityForResult(intent, R.layout.choose_categories_list); //Запуск активности с onActivityResult
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
                  new Money(0), entry.getValue().first, entry.getValue().second);
                document.insert();
              } catch(Entity.EntityException e1)
              {     }
            }
          }
          cursor.requery();
          list_adapter.notifyDataSetChanged();
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
      super(Common.application_context, layout, cursor, from, to);

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
                list_adapter.notifyDataSetChanged();
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
        Toast.makeText(Common.application_context, "Здесь надо диалог со стоимостью выводить наверное", Toast.LENGTH_LONG).show();

        if(cursor.moveToPosition(cursor_position))
        {
          DChooseCategoryParams choose_category_params = new DChooseCategoryParams();
          Bundle params = new Bundle();
          params.putParcelable("count_unit", new Planned.Chooses(_id, d_count, u_unit));
          params.putString("name", s_name);
          choose_category_params.setArguments(params);
          choose_category_params.show(getFragmentManager(), null);
        }
      }
    }
  }
  @Override
  public void resultHandler(Bundle result_values)
  {
    Planned.Chooses result_count_unit = result_values.getParcelable("count_unit");
    Pair<Double, Unit> count_unit = new Pair<>(result_count_unit.count, result_count_unit.unit);
    try
    {
      if(Document.getDocumentFromId(result_count_unit._id).setCount(result_count_unit.count).setUnit(result_count_unit.unit).update())
      {
        cursor.requery();
        list_adapter.notifyDataSetChanged();
      }
      Document document = Document.getDocumentFromId(result_count_unit._id);
    } catch(Entity.EntityException e)
    {    }
  }
}
