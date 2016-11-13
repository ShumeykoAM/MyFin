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
import com.bloodliviykot.MyFin.DB.entities.Document;
import com.bloodliviykot.MyFin.DB.entities.Transact;
import com.bloodliviykot.MyFin.DB.entities.Unit;
import com.bloodliviykot.tools.DataBase.Entity;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kot on 23.09.2016.
 */
public class Planned
  extends Activity
  implements View.OnClickListener
{
  private Cursor cursor;
  private ListView list_planned;
  private SimpleCursorAdapter list_adapter;
  private MySQLiteOpenHelper oh;
  private Button add_debit;
  private Button add_credit;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.planned);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_planned = (ListView)findViewById(R.id.planned_list_view);
    (add_debit = (Button)findViewById(R.id.planned_add_debit)).setOnClickListener(this);
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
    public final long _id_category;
    public final double count;
    public final Unit unit;
    public Chooses(long _id_category, double count, Unit unit)
    {
      this._id_category = _id_category;
      this.count = count;
      this.unit = unit;
    }

    //Parcelable
    public Chooses(Parcel in) throws Entity.EntityException
    {
      this._id_category = in.readLong();
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
      dest.writeLong(_id_category);
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
        chooses.add(new Chooses(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getDouble(cursor.getColumnIndex("count")),
          Unit.getUnitFromId(cursor.getLong(cursor.getColumnIndex("id_unit")))));
      } catch(Entity.EntityException e)
      {      }
    intent.putParcelableArrayListExtra("chooses", chooses);
    startActivityForResult(intent, R.layout.choose_categories_list); //Запуск активности с onActivityResult
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
            chooses.put(choose._id_category, new Pair<>(choose.count, choose.unit));
          //Перезапишем запланированные документы

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
      TextView name = (TextView)view.findViewById(R.id.planned_item_name);
      TextView count = (TextView)view.findViewById(R.id.planned_item_count);
      view.setOnTouchListener(new OnTouch(count, cursor.getLong(cursor.getColumnIndex("_id"))));
      /*view.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {

        }
      });
      */

      TextView unit = (TextView)view.findViewById(R.id.planned_item_unit);
      CheckBox choose = (CheckBox)view.findViewById(R.id.planned_item_choose);

      name.setText(cursor.getString(cursor.getColumnIndex("name")));
      count.setText(new Double(cursor.getDouble(cursor.getColumnIndex("count"))).toString());
      try
      {
        unit.setText(Unit.getUnitFromId(cursor.getLong(cursor.getColumnIndex("id_unit"))).getName());
      } catch(Entity.EntityException e)
      {   }
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
  }
}
