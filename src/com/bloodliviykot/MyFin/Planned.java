package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
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
      view.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
          int i = 0;
          i++;
        }
      });
      EditText count = (EditText)view.findViewById(R.id.planned_item_count);
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

  }
}
