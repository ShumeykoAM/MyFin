package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.MyFin.DB.entities.Document;
import com.bloodliviykot.MyFin.DB.entities.Unit;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Kot on 23.09.2016.
 */
public class Planned
  extends Activity
{
  private Cursor cursor;
  private ListView list_planned;
  private SimpleCursorAdapter list_adapter;
  private MySQLiteOpenHelper oh;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.planned);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_planned = (ListView)findViewById(R.id.planned_list_view);

    cursor = oh.db.rawQuery(oh.getQuery(EQ.PLANNED), null);
    list_adapter = new PlannedItemAdapter(R.layout.planned_item, cursor,
      new String[]{},
      new int[]{R.id.planned_item_name, R.id.planned_item_count, R.id.planned_item_choose});
    list_adapter.changeCursor(cursor);
    list_planned.setAdapter(list_adapter);

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
