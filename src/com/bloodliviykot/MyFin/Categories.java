package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;

/**
 * Created by Kot on 22.10.2016.
 */
public class Categories
  extends Activity
{
  ListView categories;

  private MySQLiteOpenHelper oh;
  private Cursor cursor;
  private SimpleCursorAdapter list_adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.categories);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    categories = (ListView)findViewById(R.id.categories_list_view);
    cursor = oh.db.rawQuery(oh.getQuery(EQ.CATEGORIES), null);
    list_adapter = new CategoriesItemAdapter(R.layout.categories_item, cursor,
      new String[]{},
      new int[]{R.id.categories_item_name});
    list_adapter.changeCursor(cursor);
    categories.setAdapter(list_adapter);

  }

  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class CategoriesItemAdapter
    extends SimpleCursorAdapter
  {

    public CategoriesItemAdapter(int layout, Cursor cursor, String[] from, int[] to)
    {
      super(Common.application_context, layout, cursor, from, to);

    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      //Сопоставляем
      TextView tv_name    = (TextView)view.findViewById(R.id.categories_item_name);
      LinearLayout ll = (LinearLayout)view;
      tv_name.setText(cursor.getString(cursor.getColumnIndex("name")));
      /*
      if(!cursor.isNull(cursor.getColumnIndex("_id_parent")))
      {
        ll.setVisibility(View.GONE);
        ViewGroup.LayoutParams prs = ll.getLayoutParams();
        prs.height = 0;
        ll.setLayoutParams(prs);
        //ll.requestLayout();

        //tv_name.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
      }
      else
        ll.setVisibility(View.VISIBLE);

      cursor.moveToFirst();
      */
    }

  }
}

